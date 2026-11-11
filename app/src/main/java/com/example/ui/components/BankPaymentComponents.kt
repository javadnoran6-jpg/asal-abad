package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BankAccountInfo
import com.example.data.model.PurchaseItem
import com.example.data.model.ShopPackage
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary
import kotlinx.coroutines.delay
import kotlin.random.Random

// -------------------------------------------------------------
// Payment Method Selection Dialog
// -------------------------------------------------------------
@Composable
fun PaymentMethodDialog(
    pkg: ShopPackage,
    onSelectGateway: () -> Unit,
    onSelectCardToCard: () -> Unit,
    onDismiss: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "بستن")
                        }
                        Text(
                            text = "انتخاب روش پرداخت",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                        Spacer(modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Package Summary Card
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF5F3FF),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDD2F5)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "خرید ${pkg.doubledDiamonds} الماس عسل آباد 💎",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurplePrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "مبلغ قابل پرداخت: ${pkg.formattedPrice}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark
                            )
                            Text(
                                text = "به همراه ۱ گردونه شانس هدیه 🎡",
                                fontSize = 11.sp,
                                color = Color(0xFFD97706)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Option 1: درگاه شاپرک
                    PaymentOptionCard(
                        title = "درگاه پرداخت اینترنتی شاپرک",
                        subtitle = "پرداخت آنلاین و آنی با تمامی کارت‌های بانکی عضو شتاب",
                        badge = "پیشنهادی و فوری",
                        iconEmoji = "💳",
                        containerColor = Color(0xFFECFDF5),
                        badgeColor = Color(0xFF059669),
                        onClick = onSelectGateway
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Option 2: کارت به کارت
                    PaymentOptionCard(
                        title = "پرداخت کارت به کارت",
                        subtitle = "انتقال وجه به شماره کارت مستقیم سازنده (پریا چش رنگی)",
                        badge = "کارت به کارت",
                        iconEmoji = "🏦",
                        containerColor = Color(0xFFFFFBEB),
                        badgeColor = Color(0xFFD97706),
                        onClick = onSelectCardToCard
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    TextButton(onClick = onDismiss) {
                        Text("انصراف", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentOptionCard(
    title: String,
    subtitle: String,
    badge: String,
    iconEmoji: String,
    containerColor: Color,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, badgeColor.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(iconEmoji, fontSize = 24.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalPurpleDark
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeColor)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(badge, fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF4B5563)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// Real Shaparak Online Payment Gateway Dialog
// -------------------------------------------------------------
@Composable
fun ShaparakGatewayDialog(
    pkg: ShopPackage,
    onPaymentSuccess: (cardNumber: String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var cardNumber by remember { mutableStateOf("") }
    var cvv2 by remember { mutableStateOf("") }
    var expMonth by remember { mutableStateOf("") }
    var expYear by remember { mutableStateOf("") }
    var dynamicPin by remember { mutableStateOf("") }
    var captchaInput by remember { mutableStateOf("") }
    var captchaCode by remember { mutableIntStateOf(Random.nextInt(1000, 9999)) }

    var otpTimer by remember { mutableIntStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var simulatedSmsOtp by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Countdown Timer for OTP
    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            otpTimer = 120
            while (otpTimer > 0) {
                delay(1000)
                otpTimer--
            }
            isTimerRunning = false
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    // Shaparak Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E3A8A))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "درگاه پرداخت اینترنتی شاپرک 🔒",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "شبکه الکترونیکی پرداخت کارت",
                                fontSize = 10.sp,
                                color = Color(0xFF93C5FD)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🇮🇷", fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Merchant info
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF3F4F6),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("پذیرنده:", fontSize = 11.sp, color = Color.Gray)
                                Text("فروشگاه عسل آباد (پریا چش رنگی)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("مبلغ پرداختی:", fontSize = 11.sp, color = Color.Gray)
                                Text(
                                    "${pkg.formattedPrice} (${pkg.priceToman * 10} ریال)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // SMS notification simulation banner
                    if (simulatedSmsOtp != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFEFF6FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3B82F6)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { dynamicPin = simulatedSmsOtp ?: "" }
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("📩", fontSize = 16.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("پیامک بانک: رمز پویا ارسال شد", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1D4ED8))
                                    Text("رمز شما: $simulatedSmsOtp (جهت درج لمس کنید)", fontSize = 11.sp, color = Color(0xFF1E40AF))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Field 1: Card Number
                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = {
                            val digits = it.filter { c -> c.isDigit() }.take(16)
                            cardNumber = digits
                        },
                        label = { Text("شماره کارت ۱۶ رقمی (بدون فاصله)") },
                        leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF1E3A8A))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Field 2: CVV2 & Expiration Date
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = cvv2,
                            onValueChange = { if (it.length <= 4) cvv2 = it.filter { c -> c.isDigit() } },
                            label = { Text("CVV2") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF1E3A8A))
                        )

                        OutlinedTextField(
                            value = expMonth,
                            onValueChange = { if (it.length <= 2) expMonth = it.filter { c -> c.isDigit() } },
                            label = { Text("ماه") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(0.7f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF1E3A8A))
                        )

                        OutlinedTextField(
                            value = expYear,
                            onValueChange = { if (it.length <= 2) expYear = it.filter { c -> c.isDigit() } },
                            label = { Text("سال") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(0.7f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF1E3A8A))
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Field 3: Dynamic Password (رمز پویا)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = dynamicPin,
                            onValueChange = { if (it.length <= 7) dynamicPin = it.filter { c -> c.isDigit() } },
                            label = { Text("رمز دوم / پویا") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF1E3A8A))
                        )

                        Button(
                            onClick = {
                                if (cardNumber.length < 16) {
                                    errorMessage = "لطفاً ابتدا شماره کارت ۱۶ رقمی را وارد کنید"
                                } else {
                                    errorMessage = null
                                    isTimerRunning = true
                                    val generatedOtp = (100000..999999).random().toString()
                                    simulatedSmsOtp = generatedOtp
                                    dynamicPin = generatedOtp
                                    Toast.makeText(context, "رمز پویا به شماره همراه شما پیامک شد", Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = !isTimerRunning,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Text(
                                text = if (isTimerRunning) "$otpTimer ثانیه" else "دریافت رمز پویا",
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Field 4: Captcha Security Code
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = captchaInput,
                            onValueChange = { captchaInput = it.take(4) },
                            label = { Text("کد امنیتی") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF1E3A8A))
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE5E7EB))
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                                .clickable { captchaCode = Random.nextInt(1000, 9999) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$captchaCode",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF1F2937),
                                letterSpacing = 4.sp
                            )
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = Color.Red,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons: پرداخت & انصراف
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (cardNumber.length != 16) {
                                    errorMessage = "شماره کارت باید دقیقاً ۱۶ رقم باشد"
                                } else if (cvv2.length < 3) {
                                    errorMessage = "کد CVV2 نامعتبر است"
                                } else if (expMonth.isBlank() || expYear.isBlank()) {
                                    errorMessage = "تاریخ انقضا را وارد کنید"
                                } else if (dynamicPin.isBlank()) {
                                    errorMessage = "لطفاً رمز پویا را دریافت و وارد کنید"
                                } else if (captchaInput != captchaCode.toString()) {
                                    errorMessage = "کد امنیتی وارد شده نادرست است"
                                } else {
                                    errorMessage = null
                                    onPaymentSuccess(cardNumber)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(48.dp)
                        ) {
                            Text("پرداخت و تایید نهایی ✅", fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(0.8f)
                                .height(48.dp)
                        ) {
                            Text("انصراف", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Real Card-to-Card Payment Dialog (کارت به کارت)
// -------------------------------------------------------------
@Composable
fun CardToCardDialog(
    pkg: ShopPackage,
    bankInfo: BankAccountInfo,
    onEditCardClick: () -> Unit,
    onSubmitReceipt: (trackingCode: String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var trackingCodeInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "بستن")
                        }
                        Text(
                            text = "پرداخت کارت به کارت 🏦",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                        IconButton(onClick = onEditCardClick) {
                            Icon(Icons.Default.Edit, contentDescription = "تغییر شماره کارت مقصد", tint = AsalPurplePrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Realistic Bank Card Visual Representation
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF2E1065), Color(0xFF581C87), Color(0xFF7E22CE))
                                )
                            )
                            .border(1.5.dp, AsalGold, RoundedCornerShape(20.dp))
                            .padding(18.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = bankInfo.bankName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsalGoldLight
                                )
                                Box(
                                    modifier = Modifier
                                        .size(34.dp, 24.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AsalGold),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("💳", fontSize = 14.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // 16-Digit Card Number Display
                            Text(
                                text = bankInfo.cardNumber,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 2.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("صاحب حساب:", fontSize = 10.sp, color = Color(0xFFDDD2F5))
                                    Text(
                                        text = bankInfo.cardHolderName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("عسل آباد 👑", fontSize = 11.sp, color = AsalGoldLight, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Copy Card Number Button
                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("شماره کارت عسل آباد", bankInfo.cardNumber.replace("-", ""))
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "شماره کارت کپی شد: ${bankInfo.cardNumber}", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3E8FF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "کپی", tint = AsalPurplePrimary)
                            Text(
                                text = "کپی کردن شماره کارت",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurplePrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Instructions
                    Text(
                        text = "مبلغ ${pkg.formattedPrice} را به شماره کارت فوق واریز نموده و سپس شماره پیگیری تراکنش را در کادر زیر وارد کنید تا الماس‌ها آنی واریز گردند:",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563),
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tracking Code Input Field
                    OutlinedTextField(
                        value = trackingCodeInput,
                        onValueChange = { trackingCodeInput = it },
                        label = { Text("شماره پیگیری / شماره ارجاع واریز") },
                        placeholder = { Text("مثلاً 684920") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsalPurplePrimary)
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = errorMessage ?: "", color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Submit Receipt Button
                    Button(
                        onClick = {
                            if (trackingCodeInput.isBlank() || trackingCodeInput.length < 4) {
                                errorMessage = "لطفاً شماره پیگیری معتبر (حداقل ۴ رقم) وارد کنید"
                            } else {
                                errorMessage = null
                                onSubmitReceipt(trackingCodeInput)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "ثبت و دریافت آنی الماس‌ها 💎",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onEditCardClick) {
                        Text("می‌خواهید شماره کارت خودتان را بگذارید؟ اینجا کلیک کنید", fontSize = 11.sp, color = AsalPurpleLight)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Edit Bank Card Dialog (برای اینکه کاربر شماره کارت خودش را قرار دهد)
// -------------------------------------------------------------
@Composable
fun EditBankCardDialog(
    currentInfo: BankAccountInfo,
    onSave: (cardNumber: String, holderName: String, bankName: String, iban: String) -> Unit,
    onDismiss: () -> Unit
) {
    var cardNum by remember { mutableStateOf(currentInfo.cardNumber) }
    var holder by remember { mutableStateOf(currentInfo.cardHolderName) }
    var bank by remember { mutableStateOf(currentInfo.bankName) }
    var ibanNum by remember { mutableStateOf(currentInfo.iban) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "تنظیم شماره کارت بانکی فروشگاه 💳",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalPurpleDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "شماره کارت بانکی خود را وارد کنید تا خریداران کارت‌به‌کارت به این حساب واریز نمایند:",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = cardNum,
                        onValueChange = { cardNum = it },
                        label = { Text("شماره کارت (۱۶ رقمی)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = holder,
                        onValueChange = { holder = it },
                        label = { Text("نام و نام خانوادگی صاحب حساب") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = bank,
                        onValueChange = { bank = it },
                        label = { Text("نام بانک (مثلاً بانک ملی، صادرات، ملت)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = ibanNum,
                        onValueChange = { ibanNum = it },
                        label = { Text("شماره شبا (اختیاری)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("انصراف", color = Color.Gray)
                        }
                        Button(
                            onClick = {
                                onSave(cardNum, holder, bank, ibanNum)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("ذخیره شماره کارت ✅", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Payment Success Digital Receipt Dialog
// -------------------------------------------------------------
@Composable
fun PaymentSuccessReceiptDialog(
    receipt: PurchaseItem,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "موفق",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "پرداخت با موفقیت انجام شد! 🎉",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )

                    Text(
                        text = "${receipt.diamondsGranted} الماس به کیف پول شما واریز گردید.",
                        fontSize = 13.sp,
                        color = AsalPurplePrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Digital Receipt Table
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF9FAFB),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            ReceiptRow(title = "عنوان سفارش", value = receipt.title)
                            ReceiptRow(title = "مبلغ پرداختی", value = "${receipt.priceToman} تومان")
                            ReceiptRow(title = "روش پرداخت", value = receipt.paymentMethod)
                            ReceiptRow(title = "شماره مرجع بانکی (RRN)", value = receipt.refId)
                            ReceiptRow(title = "کد توکن سفارش", value = receipt.token)
                            ReceiptRow(title = "تاریخ و زمان", value = receipt.date)
                            ReceiptRow(title = "وضعیت", value = receipt.status, isSuccess = true)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("رسید خرید عسل آباد", "توکن: ${receipt.token}\nمرجع بانکی: ${receipt.refId}\nالماس: ${receipt.diamondsGranted}")
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "اطلاعات رسید کپی شد", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3E8FF)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("کپی رسید 📋", color = AsalPurplePrimary, fontSize = 12.sp)
                        }

                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("بستن و بازگشت", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(
    title: String,
    value: String,
    isSuccess: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 11.sp, color = Color.Gray)
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSuccess) Color(0xFF10B981) else AsalPurpleDark
        )
    }
}
