package com.devshiv.dailyquotes.screens

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devshiv.dailyquotes.App
import com.devshiv.dailyquotes.MainActivity
import com.devshiv.dailyquotes.R
import com.devshiv.dailyquotes.db.entity.SettingsEntity
import com.devshiv.dailyquotes.utils.ApiState
import com.devshiv.dailyquotes.utils.Constants
import com.devshiv.dailyquotes.utils.Constants.TAG
import com.devshiv.dailyquotes.viewmodels.SplashViewModel
import com.onesignal.OneSignal
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateRequired: (screen: String) -> Unit) {
    val viewModel: SplashViewModel = hiltViewModel()

    var sizeState by remember { mutableStateOf(20.dp) }
    val size by animateDpAsState(
        targetValue = sizeState, tween(
            durationMillis = 500,
            easing = LinearEasing
        ), label = ""
    )

    val context = LocalContext.current as MainActivity

    LaunchedEffect(key1 = true, block = {
        sizeState = 160.dp
        viewModel.settings.collect {
            when (it) {
                is ApiState.Empty -> {
                    Log.d(TAG, "SplashScreen: Empty")
                }

                is ApiState.Loading -> {
                    Log.d(TAG, "SplashScreen: Loading")
                }

                is ApiState.Success<*> -> {
                    Log.d(TAG, "SplashScreen: Success")
                    viewModel.saveSettings(it.data as SettingsEntity)

                    OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
                    OneSignal.initWithContext(context)
                    OneSignal.setAppId(App.settings.one_signal_app_id)

                    delay(500L)
                    viewModel.checkUserLoginStatus().collect {
                        if (it) {
                            onNavigateRequired(Constants.HOME_NAV)
                        } else {
                            onNavigateRequired(Constants.LOGIN_NAV + "/false")
                        }
                    }
                }

                is ApiState.Failure -> {
                    Log.d(TAG, "SplashScreen: Failure ${it.msg}")
                }
            }
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.splash_bg),
                contentScale = ContentScale.FillBounds
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "App Icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(10.dp)),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier
                .padding(20.dp, 0.dp, 20.dp),
            text = "Shayari aur Quotes ka Magical Safar Shuru Karein!",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.main_font)),
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}