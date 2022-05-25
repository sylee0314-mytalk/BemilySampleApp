package com.olivia.architecturetemplate.domain.inspactor.smileme

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.olivia.architecturetemplate.domain.inspactor.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.io.File

/**
 * Bemily
 * 이모티콘 비트맵 로드 UseCase
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-08-06
 **/
class SmileMeEmoticonBitmapGetUseCase(
    dispatcher: CoroutineDispatcher
) : UseCase<List<String>, List<Bitmap>>(dispatcher) {

    override suspend fun execute(parameters: List<String>): List<Bitmap> {
        return parameters.map { path ->
            val file = File(path)
            file.inputStream().use {
                BitmapFactory.decodeStream(it)
            }
        }
    }

}
