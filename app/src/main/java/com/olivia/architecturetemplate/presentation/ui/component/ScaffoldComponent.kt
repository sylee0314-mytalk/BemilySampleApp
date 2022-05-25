package com.olivia.architecturetemplate.presentation.ui.component


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.google.accompanist.insets.statusBarsPadding

/**
 * # bemily_messenger-ui-android
 * # scaffold component
 * @author hsjun85
 * @since 2022-02-23
 */

/**
 * # ScaffoldComponent
 * scaffold component
 *
 * @param modifier ScaffoldComponent modifier
 * @param scaffoldState state of this scaffold
 * @param layoutDirection layout direction of this scaffold
 * @param backgroundColor background of the scaffold
 * @param drawerContent content of the Drawer sheet that can be pulled from the left side
 * @param modifier TopAppBarComponent modifier
 * @param title title of the TopAppBar
 * @param navigationIcon navigation icon of the TopAppBar
 * @param actions actions icon of the TopAppBar
 * @param elevation elevation of TopAppBar
 * @param bottomBar bottom app bar of the page
 * @param snackbarHost snackbar host bar of the page
 * @param content content of the page
 *
 * @see <a href="">figma</a>
 */
@Composable
fun ScaffoldComponent(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    elevation: Dp = TopAppBarElevation,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (BoxScope.(SnackbarHostState) -> Unit) = { SnackbarHost(it) },
    content: @Composable (PaddingValues) -> Unit
) {
    ScaffoldComponent(
        modifier = modifier,
        scaffoldState = scaffoldState,
        layoutDirection = layoutDirection,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        drawerContent = drawerContent,
        topBar = {
            TopAppBarComponent(
                modifier = Modifier.fillMaxWidth(),
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                elevation = elevation,
            )
        },
        bottomBar = bottomBar,
        snackbarHost = {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize(),
            ) {
                snackbarHost(it)
            }
        },
        content = content
    )
}

/**
 * # ScaffoldComponent
 * scaffold component
 *
 * @param modifier ScaffoldComponent modifier
 * @param scaffoldState state of this scaffold
 * @param layoutDirection layout direction of this scaffold
 * @param backgroundColor background of the scaffold
 * @param drawerContent content of the Drawer sheet that can be pulled from the left side
 * @param topBar top app bar of the page
 * @param bottomBar bottom app bar of the page
 * @param snackbarHost snackbar host bar of the page
 * @param content content of the page
 */
@Composable
fun ScaffoldComponent(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    content: @Composable (PaddingValues) -> Unit
) {
    val oppositeLayoutDirection = when (layoutDirection) {
        LayoutDirection.Ltr -> LayoutDirection.Ltr
        LayoutDirection.Rtl -> LayoutDirection.Ltr
    }

    WithLayoutDirection(layoutDirection) {
        Scaffold(
            modifier = modifier,
            scaffoldState = scaffoldState,
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            drawerContent = drawerContent?.let {
                {
                    WithLayoutDirection(oppositeLayoutDirection) {
                        it()
                    }
                }
            },
            topBar = {
                WithLayoutDirection(oppositeLayoutDirection, topBar)
            },
            bottomBar = {
                WithLayoutDirection(oppositeLayoutDirection, bottomBar)
            },
            snackbarHost = snackbarHost,
            content = {
                WithLayoutDirection(oppositeLayoutDirection) {
                    content(it)
                }
            })
    }
}

@Composable
private fun WithLayoutDirection(
    layoutDirection: LayoutDirection,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection
    ) {
        content()
    }
}