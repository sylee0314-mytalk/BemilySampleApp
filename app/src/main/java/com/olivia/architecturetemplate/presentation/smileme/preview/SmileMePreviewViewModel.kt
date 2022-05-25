package com.olivia.architecturetemplate.presentation.smileme.preview

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bemily.messenger.smileme.domain.model.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.olivia.architecturetemplate.domain.inspactor.smileme.SmileMeEmoticonBitmapGetUseCase
import com.olivia.architecturetemplate.domain.inspactor.smileme.SmileMeEmoticonSaveParams
import com.olivia.architecturetemplate.domain.inspactor.smileme.SmileMeEmoticonSaveUseCase
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * # bemily_messenger-app-android
 * # SmileMePreviewViewModel
 * @author hsjun85
 * @since 2022-03-02
 */

@HiltViewModel
class SmileMePreviewViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val smileMeEmoticonBitmapGetUseCase: SmileMeEmoticonBitmapGetUseCase,
    private val smileMeEmoticonSaveUseCase: SmileMeEmoticonSaveUseCase
) : SmileMeBaseViewModel<SmileMePreviewEvent, SmileMePreviewEffect, SmileMePreviewState>() {

    init {
        createBitmap()
    }


    override fun setInitialState() = SmileMePreviewState()

    override suspend fun handleEvent(event: SmileMePreviewEvent) {
        when (event) {
            is SmileMePreviewEvent.SaveSmileMeEvent -> {
                saveSmileMeEmoticon(
                    bitmapList = event.bitmapList,
                    cachePath = event.cachePath
                )
            }
            is SmileMePreviewEvent.CreatedDrawableEvent -> {
                updateState { copy(animationDrawable = event.animationDrawable) }
            }
        }
    }

    private fun createBitmap() {
        viewModelScope.launch {
            val json = stateHandle.get<String>(SMILE_ME_BITMAP_PATH) ?: ""
            val listType = object : TypeToken<List<String>>() {}.type
            val bitmapPathList: List<String> = Gson().fromJson(json, listType)

            when (val result = smileMeEmoticonBitmapGetUseCase(bitmapPathList)) {
                is Result.Success -> {
                    updateState { copy(bitmapList = result.data) }
                    setEffect { SmileMePreviewEffect.CreatedBitmap(result.data) }
                }
                else -> Unit
            }
        }
    }

    private fun saveSmileMeEmoticon(
        bitmapList: List<Bitmap>,
        cachePath: String
    ) {
        viewModelScope.launch {
            try {
                val params = SmileMeEmoticonSaveParams(
                    bitmaps = bitmapList,
                    cachePath = cachePath,
                    emoticonPath = "",
                    emoticonThumbPath = "",
                    emoticonImage = "",
                    emoticonImageThumb = ""
                )
                smileMeEmoticonSaveUseCase(params).collect {
                    when (it) {
                        is Result.Success -> updateState { copy(saveState = SmileMeSaveState.SMILE_ME_SAVED) }
                        is Result.Loading -> updateState { copy(saveState = SmileMeSaveState.SMILE_ME_SAVING) }
                        is Result.Error -> updateState { copy(saveState = SmileMeSaveState.SMILE_ME_FAILED) }
                        else -> Unit
                    }
                }
            } catch (e: CancellationException) {
                updateState { copy(saveState = SmileMeSaveState.SMILE_ME_FAILED) }
            }
        }
    }

    companion object {
        const val SMILE_ME_BITMAP_PATH = "smile_me_bitmap_path"
    }
}