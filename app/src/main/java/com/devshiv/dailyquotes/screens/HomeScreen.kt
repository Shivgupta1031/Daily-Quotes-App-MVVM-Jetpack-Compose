package com.devshiv.dailyquotes.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devshiv.dailyquotes.MainActivity
import com.devshiv.dailyquotes.R
import com.devshiv.dailyquotes.ui.theme.AccentColor
import com.devshiv.dailyquotes.ui.theme.PrimaryColor
import com.devshiv.dailyquotes.ui.theme.PrimaryDarkColor
import com.devshiv.dailyquotes.ui.theme.PrimaryLightColor

@Composable
fun HomeScreen(
    onNavigateRequired: (screen: String) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current as MainActivity

    BackHandler(enabled = true) {
        context.finish()
    }

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    val clickScaleSelected = 1f
    val clickScaleDeSelected = 0.8f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryDarkColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.main_font)),
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                1 -> {
                    ProfileScreen(
                        onNavigateRequired,
                        onLogout
                    )
                }

                else -> {
                    QuotesScreen(onNavigateRequired)
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(58.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(10.dp),
                    spotColor = Color.Black
                )
                .background(color = PrimaryColor, shape = RoundedCornerShape(10.dp))
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 8.dp, bottom = 8.dp)
                    .width(2.dp)
                    .background(
                        PrimaryLightColor
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        selectedTab = 0
                    }
                    .animateContentSize(tween(300))
                    .graphicsLayer(
                        scaleX = if (selectedTab == 0) {
                            clickScaleSelected
                        } else {
                            clickScaleDeSelected
                        }, scaleY = if (selectedTab == 0) {
                            clickScaleSelected
                        } else {
                            clickScaleDeSelected
                        }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_quote_open),
                    contentDescription = "",
                    colorFilter = if (selectedTab == 0) {
                        ColorFilter.tint(AccentColor)
                    } else {
                        ColorFilter.tint(Color.White)
                    },
                    modifier = Modifier
                        .size(26.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Quotes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.main_font)),
                    fontWeight = FontWeight.SemiBold,
                    color = if (selectedTab == 0) {
                        AccentColor
                    } else {
                        Color.White
                    },
                    textAlign = TextAlign.Center
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 5.dp, bottom = 5.dp)
                    .width(2.dp)
                    .background(PrimaryLightColor)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        selectedTab = 1
                    }
                    .animateContentSize(tween(300))
                    .graphicsLayer(
                        scaleX = if (selectedTab == 1) {
                            clickScaleSelected
                        } else {
                            clickScaleDeSelected
                        }, scaleY = if (selectedTab == 1) {
                            clickScaleSelected
                        } else {
                            clickScaleDeSelected
                        }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "",
                    colorFilter = if (selectedTab == 1) {
                        ColorFilter.tint(AccentColor)
                    } else {
                        ColorFilter.tint(Color.White)
                    },
                    modifier = Modifier
                        .size(26.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.main_font)),
                    fontWeight = FontWeight.SemiBold,
                    color = if (selectedTab == 1) {
                        AccentColor
                    } else {
                        Color.White
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHome() {
    HomeScreen({}, {})
}