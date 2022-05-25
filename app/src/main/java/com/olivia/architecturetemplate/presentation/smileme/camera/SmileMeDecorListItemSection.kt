package com.olivia.architecturetemplate.presentation.smileme.camera


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.bemily.messenger.smileme.domain.model.SmileMeDecor
import com.olivia.architecturetemplate.R
import com.olivia.architecturetemplate.presentation.ui.theme.SampleAppTheme

/**
 * # bemily_messenger-app-android
 * # SmileMeDecorListItemSection
 * @author M.Y.Seong
 * @since 2022-03-03
 */

@Composable
fun SmileMeDecorListItemSection(
    modifier: Modifier = Modifier,
    smileMeDecor: SmileMeDecor,
    isSelected: Boolean = false
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colors.onPrimary,
    ) {

        Box(
            modifier = modifier
                .padding(2.dp)
                .size(
                    SMILE_ME_LIST_ITEM_WIDTH.dp,
                    SMILE_ME_LIST_ITEM_HEIGHT.dp
                )
                .clip(RoundedCornerShape(SMILE_ME_LIST_ITEM_RADIUS.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SMILE_ME_LIST_ITEM_IMAGE_HEIGHT.dp),
                    painter = rememberImagePainter(data = smileMeDecor.thumb),
                    contentScale = ContentScale.Crop,
                    contentDescription = "SmileMeListItemSectionImage"
                )

                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(2.dp)
                            .wrapContentSize(),
                        text = stringResource(id = smileMeDecor.name),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(SMILE_ME_LIST_ITEM_CHECK_SIZE.dp)
                            .border(1.dp, MaterialTheme.colors.onBackground, CircleShape),
                        painter = rememberVectorPainter(image = Icons.Default.Check),
                        tint = MaterialTheme.colors.onBackground,
                        contentDescription = "SmileMeListItemSectionImageCheck"
                    )
                }
            }
        }
    }
}

private const val SMILE_ME_LIST_ITEM_WIDTH = 52
private const val SMILE_ME_LIST_ITEM_HEIGHT = 74
private const val SMILE_ME_LIST_ITEM_RADIUS = 4

private const val SMILE_ME_LIST_ITEM_IMAGE_HEIGHT = 50

private const val SMILE_ME_LIST_ITEM_CHECK_SIZE = 18

@Preview("light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SmileMeListItemSectionPreview() {
    SampleAppTheme() {
        Surface() {

            Column() {
                SmileMeDecorListItemSection(
                    smileMeDecor = SmileMeDecor(
                        id = 0,
                        name = R.string.smile_me_sticker_angry,
                        thumb = R.drawable.smile_me_angry_thumb
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                SmileMeDecorListItemSection(
                    smileMeDecor = SmileMeDecor(
                        id = 0,
                        name = R.string.smile_me_sticker_angry,
                        thumb = R.drawable.smile_me_angry_thumb
                    ),
                    isSelected = true
                )
            }
        }
    }
}