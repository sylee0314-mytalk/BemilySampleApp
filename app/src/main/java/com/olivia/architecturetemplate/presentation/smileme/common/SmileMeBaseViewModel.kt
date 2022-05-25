package com.olivia.architecturetemplate.presentation.smileme.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * # bemily_messenger-app-android
 * # BaseViewModel
 * @author LEESOYOUNG
 * @since 2022-01-26
 */
interface SmileMeBaseEvent
interface SmileMeBaseState
interface SmileMeBaseEffect

abstract class SmileMeBaseViewModel<EVENT : SmileMeBaseEvent, EFFECT : SmileMeBaseEffect, STATE : SmileMeBaseState> :
    ViewModel() {

    private val initialState: STATE by lazy { setInitialState() }
    protected abstract fun setInitialState(): STATE
    private val _viewState: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _viewState

    protected fun updateState(reducer: STATE.() -> STATE) {
        _viewState.update(reducer)
    }

    fun event(event: EVENT) {
        viewModelScope.launch {
            handleEvent(event)
        }
    }

    protected abstract suspend fun handleEvent(event: EVENT)

    private val _effect: Channel<EFFECT> = Channel()
    val effect: Flow<EFFECT> = _effect.receiveAsFlow()

    protected fun setEffect(builder: () -> EFFECT) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
}

data class StateEffectDispatch<STATE, EFFECT, ACTION>(
    val state: STATE,
    val effectFlow: Flow<EFFECT>,
    val dispatch: (ACTION) -> Unit,
)

@Composable
internal inline fun <reified STATE : SmileMeBaseState, EFFECT : SmileMeBaseEffect, ACTION : SmileMeBaseEvent> use(
    viewModel: SmileMeBaseViewModel<ACTION, EFFECT, STATE>
): StateEffectDispatch<STATE, EFFECT, ACTION> {
    val state by viewModel.state.collectAsState()

    val dispatch: (ACTION) -> Unit = { event ->
        viewModel.event(event)
    }

    return StateEffectDispatch(
        state = state,
        effectFlow = viewModel.effect,
        dispatch = dispatch
    )
}