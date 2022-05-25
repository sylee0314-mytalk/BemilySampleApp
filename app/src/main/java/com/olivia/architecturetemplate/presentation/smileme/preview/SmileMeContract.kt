package com.olivia.architecturetemplate.presentation.smileme.preview

import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeBaseEffect
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeBaseEvent
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeBaseState

/**
 * # bemily_messenger-app-android
 * # smile me preview contact
 * @author mjkim
 * @since 2022-03-02
 */

data class SmileMePreviewState(
    val saveState: SmileMeSaveState = SmileMeSaveState.SMILE_ME_IDLE,
    val animationDrawable: AnimationDrawable? = null,
    val bitmapList: List<Bitmap> = emptyList()
) : SmileMeBaseState

sealed class SmileMePreviewEvent : SmileMeBaseEvent {
    data class SaveSmileMeEvent(
        val bitmapList: List<Bitmap>,
        val cachePath: String
    ) : SmileMePreviewEvent()

    data class CreatedDrawableEvent(
        val animationDrawable: AnimationDrawable
    ) : SmileMePreviewEvent()
}

sealed class SmileMePreviewEffect : SmileMeBaseEffect {
    data class CreatedBitmap(val bitmapList: List<Bitmap>) : SmileMePreviewEffect()
}

enum class SmileMeSaveState {
    SMILE_ME_SAVING,
    SMILE_ME_SAVED,
    SMILE_ME_FAILED,
    SMILE_ME_IDLE
}