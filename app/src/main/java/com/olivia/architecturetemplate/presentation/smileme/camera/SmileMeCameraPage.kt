package com.olivia.architecturetemplate.presentation.smileme.camera


import android.Manifest
import android.content.Context
import android.content.pm.ActivityInfo
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.olivia.architecturetemplate.R
import com.olivia.architecturetemplate.presentation.smileme.common.LockScreenOrientation
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeBottomComponent
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeRecordSection
import com.olivia.architecturetemplate.presentation.smileme.common.use
import com.olivia.architecturetemplate.presentation.ui.component.ScaffoldComponent
import com.otaliastudios.cameraview.CameraView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

/**
 * # bemily_messenger-app-android
 * # SmileMeCameraPage
 * @author M.Y.Seong
 * @since 2022-02-28
 */

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SmileMeCameraPage(
    viewModel: SmileMeCameraViewModel = hiltViewModel(),
    onRecordSuccess: (String) -> Unit = {}
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val (state, effect, dispatch) = use(viewModel = viewModel)

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val camerasPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val context = LocalContext.current
    var camera: CameraView? by remember { mutableStateOf(null) }
    var waterMark: ImageView? by remember { mutableStateOf(null) }

    LaunchedEffect(camera, state.cameraFacing) {
        camera?.facing = state.cameraFacing
    }

    LaunchedEffect(key1 = Unit) {
        effect.onEach {
            when (it) {
                is SmileMeCameraEffect.Prepare -> {
                    val resource = waterMark?.drawable
                    if (resource is WebpDrawable) {
                        resource.loopCount = WebpDrawable.LOOP_FOREVER
                        resource.stop()
                        resource.startFromFirstFrame()
                    }
                }
                is SmileMeCameraEffect.TakingImage -> {
                    camera?.takePictureSnapshot()
                }
                is SmileMeCameraEffect.CacheNotSaved -> {
                    // TODO : Dialog 로 교체 해야 함
                    Toast.makeText(
                        context,
                        context.getString(R.string.smile_me_emoticon_take_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is SmileMeCameraEffect.CacheSaved -> {
                    onRecordSuccess(it.bitmapPathListJson)
                }
            }
        }.collect()
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            camera = null
            waterMark = null
            dispatch(SmileMeCameraEvent.ResetRecordEvent)
        }
    }

    ScaffoldComponent(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        actions = {
            IconButton(
                onClick = {
                    scope.launch {
                        scaffoldState
                            .snackbarHostState
                            .showSnackbar(message = context.getString(R.string.smile_me_message_info))
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "SmileMeConfirmPageInfo",
                    tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
            }
        },
        snackbarHost = { hostState ->
            SnackbarHost(
                hostState = hostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp, start = 40.dp, end = 40.dp)
            ) { data ->
                Snackbar(snackbarData = data)
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                PermissionRequired(
                    permissionState = camerasPermissionState,
                    permissionNotGrantedContent = {
                        SideEffect { camerasPermissionState.launchPermissionRequest() }
                    },
                    permissionNotAvailableContent = {}
                ) {
                    SmileMeCameraViewSection(
                        facing = state.cameraFacing,
                        selectedFilter = state.selectedFilter,
                        selectedSticker = state.selectedSticker,
                        update = { cameraView, imageView ->
                            camera = cameraView
                            waterMark = imageView
                        },
                        onPictureTaken = {
                            dispatch(SmileMeCameraEvent.SmileMeSnapshotEvent(it))
                        }
                    )
                }
            }

            SmileMeDecorListSection(
                isVisible = state.recordingProgress == null,
                smileMeDecorList = state.smileMeDecorList,
                selected = state.selectedSmileMeDecor,
                onClickDecor = { smileMeDecor ->
                    dispatch(SmileMeCameraEvent.OnClickDecorItemEvent(smileMeDecor))
                }
            )

            SmileMeBottomComponent(
                leftButton = {
                    if (state.recordingProgress == null) {
                        IconButton(onClick = { dispatch(SmileMeCameraEvent.OnClickDecorEvent) }) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                imageVector = when (state.smileMeDecorType) {
                                    SmileMeCameraContract.CameraDecorType.STICKER -> Icons.Default.PhotoFilter
                                    SmileMeCameraContract.CameraDecorType.FILTER -> Icons.Default.MovieFilter
                                },
                                contentDescription = when (state.smileMeDecorType) {
                                    SmileMeCameraContract.CameraDecorType.STICKER -> "SmileMeConfirmPagePhotoFilter"
                                    SmileMeCameraContract.CameraDecorType.FILTER -> "SmileMeConfirmPageMovieFilter"
                                }
                            )
                        }
                    }
                },
                centerButton = {
                    SmileMeRecordSection(
                        progress = state.recordingProgress,
                        icon = if (state.recordingProgress == null) Icons.Default.TouchApp else null,
                        desc = "SmileMeCameraPageTouchApp",
                        recordColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                        progressColor = MaterialTheme.colors.primary,
                        title = if (state.recordingProgress == null) stringResource(R.string.smile_me_button_info) else "",
                        onClick = {
                            dispatch(
                                SmileMeCameraEvent.OnClickRecordEvent(
                                    context.getCacheDir(SMILE_ME_CACHE_DIR).absolutePath
                                )
                            )
                        }
                    )
                },
                rightButton = {
                    if (state.recordingProgress == null) {
                        IconButton(onClick = { dispatch(SmileMeCameraEvent.OnClickCameraFacingEvent) }) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                imageVector = Icons.Default.Cameraswitch,
                                contentDescription = "SmileMeConfirmPageCameraswitch"
                            )
                        }
                    }
                },
            )
        }
    }


}

private const val SMILE_ME_CACHE_DIR = "smileMeCache"

private fun Context.getCacheDir(folderName: String): File {
    val folderPath = if (folderName.isNotEmpty()) {
        cacheDir.absolutePath + File.separator + folderName
    } else {
        cacheDir.absolutePath
    }

    val folder = File(folderPath)
    if (!folder.exists()) {
        folder.mkdirs()
    }

    return folder
}

private fun Context.deleteTempFile(parent: String = "") {
    val folderPath = cacheDir.absolutePath + File.separator + parent
    val folder = File(folderPath)
    if (folder.exists()) {
        folder.deleteRecursively()
    }
}
