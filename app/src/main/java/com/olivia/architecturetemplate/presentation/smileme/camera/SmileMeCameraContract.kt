package com.olivia.architecturetemplate.presentation.smileme.camera

import com.bemily.messenger.smileme.domain.model.SmileMeDecor
import com.bemily.messenger.smileme.domain.model.SmileMeFilter
import com.bemily.messenger.smileme.domain.model.SmileMeStore
import com.olivia.architecturetemplate.domain.model.SmileMeSticker
import com.olivia.architecturetemplate.presentation.smileme.camera.SmileMeCameraContract.CameraDecorType
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeBaseEffect
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeBaseEvent
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeBaseState
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing

/**
 * # bemily_messenger-app-android
 * # SmileMeCameraContract
 * @author mjkim
 * @since 2022-01-19
 */
data class SmileMeCameraState(
    val cameraFacing: Facing = Facing.FRONT,
    val recordingProgress: Float? = null,
    val recordTime: Long = 0,
    val smileMeDecorType: CameraDecorType = CameraDecorType.STICKER,
    val smileMeDecorList: List<SmileMeDecor> = SmileMeStore.stickers,
    val selectedSmileMeDecor: SmileMeDecor = smileMeDecorList.first(),
    val selectedSticker: SmileMeSticker = SmileMeSticker.getSticker(0),
    val selectedFilter: SmileMeFilter = SmileMeFilter.getFilter(0)
) : SmileMeBaseState

sealed class SmileMeCameraEvent : SmileMeBaseEvent {
    data class OnClickRecordEvent(val cachePath: String) : SmileMeCameraEvent()
    object OnClickCameraFacingEvent : SmileMeCameraEvent()
    object OnClickDecorEvent : SmileMeCameraEvent()
    data class OnClickDecorItemEvent(val smileMeDecor: SmileMeDecor) : SmileMeCameraEvent()
    object RecordCancelEvent : SmileMeCameraEvent()
    data class SmileMeSnapshotEvent(val picture: PictureResult) : SmileMeCameraEvent()
    object ResetRecordEvent : SmileMeCameraEvent()
}

sealed class SmileMeCameraEffect : SmileMeBaseEffect {
    object Prepare : SmileMeCameraEffect()
    object TakingImage : SmileMeCameraEffect()
    data class CacheSaved(val bitmapPathListJson: String) : SmileMeCameraEffect()
    object CacheNotSaved : SmileMeCameraEffect()
}

object SmileMeCameraContract {
    enum class CameraDecorType {
        STICKER,
        FILTER
    }
}