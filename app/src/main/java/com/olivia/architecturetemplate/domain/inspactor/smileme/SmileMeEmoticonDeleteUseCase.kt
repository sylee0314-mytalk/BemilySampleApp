package com.olivia.architecturetemplate.domain.inspactor.smileme

import com.bemily.messenger.smileme.domain.model.Result
import com.olivia.architecturetemplate.domain.inspactor.FlowUseCase
import com.olivia.architecturetemplate.domain.repository.SmileMeEmoticonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Bemily
 * 스마일미 이모티콘 삭제 UseCase
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-07-22
 **/
class SmileMeEmoticonDeleteUseCase(
    private val repository: SmileMeEmoticonRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<String, Unit>(dispatcher) {

    override suspend fun execute(parameters: String): Flow<Result<Unit>> {
        return repository.deleteSmileMeEmoticon(parameters).map {
            Result.Success(it)
        }
    }
}



