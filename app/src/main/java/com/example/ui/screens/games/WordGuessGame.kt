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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary
import com.example.ui.viewmodel.WordGuessState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordGuessGameScreen(
    state: WordGuessState,
    onGuessLetter: (Char) -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    val persianLetters = listOf(
        "ا", "ب", "پ", "ت", "ث", "ج", "چ", "ح", "خ",
        "د", "ذ", "ر", "ز", "ژ", "س", "ش", "ص", "ض",
        "ط", "ظ", "ع", "غ", "ف", "ق", "ک", "گ", "ل",
        "م", "ن", "و", "ه", "ی"
    )

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "حدس کلمه 📝",
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
                            listOf(Color(0xFFEDE9FE), Color(0xFFF9F7FD), Color.White)
                        )
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Hint Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "راهنمایی: ${state.hint}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "فرصت‌های باقیمانده: ${state.attemptsLeft} از ۶",
                            fontSize = 12.sp,
                            color = if (state.attemptsLeft <= 2) Color.Red else Color.Gray
                        )
                    }
                }

                // Word Letter Slots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    state.secretWord.forEach { char ->
                        val isRevealed = state.guessedLetters.contains(char) || state.isFinished
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isRevealed) Color.White else Color(0xFFEDE9FE))
                                .border(
                                    2.dp,
                                    if (isRevealed) AsalPurplePrimary else Color(0xFFDDD2F5),
                                    RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isRevealed) "$char" else "",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark
                            )
                        }
                    }
                }

                // Result or Finish status
                if (state.isFinished) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (state.isWon) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (state.isWon) "🎉 آفرین! کلمه را درست حدس زدی! (+۱۵۰ الماس)" else "❌ باختید! کلمه مورد نظر «${state.secretWord}» بود.",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.isWon) Color(0xFF15803D) else Color(0xFFB91C1C),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onReset,
                                colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary)
                            ) {
                                Text("کلمه بعدی 🔄", color = Color.White)
                            }
                        }
                    }
                }

                // Persian Letter Keypad
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val rows = persianLetters.chunked(8)
                    rows.forEach { rowLetters ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowLetters.forEach { letterStr ->
                                val letterChar = letterStr[0]
                                val isGuessed = state.guessedLetters.contains(letterChar)

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isGuessed) Color(0xFFE5E7EB) else Color.White)
                                        .border(
                                            1.dp,
                                            if (isGuessed) Color.LightGray else AsalPurpleLight,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable(enabled = !isGuessed && !state.isFinished) {
                                            onGuessLetter(letterChar)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = letterStr,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isGuessed) Color.Gray else AsalPurpleDark
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
