package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BankAccountInfo
import com.example.data.model.BeautyFilter
import com.example.data.model.ChatRoom
import com.example.data.model.UserProfile
import com.example.data.model.UserStatus
import com.example.data.model.VerificationTick
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary
import kotlinx.coroutines.delay

// =============================================================
// 1. BEAUTY VIDEO CALL DIALOG WITH MAKEUP & AESTHETIC FILTERS
// =============================================================
@Composable
fun BeautyVideoCallDialog(
    room: ChatRoom,
    onDismiss: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf(BeautyFilter.GLAMOUR) }
    var isMicMuted by remember { mutableStateOf(false) }
    var isCameraFront by remember { mutableStateOf(true) }
    var callSeconds by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            callSeconds++
        }
    }

    val callDurationFormatted = remember(callSeconds) {
        val m = callSeconds / 60
        val s = callSeconds % 60
        String.format(java.util.Locale.getDefault(), "%02d:%02d", m, s)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val filterPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "filterPulse"
    )

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F0728))
            ) {
                // Background Simulated Remote Video Feed with aesthetic tint
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF2E1065),
                                    Color(0xFF581C87),
                                    Color(0xFF1E1035)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Remote User Avatar
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .scale(filterPulse)
                                .clip(CircleShape)
                                .background(
                                    Brush.sweepGradient(
                                        listOf(
                                            Color(selectedFilter.glowColorHex),
                                            AsalGold,
                                            Color(selectedFilter.glowColorHex)
                                        )
                                    )
                                )
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF230B48)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(room.avatarEmoji, fontSize = 68.sp)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = room.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Active Filter Badge Tag
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(selectedFilter.glowColorHex).copy(alpha = 0.25f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(selectedFilter.glowColorHex))
                        ) {
                            Text(
                                text = "فیلتر فعال: ${selectedFilter.label}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "تماس تصویری زنده HD • $callDurationFormatted",
                            fontSize = 12.sp,
                            color = Color(0xFFDDD2F5)
                        )
                    }

                    // Floating animated filter decoration icons on the screen
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (selectedFilter) {
                            BeautyFilter.GLAMOUR -> {
                                Text("💄", fontSize = 28.sp, modifier = Modifier.align(Alignment.TopStart).padding(start = 30.dp, top = 100.dp))
                                Text("✨", fontSize = 24.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 40.dp, top = 120.dp))
                                Text("💋", fontSize = 28.sp, modifier = Modifier.align(Alignment.BottomStart).padding(start = 40.dp, bottom = 180.dp))
                            }
                            BeautyFilter.LILAC_ROSE -> {
                                Text("🌸", fontSize = 32.sp, modifier = Modifier.align(Alignment.TopStart).padding(start = 30.dp, top = 90.dp))
                                Text("🌺", fontSize = 28.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 30.dp, top = 110.dp))
                                Text("🌸", fontSize = 30.sp, modifier = Modifier.align(Alignment.BottomEnd).padding(end = 40.dp, bottom = 180.dp))
                            }
                            BeautyFilter.HEART_GLOW -> {
                                Text("❤️", fontSize = 30.sp, modifier = Modifier.align(Alignment.TopStart).padding(start = 30.dp, top = 90.dp))
                                Text("💖", fontSize = 26.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 30.dp, top = 110.dp))
                                Text("💕", fontSize = 28.sp, modifier = Modifier.align(Alignment.BottomStart).padding(start = 40.dp, bottom = 180.dp))
                            }
                            BeautyFilter.STARLIGHT -> {
                                Text("⭐", fontSize = 28.sp, modifier = Modifier.align(Alignment.TopStart).padding(start = 30.dp, top = 90.dp))
                                Text("✨", fontSize = 32.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 30.dp, top = 110.dp))
                                Text("🌟", fontSize = 26.sp, modifier = Modifier.align(Alignment.BottomEnd).padding(end = 40.dp, bottom = 180.dp))
                            }
                            BeautyFilter.NATURAL -> {
                                Text("✨", fontSize = 26.sp, modifier = Modifier.align(Alignment.TopStart).padding(start = 30.dp, top = 100.dp))
                                Text("🌟", fontSize = 24.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 40.dp, top = 100.dp))
                            }
                        }
                    }
                }

                // PiP Local Camera Preview (Top Right)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 40.dp, end = 16.dp)
                        .size(100.dp, 140.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1E1035))
                        .border(2.dp, Color(selectedFilter.glowColorHex), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("👸", fontSize = 42.sp)
                        Text("تصویر شما", fontSize = 9.sp, color = Color.White)
                        Text(selectedFilter.emoji, fontSize = 12.sp)
                    }
                }

                // Bottom Controls: Beauty Filter Selector + Action Buttons
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0xFF0F0728).copy(alpha = 0.95f))
                            )
                        )
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Filter selection title
                    Text(
                        text = "فیلترهای آرایشی و زیبایی عسل آباد 💄✨",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalGoldLight
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Horizontal Filter Selector
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(BeautyFilter.entries) { filter ->
                            val isSelected = selectedFilter == filter
                            Surface(
                                onClick = { selectedFilter = filter },
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) Color(filter.glowColorHex) else Color.White.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) Color.White else Color.Transparent
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(filter.emoji, fontSize = 14.sp)
                                    Text(
                                        text = filter.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else Color(0xFFDDD2F5)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Call Actions Row (Mute, Flip Camera, Hangup)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mic toggle
                        IconButton(
                            onClick = { isMicMuted = !isMicMuted },
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (isMicMuted) Color.Red else Color.White.copy(alpha = 0.25f))
                        ) {
                            Icon(
                                if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "میکروفون",
                                tint = Color.White
                            )
                        }

                        // Hangup Button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444))
                        ) {
                            Icon(
                                Icons.Default.CallEnd,
                                contentDescription = "قطع تماس",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Camera Flip toggle
                        IconButton(
                            onClick = { isCameraFront = !isCameraFront },
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f))
                        ) {
                            Icon(
                                Icons.Default.Cameraswitch,
                                contentDescription = "چرخش دوربین",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

// =============================================================
// 2. GROUP VOICE CALL DIALOG (ویس‌کال گروهی عسل آباد)
// =============================================================
@Composable
fun GroupVoiceCallDialog(
    room: ChatRoom,
    onDismiss: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(true) }
    var secondsElapsed by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            secondsElapsed++
        }
    }

    val callDuration = remember(secondsElapsed) {
        val m = secondsElapsed / 60
        val s = secondsElapsed % 60
        String.format(java.util.Locale.getDefault(), "%02d:%02d", m, s)
    }

    // Active talkers in group voice room
    val voiceMembers = listOf(
        Pair("پریا چش رنگی 👑", "👑"),
        Pair("پانی 🌸", "🦋"),
        Pair("شاهین 🔥", "🦅"),
        Pair("من (شما)", "👸")
    )

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1035)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "بستن", tint = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "ویس‌کال گروهی: ${room.title} 🎙️",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "در حال مکالمه صوتی • $callDuration",
                                fontSize = 11.sp,
                                color = Color(0xFFC4B5FD)
                            )
                        }
                        Text("🔊", fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Members Grid (2x2)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val pairs = voiceMembers.chunked(2)
                        pairs.forEachIndexed { rowIdx, rowMembers ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowMembers.forEachIndexed { colIdx, (name, emoji) ->
                                    val isSpeaking = (rowIdx + colIdx + secondsElapsed) % 2 == 0
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = if (isSpeaking) Color(0xFF581C87) else Color(0xFF2E1065),
                                        border = androidx.compose.foundation.BorderStroke(
                                            if (isSpeaking) 2.dp else 1.dp,
                                            if (isSpeaking) Color(0xFF10B981) else Color(0xFF7E22CE)
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(52.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF3B1578))
                                                    .border(
                                                        2.dp,
                                                        if (isSpeaking) Color(0xFF10B981) else Color.Transparent,
                                                        CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(emoji, fontSize = 26.sp)
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = name,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                maxLines = 1
                                            )
                                            Text(
                                                text = if (isSpeaking) "در حال صحبت 🎙️" else "گوش می‌دهد 🎧",
                                                fontSize = 9.sp,
                                                color = if (isSpeaking) Color(0xFF10B981) else Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Control Buttons (Mute, Speaker, Leave Call)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { isMuted = !isMuted },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(if (isMuted) Color.Red else Color.White.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "میکروفون",
                                tint = Color.White
                            )
                        }

                        // Leave Call Button
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text("خروج از ویس‌کال 📞", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        IconButton(
                            onClick = { isSpeakerOn = !isSpeakerOn },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(if (isSpeakerOn) AsalPurplePrimary else Color.White.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                Icons.Default.VolumeUp,
                                contentDescription = "اسپیکر",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================
// 3. OWNER MANAGEMENT ADMIN PANEL DIALOG (پنل مدیریت مالک)
// =============================================================
@Composable
fun OwnerAdminPanelDialog(
    user: UserProfile,
    bankInfo: BankAccountInfo,
    onGrantDiamonds: () -> Unit,
    onUpdateBankCard: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var announcementText by remember { mutableStateOf("به عسل آباد خوش آمدید! با مدیریت پریا چش رنگی 👑") }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "بستن")
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("👑", fontSize = 22.sp)
                            Text(
                                text = "پنل مدیریت پریا چش رنگی",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark
                            )
                        }
                        Spacer(modifier = Modifier.size(36.dp))
                    }

                    Text(
                        text = "دسترسی‌های ویژه و انحصاری مالک و سازنده عسل آباد",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Section 1: Free Unlimited Diamonds for Owner
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFFBEB),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("💎", fontSize = 24.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "الماس نامحدود و رایگان (ویژه مالک)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                    Text(
                                        text = "الماس فقط برای مالک برنامه کاملاً رایگان است.",
                                        fontSize = 10.sp,
                                        color = Color(0xFFB45309)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    onGrantDiamonds()
                                    Toast.makeText(context, "۱۰۰,۰۰۰ الماس رایگان به حساب شما اضافه شد! 💎", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AsalGold),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("افزودن ۱۰۰,۰۰۰ الماس رایگان به کیف پول 💎", color = AsalPurpleDark, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Section 2: Destination Bank Card Quick Config
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFAF5FF),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("💳", fontSize = 24.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "شماره کارت بانکی فروشگاه",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AsalPurplePrimary
                                    )
                                    Text(
                                        text = "شماره کارت فعلی: ${bankInfo.cardNumber}",
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onUpdateBankCard,
                                colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("تنظیم و تغییر شماره کارت بانکی ✍️", color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Section 3: Global Broadcast Announcement
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF3F4F6),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "ارسال اعلان و پیام سراسری به همه چت‌ها 📢",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = announcementText,
                                onValueChange = { announcementText = it },
                                label = { Text("متن پیام همگانی") },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsalPurplePrimary)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    Toast.makeText(context, "پیام سراسری به تمام گروه‌ها و کاربران ارسال گردید 📢", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AsalPurpleDark),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("انتشار پیام همگانی 🚀", color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B7280)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("بستن پنل مدیریت", color = Color.White)
                    }
                }
            }
        }
    }
}

// =============================================================
// 4. FULL PROFILE EDIT DIALOG (عکس، بیو، اسم، تیک، آنلاین/آفلاین)
// =============================================================
@Composable
fun EditUserProfileFullDialog(
    user: UserProfile,
    onSave: (
        name: String,
        username: String,
        bio: String,
        avatarEmoji: String,
        avatarImageUri: String?,
        status: UserStatus,
        tick: VerificationTick
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var nameInput by remember { mutableStateOf(user.name) }
    var usernameInput by remember { mutableStateOf(user.username) }
    var bioInput by remember { mutableStateOf(user.bio) }
    var selectedEmoji by remember { mutableStateOf(user.avatarEmoji) }
    var selectedStatus by remember { mutableStateOf(user.status) }
    var selectedTick by remember { mutableStateOf(user.verificationTick) }

    val presetAvatars = listOf("👑", "👸", "🌸", "🦋", "🦁", "🦅", "💎", "🐝", "🐱", "🍓", "⚡", "✨")

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "بستن")
                        }
                        Text(
                            text = "ویرایش کامل پروفایل کاربری 🎨",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                        Spacer(modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Avatar Preview & Selector
                    Text("انتخاب عکس و نماد آواتار:", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(presetAvatars) { emoji ->
                            val isSelected = selectedEmoji == emoji
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color(0xFFDDD2F5) else Color(0xFFF9FAFB))
                                    .border(
                                        2.dp,
                                        if (isSelected) AsalPurplePrimary else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { selectedEmoji = emoji },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emoji, fontSize = 24.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Status (آنلاین، آفلاین، در حال بازی، مشغول)
                    Text("وضعیت حضور (آنلاین / آفلاین):", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        UserStatus.entries.forEach { st ->
                            val isSelected = selectedStatus == st
                            Surface(
                                onClick = { selectedStatus = st },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(st.colorHex) else Color(0xFFF3F4F6),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = st.label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else Color(0xFF374151),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Verification Tick (آبی، سبز، قرمز، بنفش، طلایی)
                    Text("انتخاب تیک تایید حساب:", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(VerificationTick.entries) { tick ->
                            val isSelected = selectedTick == tick
                            Surface(
                                onClick = { selectedTick = tick },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(tick.colorHex) else Color(0xFFF3F4F6),
                                modifier = Modifier.padding(horizontal = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (tick.symbol.isNotEmpty()) {
                                        Text(tick.symbol, fontSize = 11.sp, color = if (isSelected) Color.White else Color(tick.colorHex))
                                    }
                                    Text(
                                        text = tick.label,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else Color(0xFF374151)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Name Input
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("نام نمایشی شما") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsalPurplePrimary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Username Input
                    OutlinedTextField(
                        value = usernameInput,
                        onValueChange = { usernameInput = it },
                        label = { Text("آیدی کاربری (@username)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsalPurplePrimary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bio Input
                    OutlinedTextField(
                        value = bioInput,
                        onValueChange = { bioInput = it },
                        label = { Text("متن بیوگرافی (درباره من)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsalPurplePrimary)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("انصراف", color = Color.Gray)
                        }
                        Button(
                            onClick = {
                                onSave(
                                    nameInput,
                                    usernameInput,
                                    bioInput,
                                    selectedEmoji,
                                    null,
                                    selectedStatus,
                                    selectedTick
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("ذخیره تغییرات ✅", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
