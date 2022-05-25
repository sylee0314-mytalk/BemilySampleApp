package com.olivia.architecturetemplate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.olivia.architecturetemplate.presentation.smileme.camera.SmileMeCameraPage
import com.olivia.architecturetemplate.presentation.smileme.preview.SmileMePreviewPage
import com.olivia.architecturetemplate.presentation.ui.theme.SampleAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainPage()
                }
            }
        }
    }
}


@Composable
fun MainPage(
    modifier: Modifier = Modifier,
) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = CHAT_ROOM_SMILE_ME
    ) {
        addSmileMeComposable(navController)
    }
}

private fun NavGraphBuilder.addSmileMeComposable(
    navController: NavHostController
) {
    composable(
        route = CHAT_ROOM_SMILE_ME,
    ) {
        SmileMeCameraPage(
            onRecordSuccess = { bitmapPathListJson ->
                navController.navigate(
                    "${CHAT_ROOM_SMILE_ME_PREVIEW}?path=${bitmapPathListJson}"
                )
            }
        )
    }

    composable(
        route = "${CHAT_ROOM_SMILE_ME_PREVIEW}?path={${SMILE_ME_BITMAP_PATH}}",
        arguments = listOf(navArgument(SMILE_ME_BITMAP_PATH) {
            type = NavType.StringType
        }),
    ) {
        SmileMePreviewPage(
            onClickClose = {
                navController.popBackStack(
                    route =CHAT_ROOM_SMILE_ME,
                    inclusive = true
                )
            },
            onClickChange = { navController.popBackStack() }
        )
    }
}

const val CHAT_ROOM_SMILE_ME = "smile_me"
const val CHAT_ROOM_SMILE_ME_PREVIEW = "$CHAT_ROOM_SMILE_ME/preview"
const val SMILE_ME_BITMAP_PATH = "smile_me_bitmap_path"

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SampleAppTheme {
        MainPage()
    }
}