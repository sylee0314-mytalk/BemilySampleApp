package com.olivia.architecturetemplate.domain.inspactor.smileme

import com.olivia.architecturetemplate.domain.inspactor.UseCase
import com.olivia.architecturetemplate.domain.repository.SmileMeEmoticonRepository
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Bemily
 * 스마일미 이모티콘 개수 UseCase
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-07-22
 **/
class SmileMeEmoticonCountUseCase(
    private val repository: SmileMeEmoticonRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<Unit, Int>(dispatcher) {

    override suspend fun execute(parameters: Unit): Int {
        return repository.getSmileMeEmoticonCount()
    }

}
