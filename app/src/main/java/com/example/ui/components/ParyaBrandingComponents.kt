package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CandidateUser
import com.example.data.model.UserProfile
import com.example.data.model.UserStatus
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary

/**
 * High-fidelity User Avatar with dynamic status indicator reflecting
 * 'Parya Chesh Rangi' management branding.
 */
@Composable
fun ParyaUserAvatarWithStatus(
    avatarEmoji: String,
    status: UserStatus = UserStatus.ONLINE,
    isManager: Boolean = false,
    size: Dp = 54.dp,
    avatarImageUri: String? = null,
    showCrown: Boolean = isManager,
    modifier: Modifier = Modifier
) {
    val statusColor = Color(status.colorHex)
    val indicatorSize = (size.value * 0.28f).coerceAtLeast(10f).dp

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (status == UserStatus.ONLINE) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Outer halo / luxury border reflecting Parya Chesh Rangi signature colors
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    if (isManager) Brush.sweepGradient(
                        listOf(AsalGold, AsalPurplePrimary, AsalGoldLight, AsalPurpleLight, AsalGold)
                    ) else Brush.linearGradient(
                        listOf(Color(0xFFEDE9FE), Color(0xFFC4B5FD))
                    )
                )
                .padding(if (isManager) 2.5.dp else 1.5.dp)
        ) {
            // Inner Avatar container
            val innerBrush = if (isManager) {
                Brush.verticalGradient(listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF)))
            } else {
                androidx.compose.ui.graphics.SolidColor(Color.White)
            }
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(innerBrush),
                contentAlignment = Alignment.Center
            ) {
                UserAvatar(
                    imageUri = avatarImageUri,
                    emoji = avatarEmoji,
                    size = size,
                    emojiFontSize = (size.value * 0.52f).sp
                )
            }
        }

        // Management Crown Pin on top-start
        if (showCrown) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size((size.value * 0.36f).dp)
                    .clip(CircleShape)
                    .background(AsalGold)
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("👑", fontSize = (size.value * 0.22f).sp)
            }
        }

        // Status Indicator Dot on bottom-end
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(indicatorSize)
                .scale(if (status == UserStatus.ONLINE) pulseScale else 1f)
                .clip(CircleShape)
                .background(statusColor)
                .border(2.dp, Color.White, CircleShape)
        )
    }
}

/**
 * Complete UI component displaying user avatar, status indicator, display name,
 * and role badges, prominently styled with 'Parya Chesh Rangi' management branding.
 */
@Composable
fun ParyaUserProfileHeaderCard(
    user: UserProfile,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(3.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                if (user.isCreatorOrManager) AsalGold else Color(0xFFEDE9FE)
            ),
            modifier = modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .testTag("parya_user_profile_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Top Row: Avatar + Names + Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ParyaUserAvatarWithStatus(
                        avatarEmoji = user.avatarEmoji,
                        avatarImageUri = user.avatarImageUri,
                        status = user.status,
                        isManager = user.isCreatorOrManager,
                        size = 56.dp,
                        showCrown = user.isCreatorOrManager
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = user.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (user.isCreatorOrManager) AsalPurplePrimary else AsalPurpleDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (user.verifiedBadge) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "تایید شده",
                                    tint = if (user.isCreatorOrManager) AsalGold else Color(0xFF3B82F6),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            if (user.isCreatorOrManager) {
                                Text("👑", fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = user.username,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Status Row with colored dot indicator
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(user.status.colorHex))
                            )
                            Text(
                                text = user.statusCustomText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (user.isCreatorOrManager) AsalPurplePrimary else Color(0xFF4B5563),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Branding Pill: 'Parya Chesh Rangi' management hallmark
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (user.isCreatorOrManager) Color(0xFFFFFBEB) else Color(0xFFF5F3FF),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (user.isCreatorOrManager) AsalGoldLight else Color(0xFFDDD2F5)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("👑", fontSize = 13.sp)
                            Text(
                                text = user.managementRole,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (user.isCreatorOrManager) Color(0xFF92400E) else AsalPurpleDark
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (user.isVip) AsalPurplePrimary else Color(0xFFE5E7EB)
                        ) {
                            Text(
                                text = if (user.isCreatorOrManager) "مالک رسمی" else "سطح ${user.level}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Compact User Identity Row used for multi-user group selection, candidate lists,
 * and thread participant views.
 */
@Composable
fun ParyaUserCandidateRow(
    user: CandidateUser,
    isSelected: Boolean,
    onToggleSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            onClick = onToggleSelect,
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) Color(0xFFFAF5FF) else Color.White,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) AsalPurplePrimary else Color(0xFFEDE9FE)
            ),
            shadowElevation = if (isSelected) 2.dp else 1.dp,
            modifier = modifier
                .fillMaxWidth()
                .testTag("candidate_user_${user.id}")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Status
                ParyaUserAvatarWithStatus(
                    avatarEmoji = user.avatarEmoji,
                    status = user.status,
                    isManager = user.isManager,
                    size = 46.dp,
                    showCrown = user.isManager
                )

                Spacer(modifier = Modifier.width(10.dp))

                // User details
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = user.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (user.isManager) AsalPurplePrimary else AsalPurpleDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (user.isManager) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "مدیریت",
                                tint = AsalGold,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = user.username,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Color(user.status.colorHex))
                        )
                        Text(
                            text = user.status.label,
                            fontSize = 10.sp,
                            color = Color(user.status.colorHex),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = user.roleTitle,
                        fontSize = 10.sp,
                        color = if (user.isManager) AsalGold else Color(0xFF6B7280)
                    )
                }

                // Checkbox indicator
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) AsalPurplePrimary else Color(0xFFF3F4F6))
                        .border(
                            1.dp,
                            if (isSelected) AsalPurplePrimary else Color(0xFFD1D5DB),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Signature banner presenting the official 'Parya Chesh Rangi' management branding.
 */
@Composable
fun ParyaManagementSignatureBanner(
    onContactCreator: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(3.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, AsalGold),
            modifier = modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFFFBEB), Color(0xFFFAF5FF), Color(0xFFEDE4FA))
                        )
                    )
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        ParyaUserAvatarWithStatus(
                            avatarEmoji = "👑",
                            status = UserStatus.ONLINE,
                            isManager = true,
                            size = 48.dp,
                            showCrown = true
                        )
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "مدیریت: پریا چش رنگی 👑",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsalPurplePrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "تایید",
                                    tint = AsalGold,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "پیام‌رسان، چت و بازی‌های آنلاین عسل آباد",
                                fontSize = 10.sp,
                                color = AsalPurpleDark
                            )
                            Text(
                                text = "پشتیبانی ۲۴ ساعته و نظارت بر امنیت گروه‌ها",
                                fontSize = 9.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    Surface(
                        onClick = onContactCreator,
                        shape = RoundedCornerShape(12.dp),
                        color = AsalPurplePrimary,
                        shadowElevation = 2.dp
                    ) {
                        Text(
                            text = "چت با پریا 💬",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }
    }
}
