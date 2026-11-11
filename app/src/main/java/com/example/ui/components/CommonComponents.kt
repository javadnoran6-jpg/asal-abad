package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.GroupMarketItem
import com.example.data.model.PurchaseItem
import com.example.data.model.ShopPackage
import com.example.data.model.UserProfile
import com.example.data.model.WheelSector
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary
import com.example.ui.viewmodel.MainTab
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AsalAbadTopHeader(
    user: UserProfile,
    onShopClick: () -> Unit,
    onJanousClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Balance Pills
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Diamond Pill
                Surface(
                    onClick = onShopClick,
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    shadowElevation = 2.dp,
                    modifier = Modifier.height(38.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "💎", fontSize = 16.sp)
                        Text(
                            text = "${user.diamonds}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurplePrimary
                        )
                    }
                }

                // Janous Pill
                Surface(
                    onClick = onJanousClick,
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    shadowElevation = 2.dp,
                    modifier = Modifier.height(38.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(AsalPurplePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "J", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                        Text(
                            text = "${user.janous}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                    }
                }
            }

            // User Avatar & Level Laurel
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Level Wreath Badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .border(1.5.dp, AsalGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${user.level}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = AsalPurplePrimary
                    )
                }

                // User Circular Avatar
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(AsalPurpleLight, AsalGold))
                        )
                        .clickable { onProfileClick() }
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2E1065)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👸", fontSize = 24.sp)
                }
            }
        }
    }
}

@Composable
fun AsalAbadBottomNav(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    title = "خانه",
                    icon = Icons.Default.Home,
                    isSelected = currentTab == MainTab.HOME,
                    onClick = { onTabSelected(MainTab.HOME) }
                )
                BottomNavItem(
                    title = "پیام‌رسان",
                    icon = Icons.Default.Forum,
                    isSelected = currentTab == MainTab.MESSENGER,
                    onClick = { onTabSelected(MainTab.MESSENGER) }
                )
                BottomNavItem(
                    title = "فروشگاه",
                    icon = Icons.Default.Storefront,
                    isSelected = currentTab == MainTab.SHOP,
                    onClick = { onTabSelected(MainTab.SHOP) }
                )
                BottomNavItem(
                    title = "پروفایل",
                    icon = Icons.Default.Person,
                    isSelected = currentTab == MainTab.PROFILE,
                    onClick = { onTabSelected(MainTab.PROFILE) }
                )
                BottomNavItem(
                    title = "آمار",
                    icon = Icons.Default.BarChart,
                    isSelected = currentTab == MainTab.STATS,
                    onClick = { onTabSelected(MainTab.STATS) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = AsalPurplePrimary
    val inactiveColor = Color(0xFF9E92B0)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) activeColor else inactiveColor
        )
    }
}

// -------------------------------------------------------------
// Interactive Spin Wheel Dialog
// -------------------------------------------------------------
@Composable
fun LuckyWheelDialog(
    sectors: List<WheelSector>,
    rotationAngle: Float,
    isSpinning: Boolean,
    rewardMessage: String?,
    onSpinClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 3500, easing = FastOutSlowInEasing),
        label = "wheelRotation"
    )

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = { if (!isSpinning) onDismiss() }) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss, enabled = !isSpinning) {
                            Icon(Icons.Default.Close, contentDescription = "بستن")
                        }
                        Text(
                            text = "گردونه شانس عسل آباد 🎡",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                        Spacer(modifier = Modifier.size(40.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // The Spinning Wheel Component matching the screenshot
                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // 1. ROTATING WHEEL DISK
                        Box(
                            modifier = Modifier
                                .size(246.dp)
                                .rotate(animatedRotation),
                            contentAlignment = Alignment.Center
                        ) {
                            // Wheel Sectors Background Canvas
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val center = Offset(size.width / 2, size.height / 2)
                                val radius = size.minDimension / 2
                                val sectorAngle = 360f / sectors.size

                                sectors.forEachIndexed { i, _ ->
                                    val startAngle = i * sectorAngle - 90f - (sectorAngle / 2f)
                                    // Soft lilac and lavender purple alternating
                                    val sectorColor = if (i % 2 == 0) Color(0xFFC4B5FD) else Color(0xFFDDD2F5)
                                    drawArc(
                                        color = sectorColor,
                                        startAngle = startAngle,
                                        sweepAngle = sectorAngle,
                                        useCenter = true,
                                        size = Size(radius * 2, radius * 2),
                                        topLeft = Offset(center.x - radius, center.y - radius)
                                    )
                                    // Radial divider lines
                                    val rad = Math.toRadians(startAngle.toDouble())
                                    val endX = center.x + radius * Math.cos(rad).toFloat()
                                    val endY = center.y + radius * Math.sin(rad).toFloat()
                                    drawLine(
                                        color = Color.White.copy(alpha = 0.8f),
                                        start = center,
                                        end = Offset(endX, endY),
                                        strokeWidth = 2.dp.toPx()
                                    )
                                }
                            }

                            // Sector Content (Both Diamond prize and Janous token badge)
                            val sectorAngle = 360f / sectors.size
                            sectors.forEachIndexed { i, sector ->
                                val rotationAngle = i * sectorAngle
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .rotate(rotationAngle),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    Column(
                                        modifier = Modifier.padding(top = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // Outer Prize: 3D Diamond + Number
                                        Text("💎", fontSize = 18.sp)
                                        Text(
                                            text = "${sector.diamonds}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF4C1D95)
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Inner Prize: Purple droplet token + Number
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF7E22CE),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD8B4FE))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                Text(
                                                    text = "${sector.janous}",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                                Text("🟣", fontSize = 8.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 2. FIXED ORNATE GOLDEN OUTER RIM WITH PEARLS AND LAUREL LEAVES
                        Canvas(modifier = Modifier.size(268.dp)) {
                            val center = Offset(size.width / 2, size.height / 2)
                            val radius = 124.dp.toPx()

                            // Outer gold ring
                            drawCircle(
                                brush = Brush.sweepGradient(listOf(Color(0xFFFFB703), Color(0xFFFDE047), Color(0xFFD97706), Color(0xFFFFB703))),
                                radius = radius,
                                center = center,
                                style = Stroke(width = 9.dp.toPx())
                            )
                            // Inner thin gold ring
                            drawCircle(
                                color = Color(0xFFFEF08A),
                                radius = radius - 5.dp.toPx(),
                                center = center,
                                style = Stroke(width = 2.dp.toPx())
                            )

                            // Decorative pink pearls around the golden ring
                            val dotCount = 24
                            for (dotIdx in 0 until dotCount) {
                                val dotAngleRad = Math.toRadians((dotIdx * (360.0 / dotCount)).toDouble())
                                val dotR = radius
                                val dotX = center.x + dotR * Math.cos(dotAngleRad).toFloat()
                                val dotY = center.y + dotR * Math.sin(dotAngleRad).toFloat()
                                drawCircle(
                                    color = Color(0xFFF472B6),
                                    radius = 2.5.dp.toPx(),
                                    center = Offset(dotX, dotY)
                                )
                            }
                        }

                        // 3. FOUR CARDINAL GOLDEN STAR DIAMOND GEMS (North, South, East, West)
                        // Top Star with Pointer (North)
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(44.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⭐", fontSize = 32.sp)
                        }
                        // Bottom Star (South)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .size(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⭐", fontSize = 26.sp)
                        }
                        // Left Star (West)
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⭐", fontSize = 26.sp)
                        }
                        // Right Star (East)
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⭐", fontSize = 26.sp)
                        }

                        // 4. CENTER HUB WITH GOLDEN NEEDLE & PURPLE AMETHYST JEWEL
                        Box(
                            modifier = Modifier.size(56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Upward Pointer Needle
                            Canvas(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.TopCenter)
                            ) {
                                val path = Path().apply {
                                    moveTo(size.width / 2, 0f)
                                    lineTo(size.width, size.height)
                                    lineTo(0f, size.height)
                                    close()
                                }
                                drawPath(path, color = Color(0xFFF59E0B))
                                drawPath(path, color = Color.White, style = Stroke(width = 1.5.dp.toPx()))
                            }

                            // Center Amethyst Jewel Hub
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.Center)
                                    .clip(CircleShape)
                                    .background(Brush.radialGradient(listOf(Color(0xFFFDE047), Color(0xFFD97706))))
                                    .border(2.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Brush.radialGradient(listOf(Color(0xFFC084FC), Color(0xFF7E22CE))))
                                        .border(1.5.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("💎", fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (rewardMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF3E8FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AsalPurpleLight),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = rewardMessage,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurplePrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Spin Button
                    Button(
                        onClick = onSpinClick,
                        enabled = !isSpinning,
                        colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = if (isSpinning) "در حال چرخش... 🎲" else "چرخاندن گردونه شانس 🎡",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFEF3C7),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("⏳", fontSize = 13.sp)
                            Text(
                                text = "چرخش رایگان هر ۳ ساعت عوض می‌شود! زمان باقیمانده: ۰۲:۴۵:۰۰ 💎",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "با هر بار چرخش، الماس و ژانوس رایگان عسل آباد هدیه بگیرید!",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Daily Lottery Dialog (Matches Screenshot 9)
// -------------------------------------------------------------
@Composable
fun DailyLotteryDialog(
    onJoinClick: () -> Unit,
    onPreviousWinnersClick: () -> Unit,
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
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Header Banner with gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFEDE9FE), Color.White)
                                )
                            )
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "قرعه‌کشی روزانه",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "قرعه‌کشی روزانه الماس‌ها",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurplePrimary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        val infoList = listOf(
                            "تعداد شرکت‌کنندگان تاکنون: ۱۰۳ نفر",
                            "بیش از ۱۰۰ شرکت‌کننده: ۱۰ برنده",
                            "مجموع صندوق جوایز: ۴۶,۸۵۰ الماس",
                            "سهم تقریبی هر برنده: ۴,۶۸۵ الماس",
                            "هزینه ورود: ۵۰۰ الماس\n(۴۵۰ الماس به صندوق + ۵۰ الماس هزینه اجرایی)"
                        )

                        infoList.forEach { info ->
                            Text(
                                text = info,
                                fontSize = 13.sp,
                                color = Color(0xFF333333),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text("بستن", color = AsalPurplePrimary, fontWeight = FontWeight.Bold)
                            }
                            TextButton(onClick = onPreviousWinnersClick) {
                                Text("برندگان دیروز", color = AsalPurplePrimary, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = onJoinClick,
                                colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("ورود به قرعه‌کشی", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Previous Winners Dialog (Matches Screenshot 15)
// -------------------------------------------------------------
@Composable
fun PreviousWinnersDialog(
    winners: List<String>,
    onDismiss: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "برنده دوره قبل 🏆",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalPurpleDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    winners.forEach { winner ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFF5F3FF),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(AsalPurplePrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("👑", fontSize = 18.sp)
                                }
                                Column {
                                    Text(
                                        text = winner,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AsalPurpleDark
                                    )
                                    Text(
                                        text = "برنده جایزه طلایی الماس عسل آباد",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("بستن", color = Color.White)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Janous Converter Dialog (Matches Screenshot 2 Exactly)
// -------------------------------------------------------------
@Composable
fun JanousConvertDialog(
    currentDiamonds: Long,
    onConvert: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var inputAmount by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val calculatedJanous = (inputAmount.toLongOrNull() ?: 0L)
    val requiredDiamonds = calculatedJanous * 1000L

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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Header Banner with soft lavender gradient matching Screenshot 2
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFF3E8FF), Color(0xFFFAF5FF))
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 18.dp)
                    ) {
                        Text(
                            text = "جانوس (${currentDiamonds / 1000})",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E1E2E),
                            modifier = Modifier.align(Alignment.Center)
                        )
                        Row(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🎰", fontSize = 22.sp)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Input box matching Screenshot 2
                        OutlinedTextField(
                            value = inputAmount,
                            onValueChange = {
                                inputAmount = it.filter { char -> char.isDigit() }
                                errorMessage = null
                            },
                            placeholder = {
                                Text(
                                    text = "تعداد جانوس موردنظر را وارد کنید",
                                    fontSize = 13.sp,
                                    color = Color(0xFF6B7280),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AsalPurplePrimary,
                                unfocusedBorderColor = Color(0xFFD8B4FE)
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Box "تبدیل الماس به جانوس" matching Screenshot 2
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0xFFFAF5FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "تبدیل الماس به جانوس",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF1F2937)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "هر ", fontSize = 12.sp, color = Color(0xFF4B5563))
                                        Text(text = "۱۰۰۰", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AsalPurplePrimary)
                                        Text(text = " الماس معادل ۱ جانوس است", fontSize = 12.sp, color = Color(0xFF4B5563))
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // Purple J icon badge
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "J",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 22.sp
                                    )
                                }
                            }
                        }

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = errorMessage ?: "", color = Color.Red, fontSize = 12.sp)
                        } else if (inputAmount.isNotBlank() && calculatedJanous > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "الماس مورد نیاز: $requiredDiamonds 💎",
                                fontSize = 12.sp,
                                color = AsalPurplePrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Bottom Actions: تبدیل, بستن matching Screenshot 2
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    val janous = inputAmount.toLongOrNull() ?: 0L
                                    val req = janous * 1000L
                                    if (janous <= 0) {
                                        errorMessage = "لطفاً تعداد معتبر وارد کنید"
                                    } else if (req > currentDiamonds) {
                                        errorMessage = "الماس کافی ندارید! موجودی: $currentDiamonds 💎"
                                    } else {
                                        onConvert(req)
                                        onDismiss()
                                    }
                                }
                            ) {
                                Text(
                                    text = "تبدیل",
                                    color = AsalPurplePrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            TextButton(onClick = onDismiss) {
                                Text(
                                    text = "بستن",
                                    color = AsalPurplePrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Group Marketplace Dialog (Matches Screenshot 7)
// -------------------------------------------------------------
@Composable
fun GroupMarketDialog(
    listings: List<GroupMarketItem>,
    onDismiss: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(520.dp)
                    .padding(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
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
                            text = "بازارچه گروه‌ها 🏪",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                        Spacer(modifier = Modifier.size(40.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Filter tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF3E8FF))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text("بیشترین", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AsalPurplePrimary)
                        Text("قیمت", fontSize = 12.sp, color = Color.Gray)
                        Text("امتیاز", fontSize = 12.sp, color = Color.Gray)
                        Text("زمان", fontSize = 12.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listings) { item ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFFAFAFA),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEDE9FE)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.groupName,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AsalPurpleDark
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "امتیاز: ${item.formattedScore}",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = "قیمت: ${item.formattedPrice}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AsalPurplePrimary
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFCE7F3)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("💬", fontSize = 20.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("بستن", color = Color.White)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// VIP Account Info Dialog (Matches Screenshot 12)
// -------------------------------------------------------------
@Composable
fun VipInfoDialog(
    onBuyVip: () -> Unit,
    onDismiss: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "حساب اختصاصی 👑",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalPurpleDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "مدت اعتبار: ۱۴ روز",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalPurplePrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val features = listOf(
                        "• گذاشتن بیوگرافی شخصی",
                        "• استفاده از نمادها و بج‌های ویژه",
                        "• تغییر رنگ و پس‌زمینه پیام‌ها",
                        "• تغییر حالت آخرین بازدید (مخفی/عمومی)",
                        "• ارسال عکس و فایل در پی‌وی و گروه‌ها",
                        "• گردونه اختصاصی با شانس برنده شدن ۲ برابر"
                    )

                    features.forEach { feat ->
                        Text(
                            text = feat,
                            fontSize = 13.sp,
                            color = Color(0xFF333333),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "توجه: با خرید حساب اختصاصی، زمان باقی‌مانده تمدید می‌شود.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("بستن", color = AsalPurplePrimary)
                        }
                        Button(
                            onClick = {
                                onBuyVip()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("خرید (۲۰۰۰ الماس)", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Merge Groups Dialog (Matches Screenshot 4)
// -------------------------------------------------------------
@Composable
fun MergeGroupsDialog(onDismiss: () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3E8FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👥", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "ادغام گروه‌ها",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalPurpleDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "برای ادغام گروه‌ها، حداقل باید دو گروه داشته باشید.",
                        fontSize = 14.sp,
                        color = Color(0xFF333333),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("بستن", color = Color.White)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Tariff Dialog (Matches Screenshot 3 Exactly)
// -------------------------------------------------------------
@Composable
fun TariffDialog(
    currentDiamonds: Long = 1000,
    onDonatePoints: (Long) -> Unit = {},
    onDismiss: () -> Unit
) {
    var donateInput by remember { mutableStateOf("1000") }
    var donationResult by remember { mutableStateOf<String?>(null) }

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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Header with soft lavender background matching Screenshot 3
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFF3E8FF), Color(0xFFFAF5FF))
                                )
                            )
                            .padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "تعرفه اهدای امتیاز",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E1E2E)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        // Section 1: نرخ تبدیل الماس به امتیاز گروه
                        Text(
                            text = "نرخ تبدیل الماس به امتیاز گروه:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val rates = listOf(
                            "• از ۰ تا ۱۰،۰۰۰ ← هر ۵ الماس = ۱ امتیاز",
                            "• از ۱۰،۰۰۰ تا ۲۰،۰۰۰ ← هر ۵۰ الماس = ۱ امتیاز",
                            "• بیشتر از ۲۰،۰۰۰ ← هر ۱۰۰ الماس = ۱ امتیاز"
                        )
                        rates.forEach { rate ->
                            Text(
                                text = rate,
                                fontSize = 13.sp,
                                color = Color(0xFF374151),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Section 2: جوایز رقابت‌ها
                        Text(
                            text = "جوایز رقابت‌ها:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val awards = listOf(
                            "• پایان هر هفته: ۶ گروه برتر ← ۳،۰۰۰ امتیاز کلی +\n۵،۰۰۰ امتیاز ماهانه",
                            "• پایان هر ماه: ۶ گروه برتر ← ۱۰،۰۰۰ امتیاز کلی"
                        )
                        awards.forEach { award ->
                            Text(
                                text = award,
                                fontSize = 13.sp,
                                color = Color(0xFF374151),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Interactive Instant Donation / Calculator Box
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFFAF5FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    "محاسبه و اهدای مستقیم امتیاز به گروه:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsalPurpleDark
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedTextField(
                                        value = donateInput,
                                        onValueChange = { donateInput = it.filter { c -> c.isDigit() } },
                                        placeholder = { Text("تعداد الماس", fontSize = 10.sp) },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    Button(
                                        onClick = {
                                            val d = donateInput.toLongOrNull() ?: 0L
                                            if (d <= 0) {
                                                donationResult = "لطفاً تعداد معتبر وارد کنید"
                                            } else if (d > currentDiamonds) {
                                                donationResult = "الماس کافی ندارید! موجودی: $currentDiamonds"
                                            } else {
                                                val calculatedPoints = when {
                                                    d <= 10000 -> d / 5
                                                    d <= 20000 -> 2000 + ((d - 10000) / 50)
                                                    else -> 2200 + ((d - 20000) / 100)
                                                }
                                                onDonatePoints(d)
                                                donationResult = "✅ $calculatedPoints امتیاز با موفقیت اهدا شد."
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("اهدا 💎", fontSize = 11.sp, color = Color.White)
                                    }
                                }
                                if (donationResult != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(donationResult!!, fontSize = 10.sp, color = AsalPurplePrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Bottom Action: بستن (matching Screenshot 3)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text(
                                    text = "بستن",
                                    color = AsalPurplePrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Create Group Dialog (ساخت گروه با پانی)
// -------------------------------------------------------------
@Composable
fun CreateGroupDialog(
    onCreateGroup: (title: String, description: String, emoji: String, withPani: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var groupName by remember { mutableStateOf("گپ صمیمی با پانی 🌸") }
    var groupDesc by remember { mutableStateOf("دورهمی، چت و سرگرمی دوستان عسل آباد 💜") }
    var selectedEmoji by remember { mutableStateOf("🌸") }
    var withPani by remember { mutableStateOf(true) }
    var withParia by remember { mutableStateOf(true) }

    val emojis = listOf("🌸", "🦋", "👑", "🐝", "✨", "💖", "⚡", "🦅", "💎")

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
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
                            text = "ساخت گروه با پانی 🌸",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                        Spacer(modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Group Name
                    OutlinedTextField(
                        value = groupName,
                        onValueChange = { groupName = it },
                        label = { Text("نام گروه") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsalPurplePrimary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Group Description
                    OutlinedTextField(
                        value = groupDesc,
                        onValueChange = { groupDesc = it },
                        label = { Text("توضیحات و بیو گروه") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsalPurplePrimary)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Emoji Selection
                    Text("انتخاب نماد گروه:", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        emojis.take(6).forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedEmoji == emoji) Color(0xFFF3E8FF) else Color(0xFFF9FAFB))
                                    .border(
                                        1.5.dp,
                                        if (selectedEmoji == emoji) AsalPurplePrimary else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { selectedEmoji = emoji },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emoji, fontSize = 20.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Member Checkbox 1: پانی 🌸
                    Surface(
                        onClick = { withPani = !withPani },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF5F3FF),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("🦋", fontSize = 20.sp)
                                Column {
                                    Text("افزودن پانی به عنوان ادمین گروه", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                                    Text("پانی بلافاصله در چت حضور پیدا می‌کند", fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                            Text(if (withPani) "✅" else "⬜", fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Member Checkbox 2: پریا چش رنگی 👑
                    Surface(
                        onClick = { withParia = !withParia },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFFBEB),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("👑", fontSize = 20.sp)
                                Column {
                                    Text("دعوت پریا چش رنگی (سازنده عسل آباد)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                                    Text("حضور رسمی سازنده در گروه", fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                            Text(if (withParia) "✅" else "⬜", fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (groupName.isNotBlank()) {
                                    onCreateGroup(groupName, groupDesc, selectedEmoji, withPani)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(46.dp)
                        ) {
                            Text("ایجاد گروه و ورود به چت 🚀", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                        }

                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(0.8f)
                                .height(46.dp)
                        ) {
                            Text("انصراف", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// In-App Heads-Up Notification Banner (اعلان ساده درون‌برنامه‌ای)
// -------------------------------------------------------------
@Composable
fun InAppNotificationBanner(
    notification: com.example.data.model.InAppNotification,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (notification.type) {
                    com.example.data.model.NotificationType.PURCHASE_SUCCESS -> Color(0xFFF0FDF4)
                    com.example.data.model.NotificationType.CHAT_MESSAGE -> Color(0xFFFAF5FF)
                    com.example.data.model.NotificationType.WALLET_UPDATE -> Color(0xFFFFFBEB)
                }
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                when (notification.type) {
                    com.example.data.model.NotificationType.PURCHASE_SUCCESS -> Color(0xFF10B981)
                    com.example.data.model.NotificationType.CHAT_MESSAGE -> Color(0xFFA855F7)
                    com.example.data.model.NotificationType.WALLET_UPDATE -> Color(0xFFFFB703)
                }
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .clickable { onClick() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Emoji Avatar / Icon Circle
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                when (notification.type) {
                                    com.example.data.model.NotificationType.PURCHASE_SUCCESS -> Color(0xFFDCFCE7)
                                    com.example.data.model.NotificationType.CHAT_MESSAGE -> Color(0xFFEDE9FE)
                                    com.example.data.model.NotificationType.WALLET_UPDATE -> Color(0xFFFEF3C7)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(notification.iconEmoji, fontSize = 22.sp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = notification.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Text(
                                text = "• ${notification.timestamp}",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = notification.message,
                            fontSize = 12.sp,
                            color = Color(0xFF374151),
                            maxLines = 2,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            lineHeight = 16.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        onClick = onClick,
                        shape = RoundedCornerShape(8.dp),
                        color = AsalPurplePrimary
                    ) {
                        Text(
                            text = "مشاهده",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "بستن",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}


