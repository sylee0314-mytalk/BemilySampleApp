package com.olivia.architecturetemplate.domain.inspactor.smileme

import android.graphics.Bitmap
import com.bemily.messenger.smileme.domain.model.Result
import com.olivia.architecturetemplate.data.encoder.EmoticonEncoder
import com.olivia.architecturetemplate.domain.di.IoDispatcher
import com.olivia.architecturetemplate.domain.inspactor.FlowUseCase
import com.olivia.architecturetemplate.domain.repository.SmileMeEmoticonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Bemily
 * 스마일미 이모티콘 업로드 및 추가 UseCase
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-07-22
 **/
class SmileMeEmoticonSaveUseCase @Inject constructor(
    private val repository: SmileMeEmoticonRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<SmileMeEmoticonSaveParams, Long>(dispatcher) {

    override suspend fun execute(parameters: SmileMeEmoticonSaveParams): Flow<Result<Long>> = flow {
        emitAll(encodeSmileEmoticon(parameters))
        emitAll(addSmileMeEmoticon(parameters))
    }

    private fun encodeSmileEmoticon(emoticonParam: SmileMeEmoticonSaveParams): Flow<Result.Loading> = flow {
        emit(Result.Loading)
        val encoder = EmoticonEncoder()

        encoder.encode(
            cachePath = emoticonParam.cachePath,
            bitmaps = emoticonParam.bitmaps
        ).collect {
            emoticonParam.emoticonPath = it.emoticonPath
            emoticonParam.emoticonThumbPath = it.emoticonThumbPath
        }
    }

    private fun addSmileMeEmoticon(emoticonParam: SmileMeEmoticonSaveParams): Flow<Result<Long>> = flow {
        val image = emoticonParam.emoticonImage ?: throw NullPointerException("Image is empty")
        val imageThumb = emoticonParam.emoticonImageThumb ?: throw NullPointerException("Image thumbnail is empty")
//        repository.addSmileMeEmoticon(image, imageThumb).map { Result.Success(it) }

        emit(Result.Success(0L))
    }
}

data class SmileMeEmoticonSaveParams(
    val bitmaps: List<Bitmap>,
    val cachePath: String,
    var emoticonPath: String? = null,
    var emoticonThumbPath: String? = null,
    var emoticonImage: String? = null,
    var emoticonImageThumb: String? = null
)


