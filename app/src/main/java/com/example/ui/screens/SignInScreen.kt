package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AsalCardWhite
import com.example.ui.theme.AsalError
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary
import com.example.ui.theme.AsalSoftBgBottom
import com.example.ui.theme.AsalSoftBgCenter
import com.example.ui.theme.AsalSoftBgTop
import com.example.ui.theme.AsalSuccess
import com.example.ui.viewmodel.AsalAbadViewModel
import com.example.ui.viewmodel.AuthUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignInScreen(
    viewModel: AsalAbadViewModel,
    onContinueAsGuest: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val authUiState by viewModel.authUiState.collectAsState()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Login, 1: Register
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var confirmPasswordText by remember { mutableStateOf("") }
    var displayNameText by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    LaunchedEffect(selectedTab) {
        viewModel.clearAuthError()
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            AsalSoftBgTop,
                            AsalSoftBgCenter,
                            AsalSoftBgBottom
                        )
                    )
                )
                .statusBarsPadding()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header & Branding
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(AsalPurplePrimary, Color(0xFFD946EF))
                            )
                        )
                        .border(3.dp, AsalGold, CircleShape)
                        .shadow(8.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🍯", fontSize = 42.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "عسل آباد",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AsalPurpleDark
                    )
                    Text("🐝", fontSize = 24.sp)
                }

                Text(
                    text = "پیام‌رسان، چت و بازی‌های آنلاین",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = AsalPurplePrimary
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AsalPurpleDark,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Text(
                        text = "👑 با مدیریت پریا چش رنگی",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalGoldLight,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Auth Form Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = AsalCardWhite)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Tabs: Sign In / Register
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = Color(0xFFF3E8FF),
                            contentColor = AsalPurplePrimary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp)),
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    height = 3.dp,
                                    color = AsalPurplePrimary
                                )
                            }
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = {
                                    Text(
                                        text = "ورود به حساب",
                                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = {
                                    Text(
                                        text = "ثبت‌نام جدید",
                                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // State Feedback Messages
                        AnimatedVisibility(
                            visible = authUiState is AuthUiState.Error,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            val errorMessage = (authUiState as? AuthUiState.Error)?.message ?: ""
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFEE2E2),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF87171)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = "خطا",
                                        tint = AsalError,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = errorMessage,
                                        fontSize = 12.sp,
                                        color = Color(0xFF991B1B),
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.clearAuthError() },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "بستن",
                                            tint = Color(0xFF991B1B)
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = authUiState is AuthUiState.Success,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            val successMessage = (authUiState as? AuthUiState.Success)?.message ?: ""
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFD1FAE5),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF34D399)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🎉", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = successMessage,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF065F46)
                                    )
                                }
                            }
                        }

                        // Form Inputs
                        if (selectedTab == 1) {
                            // Register: Display Name
                            OutlinedTextField(
                                value = displayNameText,
                                onValueChange = { displayNameText = it },
                                label = { Text("نام نمایشی یا لقب") },
                                placeholder = { Text("مثلاً: پریای مهربان، شاهین...") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = AsalPurplePrimary)
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AsalPurplePrimary,
                                    focusedLabelColor = AsalPurplePrimary
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_name_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Email Field (Common to Login & Register)
                        OutlinedTextField(
                            value = emailText,
                            onValueChange = { emailText = it },
                            label = { Text("آدرس ایمیل") },
                            placeholder = { Text("you@example.com") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = AsalPurplePrimary)
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AsalPurplePrimary,
                                focusedLabelColor = AsalPurplePrimary
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_email_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Password Field
                        OutlinedTextField(
                            value = passwordText,
                            onValueChange = { passwordText = it },
                            label = { Text("رمز عبور") },
                            placeholder = { Text("حداقل ۶ کاراکتر") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = AsalPurplePrimary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (passwordVisible) "مخفی‌سازی رمز" else "نمایش رمز",
                                        tint = Color.Gray
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AsalPurplePrimary,
                                focusedLabelColor = AsalPurplePrimary
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = if (selectedTab == 1) ImeAction.Next else ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                                onDone = {
                                    focusManager.clearFocus()
                                    if (selectedTab == 0) {
                                        viewModel.signInWithEmail(emailText, passwordText)
                                    }
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_password_input")
                        )

                        if (selectedTab == 1) {
                            Spacer(modifier = Modifier.height(12.dp))

                            // Confirm Password Field
                            OutlinedTextField(
                                value = confirmPasswordText,
                                onValueChange = { confirmPasswordText = it },
                                label = { Text("تکرار رمز عبور") },
                                placeholder = { Text("رمز عبور را دوباره وارد کنید") },
                                leadingIcon = {
                                    Icon(Icons.Default.LockReset, contentDescription = null, tint = AsalPurplePrimary)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                        Icon(
                                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = if (confirmPasswordVisible) "مخفی‌سازی رمز" else "نمایش رمز",
                                            tint = Color.Gray
                                        )
                                    }
                                },
                                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AsalPurplePrimary,
                                    focusedLabelColor = AsalPurplePrimary
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                        viewModel.signUpWithEmail(
                                            emailText,
                                            passwordText,
                                            confirmPasswordText,
                                            displayNameText
                                        )
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_confirm_password_input")
                            )
                        }

                        // Forgot Password Link in Login Tab
                        if (selectedTab == 0) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                TextButton(
                                    onClick = { showForgotPasswordDialog = true },
                                    modifier = Modifier.padding(0.dp)
                                ) {
                                    Text(
                                        text = "رمز عبور را فراموش کرده‌اید؟",
                                        fontSize = 11.sp,
                                        color = AsalPurplePrimary
                                    )
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // Primary Action Button (Login or Register)
                        val isLoading = authUiState is AuthUiState.Loading

                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                if (selectedTab == 0) {
                                    viewModel.signInWithEmail(emailText, passwordText)
                                } else {
                                    viewModel.signUpWithEmail(
                                        emailText,
                                        passwordText,
                                        confirmPasswordText,
                                        displayNameText
                                    )
                                }
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("auth_submit_button")
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.5.dp,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (selectedTab == 0) "در حال بررسی ورود..." else "در حال ایجاد حساب...",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = if (selectedTab == 0) "ورود به عسل آباد 🍯" else "ثبت نام و شروع چت 🐝",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Divider with "یا"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE9D5FF))
                            Text(
                                text = "یا",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE9D5FF))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Guest / Instant Login Button
                        OutlinedButton(
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.signInAsGuest(displayNameText.ifBlank { null })
                                onContinueAsGuest()
                            },
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, AsalPurplePrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("auth_guest_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("🌸", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ورود به عنوان مهمان (آزمایشی و سریع)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AsalPurplePrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Feature Highlights Pills
                Text(
                    text = "امکانات ویژه در عسل آباد:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AsalPurpleDark,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AuthFeaturePill(emoji = "💬", text = "چت‌روم‌های زنده و پرجمعیت")
                    AuthFeaturePill(emoji = "🎲", text = "بازی‌های جذاب ۲ نفره")
                    AuthFeaturePill(emoji = "📞", text = "تماس تصویری و ویس‌کال")
                    AuthFeaturePill(emoji = "💎", text = "الماس و جانوس قابل تبدیل")
                    AuthFeaturePill(emoji = "👑", text = "حساب‌های تاییدشده و VIP")
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Security & Firebase Notice
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEDE4FA)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🔒", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "احراز هویت ایمن با Firebase Auth",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark
                            )
                            Text(
                                text = "اطلاعات حساب کاربری شما به صورت رمزنگاری‌شده محافظت می‌شود.",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Forgot Password Dialog
        if (showForgotPasswordDialog) {
            Dialog(onDismissRequest = { showForgotPasswordDialog = false }) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AsalCardWhite),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔑", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "بازیابی رمز عبور",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "برای بازیابی حساب خود، می‌توانید با ایمیل ثبت‌شده مجدداً درخواست ارسال کنید یا با مدیریت عسل آباد در ارتباط باشید.",
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showForgotPasswordDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("متوجه شدم", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthFeaturePill(emoji: String, text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.9f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDD2F5)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = AsalPurpleDark
            )
        }
    }
}
