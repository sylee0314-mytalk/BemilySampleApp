package com.olivia.architecturetemplate.data.encoder

import android.graphics.Bitmap
import com.olivia.architecturetemplate.util.BemilyGifEncoder
import com.olivia.architecturetemplate.util.BemilyGifException
import com.olivia.architecturetemplate.util.encoder.EncodingListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.coroutines.resumeWithException
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Bemily
 * 이모티콘 생성 Encoder
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-07-22
 **/
class EmoticonEncoder {

    fun encode(cachePath: String, bitmaps: List<Bitmap>): Flow<EmoticonResult> {
        val encodeBitmaps = bitmaps.map {
            resizeEmoticonBitmap(it, EMOTICON_WIDTH_HEIGHT, EMOTICON_WIDTH_HEIGHT)
        }
        val thumbBitmap = resizeEmoticonBitmap(
            bitmaps.last(),
            EMOTICON_THUMBNAIL_WIDTH_HEIGHT,
            EMOTICON_THUMBNAIL_WIDTH_HEIGHT
        )

        return if (encodeBitmaps.size > 1) {
            encodeAnimationImage(cachePath, encodeBitmaps, thumbBitmap)
        } else {
            encodeImage(cachePath, encodeBitmaps.first(), thumbBitmap)
        }
    }

    private fun encodeImage(
        cachePath: String,
        bitmap: Bitmap,
        thumbBitmap: Bitmap
    ): Flow<EmoticonResult> = flow {
        try {
            val result = EmoticonResult(
                emoticonPath = compress(
                    cachePath,
                    bitmap,
                    EMOTICON_NAME_PREFIX,
                    EMOTICON_IMAGE_EXTENSION
                ),
                emoticonThumbPath = compress(
                    cachePath,
                    thumbBitmap,
                    EMOTICON_THUMBNAIL_NAME_PREFIX,
                    EMOTICON_THUMBNAIL_EXTENSION
                )
            )
            emit(result)
        } catch (e: Exception) {
            Timber.e("Encode animation image throws an exception: $e")
            throw e
        } finally {
            bitmap.recycle()
            thumbBitmap.recycle()
        }
    }

    private fun encodeAnimationImage(
        cachePath: String,
        bitmaps: List<Bitmap>,
        thumbBitmap: Bitmap
    ): Flow<EmoticonResult> = flow {
        try {
            val file = getEmoticonFile(
                ensureEmoticonCacheDir(cachePath).absolutePath,
                EMOTICON_NAME_PREFIX,
                EMOTICON_ANIMATION_EXTENSION
            )
            val outputStream = FileOutputStream(file)
            val encoder = BemilyGifEncoder.newInstance()
            encoder.setDelay(EMOTICON_ANIMATION_DURATION)
            encoder.encodeByBitmapsAwait(bitmaps, outputStream)
            outputStream.close()

            val result = EmoticonResult(
                emoticonPath = file.absolutePath,
                emoticonThumbPath = compress(
                    cachePath,
                    thumbBitmap,
                    EMOTICON_THUMBNAIL_NAME_PREFIX,
                    EMOTICON_THUMBNAIL_EXTENSION
                )
            )
            emit(result)
        } catch (e: Exception) {
            Timber.e("Encode animation image throws an exception: $e")
            throw e
        } finally {
            recycleBitmaps(bitmaps)
            thumbBitmap.recycle()
        }
    }

    private fun getEmoticonFile(dirPath: String, prefix: String, ext: String): File {
        val builder = StringBuilder()
        builder.append(prefix)
        builder.append(System.currentTimeMillis())
        builder.append(ext)
        return File(dirPath, builder.toString())
    }

    private fun ensureEmoticonCacheDir(path: String): File {
        return File(path).apply {
            if (!exists()) mkdirs()
        }
    }

    private fun resizeEmoticonBitmap(bitmap: Bitmap, width: Float, height: Float): Bitmap {
        val ratio: Float = min(
            width / bitmap.width, height / bitmap.height
        )
        val dstWidth = (ratio * bitmap.width).roundToInt()
        val dstHeight = (ratio * bitmap.height).roundToInt()

        return Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, false)
    }

    private fun compress(cachePath: String, bitmap: Bitmap, prefix: String, ext: String): String {
        val file = getEmoticonFile(cachePath, prefix, ext)
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return file.absolutePath
    }

    private fun recycleBitmaps(bitmaps: List<Bitmap>) {
        bitmaps.forEach {
            if (!it.isRecycled)
                it.recycle()
        }
    }

    companion object {
        const val EMOTICON_NAME_PREFIX = "emoticon_"
        const val EMOTICON_IMAGE_EXTENSION = ".jpg"
        const val EMOTICON_ANIMATION_EXTENSION = ".gif"
        const val EMOTICON_ANIMATION_DURATION = 125
        const val EMOTICON_WIDTH_HEIGHT = 375f

        const val EMOTICON_THUMBNAIL_NAME_PREFIX = "emoticon_thumb_"
        const val EMOTICON_THUMBNAIL_WIDTH_HEIGHT = 150f
        const val EMOTICON_THUMBNAIL_EXTENSION = ".jpg"
    }

}

data class EmoticonResult(
    val emoticonPath: String,
    val emoticonThumbPath: String
)

suspend fun BemilyGifEncoder.encodeByBitmapsAwait(
    bitmaps: List<Bitmap>,
    outputStream: OutputStream
) {
    return suspendCancellableCoroutine { continuation ->
        val task = encodeByBitmaps(bitmaps, outputStream, object : EncodingListener() {
            override fun onSuccess() {
                continuation.resumeWith(Result.success(Unit))
            }

            override fun onError(e: BemilyGifException) {
                continuation.resumeWithException(e)
            }
        })

        continuation.invokeOnCancellation {
            task.cancel(true)
        }
    }
}