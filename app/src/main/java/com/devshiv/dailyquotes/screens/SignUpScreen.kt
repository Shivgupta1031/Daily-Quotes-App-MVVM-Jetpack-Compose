package com.devshiv.dailyquotes.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devshiv.dailyquotes.App
import com.devshiv.dailyquotes.R
import com.devshiv.dailyquotes.model.DialogModel
import com.devshiv.dailyquotes.model.LoginResponse
import com.devshiv.dailyquotes.ui.theme.AccentColor
import com.devshiv.dailyquotes.ui.theme.AccentLightColor
import com.devshiv.dailyquotes.ui.theme.PrimaryLightColor
import com.devshiv.dailyquotes.utils.ApiState
import com.devshiv.dailyquotes.utils.Constants
import com.devshiv.dailyquotes.utils.Dialog
import com.devshiv.dailyquotes.utils.LoadingDialog
import com.devshiv.dailyquotes.utils.PasswordTextField
import com.devshiv.dailyquotes.viewmodels.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(onNavigateRequired: (screen: String) -> Unit) {

    val viewModel: SignUpViewModel = hiltViewModel()

    val usernameError by viewModel.usernameState.collectAsState()
    val passwordError by viewModel.passwordState.collectAsState()
    val emailError by viewModel.emailState.collectAsState()
    val numberError by viewModel.numberState.collectAsState()

    var sizeState by remember { mutableStateOf(20.dp) }
    var usernameET by remember {
        mutableStateOf("")
    }
    var passwordET by remember {
        mutableStateOf("")
    }
    var emailET by remember {
        mutableStateOf("")
    }
    var numberET by remember {
        mutableStateOf("")
    }
    val size by animateDpAsState(
        targetValue = sizeState, tween(
            durationMillis = 250, easing = LinearEasing
        ), label = ""
    )

    var showLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(DialogModel()) }

    BackHandler(enabled = true) {
        onNavigateRequired(Constants.LOGIN_NAV+"/false")
    }

    LaunchedEffect(key1 = true, block = {
        sizeState = 120.dp

        viewModel.signUpResponse.collect {
            when (it) {
                is ApiState.Empty -> {
                    Log.d(Constants.TAG, "SignUpScreen: Empty")
                }

                is ApiState.Loading -> {
                    Log.d(Constants.TAG, "SignUpScreen: Loading")
                    showLoading = true
                }

                is ApiState.Success<*> -> {
                    Log.d(Constants.TAG, "SignUpScreen: Success ${it.data.toString()}")
                    showLoading = false
                    val success: Boolean = (it.data as LoginResponse).status == "success"
                    val message: String = (it.data as LoginResponse).message
                    showDialog.apply {
                        this.success = success
                        title = "Sign Up"
                        description = message
                        this.showDialog = true
                    }
                    if (success) {
                        it.data.user_details?.let { user ->
                            App.curUser = user.username
                            viewModel.saveData(user)
                        }
                    }
                }

                is ApiState.Failure -> {
                    Log.d(Constants.TAG, "SignUpScreen: Failure ${it.msg}")
                    showLoading = false
                    showDialog.apply {
                        this.success = false
                        title = "Sign Up"
                        description = "Some Error Occurred"
                        this.showDialog = true
                    }
                }
            }
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.login_bg), contentScale = ContentScale.FillBounds
            )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, top = 20.dp),
            text = "Sign Up",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 34.sp,
            fontFamily = FontFamily(Font(R.font.main_font)),
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(60.dp))
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "App Icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = PrimaryLightColor)
                .shadow(10.dp, ambientColor = Color.Black)
                .align(Alignment.CenterHorizontally)
                .animateContentSize(
                    tween(500)
                )
        ) {
            TextField(
                value = usernameET,
                onValueChange = { newText ->
                    usernameET = newText
                },
                modifier = Modifier
                    .background(color = PrimaryLightColor)
                    .fillMaxWidth(),

                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.main_font)),
                    color = Color.White
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = PrimaryLightColor,
                    textColor = Color.White,
                ),

                label = {
                    Text(
                        text = "Username",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.main_font)),
                        color = Color.White
                    )
                },
                isError = usernameError,
                supportingText = {
                    if (usernameError) {
                        Text(
                            text = "* Required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            modifier = Modifier.padding(start = 6.dp, top = 0.dp, bottom = 2.dp)
                        )
                    }
                },
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = PrimaryLightColor)
                .shadow(10.dp, ambientColor = Color.Black)
                .align(Alignment.CenterHorizontally)
                .animateContentSize(
                    tween(500)
                )
        ) {
            TextField(
                value = emailET,
                onValueChange = { newText ->
                    emailET = newText
                },
                modifier = Modifier
                    .background(color = PrimaryLightColor)
                    .fillMaxWidth(),

                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.main_font)),
                    color = Color.White
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = PrimaryLightColor,
                    textColor = Color.White,
                ),

                label = {
                    Text(
                        text = "Email Address",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.main_font)),
                        color = Color.White
                    )
                },
                isError = emailError,
                supportingText = {
                    if (emailError) {
                        Text(
                            text = "Enter Valid Email Address",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            modifier = Modifier.padding(start = 6.dp, top = 0.dp, bottom = 2.dp)
                        )
                    }
                },
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = PrimaryLightColor)
                .shadow(10.dp, ambientColor = Color.Black)
                .align(Alignment.CenterHorizontally)
                .animateContentSize(
                    tween(500)
                )
        ) {
            TextField(
                value = numberET,
                onValueChange = { newText ->
                    numberET = newText
                },
                modifier = Modifier
                    .background(color = PrimaryLightColor)
                    .fillMaxWidth(),

                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.main_font)),
                    color = Color.White
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = PrimaryLightColor,
                    textColor = Color.White,
                ),

                label = {
                    Text(
                        text = "Phone Number",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.main_font)),
                        color = Color.White
                    )
                },
                isError = numberError,
                supportingText = {
                    if (numberError) {
                        Text(
                            text = "Enter Valid Phone Number",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            modifier = Modifier.padding(start = 6.dp, top = 0.dp, bottom = 2.dp)
                        )
                    }
                },
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(value = passwordET,
            onValueChange = { newText ->
                passwordET = newText
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PrimaryLightColor)
                .shadow(10.dp, ambientColor = Color.Black)
                .align(Alignment.CenterHorizontally)
                .animateContentSize(
                    tween(500)
                ),

            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.main_font)),
                color = Color.White
            ),
            isError = passwordError,
            error = "* Required",
            colors = TextFieldDefaults.textFieldColors(
                containerColor = PrimaryLightColor,
                textColor = Color.White,
            ),

            label = {
                Text(
                    text = "Password",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.main_font)),
                    color = Color.White
                )
            })

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.validateFields(usernameET, passwordET, emailET, numberET) },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .align(Alignment.CenterHorizontally),
            elevation = ButtonDefaults.buttonElevation(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentColor
            ),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues()
        ) {
            Text(
                text = "Continue",
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.main_font)),
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        val newAccountTxt = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White)) {
                append("Already Have An Account? ")
            }
            withStyle(
                style = SpanStyle(
                    color = AccentLightColor, fontWeight = FontWeight.Bold, fontSize = 20.sp
                )
            ) {
                append("Log In")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter
        ) {
            Text(text = newAccountTxt,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.main_font)),
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        onNavigateRequired(Constants.LOGIN_NAV+"/false")
                    })
        }
    }

    LoadingDialog(showLoading = showLoading)

    Dialog(showDialog = showDialog.showDialog,
        title = showDialog.title,
        description = showDialog.description,
        onClick = {
            if (showDialog.success) {
                onNavigateRequired(Constants.HOME_NAV)
            }
            showDialog = DialogModel()
        })

}