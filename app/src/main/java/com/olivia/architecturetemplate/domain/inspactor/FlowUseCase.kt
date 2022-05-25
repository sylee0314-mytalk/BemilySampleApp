package com.olivia.architecturetemplate.domain.inspactor

import com.bemily.messenger.smileme.domain.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(private val dispatcher: CoroutineDispatcher) {
    /**
     * Executes business logic in its execute method and keep posting updates to the result as
     * [Result<R>].
     * Handling an exception (emit [Result.Error] to the result) is the subclasses's responsibility.
     */
    suspend operator fun invoke(parameters: P): Flow<Result<R>> = execute(parameters)
        .catch { e ->
            when (e) {
                is Exception -> emit(Result.Error(e))
                else -> emit(Result.Error(Exception(e)))
            }
        }
        .flowOn(dispatcher)

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): Flow<Result<R>>

}