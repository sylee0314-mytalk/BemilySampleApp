package com.olivia.architecturetemplate.domain.inspactor.smileme

import com.olivia.architecturetemplate.domain.inspactor.UseCase
import com.olivia.architecturetemplate.domain.repository.SmileMeEmoticonRepository
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Bemily
 * 스마일미 이모티콘 캐쉬 파일 정리
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-07-28
 **/
class SmileMeEmoticonClearCacheUseCase(
    private val repository: SmileMeEmoticonRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<String, Unit>(dispatcher) {

    override suspend fun execute(
        parameters: String
    ) = repository.clearSmileMeEmoticonCache(parameters)

}


