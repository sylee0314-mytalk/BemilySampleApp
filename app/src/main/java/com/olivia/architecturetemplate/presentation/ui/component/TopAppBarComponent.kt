package com.olivia.architecturetemplate.presentation.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * # bemily_messenger-ui-android
 * # top app bar component
 * @author hsjun85
 * @since 2022-02-23
 */

/**
 * # TopAppBarComponent
 * top app bar component
 *
 * @param modifier TopAppBarComponent modifier
 * @param title title of the TopAppBar
 * @param navigationIcon navigation icon of the TopAppBar
 * @param actions actions icon of the TopAppBar
 * @param backgroundColor background color for the TopAppBar
 * @param contentColor content color for the TopAppBar
 * @param elevation elevation of TopAppBar
 */
@Composable
fun TopAppBarComponent(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = TopAppBarElevation
) {
    TopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    )
}

@Composable
fun TopAppBarComponent(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentPadding: PaddingValues = AppBarDefaults.ContentPadding,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = TopAppBarElevation
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        contentPadding = contentPadding,
    ) {
        if (navigationIcon == null) {
            Spacer(TitleInsetWithoutIcon)
        } else {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                    content = navigationIcon
                )
            }
        }

        Row(
            Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.h6) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                    content = title
                )
            }
        }

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Row(
                Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }
    }
}

val TopAppBarElevation = 0.dp
private val AppBarHorizontalPadding = 4.dp
private val TitleInsetWithoutIcon = Modifier.width(16.dp - AppBarHorizontalPadding)
