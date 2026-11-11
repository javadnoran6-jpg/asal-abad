package com.example.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeaderboardUser
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary

@Composable
fun LeaderboardScreen(
    users: List<LeaderboardUser>,
    modifier: Modifier = Modifier
) {
    var selectedPeriod by remember { mutableStateOf(0) } // 0: روزانه, 1: هفتگی, 2: کلی

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Period Tabs (روزانه / هفتگی / کلی) - Matches Screenshot 8
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("روزانه", "هفتگی", "کلی").forEachIndexed { index, period ->
                    val isSelected = selectedPeriod == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) Color(0xFF6B5E80) else Color.Transparent)
                            .clickable { selectedPeriod = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else AsalPurpleDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Trophy Header Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏆", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "برترین‌های عسل آباد",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalPurpleDark
                    )
                }

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text("۰", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Users Ranking List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(users) { userItem ->
                    LeaderboardCardItem(user = userItem)
                }
            }
        }
    }
}

@Composable
private fun LeaderboardCardItem(user: LeaderboardUser) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Rank Medal / Number
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                when (user.rank) {
                    1 -> Text("🥇", fontSize = 26.sp)
                    2 -> Text("🥈", fontSize = 26.sp)
                    3 -> Text("🥉", fontSize = 26.sp)
                    else -> {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${user.rank}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Avatar
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(user.avatarColorHex), Color(0xFFC084FC))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 22.sp)
                }

                // Name
                Text(
                    text = user.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AsalPurpleDark
                )
            }

            // Score with Cup
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${user.score} جام",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AsalPurplePrimary
                )
            }
        }
    }
}
