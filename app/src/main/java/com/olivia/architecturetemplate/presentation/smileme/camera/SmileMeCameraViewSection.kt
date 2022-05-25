package com.olivia.architecturetemplate.presentation.smileme.camera


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.bemily.messenger.smileme.domain.model.SmileMeFilter
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.olivia.architecturetemplate.R
import com.olivia.architecturetemplate.domain.model.SmileMeSticker
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing

/**
 * # bemily_messenger-app-android
 * # SmileMeCameraViewSection
 * @author M.Y.Seong
 * @since 2022-03-02
 */

@SuppressLint("InflateParams")
@Composable
fun SmileMeCameraViewSection(
    modifier: Modifier = Modifier,
    facing: Facing = Facing.FRONT,
    selectedSticker: SmileMeSticker,
    selectedFilter: SmileMeFilter,
    update: (CameraView, ImageView) -> Unit = { _, _ -> },
    onPictureTaken: (PictureResult) -> Unit = {}
) {
    var sticker by remember { mutableStateOf(selectedSticker.ordinal) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val listener = object : CameraListener() {
        override fun onPictureTaken(result: PictureResult) {
            onPictureTaken(result)
        }
    }

    var cameraView: CameraView? = null

    DisposableEffect(key1 = Unit) {
        onDispose {
            /**
             * 카메라가 제대로 해제 되지 않아서 preview -> back key 로 돌아왔을때
             * crash 가 발생 한다.
             * listener 와 lifecycleOwner 를 제거해줘야 한다.
             */
            cameraView?.close()
            cameraView?.removeCameraListener(listener)
            cameraView?.setLifecycleOwner(null)
            cameraView = null
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {

        val pxW = LocalDensity.current.run { maxWidth.toPx() }
        val pxH = LocalDensity.current.run { maxHeight.toPx() }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            factory = { context ->
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.smile_me_preview, null, false)

                cameraView = view.findViewById(R.id.camera)
                cameraView?.apply {
                    pictureSnapshotMetering = false
                    this.facing = facing
                    snapshotMaxWidth = pxW.toInt()
                    snapshotMaxHeight = pxH.toInt()
                    setLifecycleOwner(lifecycleOwner)
                    addCameraListener(listener)
                }


                val waterMark = view.findViewById<ImageView>(R.id.watermark)
                setWatermark(context, waterMark, selectedSticker)

                cameraView?.let {
                    update(it, waterMark)
                }

                view
            },
            update = {
                cameraView?.apply {
                    this.facing = facing
                    filter = selectedFilter.newInstance()
                }

                val waterMark = it.findViewById<ImageView>(R.id.watermark)
                if (sticker != selectedSticker.ordinal) {
                    sticker = selectedSticker.ordinal
                    setWatermark(
                        context = it.context,
                        waterMark = waterMark,
                        selectedSticker = selectedSticker
                    )
                }
            }
        )
    }
}

private fun setWatermark(
    context: Context,
    waterMark: ImageView,
    selectedSticker: SmileMeSticker
) {
    Glide.with(context)
        .asDrawable()
        .load(selectedSticker.resId)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (resource is WebpDrawable) {
                    resource.stop()
                    resource.loopCount = WebpDrawable.LOOP_FOREVER
                    resource.startFromFirstFrame()
                }

                return false
            }
        })
        .into(waterMark)
}