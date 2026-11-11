package com.example.ui.screens.games

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

private val snakes = mapOf(16 to 6, 47 to 26, 49 to 11, 56 to 53, 62 to 19, 64 to 60, 87 to 24, 93 to 73, 95 to 75, 98 to 78)
private val ladders = mapOf(1 to 38, 4 to 14, 9 to 31, 21 to 42, 28 to 84, 36 to 44, 51 to 67, 71 to 91, 80 to 100)

private val DiceFaces = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
private val LilacDark = Color(0xFF4C1D95)
private val LilacMain = Color(0xFF7E22CE)

private fun cellNumber(row: Int, col: Int): Int {
    val fromBottom = 9 - row
    val base = fromBottom * 10
    return if (fromBottom % 2 == 0) base + col + 1 else base + (9 - col) + 1
}

/**
 * بازی مار و پله: بازیکن (🟣) در برابر ربات (🟡). برنده ۴۰ الماس می‌گیرد.
 */
@Composable
fun SnakesLaddersGameScreen(
    onWin: (diamonds: Long) -> Unit,
    onBack: () -> Unit
) {
    var playerPos by remember { mutableIntStateOf(0) }
    var botPos by remember { mutableIntStateOf(0) }
    var playerTurn by remember { mutableStateOf(true) }
    var dice by remember { mutableIntStateOf(0) }
    var rolling by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("نوبت شماست؛ تاس بیندازید 🎲") }
    var winner by remember { mutableIntStateOf(0) } // 0 هنوز، 1 بازیکن، 2 ربات
    var rewarded by remember { mutableStateOf(false) }

    // حرکت یک نفر با عدد تاس و اعمال مار/پله
    fun move(isPlayer: Boolean, roll: Int) {
        val current = if (isPlayer) playerPos else botPos
        var next = current + roll
        var note = if (isPlayer) "شما $roll آوردید" else "ربات $roll آورد"
        if (next > 100) {
            next = current
            note += " (عدد زیاد بود، سر جا ماند)"
        } else {
            snakes[next]?.let { next = it; note += " 🐍 مار گازش گرفت!" }
                ?: ladders[next]?.let { next = it; note += " 🪜 از پله بالا رفت!" }
        }
        if (isPlayer) playerPos = next else botPos = next
        message = note
        if (next == 100) {
            winner = if (isPlayer) 1 else 2
        } else {
            playerTurn = !isPlayer
        }
    }

    fun rollForPlayer() {
        if (!playerTurn || rolling || winner != 0) return
        rolling = true
    }

    // انیمیشن تاس + حرکت بازیکن
    LaunchedEffect(rolling) {
        if (rolling) {
            repeat(8) {
                dice = Random.nextInt(1, 7)
                delay(70)
            }
            val roll = Random.nextInt(1, 7)
            dice = roll
            rolling = false
            move(true, roll)
        }
    }

    // نوبت ربات
    LaunchedEffect(playerTurn, winner) {
        if (!playerTurn && winner == 0) {
            delay(900)
            repeat(6) {
                dice = Random.nextInt(1, 7)
                delay(70)
            }
            val roll = Random.nextInt(1, 7)
            dice = roll
            move(false, roll)
        }
    }

    LaunchedEffect(winner) {
        if (winner == 1 && !rewarded) {
            rewarded = true
            onWin(40L)
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFEDE4FA), Color(0xFFF9F6FE))))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onBack) { Text("← بازگشت", color = LilacMain, fontWeight = FontWeight.Bold) }
                Text("🎲 مار و پله 🐍", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = LilacDark)
                Spacer(Modifier.size(70.dp))
            }

            Spacer(Modifier.height(6.dp))

            // Board
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .border(3.dp, LilacMain, RoundedCornerShape(18.dp))
                    .padding(3.dp)
            ) {
                for (r in 0..9) {
                    Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        for (c in 0..9) {
                            val n = cellNumber(r, c)
                            val dark = (r + c) % 2 == 0
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .background(if (dark) Color(0xFFF1E8FD) else Color(0xFFFFFFFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$n", fontSize = 7.sp, color = Color(0xFFB9A5D6), modifier = Modifier.align(Alignment.TopStart).padding(1.dp))
                                when {
                                    snakes.containsKey(n) -> Text("🐍", fontSize = 13.sp)
                                    ladders.containsKey(n) -> Text("🪜", fontSize = 13.sp)
                                    n == 100 -> Text("🏆", fontSize = 14.sp)
                                }
                                Row(horizontalArrangement = Arrangement.Center) {
                                    if (playerPos == n) Text("🟣", fontSize = 12.sp)
                                    if (botPos == n) Text("🟡", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Dice + status
            val diceRotation by animateFloatAsState(if (rolling) 360f else 0f, label = "dice")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally)
            ) {
                Box(
                    modifier = Modifier
                        .size(74.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFFA855F7), LilacMain)))
                        .clickable(enabled = playerTurn && !rolling && winner == 0) { rollForPlayer() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (dice in 1..6) DiceFaces[dice - 1] else "🎲",
                        fontSize = 46.sp,
                        color = Color.White,
                        modifier = Modifier.rotate(diceRotation)
                    )
                }
                Column {
                    Text(message, fontSize = 13.sp, color = LilacDark, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("🟣 شما: خانه $playerPos     🟡 ربات: خانه $botPos", fontSize = 11.sp, color = Color(0xFF6B5E80))
                }
            }

            Spacer(Modifier.height(10.dp))

            if (winner != 0) {
                Text(
                    text = if (winner == 1) "🎉 تبریک! برنده شدید (+۴۰ الماس 💎)" else "😅 ربات برد! دوباره امتحان کن",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (winner == 1) Color(0xFF059669) else Color(0xFFDC2626),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        playerPos = 0; botPos = 0; playerTurn = true; dice = 0
                        winner = 0; rewarded = false; message = "نوبت شماست؛ تاس بیندازید 🎲"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LilacMain),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("بازی دوباره 🔄", color = Color.White, fontWeight = FontWeight.Bold) }
            } else {
                Text("روی تاس بزنید تا بیندازید", fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}
