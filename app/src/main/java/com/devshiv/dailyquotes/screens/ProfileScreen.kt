package com.devshiv.dailyquotes.screens

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GifBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.devshiv.dailyquotes.App
import com.devshiv.dailyquotes.MainActivity
import com.devshiv.dailyquotes.R
import com.devshiv.dailyquotes.model.DialogModel
import com.devshiv.dailyquotes.ui.theme.PrimaryLightColor
import com.devshiv.dailyquotes.utils.ApiState
import com.devshiv.dailyquotes.utils.Constants
import com.devshiv.dailyquotes.utils.Constants.TAG
import com.devshiv.dailyquotes.utils.Dialog
import com.devshiv.dailyquotes.utils.LoadingDialog
import com.devshiv.dailyquotes.utils.Utils
import com.devshiv.dailyquotes.viewmodels.ProfileViewModel
import com.google.gson.JsonObject


@Composable
fun ProfileScreen(
    onNavigateRequired: (screen: String) -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val context = LocalContext.current as MainActivity
    val profileData by viewModel.profileData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(2.dp))

        Image(
            painter = painterResource(id = R.drawable.user), contentDescription = "User Profile",
            modifier = Modifier
                .size(140.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "@" + profileData.username ?: "",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.main_font)),
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        ProfileItem(icon = Icons.Filled.Star, title = "Rate Us") {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
            context.startActivity(intent)
        }

        ProfileItem(icon = Icons.Filled.Share, title = "Share App") {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
                var shareMessage = "Download Now\n\n"
                shareMessage =
                    shareMessage + "https://play.google.com/store/apps/details?id=" + context.packageName
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                context.startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                Toast.makeText(context, "Unable To Share App!", Toast.LENGTH_SHORT).show()
            }
        }
        ProfileItem(icon = Icons.Filled.Lock, title = "Privacy Policy") {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(App.settings.privacy_policy)
            context.startActivity(intent)
        }
        ProfileItem(icon = Icons.Filled.Message, title = "Telegram") {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(App.settings.telegram_link)
            context.startActivity(intent)
        }
        ProfileItem(icon = Icons.Filled.Logout, title = "Logout") {
            onLogout()
        }
    }
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    title: String,
    spaceTop: Dp = 10.dp,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(PrimaryLightColor, shape = RoundedCornerShape(10.dp))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = icon,
            contentDescription = "",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .padding(start = 12.dp)
                .size(30.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            modifier = Modifier
                .weight(1f),
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.main_font)),
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
        Image(
            imageVector = Icons.Filled.NavigateNext,
            contentDescription = "",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .padding(start = 12.dp)
                .size(40.dp)
        )
    }

    Spacer(modifier = Modifier.height(spaceTop))
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen({}, {})
}