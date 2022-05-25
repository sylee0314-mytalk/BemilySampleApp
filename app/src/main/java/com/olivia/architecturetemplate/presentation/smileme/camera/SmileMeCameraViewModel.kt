package com.olivia.architecturetemplate.presentation.smileme.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.SystemClock
import androidx.lifecycle.viewModelScope
import com.bemily.messenger.smileme.domain.model.Result
import com.bemily.messenger.smileme.domain.model.SmileMeDecor
import com.bemily.messenger.smileme.domain.model.SmileMeFilter
import com.bemily.messenger.smileme.domain.model.SmileMeStore
import com.google.gson.Gson
import com.olivia.architecturetemplate.domain.inspactor.smileme.SmileMeEmoticonBitmapPutParams
import com.olivia.architecturetemplate.domain.inspactor.smileme.SmileMeEmoticonBitmapPutUseCase
import com.olivia.architecturetemplate.domain.model.SmileMeSticker
import com.olivia.architecturetemplate.presentation.smileme.camera.SmileMeCameraContract.CameraDecorType
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeBaseViewModel
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * # bemily_messenger-app-android
 * # SmileMeCameraViewModel
 * @author M.Y.Seong
 * @since 2022-03-02
 */
@HiltViewModel
class SmileMeCameraViewModel @Inject constructor(
    private val smileMeEmoticonBitmapPutUseCase: SmileMeEmoticonBitmapPutUseCase
) : SmileMeBaseViewModel<SmileMeCameraEvent, SmileMeCameraEffect, SmileMeCameraState>() {

    private val emoticonBitmaps = mutableListOf<Bitmap>()

    private var timer: Timer? = null
    private var smileMeSaveJob: Job? = null

    override fun setInitialState() = SmileMeCameraState()

    override suspend fun handleEvent(event: SmileMeCameraEvent) {
        when (event) {
            is SmileMeCameraEvent.OnClickRecordEvent -> startRecordSmileMe(event.cachePath)
            is SmileMeCameraEvent.OnClickCameraFacingEvent -> toggleCameraFacing()
            is SmileMeCameraEvent.OnClickDecorEvent -> toggleSmileMeDecor()
            is SmileMeCameraEvent.RecordCancelEvent -> cancelRecordSmileMe()
            is SmileMeCameraEvent.SmileMeSnapshotEvent -> addSmileMeSnapshot(event.picture)
            is SmileMeCameraEvent.OnClickDecorItemEvent -> selectSmileMeDecor(event.smileMeDecor)
            is SmileMeCameraEvent.ResetRecordEvent -> {
                updateState { copy(recordingProgress = null) }
            }
        }
    }

    private fun selectSmileMeDecor(smileMeDecor: SmileMeDecor) {
        updateState {
            when (state.value.smileMeDecorType) {
                CameraDecorType.STICKER -> {
                    copy(
                        selectedSmileMeDecor = smileMeDecor,
                        selectedSticker = SmileMeSticker.getSticker(smileMeDecor.id)
                    )
                }
                CameraDecorType.FILTER -> {
                    copy(
                        selectedSmileMeDecor = smileMeDecor,
                        selectedFilter = SmileMeFilter.getFilter(smileMeDecor.id)
                    )
                }
            }
        }
    }

    private fun toggleSmileMeDecor() {
        when (state.value.smileMeDecorType) {
            CameraDecorType.STICKER -> {
                updateState {
                    copy(
                        smileMeDecorType = CameraDecorType.FILTER,
                        smileMeDecorList = SmileMeStore.filters,
                        selectedSmileMeDecor = SmileMeStore.filters[selectedFilter.ordinal]
                    )
                }
            }
            CameraDecorType.FILTER -> {
                updateState {
                    copy(
                        smileMeDecorType = CameraDecorType.STICKER,
                        smileMeDecorList = SmileMeStore.stickers,
                        selectedSmileMeDecor = SmileMeStore.stickers[selectedSticker.ordinal]
                    )
                }
            }
        }
    }

    private suspend fun addSmileMeSnapshot(picture: PictureResult) {
        withContext(Dispatchers.Default) {
            emoticonBitmaps.add(
                BitmapFactory.decodeByteArray(picture.data, 0, picture.data.size)
            )
        }
    }

    private fun startRecordSmileMe(cachePath: String) {
        recycleBitmaps()
        setEffect { SmileMeCameraEffect.Prepare }

        startRecordSmileMeTimer(
            tick = { progress ->
                setEffect { SmileMeCameraEffect.TakingImage }
                updateState { copy(recordingProgress = progress) }
            },
            stopTimer = {
                saveCacheSmileMe(cachePath)
            }
        )
    }

    private fun startRecordSmileMeTimer(
        tick: (progress: Float) -> Unit,
        stopTimer: () -> Unit
    ) {
        cancelTimer()

        val initialTime = SystemClock.elapsedRealtime()
        Timer()
            .apply { this@SmileMeCameraViewModel.timer = this }
            .scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val newValue = SystemClock.elapsedRealtime() - initialTime

                    tick(newValue / TIME_RECORDING)

                    if (newValue >= TIME_RECORDING) {
                        Thread.sleep(TIME_PERIOD)
                        stopTimer()
                        cancel()
                    }
                }
            }, TIME_PERIOD, TIME_PERIOD)
    }

    private fun saveCacheSmileMe(cachePath: String) {
        smileMeSaveJob = viewModelScope.launch {
            val result = smileMeEmoticonBitmapPutUseCase(
                SmileMeEmoticonBitmapPutParams(
                    cachePath = cachePath,
                    bitmaps = emoticonBitmaps.toList()
                )
            )

            when (result) {
                is Result.Success -> {
                    setEffect { SmileMeCameraEffect.CacheSaved(Gson().toJson(result.data)) }
                }
                is Result.Error -> {
                    setEffect { SmileMeCameraEffect.CacheNotSaved }
                }
                else -> Unit
            }

            recycleBitmaps()
        }
    }

    private fun toggleCameraFacing() {
        val facing = when (state.value.cameraFacing) {
            Facing.BACK -> Facing.FRONT
            Facing.FRONT -> Facing.BACK
        }

        updateState {
            copy(cameraFacing = facing)
        }
    }

    private fun cancelRecordSmileMe() {
        cancelTimer()
        cancelSaveCache()
        recycleBitmaps()

        updateState {
            copy(
                recordTime = 0,
                recordingProgress = null
            )
        }
    }

    private fun recycleBitmaps() {
        emoticonBitmaps.forEach {
            if (!it.isRecycled)
                it.recycle()
        }

        emoticonBitmaps.clear()
    }

    private fun cancelTimer() {
        timer?.cancel()
        timer = null
    }

    private fun cancelSaveCache() {
        smileMeSaveJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }

    companion object {
        const val TIME_MILLISECONDS = 1000
        const val TIME_RECORDING = 2000F
        const val TIME_PERIOD = 125L
    }
}
