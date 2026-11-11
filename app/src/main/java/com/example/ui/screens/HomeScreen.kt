package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary
import com.example.ui.viewmodel.ActiveGame

@Composable
fun HomeScreen(
    user: UserProfile,
    onOpenGame: (ActiveGame) -> Unit,
    onOpenWheel: () -> Unit,
    onOpenLottery: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Daily Poetry Banner Card (Matches Screenshot 8, 14, 16)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFEDE9FE),
                                    Color(0xFFFDF4FF),
                                    Color.White
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "دیشب گذرم فتاد براهل قبور",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "یک لحظه بیا ایستاده ماندم ز عبور",
                                fontSize = 13.sp,
                                color = Color(0xFF4B5563)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "جلال‌الدین فقهی سلجوقی",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AsalPurplePrimary
                            )
                        }

                        // Lavender Vase decoration placeholder
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFAF5FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🪻🕯️", fontSize = 28.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section 1: ماموریت آباد ...
            SectionHeader(title = "ماموریت آباد ... 🎮")
            Spacer(modifier = Modifier.height(10.dp))

            // 2x3 Grid of Games (Matches Screenshots)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GameCard(
                        emoji = "🔢",
                        title = "حدس عدد",
                        tag = "۳۰",
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenGame(ActiveGame.NUMBER_GUESS) }
                    )
                    GameCard(
                        emoji = "📝",
                        title = "حدس کلمه",
                        tag = "۳۰",
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenGame(ActiveGame.WORD_GUESS) }
                    )
                    GameCard(
                        emoji = "👥",
                        title = "چالش گروهی",
                        tag = "۵",
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenGame(ActiveGame.ROCK_PAPER_SCISSORS) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GameCard(
                        emoji = "✏️",
                        title = "نقطه خط",
                        tag = "۱۰",
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenGame(ActiveGame.ROCK_PAPER_SCISSORS) }
                    )
                    GameCard(
                        emoji = "🎲",
                        title = "مار پله",
                        tag = "۳۰",
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenGame(ActiveGame.ROCK_PAPER_SCISSORS) }
                    )
                    GameCard(
                        emoji = "✂️",
                        title = "سنگ کاغذ قیچی",
                        tag = "۱۰",
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenGame(ActiveGame.ROCK_PAPER_SCISSORS) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 2: شانس آباد ...
            SectionHeader(title = "شانس آباد ... 🎡")
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Invite and Gift
                ChanceCard(
                    emoji = "🎁",
                    title = "دعوت و هدیه",
                    containerColor = Color(0xFFD1FAE5),
                    modifier = Modifier.weight(1f),
                    onClick = onOpenLottery
                )

                // Lucky Wheel (Interactive!)
                ChanceCard(
                    emoji = "🎡",
                    title = "گردونه شانس",
                    containerColor = Color(0xFFF3E8FF),
                    modifier = Modifier.weight(1f),
                    onClick = onOpenWheel
                )

                // Lucky Diamond Chest (Lottery)
                ChanceCard(
                    emoji = "💎",
                    title = "قرعه‌کشی",
                    containerColor = Color(0xFFFEF3C7),
                    modifier = Modifier.weight(1f),
                    onClick = onOpenLottery
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 3: امتیاز آباد ... (Top Players Horizontal List)
            SectionHeader(title = "امتیاز آباد ... 🌟")
            Spacer(modifier = Modifier.height(10.dp))

            val topScorers = listOf(
                Pair("۶۰۱۵۴", "😎"),
                Pair("۴۵۰۰۳", "👑"),
                Pair("۳۵۸۹۱", "🏆"),
                Pair("۲۶۳۸۴", "⭐"),
                Pair("۲۰۲۹۸", "🌸"),
                Pair("۲۰۲۸۳", "⚡")
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(topScorers) { (score, avatarEmoji) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(AsalPurpleLight, AsalGold)
                                    )
                                )
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF311042)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(avatarEmoji, fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = score,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 4: اختصاصی آباد ...
            SectionHeader(title = "اختصاصی آباد ... 👑")
            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "کاربران دارای حساب اختصاصی و ویژه عسل آباد",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("👸", "🤴", "🦋", "🌸", "💎", "🐝").forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF3E8FF))
                                    .border(1.5.dp, AsalGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emoji, fontSize = 20.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⏱️ تا گردونه بعدی ۵ ساعت و ۲۷ دقیقه باقی مانده است",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalPurplePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = AsalPurpleDark
    )
}

@Composable
private fun GameCard(
    emoji: String,
    title: String,
    tag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
            .height(104.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tag,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "•",
                    fontSize = 10.sp,
                    color = AsalPurpleLight
                )
            }
            Text(text = emoji, fontSize = 32.sp)
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AsalPurpleDark,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ChanceCard(
    emoji: String,
    title: String,
    containerColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
            .height(90.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AsalPurpleDark
            )
        }
    }
}
