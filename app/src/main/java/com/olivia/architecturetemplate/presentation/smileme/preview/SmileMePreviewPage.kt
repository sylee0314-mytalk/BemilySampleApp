package com.olivia.architecturetemplate.presentation.smileme.preview

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.olivia.architecturetemplate.R
import com.olivia.architecturetemplate.presentation.smileme.common.LockScreenOrientation
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeBottomComponent
import com.olivia.architecturetemplate.presentation.smileme.common.SmileMeRecordSection
import com.olivia.architecturetemplate.presentation.smileme.common.use
import com.olivia.architecturetemplate.presentation.ui.component.ScaffoldComponent
import com.olivia.architecturetemplate.presentation.ui.theme.SampleAppTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

/**
 * # bemily_messenger-app-android
 * # Smile me preview page
 * @author mjkim
 * @since 2022-03-02
 */

@Composable
fun SmileMePreviewPage(
    onClickClose: () -> Unit = {},
    onClickChange: () -> Unit = {}
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val viewModel: SmileMePreviewViewModel = hiltViewModel()
    val (state, effect, dispatch) = use(viewModel)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val smileMeInfo = stringResource(R.string.smile_me_message_info)
    val smileMeSave = stringResource(R.string.smile_me_message_save)

    LaunchedEffect(state.saveState) {
        when (state.saveState) {
            SmileMeSaveState.SMILE_ME_SAVING -> {
                /* TODO : Loading */
            }
            SmileMeSaveState.SMILE_ME_SAVED -> {
                Toast.makeText(context, smileMeSave, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        effect.onEach {
            if (it is SmileMePreviewEffect.CreatedBitmap) {
                AnimationDrawable().apply {
                    it.bitmapList.forEach { bitmap ->
                        addFrame(BitmapDrawable(context.resources, bitmap), ANIMATION_DURATION)
                    }

                    start()

                    dispatch(SmileMePreviewEvent.CreatedDrawableEvent(this))
                }
            }
        }.collect()
    }

    ScaffoldComponent(
        modifier = Modifier,
        scaffoldState = scaffoldState,
        navigationIcon = {
            IconButton(onClick = onClickClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "SmileMeConfirmPageClose",
                    tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    scope.launch {
                        scaffoldState
                            .snackbarHostState
                            .showSnackbar(message = smileMeInfo)
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
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                SmileMePreviewViewSection(animationDrawable = state.animationDrawable)
            }

            Spacer(modifier = Modifier.height(86.dp))

            SmileMeBottomComponent(
                leftButton = {
                    IconButton(onClick = onClickChange) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            imageVector = when (state.saveState) {
                                SmileMeSaveState.SMILE_ME_SAVED -> Icons.Default.Repeat
                                else -> Icons.Default.Restore
                            },
                            contentDescription = when (state.saveState) {
                                SmileMeSaveState.SMILE_ME_SAVED -> "SmileMeConfirmPageRepeat"
                                else -> "SmileMeConfirmPageRestore"
                            }
                        )
                    }
                },
                centerButton = {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colors.primary
                    ) {
                        when (state.saveState) {
                            SmileMeSaveState.SMILE_ME_SAVING -> {
                                SmileMeRecordSection(
                                    progress = 100f,
                                    enabled = false
                                )
                            }
                            SmileMeSaveState.SMILE_ME_SAVED -> {
                                SmileMeRecordSection(
                                    icon = Icons.Default.Check,
                                    desc = "SmileMeConfirmPageCheck",
                                    enabled = false
                                )
                            }
                            else -> {
                                SmileMeRecordSection(
                                    icon = Icons.Default.SaveAlt,
                                    desc = "SmileMeConfirmPageSaveAlt",
                                    title = stringResource(R.string.smile_me_save),
                                    onClick = {
                                        dispatch(
                                            SmileMePreviewEvent.SaveSmileMeEvent(
                                                bitmapList = state.bitmapList,
                                                cachePath = context.getCacheDir(SMILE_ME_CACHE_DIR).absolutePath
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun SmileMePreviewViewSection(
    modifier: Modifier = Modifier,
    animationDrawable: AnimationDrawable? = null
) {
// TODO : 임시로 Box 설정. Smile me 보여지도록 설정 필요
    Box(
        modifier = modifier
            .background(color = Color.Gray)
            .aspectRatio(1f)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberDrawablePainter(drawable = animationDrawable),
            contentDescription = "SmileMePreviewViewSectionImage"
        )
    }
}

private const val ANIMATION_DURATION = 125
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

@Preview("light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SmileMePreviewPagePreview() {
    SampleAppTheme() {
        SmileMePreviewPage()
    }
}