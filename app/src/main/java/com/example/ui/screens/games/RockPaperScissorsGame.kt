package com.example.ui.screens.games

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary
import com.example.ui.viewmodel.RpsChoice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RockPaperScissorsGameScreen(
    playerChoice: RpsChoice?,
    botChoice: RpsChoice?,
    resultText: String?,
    playerScore: Int,
    botScore: Int,
    onChoose: (RpsChoice) -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "سنگ کاغذ قیچی ✂️",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "بازگشت",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onReset) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "شروع مجدد",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AsalPurplePrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFE9D5FF), Color(0xFFF5F3FF), Color.White)
                        )
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Score Board
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("امتیاز شما 👸", fontSize = 13.sp, color = AsalPurpleDark)
                            Text("$playerScore", fontSize = 26.sp, fontWeight = FontWeight.Black, color = AsalPurplePrimary)
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF3E8FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("VS", fontWeight = FontWeight.Bold, color = AsalPurplePrimary, fontSize = 12.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("امتیاز حریف 🤖", fontSize = 13.sp, color = AsalPurpleDark)
                            Text("$botScore", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626))
                        }
                    }
                }

                // Arena Display
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Bot Hand
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("دست حریف", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(2.dp, Color(0xFFE9D5FF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (botChoice) {
                                    RpsChoice.ROCK -> "🪨"
                                    RpsChoice.PAPER -> "📄"
                                    RpsChoice.SCISSORS -> "✂️"
                                    null -> "❓"
                                },
                                fontSize = 42.sp
                            )
                        }
                    }

                    // Result Banner
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text(
                            text = resultText ?: "انتخاب کنید!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                        )
                    }

                    // Player Hand
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(2.dp, AsalGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (playerChoice) {
                                    RpsChoice.ROCK -> "🪨"
                                    RpsChoice.PAPER -> "📄"
                                    RpsChoice.SCISSORS -> "✂️"
                                    null -> "👸"
                                },
                                fontSize = 42.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("انتخاب شما", fontSize = 12.sp, color = AsalPurplePrimary, fontWeight = FontWeight.Bold)
                    }
                }

                // Interactive Choice Buttons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "یکی از گزینه‌های زیر را انتخاب کنید:",
                        fontSize = 13.sp,
                        color = AsalPurpleDark,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RpsItemButton(emoji = "🪨", label = "سنگ", onClick = { onChoose(RpsChoice.ROCK) })
                        RpsItemButton(emoji = "📄", label = "کاغذ", onClick = { onChoose(RpsChoice.PAPER) })
                        RpsItemButton(emoji = "✂️", label = "قیچی", onClick = { onChoose(RpsChoice.SCISSORS) })
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "پاداش هر برد: ۲۵ الماس 💎 در عسل آباد",
                        fontSize = 11.sp,
                        color = AsalPurplePrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RpsItemButton(
    emoji: String,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(Color.White)
            .border(1.5.dp, AsalPurpleLight, RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(text = emoji, fontSize = 34.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
    }
}
