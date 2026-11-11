package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CandidateUser
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary

/**
 * UI Component for creating group chats with multi-user selection,
 * allowing multiple users to be added to a new conversation thread.
 */
@Composable
fun CreateGroupChatDialog(
    candidateUsers: List<CandidateUser>,
    onCreateGroup: (title: String, description: String, emoji: String, selectedUsers: List<CandidateUser>) -> Unit,
    onDismiss: () -> Unit
) {
    var groupName by remember { mutableStateOf("گپ دوستانه عسل آباد 🌸") }
    var groupDesc by remember { mutableStateOf("چت، سرگرمی و بازی با مدیریت پریا چش رنگی 👑") }
    var selectedEmoji by remember { mutableStateOf("🌸") }
    var userSearchQuery by remember { mutableStateOf("") }

    // Initially preselect key users (Paria and Pani)
    var selectedUserIds by remember {
        mutableStateOf(
            candidateUsers.filter { it.isPreselected }.map { it.id }.toSet()
        )
    }

    val emojis = listOf("🌸", "🦋", "👑", "🐝", "✨", "💎", "🔥", "⚡", "🦅", "🎯", "🎲", "🍯")

    val filteredCandidates = remember(candidateUsers, userSearchQuery) {
        val q = userSearchQuery.trim().lowercase()
        if (q.isBlank()) candidateUsers
        else candidateUsers.filter {
            it.name.lowercase().contains(q) ||
            it.username.lowercase().contains(q) ||
            it.roleTitle.lowercase().contains(q)
        }
    }

    val selectedUsersList = remember(candidateUsers, selectedUserIds) {
        candidateUsers.filter { selectedUserIds.contains(it.id) }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .fillMaxHeight(0.92f)
                    .padding(vertical = 12.dp)
                    .testTag("create_group_chat_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "بستن", tint = Color.Gray)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.GroupAdd, contentDescription = null, tint = AsalPurplePrimary)
                                Text(
                                    text = "ساخت گروه گفتگو 👥",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsalPurpleDark
                                )
                            }
                            Text(
                                text = "با مدیریت و نظارت پریا چش رنگی 👑",
                                fontSize = 10.sp,
                                color = AsalPurplePrimary
                            )
                        }

                        Spacer(modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Group Name Field
                        item {
                            OutlinedTextField(
                                value = groupName,
                                onValueChange = { groupName = it },
                                label = { Text("نام گروه") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("create_group_name_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AsalPurplePrimary,
                                    unfocusedBorderColor = Color(0xFFDDD2F5)
                                )
                            )
                        }

                        // Group Description Field
                        item {
                            OutlinedTextField(
                                value = groupDesc,
                                onValueChange = { groupDesc = it },
                                label = { Text("توضیحات و بیو گروه") },
                                maxLines = 2,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("create_group_desc_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AsalPurplePrimary,
                                    unfocusedBorderColor = Color(0xFFDDD2F5)
                                )
                            )
                        }

                        // Emoji Selector
                        item {
                            Text(
                                text = "نماد و آیکون گروه:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(emojis) { emoji ->
                                    val isSelected = selectedEmoji == emoji
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) Color(0xFFF3E8FF) else Color(0xFFF9FAFB))
                                            .border(
                                                1.5.dp,
                                                if (isSelected) AsalPurplePrimary else Color.Transparent,
                                                CircleShape
                                            )
                                            .clickable { selectedEmoji = emoji },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(emoji, fontSize = 22.sp)
                                    }
                                }
                            }
                        }

                        // Multi-User Participant Selection Header
                        item {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "افزودن اعضا به گفتگو (${selectedUserIds.size} نفر)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsalPurpleDark
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "انتخاب همه",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AsalPurplePrimary,
                                        modifier = Modifier.clickable {
                                            selectedUserIds = candidateUsers.map { it.id }.toSet()
                                        }
                                    )
                                    Text(
                                        text = "•",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "حذف همه",
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.clickable {
                                            selectedUserIds = emptySet()
                                        }
                                    )
                                }
                            }
                        }

                        // Selected Users Chips Row
                        if (selectedUsersList.isNotEmpty()) {
                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(selectedUsersList, key = { it.id }) { user ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (user.isManager) Color(0xFFFFFBEB) else Color(0xFFF3E8FF),
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                if (user.isManager) AsalGoldLight else Color(0xFFDDD2F5)
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(user.avatarEmoji, fontSize = 14.sp)
                                                Text(
                                                    text = user.name,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = if (user.isManager) Color(0xFF92400E) else AsalPurpleDark
                                                )
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "حذف",
                                                    tint = Color.Gray,
                                                    modifier = Modifier
                                                        .size(14.dp)
                                                        .clickable {
                                                            selectedUserIds = selectedUserIds - user.id
                                                        }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Candidate Users Search Box
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF9FAFB),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEDE9FE)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "جستجو",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    androidx.compose.foundation.text.BasicTextField(
                                        value = userSearchQuery,
                                        onValueChange = { userSearchQuery = it },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 8.dp)
                                            .testTag("create_group_user_search"),
                                        singleLine = true,
                                        textStyle = androidx.compose.ui.text.TextStyle(
                                            fontSize = 12.sp,
                                            color = AsalPurpleDark
                                        ),
                                        decorationBox = { innerTextField ->
                                            if (userSearchQuery.isEmpty()) {
                                                Text(
                                                    "جستجوی مخاطب برای افزودن به گروه...",
                                                    fontSize = 11.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )
                                    if (userSearchQuery.isNotEmpty()) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "پاک کردن",
                                            tint = Color.Gray,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable { userSearchQuery = "" }
                                        )
                                    }
                                }
                            }
                        }

                        // Candidate User Cards
                        items(filteredCandidates, key = { it.id }) { candidate ->
                            val isSelected = selectedUserIds.contains(candidate.id)
                            ParyaUserCandidateRow(
                                user = candidate,
                                isSelected = isSelected,
                                onToggleSelect = {
                                    selectedUserIds = if (isSelected) {
                                        selectedUserIds - candidate.id
                                    } else {
                                        selectedUserIds + candidate.id
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (groupName.isNotBlank()) {
                                    onCreateGroup(
                                        groupName,
                                        groupDesc,
                                        selectedEmoji,
                                        selectedUsersList
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(48.dp)
                                .testTag("create_group_submit_button")
                        ) {
                            Text(
                                text = "ایجاد گروه (${selectedUserIds.size} عضو) 🚀",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(0.7f)
                                .height(48.dp)
                                .testTag("create_group_cancel_button")
                        ) {
                            Text("انصراف", color = Color(0xFF4B5563), fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
