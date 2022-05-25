package com.olivia.architecturetemplate.data.encoder

import android.graphics.Bitmap
import android.util.Size
import androidx.core.graphics.get


typealias EmoticonFrames = List<EmoticonFrame>

/**
 * Bemily
 * 이모티콘 생성 이미지 Frame
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-07-22
 **/
class EmoticonFrame(
    val size: Size,
    val image: Array<IntArray>
) {

    companion object {
        fun create(bitmap: Bitmap) = EmoticonFrame(
            size = Size(bitmap.width, bitmap.height),
            image = bitmap.toRGB()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmoticonFrame

        if (size != other.size) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}

fun List<Bitmap>.toFrames(): EmoticonFrames {
    return map { it.toFrame() }
}

fun Bitmap.toFrame(): EmoticonFrame {
    val frame = EmoticonFrame.create(this)
    this.recycle()
    return frame
}

fun Bitmap.toRGB(): Array<IntArray> {
    val width: Int = this.width
    val height: Int = this.height
    val result = Array(height) { IntArray(width) }
    toRGB(result)
    return result
}

fun Bitmap.toRGB(result: Array<IntArray>) {
    for (row in 0 until height) {
        for (col in 0 until width) {
            result[row][col] = this[col, row]
        }
    }
}