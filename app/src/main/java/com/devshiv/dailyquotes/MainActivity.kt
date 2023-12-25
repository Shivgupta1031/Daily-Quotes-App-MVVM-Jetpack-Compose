package com.devshiv.dailyquotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devshiv.dailyquotes.screens.HomeScreen
import com.devshiv.dailyquotes.screens.LoginScreen
import com.devshiv.dailyquotes.screens.SignUpScreen
import com.devshiv.dailyquotes.screens.SplashScreen
import com.devshiv.dailyquotes.ui.theme.PrimaryDarkColor
import com.devshiv.dailyquotes.ui.theme.SoulVerseAppTheme
import com.devshiv.dailyquotes.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SoulVerseAppTheme {
                AppUI()
            }
        }
    }
}

@Composable
fun AppUI() {
    val navController = rememberNavController()
    val context = LocalContext.current as MainActivity

    BackHandler(enabled = true) {
        context.finish()
    }

    NavHost(navController = navController, startDestination = Constants.SPLASH_NAV,
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(500)
            )
        },
        modifier = Modifier.background(PrimaryDarkColor)) {
        composable(route = Constants.SPLASH_NAV) {
            SplashScreen {
                navController.navigate(it)
            }
        }
        composable(route = "${Constants.LOGIN_NAV}/{logout}") { navBackStack ->
            val logout = navBackStack.arguments?.getString("logout")?.toBoolean() ?: false
            LoginScreen(logout ?: false) {
                navController.navigate(it)
            }
        }
        composable(route = Constants.SIGN_UP_NAV) {
            SignUpScreen {
                navController.navigate(it)
            }
        }
        composable(route = Constants.HOME_NAV) {
            HomeScreen(onNavigateRequired = {
                navController.navigate(it)
            }, onLogout = {
                navController.navigate("${Constants.LOGIN_NAV}/true")
            })
        }
    }
}