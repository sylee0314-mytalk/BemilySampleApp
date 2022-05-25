package com.olivia.architecturetemplate.domain.inspactor.smileme

import android.graphics.Bitmap
import com.olivia.architecturetemplate.domain.inspactor.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.io.File

/**
 * Bemily
 * 이모티콘 비트맵 저장 UseCase
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-08-06
 **/
class SmileMeEmoticonBitmapPutUseCase(
    dispatcher: CoroutineDispatcher
) : UseCase<SmileMeEmoticonBitmapPutParams, List<String>>(dispatcher) {

    override suspend fun execute(parameters: SmileMeEmoticonBitmapPutParams): List<String> {
        val cacheDir = ensureEmoticonBitmapCacheDir(parameters.cachePath)
        return parameters.bitmaps.map { bitmap ->
            val file = File(cacheDir, System.currentTimeMillis().toString())
            file.outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            file.absolutePath
        }
    }

    private fun ensureEmoticonBitmapCacheDir(path: String): File {
        return File(path).apply {
            if (!exists()) mkdirs()
        }
    }

}

data class SmileMeEmoticonBitmapPutParams(
    val cachePath: String,
    val bitmaps: List<Bitmap>
)