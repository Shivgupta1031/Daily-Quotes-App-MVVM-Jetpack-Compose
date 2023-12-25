package com.devshiv.dailyquotes.screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import com.devshiv.dailyquotes.App
import com.devshiv.dailyquotes.MainActivity
import com.devshiv.dailyquotes.R
import com.devshiv.dailyquotes.databinding.NativeAdViewBinding
import com.devshiv.dailyquotes.model.QuotesModel
import com.devshiv.dailyquotes.utils.ApiState
import com.devshiv.dailyquotes.utils.Constants
import com.devshiv.dailyquotes.utils.Constants.TAG
import com.devshiv.dailyquotes.utils.LoadingDialog
import com.devshiv.dailyquotes.utils.ProgressIndicatorLoading
import com.devshiv.dailyquotes.utils.Timer
import com.devshiv.dailyquotes.utils.randomGradient
import com.devshiv.dailyquotes.viewmodels.QuotesViewModel
import com.google.gson.JsonObject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuotesScreen(
    onNavigateRequired: (screen: String) -> Unit
) {
    val viewModel: QuotesViewModel = hiltViewModel()
    var showLoading by remember { mutableStateOf(false) }
    var quotesData by remember { mutableStateOf<List<QuotesModel.Quotes>>(emptyList()) }
    var isVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current as MainActivity

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        quotesData.size
    }

    LaunchedEffect(key1 = true, block = {
        isVisible = true
        viewModel.quotesData.collect {
            when (it) {
                is ApiState.Empty -> {
                    Log.d(TAG, "QuotesScreen: Empty")
                }

                is ApiState.Loading -> {
                    Log.d(TAG, "QuotesScreen: Loading")
                    showLoading = true
                }

                is ApiState.Success<*> -> {
                    Log.d(TAG, "QuotesScreen: Success")
                    showLoading = false
                    quotesData = (it.data as QuotesModel).data
                }

                is ApiState.Failure -> {
                    Log.d(TAG, "QuotesScreen: Failure ${it.msg}")
                    showLoading = false
                }
            }
        }
    })

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (showLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ProgressIndicatorLoading(
                    progressIndicatorSize = 60.dp,
                    progressIndicatorColor = Color.White
                )
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize(),
                pageSize = PageSize.Fill,
                pageSpacing = 8.dp
            ) { page ->
                QuoteItem(
                    pagerState = pagerState,
                    curPage = pagerState.currentPage,
                    quote = quotesData[page]
                )
            }
        }
    }
}

@SuppressLint("NewApi")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuoteItem(
    pagerState: PagerState,
    quote: QuotesModel.Quotes,
    curPage: Int = 0,
) {

    val color = randomGradient()
    val gradientBrush = remember {
        color
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 4.dp, 10.dp, 4.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = Color.Black
            )
            .background(brush = gradientBrush, shape = RoundedCornerShape(8.dp))
    ) {

        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_quote_close),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(70.dp)
                    .rotate(180f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 26.dp, end = 20.dp),
                text = quote.quote,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.main_font)),
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 22.dp),
                text = "~${quote.written_by}",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                textAlign = TextAlign.End
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { BottomOptView(quote.quote) }
    }
}

@Composable
fun BottomOptView(quote: String) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(color = Color.Black.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    val clipboardManager =
                        context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData: ClipData = ClipData.newPlainText("text", quote)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast
                        .makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT)
                        .show()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Filled.ContentCopy,
                contentDescription = "Copy",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(26.dp)
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 6.dp, bottom = 6.dp)
                .width(2.dp)
                .background(Color.Black.copy(alpha = 0.2f))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(
                        Intent.EXTRA_SUBJECT,
                        context.getString(R.string.app_name)
                    )
                    intent.putExtra(Intent.EXTRA_TEXT, quote)
                    context.startActivity(
                        Intent.createChooser(
                            intent,
                            "Share Using"
                        )
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(26.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun QuotesScreenPreview() {
    QuoteItem(
        rememberPagerState {
            10
        },
        QuotesModel.Quotes(quote = "Title", written_by = "hello"),
    )
}