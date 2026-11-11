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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import com.example.data.model.UserProfile
import com.example.data.model.UserStatus
import com.example.data.model.VerificationTick
import com.example.ui.components.BioEditDialog
import com.example.ui.components.ConfirmDialog
import com.example.ui.components.EditPhotoDialog
import com.example.ui.components.EditUserProfileFullDialog
import com.example.ui.components.UserAvatar
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.PhotoCamera
import org.json.JSONObject
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary

@Composable
fun ProfileScreen(
    user: UserProfile,
    creatorProfile: UserProfile,
    onOpenVipModal: () -> Unit,
    onUpdateBio: (String, String) -> Unit,
    onOpenPrivateChatWithCreator: () -> Unit,
    onOpenAdminPanel: () -> Unit = {},
    onSetPresence: (UserStatus) -> Unit = {},
    onUpdateFullProfile: (
        name: String,
        username: String,
        bio: String,
        avatarEmoji: String,
        avatarImageUri: String?,
        status: UserStatus,
        tick: VerificationTick
    ) -> Unit = { _, _, _, _, _, _, _ -> },
    onSignOut: () -> Unit = {},
    onSetDefaultAvatar: (female: Boolean) -> Unit = {},
    onPickGalleryAvatar: (path: String) -> Boolean = { false },
    onSaveBio: (String) -> Unit = {},
    onRestoreBackup: (name: String, username: String, bio: String, emoji: String) -> Unit = { _, _, _, _ -> },
    onDeleteAccount: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showPhotoDialog by remember { mutableStateOf(false) }
    var showBioDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var isViewingCreator by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val activeProfile = if (isViewingCreator) creatorProfile else user

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // View Switcher (پروفایل من / پریا چش رنگی سازنده)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isViewingCreator) AsalPurplePrimary else Color.Transparent)
                        .clickable { isViewingCreator = false }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "پروفایل من",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!isViewingCreator) Color.White else AsalPurpleDark
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isViewingCreator) AsalPurplePrimary else Color.Transparent)
                        .clickable { isViewingCreator = true }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "پریا چش رنگی 👑 (سازنده)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isViewingCreator) Color.White else AsalPurpleDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Large Circular Avatar with Online/Offline Status Indicator
            Box(
                modifier = Modifier.size(116.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer Luxury Halo
                Box(
                    modifier = Modifier
                        .size(116.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                listOf(AsalGold, AsalPurplePrimary, AsalGoldLight, Color(0xFFEC4899), AsalGold)
                            )
                        )
                        .padding(3.5.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF230B48)),
                    contentAlignment = Alignment.Center
                ) {
                    UserAvatar(
                        imageUri = activeProfile.avatarImageUri,
                        emoji = activeProfile.avatarEmoji,
                        size = 109.dp,
                        modifier = Modifier.clickable(enabled = !isViewingCreator) { showPhotoDialog = true }
                    )
                }

                // Status Indicator Dot (Online / Offline / Gaming / Busy)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(activeProfile.status.colorHex))
                        .border(3.dp, Color.White, CircleShape)
                )

                if (!isViewingCreator) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(AsalPurplePrimary)
                            .border(2.dp, Color.White, CircleShape)
                            .clickable { showPhotoDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "ویرایش عکس", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                // Creator Crown on Top
                if (activeProfile.isCreatorOrManager) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AsalGold)
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👑", fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Profile Info Card
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (activeProfile.isCreatorOrManager) AsalGold else Color(0xFFEDE9FE)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = activeProfile.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurpleDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        // Custom Verification Tick (سبز، آبی، قرمز، بنفش، طلایی)
                        if (activeProfile.verificationTick != VerificationTick.NONE) {
                            Surface(
                                shape = CircleShape,
                                color = Color(activeProfile.verificationTick.colorHex),
                                modifier = Modifier.size(20.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = activeProfile.verificationTick.symbol,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = activeProfile.username,
                        fontSize = 13.sp,
                        color = AsalPurplePrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Status Indicator Text with live color dot
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(activeProfile.status.colorHex))
                        )
                        Text(
                            text = activeProfile.statusCustomText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4B5563)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "سن آباد : ${activeProfile.ageDays} روز",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bio Text
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFAF5FF),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = activeProfile.bio,
                            fontSize = 12.sp,
                            color = Color(0xFF374151),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            if (!isViewingCreator) {
                Spacer(modifier = Modifier.height(10.dp))
                // Real-Time Presence Status Selector
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val pulseAlpha by rememberInfiniteTransition().animateFloat(
                                    initialValue = 0.3f,
                                    targetValue = 1f,
                                    animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color(user.status.colorHex).copy(alpha = pulseAlpha))
                                )
                                Text(
                                    text = "وضعیت آنلاین / حضور زنده:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsalPurpleDark
                                )
                            }
                            Text(
                                text = user.status.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(user.status.colorHex)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Status selection buttons (4 options)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                Triple(UserStatus.ONLINE, "آنلاین", "🟢"),
                                Triple(UserStatus.IN_GAME, "در بازی", "🟣"),
                                Triple(UserStatus.BUSY, "مشغول", "🟡"),
                                Triple(UserStatus.OFFLINE, "آفلاین", "⚫")
                            ).forEach { (st, label, emoji) ->
                                val isSelected = user.status == st
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) Color(st.colorHex) else Color(0xFFF3E8FF))
                                        .clickable { onSetPresence(st) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(emoji, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = label,
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else AsalPurpleDark
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "نشانگر رنگی وضعیت حضور شما در پروفایل و لیست گفتگوها با نقطه رنگی نمایش داده می‌شود.",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats 3-Col Card (الماس | اهدای اختصاصی | اهدای امتیاز)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💎", fontSize = 18.sp)
                        Text("${activeProfile.diamonds}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                        Text("الماس", fontSize = 11.sp, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("👑", fontSize = 18.sp)
                        Text("${activeProfile.specialGiftsCount}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                        Text("اهدای اختصاصی", fontSize = 11.sp, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⭐", fontSize = 18.sp)
                        Text("${activeProfile.pointsGiftCount}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                        Text("اهدای امتیاز", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Profile Buttons (ویرایش پروفایل / پنل مدیریت)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!isViewingCreator) {
                    Button(
                        onClick = { showEditProfileDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Text("ویرایش کامل پروفایل 🎨", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                // Owner Admin Panel Button
                Button(
                    onClick = onOpenAdminPanel,
                    colors = ButtonDefaults.buttonColors(containerColor = AsalGold),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = AsalPurpleDark, modifier = Modifier.size(16.dp))
                        Text("پنل مدیریت مالک 👑", color = AsalPurpleDark, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Secondary Buttons: چت خصوصی با سازنده
            if (isViewingCreator) {
                Button(
                    onClick = onOpenPrivateChatWithCreator,
                    colors = ButtonDefaults.buttonColors(containerColor = AsalPurpleDark),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text("چت خصوصی با پریا چش رنگی 💬", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Profile Settings List (همه دکمه‌ها واقعی کار می‌کنند)
            SettingsSection(title = "حساب اختصاصی ...") {
                ProfileSettingItem(
                    title = "خرید حساب",
                    subtitle = "از حساب اختصاصی ${user.vipRemainingText}",
                    emoji = "👑",
                    onClick = onOpenVipModal
                )
                ProfileSettingItem(
                    title = "بیوگرافی",
                    subtitle = user.bio.take(40),
                    emoji = "📖",
                    onClick = { showBioDialog = true }
                )
                ProfileSettingItem(
                    title = "ویرایش عکس",
                    subtitle = "پیش‌فرض خانم / آقا یا گالری (۱۰ الماس)",
                    emoji = "🖼️",
                    onClick = { showPhotoDialog = true }
                )
                ProfileSettingItem(
                    title = "نام، آیدی، وضعیت و تیک",
                    subtitle = "ویرایش کامل پروفایل",
                    emoji = "🎨",
                    onClick = { showEditProfileDialog = true }
                )
                ProfileSettingItem(
                    title = "پنل مدیریت مالک",
                    subtitle = "تنظیمات انحصاری مالک و سازنده عسل آباد",
                    emoji = "🛡️",
                    onClick = onOpenAdminPanel
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            SettingsSection(title = "ذخیره حساب ...") {
                ProfileSettingItem(
                    title = "ذخیره حساب",
                    subtitle = "حساب خود را با Gmail ذخیره کنید",
                    emoji = "☁️",
                    onClick = {
                        val backup = JSONObject().apply {
                            put("app", "asal_abad")
                            put("name", user.name)
                            put("username", user.username)
                            put("bio", user.bio)
                            put("emoji", user.avatarEmoji)
                        }.toString()
                        val send = Intent(Intent.ACTION_SEND).apply {
                            type = "message/rfc822"
                            putExtra(Intent.EXTRA_SUBJECT, "نسخه پشتیبان حساب عسل آباد")
                            putExtra(Intent.EXTRA_TEXT, backup)
                        }
                        try {
                            context.startActivity(Intent.createChooser(send, "ذخیره حساب با Gmail").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        } catch (_: Throwable) {
                            Toast.makeText(context, "برنامه‌ای برای ارسال پیدا نشد", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                ProfileSettingItem(
                    title = "بازیابی حساب",
                    subtitle = "متن پشتیبان کپی‌شده را بازیابی کنید",
                    emoji = "🔄",
                    onClick = {
                        try {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val raw = cm.primaryClip?.getItemAt(0)?.coerceToText(context)?.toString().orEmpty()
                            val start = raw.indexOf('{')
                            val end = raw.lastIndexOf('}')
                            val json = JSONObject(raw.substring(start, end + 1))
                            require(json.optString("app") == "asal_abad")
                            onRestoreBackup(
                                json.optString("name"),
                                json.optString("username"),
                                json.optString("bio"),
                                json.optString("emoji")
                            )
                            Toast.makeText(context, "حساب با موفقیت بازیابی شد ✅", Toast.LENGTH_SHORT).show()
                        } catch (_: Throwable) {
                            Toast.makeText(
                                context,
                                "ابتدا متن پشتیبان ایمیل را کپی کنید، سپس دوباره بزنید",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            SettingsSection(title = "خروج و حذف ...") {
                ProfileSettingItem(
                    title = if (!user.email.isNullOrBlank()) "خروج از حساب (${user.email})" else "خروج از حساب / تغییر کاربر",
                    subtitle = "ورود مجدد، ورود با ایمیل دیگر یا خروج از سیستم",
                    emoji = "🚪",
                    onClick = onSignOut
                )
                ProfileSettingItem(
                    title = "حذف حساب",
                    subtitle = "پاک شدن اطلاعات و خروج از برنامه",
                    emoji = "🗑️",
                    onClick = { showDeleteDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showPhotoDialog) {
            EditPhotoDialog(
                currentDiamonds = user.diamonds,
                onPickDefault = onSetDefaultAvatar,
                onPickGallery = onPickGalleryAvatar,
                onDismiss = { showPhotoDialog = false }
            )
        }
        if (showBioDialog) {
            BioEditDialog(
                currentBio = user.bio,
                onSave = { onSaveBio(it); showBioDialog = false },
                onDismiss = { showBioDialog = false }
            )
        }
        if (showDeleteDialog) {
            ConfirmDialog(
                title = "حذف حساب 🗑️",
                message = "با حذف حساب، همه اطلاعات شما (عکس، بیو، الماس) پاک می‌شود و از برنامه خارج می‌شوید. مطمئنید؟",
                confirmText = "بله، حذف شود",
                onConfirm = { showDeleteDialog = false; onDeleteAccount() },
                onDismiss = { showDeleteDialog = false }
            )
        }

        // Edit Profile Full Dialog
        if (showEditProfileDialog) {
            EditUserProfileFullDialog(
                user = user,
                onSave = { name, username, bio, emoji, uri, status, tick ->
                    onUpdateFullProfile(name, username, bio, emoji, uri, status, tick)
                    showEditProfileDialog = false
                },
                onDismiss = { showEditProfileDialog = false }
            )
        }
    }
}

@Composable
private fun ProfileSettingItem(
    title: String,
    subtitle: String,
    emoji: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3E8FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 20.sp)
                }
                Column {
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                    Text(subtitle, fontSize = 11.sp, color = Color.Gray)
                }
            }
            Text("‹", fontSize = 18.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFF6F0FD),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCCBF3)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AsalPurpleDark,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            content()
        }
    }
}
