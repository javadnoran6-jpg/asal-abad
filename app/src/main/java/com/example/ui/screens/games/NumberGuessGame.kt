package com.example.ui.screens.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurplePrimary
import com.example.ui.viewmodel.NumberGuessState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberGuessGameScreen(
    state: NumberGuessState,
    onGuess: (Int) -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    var inputGuess by remember { mutableStateOf("") }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "حدس عدد 🔢",
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Info Card
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
                            text = "عددی بین ۱ تا ۱۰۰ انتخاب شده است!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "فرصت‌های باقی‌مانده:",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(AsalPurplePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${state.attemptsLeft}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input & Submit
                if (!state.isFinished) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = inputGuess,
                            onValueChange = { if (it.length <= 3) inputGuess = it.filter { c -> c.isDigit() } },
                            label = { Text("عدد پیشنهادی شما") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AsalPurplePrimary
                            )
                        )

                        Button(
                            onClick = {
                                val n = inputGuess.toIntOrNull()
                                if (n != null && n in 1..100) {
                                    onGuess(n)
                                    inputGuess = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Text("ثبت حدس", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                } else {
                    // Finished Card
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
                                text = if (state.isWon) "🎉 آفرین! برنده شدید! (+۱۰۰ الماس)" else "❌ باختید! عدد مخفی ${state.targetNumber} بود.",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.isWon) Color(0xFF15803D) else Color(0xFFB91C1C),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = onReset,
                                colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("بازی دوباره 🔄", color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Guesses History
                Text(
                    text = "تاریخچه حدس‌های شما:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AsalPurpleDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.guesses.reversed()) { (guess, feedback) ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF3E8FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$guess",
                                        fontWeight = FontWeight.Bold,
                                        color = AsalPurplePrimary
                                    )
                                }
                                Text(
                                    text = feedback,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AsalPurpleDark
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
