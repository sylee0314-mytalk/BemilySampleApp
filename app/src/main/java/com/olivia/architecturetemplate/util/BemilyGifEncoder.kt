/*
Copyright 2018 NAVER Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.olivia.architecturetemplate.util

import android.graphics.Bitmap
import android.os.AsyncTask
import com.olivia.architecturetemplate.util.encoder.EncodingListener
import com.olivia.architecturetemplate.util.encoder.GifBatchEncoderAsyncTask
import com.olivia.architecturetemplate.util.encoder.GifBatchEncoderAsyncTask.BitmapIterator
import com.olivia.architecturetemplate.util.encoder.GifBatchEncoderAsyncTask.ImagePathIterator
import com.olivia.architecturetemplate.util.encoder.GifQueuingEncodable
import com.olivia.architecturetemplate.util.encoder.GifQueuingEncoderAsyncTask
import java.io.FileOutputStream
import java.io.OutputStream

class BemilyGifEncoder {
    private var quality = 10 // (or sampling factor) default 10. (10-30)
    private var colorLevel = 7 // (or map quality) default 7 (8-6)
    private var delay = 100
    fun setQuality(quality: Int): BemilyGifEncoder {
        this.quality = quality
        return this
    }

    fun setColorLevel(colorLevel: Int): BemilyGifEncoder {
        if (colorLevel > 8 || colorLevel < 6) throw BemilyGifException(BemilyGifError.INVALID_COLOR_LEVEL_VALUE)
        this.colorLevel = colorLevel
        return this
    }

    fun setDelay(delay: Int): BemilyGifEncoder {
        this.delay = delay
        return this
    }

    fun encodeByBitmaps(
        bitmaps: List<Bitmap?>?,
        outputFilePath: String?,
        encodingListener: EncodingListener
    ) {
        try {
            encodeByBitmaps(bitmaps, FileOutputStream(outputFilePath), encodingListener)
        } catch (ex: Exception) {
            encodingListener.onError(BemilyGifException(BemilyGifError.FAILED_TO_CREATE_GIF))
        }
    }

    fun encodeByBitmaps(
        bitmaps: List<Bitmap?>?,
        outputStream: OutputStream?,
        encodingListener: EncodingListener?
    ): AsyncTask<*, *, *> {
        return GifBatchEncoderAsyncTask(
            BitmapIterator(bitmaps),
            quality,
            colorLevel,
            delay,
            outputStream,
            encodingListener
        ).execute()
    }

    fun encodeByImagePaths(
        imagePaths: List<String?>?,
        outputFilePath: String?,
        encodingListener: EncodingListener
    ) {
        try {
            encodeByImagePaths(imagePaths, FileOutputStream(outputFilePath), encodingListener)
        } catch (ex: Exception) {
            encodingListener.onError(BemilyGifException(BemilyGifError.FAILED_TO_CREATE_GIF))
        }
    }

    fun encodeByImagePaths(
        imagePaths: List<String?>?,
        outputStream: OutputStream?,
        encodingListener: EncodingListener?
    ) {
        GifBatchEncoderAsyncTask(
            ImagePathIterator(imagePaths),
            quality,
            colorLevel,
            delay,
            outputStream,
            encodingListener
        ).execute()
    }

    fun encodeWithQueuing(
        outputStream: OutputStream?,
        encodingListener: EncodingListener?
    ): GifQueuingEncodable {
        val task = GifQueuingEncoderAsyncTask(
            MAX_QUEUE_SIZE,
            MAX_QUEUE_CAPACITY,
            quality,
            colorLevel,
            delay,
            outputStream,
            encodingListener
        )
        task.execute()
        return task
    }

    companion object {
        private const val MAX_QUEUE_SIZE = 100
        private const val MAX_QUEUE_CAPACITY = (64 * 1024 * 1024).toLong()
        fun newInstance(): BemilyGifEncoder {
            return BemilyGifEncoder()
        }
    }
}