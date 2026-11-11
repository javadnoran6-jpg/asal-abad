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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AttachmentType
import com.example.data.model.ChatMessage
import com.example.data.model.ChatRoom
import kotlinx.coroutines.delay
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary

data class MessageSearchResult(
    val room: ChatRoom,
    val message: ChatMessage
)

@Composable
fun MessengerScreen(
    rooms: List<ChatRoom>,
    activeRoomId: String?,
    messages: List<ChatMessage>,
    allMessagesMap: Map<String, List<ChatMessage>> = emptyMap(),
    onOpenRoom: (String) -> Unit,
    onCloseRoom: () -> Unit,
    onSendMessage: (String, String?, String?) -> Unit, // roomId, text, sticker
    onSendMedia: (roomId: String, type: AttachmentType, mediaUri: String, caption: String) -> Unit = { _, _, _, _ -> },
    onSendVoice: (roomId: String, durationSeconds: Int) -> Unit = { _, _ -> },
    onStartVideoCall: (ChatRoom) -> Unit = {},
    onStartVoiceCall: (ChatRoom) -> Unit = {},
    onOpenMarket: () -> Unit,
    onOpenMergeGroups: () -> Unit,
    onOpenCreateGroup: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (activeRoomId != null) {
        val currentRoom = rooms.find { it.id == activeRoomId } ?: rooms.first()
        ChatDetailScreen(
            room = currentRoom,
            messages = messages,
            onBack = onCloseRoom,
            onSend = { text, sticker -> onSendMessage(currentRoom.id, text, sticker) },
            onSendMedia = { type, uri, caption -> onSendMedia(currentRoom.id, type, uri, caption) },
            onSendVoice = { duration -> onSendVoice(currentRoom.id, duration) },
            onStartVideoCall = { onStartVideoCall(currentRoom) },
            onStartVoiceCall = { onStartVoiceCall(currentRoom) }
        )
    } else {
        ChatRoomsListScreen(
            rooms = rooms,
            allMessagesMap = allMessagesMap,
            onOpenRoom = onOpenRoom,
            onOpenMarket = onOpenMarket,
            onOpenMergeGroups = onOpenMergeGroups,
            onOpenCreateGroup = onOpenCreateGroup,
            modifier = modifier
        )
    }
}

@Composable
private fun ChatRoomsListScreen(
    rooms: List<ChatRoom>,
    allMessagesMap: Map<String, List<ChatMessage>> = emptyMap(),
    onOpenRoom: (String) -> Unit,
    onOpenMarket: () -> Unit,
    onOpenMergeGroups: () -> Unit,
    onOpenCreateGroup: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(0) } // 0: همه, 1: دوستان, 2: گروه‌ها
    var searchResultFilter by remember { mutableStateOf(0) } // 0: همه, 1: گفتگوها, 2: پیام‌ها

    val trimmedQuery = searchQuery.trim()
    val isSearching = trimmedQuery.isNotEmpty()

    // Matching rooms based on title, subtitle, or last message
    val matchingRooms = remember(rooms, trimmedQuery) {
        if (!isSearching) emptyList()
        else {
            rooms.filter { room ->
                room.title.contains(trimmedQuery, ignoreCase = true) ||
                room.subtitle.contains(trimmedQuery, ignoreCase = true) ||
                room.lastMessage.contains(trimmedQuery, ignoreCase = true)
            }
        }
    }

    // Matching individual messages across all conversations
    val matchingMessages = remember(rooms, allMessagesMap, trimmedQuery) {
        if (!isSearching) emptyList()
        else {
            val results = mutableListOf<MessageSearchResult>()
            rooms.forEach { room ->
                val roomMessages = allMessagesMap[room.id] ?: emptyList()
                roomMessages.forEach { msg ->
                    if (msg.text.contains(trimmedQuery, ignoreCase = true) ||
                        msg.senderName.contains(trimmedQuery, ignoreCase = true)
                    ) {
                        results.add(MessageSearchResult(room, msg))
                    }
                }
            }
            results
        }
    }

    // Standard filter for non-search mode (0: همه, 1: آنلاین‌ها, 2: دوستان, 3: گروه‌ها)
    val filteredRooms = remember(rooms, selectedFilter) {
        when (selectedFilter) {
            1 -> rooms.filter { (it.isGroup && it.onlineCount > 0) || (!it.isGroup && it.contactStatus == com.example.data.model.UserStatus.ONLINE) }
            2 -> rooms.filter { !it.isGroup }
            3 -> rooms.filter { it.isGroup }
            else -> rooms
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // ==========================================
            // SEARCH BAR AT THE TOP OF THE CHAT LIST
            // ==========================================
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 3.dp,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSearching) AsalPurplePrimary else Color(0xFFDDD2F5)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "جستجو",
                        tint = if (isSearching) AsalPurplePrimary else Color(0xFF9333EA),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 12.dp)
                            .testTag("chat_search_input"),
                        singleLine = true,
                        cursorBrush = SolidColor(AsalPurplePrimary),
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            color = AsalPurpleDark,
                            fontFamily = FontFamily.Default
                        ),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "جستجوی گفتگوها یا پیام‌ها...",
                                    fontSize = 13.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                            innerTextField()
                        }
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("chat_search_clear_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "پاک کردن جستجو",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // ==========================================
            // SEARCH MODE ACTIVE vs REGULAR MODE
            // ==========================================
            if (isSearching) {
                // Search Result Filter Tabs (همه، گفتگوها، پیام‌ها)
                val totalResults = matchingRooms.size + matchingMessages.size
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val searchTabs = listOf(
                        "همه ($totalResults)",
                        "گفتگوها (${matchingRooms.size})",
                        "پیام‌ها (${matchingMessages.size})"
                    )
                    searchTabs.forEachIndexed { index, title ->
                        val isSelected = searchResultFilter == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) AsalPurplePrimary else Color.Transparent)
                                .clickable { searchResultFilter = index }
                                .padding(vertical = 7.dp)
                                .testTag(
                                    when (index) {
                                        0 -> "chat_search_tab_all"
                                        1 -> "chat_search_tab_rooms"
                                        else -> "chat_search_tab_messages"
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else AsalPurpleDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (totalResults == 0) {
                    // Empty search state
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF3E8FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🔍", fontSize = 28.sp)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "نتیجه‌ای برای «$trimmedQuery» یافت نشد",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "می‌توانید کلمه دیگری مثل «سلام»، «الماس»، «پریا»، «گروه» یا بخشی از نام مخاطب را جستجو کنید.",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                onClick = { searchQuery = "" },
                                shape = RoundedCornerShape(12.dp),
                                color = AsalPurpleLight
                            ) {
                                Text(
                                    text = "پاک کردن جستجو",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsalPurplePrimary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                } else {
                    // Search results list
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Section 1: Matching Conversations
                        if ((searchResultFilter == 0 || searchResultFilter == 1) && matchingRooms.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "💬 گفتگوهای منطبق (${matchingRooms.size})",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AsalPurplePrimary
                                    )
                                    Text(
                                        text = "برای ورود لمس کنید",
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            items(matchingRooms, key = { "room_${it.id}" }) { room ->
                                ChatRoomCard(
                                    room = room,
                                    searchQuery = trimmedQuery,
                                    onClick = { onOpenRoom(room.id) }
                                )
                            }
                        }

                        // Section 2: Matching Individual Messages
                        if ((searchResultFilter == 0 || searchResultFilter == 2) && matchingMessages.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "✉️ پیام‌های پیدا شده (${matchingMessages.size})",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AsalPurplePrimary
                                    )
                                    Text(
                                        text = "درون تمام چت‌ها",
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            items(matchingMessages, key = { "msg_${it.message.id}" }) { result ->
                                MessageSearchResultCard(
                                    result = result,
                                    searchQuery = trimmedQuery,
                                    onClick = { onOpenRoom(result.room.id) }
                                )
                            }
                        }
                    }
                }
            } else {
                // ==========================================
                // REGULAR CHAT LIST MODE
                // ==========================================
                // Top Action Quick Bar (Create Group with Pani + Market + Merge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Create Group with Pani Button
                    Surface(
                        onClick = onOpenCreateGroup,
                        shape = RoundedCornerShape(16.dp),
                        color = AsalPurplePrimary,
                        shadowElevation = 3.dp,
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("ساخت گروه با پانی 🌸", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("➕", fontSize = 13.sp)
                        }
                    }

                    // Merge Groups Button
                    Surface(
                        onClick = onOpenMergeGroups,
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("ادغام", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("👥", fontSize = 13.sp)
                        }
                    }

                    // Market Button
                    Surface(
                        onClick = onOpenMarket,
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("بازارچه", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AsalPurplePrimary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("🏪", fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sub-tabs: همه, آنلاین‌ها 🟢, دوستان, گروه‌ها
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("همه", "آنلاین‌ها 🟢", "دوستان", "گروه‌ها").forEachIndexed { index, title ->
                        val isSelected = selectedFilter == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) AsalPurplePrimary else Color.Transparent)
                                .clickable { selectedFilter = index }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else AsalPurpleDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Filtered Rooms List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredRooms, key = { it.id }) { room ->
                        ChatRoomCard(
                            room = room,
                            searchQuery = "",
                            onClick = { onOpenRoom(room.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatRoomCard(
    room: ChatRoom,
    searchQuery: String = "",
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("chat_room_item_${room.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with Real-Time Presence Indicator Dot
            Box(
                modifier = Modifier.size(54.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            if (room.isCreatorChat) Brush.linearGradient(
                                listOf(AsalGold, AsalPurpleLight)
                            ) else Brush.linearGradient(
                                listOf(Color(0xFFEDE9FE), Color(0xFFC4B5FD))
                            )
                        )
                        .border(
                            if (room.isCreatorChat) 2.dp else 1.dp,
                            if (room.isCreatorChat) AsalGold else Color(0xFFDDD2F5),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(room.avatarEmoji, fontSize = 24.sp)
                }

                // Colored Presence Indicator Dot (Green: Online, Purple: In-Game, Amber: Busy, Gray: Offline)
                val dotColor = if (room.isGroup) {
                    if (room.onlineCount > 0) Color(0xFF10B981) else Color(0xFF9CA3AF)
                } else {
                    Color(room.contactStatus.colorHex)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                        .border(2.dp, Color.White, CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HighlightedText(
                        text = room.title,
                        query = searchQuery,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (room.isCreatorChat) AsalPurplePrimary else AsalPurpleDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = room.lastMessageTime,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HighlightedText(
                        text = room.lastMessage,
                        query = searchQuery,
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (room.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(AsalPurplePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${room.unreadCount}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageSearchResultCard(
    result: MessageSearchResult,
    searchQuery: String,
    onClick: () -> Unit
) {
    val room = result.room
    val message = result.message

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEDE9FE)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("chat_search_message_item_${message.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header: Room info & timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(room.avatarEmoji, fontSize = 18.sp)
                    Text(
                        text = room.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalPurplePrimary
                    )
                    if (room.isGroup) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF3E8FF)
                        ) {
                            Text(
                                "گروه",
                                fontSize = 9.sp,
                                color = AsalPurplePrimary,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = message.timestamp,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Sender name & message badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (message.isMe) "شما:" else "${message.senderName}:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (message.isMe) Color.Gray else AsalPurpleDark
                )
                if (message.badge.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = message.badge,
                            fontSize = 8.sp,
                            color = Color(0xFF92400E),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Message text snippet with highlighting
            HighlightedText(
                text = message.text.ifEmpty { "[استیکر]" },
                query = searchQuery,
                fontSize = 12.sp,
                color = Color(0xFF374151),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "مشاهده در گفتگو 👈",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AsalPurplePrimary
                )
            }
        }
    }
}

@Composable
fun HighlightedText(
    text: String,
    query: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp,
    color: Color = AsalPurpleDark,
    highlightBgColor: Color = Color(0xFFFEF08A),
    highlightTextColor: Color = AsalPurplePrimary,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    val cleanQuery = query.trim().lowercase()
    if (cleanQuery.isBlank() || !text.lowercase().contains(cleanQuery)) {
        Text(
            text = text,
            modifier = modifier,
            fontSize = fontSize,
            color = color,
            fontWeight = fontWeight,
            maxLines = maxLines,
            overflow = overflow
        )
        return
    }

    val annotated = buildAnnotatedString {
        val lowerText = text.lowercase()
        var cursor = 0
        while (cursor < text.length) {
            val index = lowerText.indexOf(cleanQuery, cursor)
            if (index == -1) {
                append(text.substring(cursor))
                break
            }
            if (index > cursor) {
                append(text.substring(cursor, index))
            }
            val matchEnd = index + cleanQuery.length
            withStyle(
                SpanStyle(
                    color = highlightTextColor,
                    fontWeight = FontWeight.ExtraBold,
                    background = highlightBgColor
                )
            ) {
                append(text.substring(index, matchEnd))
            }
            cursor = matchEnd
        }
    }

    Text(
        text = annotated,
        modifier = modifier,
        fontSize = fontSize,
        color = color,
        fontWeight = fontWeight,
        maxLines = maxLines,
        overflow = overflow
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatDetailScreen(
    room: ChatRoom,
    messages: List<ChatMessage>,
    onBack: () -> Unit,
    onSend: (String, String?) -> Unit,
    onSendMedia: (AttachmentType, String, String) -> Unit,
    onSendVoice: (Int) -> Unit,
    onStartVideoCall: () -> Unit,
    onStartVoiceCall: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var showStickerTray by remember { mutableStateOf(false) }
    var inChatSearchActive by remember { mutableStateOf(false) }
    var inChatSearchQuery by remember { mutableStateOf("") }
    var showAttachmentDialog by remember { mutableStateOf(false) }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var recordingSeconds by remember { mutableIntStateOf(0) }
    var previewPhotoUri by remember { mutableStateOf<String?>(null) }
    var previewVideoTitle by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(isRecordingVoice) {
        if (isRecordingVoice) {
            recordingSeconds = 0
            while (isRecordingVoice) {
                delay(1000)
                recordingSeconds++
            }
        }
    }

    val cuteStickers = listOf(
        Pair("cat_wink", "😉"),
        Pair("cat_hug", "🤗"),
        Pair("cat_love", "🥰"),
        Pair("cat_cry", "😭"),
        Pair("cat_sleep", "😴"),
        Pair("cat_cart", "🛒"),
        Pair("cat_angel", "😇"),
        Pair("cat_devil", "😈")
    )

    val displayedMessages = remember(messages, inChatSearchQuery) {
        val q = inChatSearchQuery.trim().lowercase()
        if (q.isBlank()) messages
        else messages.filter {
            it.text.lowercase().contains(q) || it.senderName.lowercase().contains(q)
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                if (inChatSearchActive) {
                    TopAppBar(
                        title = {
                            BasicTextField(
                                value = inChatSearchQuery,
                                onValueChange = { inChatSearchQuery = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .testTag("in_chat_search_input"),
                                singleLine = true,
                                cursorBrush = SolidColor(Color.White),
                                textStyle = TextStyle(fontSize = 14.sp, color = Color.White),
                                decorationBox = { innerTextField ->
                                    if (inChatSearchQuery.isEmpty()) {
                                        Text(
                                            "جستجو در این گفتگو...",
                                            fontSize = 13.sp,
                                            color = Color(0xFFDDD2F5)
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                inChatSearchActive = false
                                inChatSearchQuery = ""
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "بستن جستجو",
                                    tint = Color.White
                                )
                            }
                        },
                        actions = {
                            if (inChatSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { inChatSearchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "پاک کردن", tint = Color.White)
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = AsalPurplePrimary)
                    )
                } else {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = room.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    val dotColor = if (room.isGroup) {
                                        if (room.onlineCount > 0) Color(0xFF10B981) else Color(0xFF9CA3AF)
                                    } else {
                                        Color(room.contactStatus.colorHex)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(dotColor)
                                            .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                                    )
                                    Text(
                                        text = if (room.isGroup) "${room.memberCount} عضو • ${room.onlineCount} آنلاین" else "${room.contactStatus.label} • عسل آباد",
                                        fontSize = 11.sp,
                                        color = Color(0xFFE9D5FF)
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت", tint = Color.White)
                            }
                        },
                        actions = {
                            IconButton(onClick = { inChatSearchActive = true }) {
                                Icon(Icons.Default.Search, contentDescription = "جستجو در پیام‌ها", tint = Color.White)
                            }
                            IconButton(onClick = onStartVoiceCall) {
                                Icon(Icons.Default.Call, contentDescription = "ویس‌کال", tint = Color.White)
                            }
                            IconButton(onClick = onStartVideoCall) {
                                Icon(Icons.Default.Videocam, contentDescription = "تماس تصویری با فیلتر", tint = Color.White)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = AsalPurplePrimary)
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFFDDD2F5),
                                Color(0xFFEDE4FA),
                                Color(0xFFFAF5FF)
                            )
                        )
                    )
            ) {
                // In-Chat Search Summary banner if filter active
                if (inChatSearchActive && inChatSearchQuery.isNotBlank()) {
                    Surface(
                        color = Color(0xFFFEF3C7),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "نتایج جستجو: ${displayedMessages.size} پیام پیدا شد",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }

                // Messages List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(displayedMessages) { msg ->
                        ChatBubbleItem(
                            message = msg,
                            searchQuery = if (inChatSearchActive) inChatSearchQuery else "",
                            onPreviewPhoto = { uri -> previewPhotoUri = uri },
                            onPreviewVideo = { caption -> previewVideoTitle = caption }
                        )
                    }
                }

                // Sticker Tray (if opened)
                if (showStickerTray) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        color = Color.White,
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "استیکرهای ناز گربه‌ای عسل آباد 🐱",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurplePrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(cuteStickers) { (code, emoji) ->
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFF3E8FF))
                                            .clickable {
                                                onSend("", code)
                                                showStickerTray = false
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = emoji, fontSize = 32.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom Input Bar
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isRecordingVoice) {
                        // Interactive Voice Recording Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val pulseAlpha by rememberInfiniteTransition().animateFloat(
                                    initialValue = 0.3f,
                                    targetValue = 1f,
                                    animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red.copy(alpha = pulseAlpha))
                                )
                                val minutes = recordingSeconds / 60
                                val seconds = recordingSeconds % 60
                                Text(
                                    text = String.format("%02d:%02d", minutes, seconds),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                                Text(
                                    text = "در حال ضبط ویس...",
                                    fontSize = 12.sp,
                                    color = AsalPurpleDark
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextButton(onClick = {
                                    isRecordingVoice = false
                                    recordingSeconds = 0
                                }) {
                                    Text("لغو", color = Color.Gray, fontSize = 12.sp)
                                }

                                Button(
                                    onClick = {
                                        val duration = if (recordingSeconds > 0) recordingSeconds else 3
                                        onSendVoice(duration)
                                        isRecordingVoice = false
                                        recordingSeconds = 0
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "ارسال ویس",
                                        modifier = Modifier.size(16.dp),
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("ارسال ویس", fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    } else {
                        // Normal input row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Sticker toggle
                            IconButton(onClick = { showStickerTray = !showStickerTray }) {
                                Icon(
                                    Icons.Default.SentimentSatisfied,
                                    contentDescription = "استیکر",
                                    tint = if (showStickerTray) AsalPurplePrimary else Color.Gray
                                )
                            }

                            // Attachment Picker (عکس و فیلم)
                            IconButton(onClick = { showAttachmentDialog = true }) {
                                Icon(
                                    Icons.Default.AddPhotoAlternate,
                                    contentDescription = "ارسال عکس یا فیلم",
                                    tint = AsalPurplePrimary
                                )
                            }

                            // Voice Recorder Trigger
                            IconButton(onClick = { isRecordingVoice = true }) {
                                Icon(
                                    Icons.Default.Mic,
                                    contentDescription = "ضبط ویس",
                                    tint = AsalPurplePrimary
                                )
                            }

                            // Text Field
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                placeholder = { Text("متن پیام ...", fontSize = 13.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AsalPurplePrimary,
                                    unfocusedBorderColor = Color(0xFFEDE9FE)
                                ),
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            // Send Button
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(AsalPurplePrimary)
                                    .clickable {
                                        if (inputText.isNotBlank()) {
                                            onSend(inputText, null)
                                            inputText = ""
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "ارسال",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Media Attachment Dialog
        if (showAttachmentDialog) {
            MediaAttachmentDialog(
                onSendImage = { uri, caption ->
                    onSendMedia(AttachmentType.IMAGE, uri, caption)
                },
                onSendVideo = { uri, caption ->
                    onSendMedia(AttachmentType.VIDEO, uri, caption)
                },
                onDismiss = { showAttachmentDialog = false }
            )
        }

        // Photo Preview Dialog
        if (previewPhotoUri != null) {
            PhotoPreviewDialog(
                uri = previewPhotoUri!!,
                onDismiss = { previewPhotoUri = null }
            )
        }

        // Video Player Dialog
        if (previewVideoTitle != null) {
            VideoPlayerDialog(
                title = previewVideoTitle!!,
                onDismiss = { previewVideoTitle = null }
            )
        }
    }
}

@Composable
private fun ChatBubbleItem(
    message: ChatMessage,
    searchQuery: String = "",
    onPreviewPhoto: (String) -> Unit = {},
    onPreviewVideo: (String) -> Unit = {}
) {
    val isMe = message.isMe
    val align = if (isMe) Alignment.End else Alignment.Start
    val bubbleColor = if (isMe) Color.White else Color(0xFFF3E8FF)
    var isPlayingVoice by remember { mutableStateOf(false) }
    var currentReaction by remember { mutableStateOf(message.reaction) }
    var showReactionMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        if (!isMe) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                HighlightedText(
                    text = message.senderName,
                    query = searchQuery,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (message.isCreator) AsalPurplePrimary else Color(0xFF4B5563)
                )
                if (message.badge.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(message.badge, fontSize = 9.sp, color = Color(0xFF92400E))
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 16.dp
            ),
            color = bubbleColor,
            shadowElevation = 1.dp,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isMe) Color(0xFFE9D5FF) else Color(0xFFDDD2F5)
            ),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .clickable { showReactionMenu = !showReactionMenu }
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                if (message.replyToText != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEDE9FE))
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(message.replyToText, fontSize = 10.sp, color = AsalPurplePrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // 1. IMAGE ATTACHMENT
                if (message.attachmentType == AttachmentType.IMAGE) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE9D5FF),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .clickable { onPreviewPhoto(message.mediaUri ?: "photo") }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (message.mediaUri?.startsWith("/") == true) {
                                coil.compose.AsyncImage(
                                    model = java.io.File(message.mediaUri),
                                    contentDescription = "عکس ارسالی",
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                            } else
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFFB794F4), Color(0xFF805AD5))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("📷", fontSize = 42.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "تصویر ارسالی عسل آباد",
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("لمس کنید برای بزرگنمایی", fontSize = 9.sp, color = Color(0xFFE9D5FF))
                                }
                            }
                            if (!message.mediaCaption.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = message.mediaCaption,
                                    fontSize = 12.sp,
                                    color = AsalPurpleDark,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // 2. VIDEO ATTACHMENT
                if (message.attachmentType == AttachmentType.VIDEO) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF2D1B4E),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .clickable { onPreviewVideo(message.mediaCaption ?: "ویدیو عسل آباد") }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF44337A), Color(0xFF1A102F))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(AsalPurplePrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.PlayArrow,
                                            contentDescription = "پخش ویدیو",
                                            tint = Color.White,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("🎬 پخش ویدیو کلیپ", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("00:28 • با کیفیت HD", fontSize = 9.sp, color = Color(0xFFD6BCFA))
                                }
                            }
                            if (!message.mediaCaption.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = message.mediaCaption,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // 3. VOICE NOTE ATTACHMENT
                if (message.attachmentType == AttachmentType.VOICE) {
                    val duration = if (message.voiceDurationSeconds > 0) message.voiceDurationSeconds else 5
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isMe) Color(0xFFF3E8FF) else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD6BCFA)),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(AsalPurplePrimary)
                                    .clickable { isPlayingVoice = !isPlayingVoice },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (isPlayingVoice) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "پخش ویس",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Simulated voice audio waveform bars
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val barHeights = listOf(10, 18, 24, 14, 28, 20, 12, 22, 16, 26, 12, 18)
                                val animAlpha by rememberInfiniteTransition().animateFloat(
                                    initialValue = 0.5f,
                                    targetValue = 1f,
                                    animationSpec = infiniteRepeatable(tween(400), RepeatMode.Reverse)
                                )
                                barHeights.forEach { h ->
                                    Box(
                                        modifier = Modifier
                                            .width(3.dp)
                                            .height(if (isPlayingVoice) (h * animAlpha).dp else (h * 0.7f).dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (isPlayingVoice) AsalPurplePrimary else Color(0xFF9F7AEA))
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = String.format("0:%02d", duration),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsalPurpleDark
                                )
                                Text(
                                    text = if (isPlayingVoice) "در حال پخش" else "ویس صوتی",
                                    fontSize = 9.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Sticker
                if (message.stickerType != null) {
                    val stickerEmoji = when (message.stickerType) {
                        "cat_wink" -> "😉 🐱"
                        "cat_hug" -> "🤗 🐱"
                        "cat_love" -> "🥰 🐱"
                        "cat_cry" -> "😭 🐱"
                        "cat_sleep" -> "😴 🐱"
                        "cat_cart" -> "🛒 🐱"
                        "cat_angel" -> "😇 🐱"
                        else -> "😻"
                    }
                    Text(text = stickerEmoji, fontSize = 44.sp)
                }

                // Text
                if (message.text.isNotEmpty()) {
                    HighlightedText(
                        text = message.text,
                        query = searchQuery,
                        fontSize = 13.sp,
                        color = AsalPurpleDark
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.timestamp,
                        fontSize = 9.sp,
                        color = Color.Gray
                    )
                    if (isMe) {
                        Text(text = "✓✓", fontSize = 9.sp, color = AsalPurplePrimary)
                    }
                }
            }
        }

        // Active reaction badge
        if (currentReaction != null) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp, start = 8.dp, end = 8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE9D5FF), RoundedCornerShape(10.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(currentReaction!!, fontSize = 12.sp)
            }
        }

        // Quick Reaction selector bar
        if (showReactionMenu) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 6.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("❤️", "👑", "🔥", "😂", "👏", "🐝").forEach { emoji ->
                        Text(
                            text = emoji,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    currentReaction = emoji
                                    showReactionMenu = false
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaAttachmentDialog(
    onSendImage: (uri: String, caption: String) -> Unit,
    onSendVideo: (uri: String, caption: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: عکس, 1: فیلم
    var captionInput by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableIntStateOf(0) }

    val photoPresets = listOf(
        Pair("📸 سلفی یاسی پریا چش رنگی 👑", "image_paria_lilac"),
        Pair("🐝 دورهمی روم دوستان عسل آباد", "image_room_friends"),
        Pair("🎡 جایزه چرخنده الماس و ژانوس", "image_wheel_reward"),
        Pair("🎮 اسکرین بازی و پیروزی لابی", "image_game_victory")
    )

    val videoPresets = listOf(
        Pair("🎬 ویدیو کلیپ چالش و خنده روم", "video_challenge_fun"),
        Pair("🎵 کلیپ دابسمش و موزیک عسل آباد", "video_music_clip"),
        Pair("🎥 فیلم معرفی روم و بازی‌ها", "video_intro_game")
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ارسال عکس و فیلم در چت عسل آباد ✨",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AsalPurplePrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs (عکس / فیلم)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF3E8FF))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedTab == 0) AsalPurplePrimary else Color.Transparent)
                            .clickable { selectedTab = 0; selectedPreset = 0 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📷 ارسال عکس",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 0) Color.White else AsalPurpleDark
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedTab == 1) AsalPurplePrimary else Color.Transparent)
                            .clickable { selectedTab = 1; selectedPreset = 0 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎬 ارسال فیلم / کلیپ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 1) Color.White else AsalPurpleDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (selectedTab == 0) "انتخاب عکس دلخواه:" else "انتخاب ویدیو کلیپ:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = AsalPurpleDark,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                val currentPresets = if (selectedTab == 0) photoPresets else videoPresets
                currentPresets.forEachIndexed { idx, item ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedPreset == idx) Color(0xFFF3E8FF) else Color(0xFFFBF9FF),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (selectedPreset == idx) AsalPurplePrimary else Color(0xFFE9D5FF)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clickable { selectedPreset = idx }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.first,
                                fontSize = 12.sp,
                                fontWeight = if (selectedPreset == idx) FontWeight.Bold else FontWeight.Normal,
                                color = AsalPurpleDark
                            )
                            if (selectedPreset == idx) {
                                Text("✓", color = AsalPurplePrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (selectedTab == 0) {
                    val ctx = androidx.compose.ui.platform.LocalContext.current
                    val pickLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                        androidx.activity.result.contract.ActivityResultContracts.GetContent()
                    ) { picked: android.net.Uri? ->
                        if (picked != null) {
                            val path = com.example.ui.components.copyPickedImageToInternal(
                                ctx, picked, dirName = "chat_images", cleanOld = false
                            )
                            if (path != null) {
                                onSendImage(path, captionInput)
                            } else {
                                android.widget.Toast.makeText(ctx, "خواندن عکس ممکن نشد", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    Button(
                        onClick = { pickLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDE4FA)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🖼️ انتخاب عکس از گالری گوشی", color = AsalPurplePrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Caption
                OutlinedTextField(
                    value = captionInput,
                    onValueChange = { captionInput = it },
                    placeholder = { Text("کپشن / توضیحات عکس یا فیلم...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AsalPurplePrimary,
                        unfocusedBorderColor = Color(0xFFE9D5FF)
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("انصراف", color = Color.Gray, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val uri = currentPresets.getOrNull(selectedPreset)?.second ?: "media_item"
                            val cap = captionInput.ifBlank { currentPresets.getOrNull(selectedPreset)?.first ?: "" }
                            if (selectedTab == 0) {
                                onSendImage(uri, cap)
                            } else {
                                onSendVideo(uri, cap)
                            }
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1.4f)
                    ) {
                        Text(
                            text = if (selectedTab == 0) "ارسال عکس 📸" else "ارسال فیلم 🎬",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoPreviewDialog(
    uri: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1035)),
            elevation = CardDefaults.cardElevation(12.dp),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("نمایش عکس کامل 📸", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "بستن", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (uri.startsWith("/")) {
                    coil.compose.AsyncImage(
                        model = java.io.File(uri),
                        contentDescription = "عکس",
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF9F7AEA), Color(0xFF6B46C1), Color(0xFF322659))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("👑", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "پریا چش رنگی • عسل آباد چت آنلاین",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "با کیفیت و استایل یاسی بنفش 💜",
                            fontSize = 11.sp,
                            color = Color(0xFFE9D5FF)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("بستن", color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun VideoPlayerDialog(
    title: String,
    onDismiss: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var progressSeconds by remember { mutableIntStateOf(1) }

    LaunchedEffect(isPlaying) {
        while (isPlaying && progressSeconds < 28) {
            delay(1000)
            progressSeconds++
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF140D24)),
            elevation = CardDefaults.cardElevation(12.dp),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "بستن", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF553C9A), Color(0xFF231942), Color(0xFF0F0B18))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(AsalPurplePrimary)
                                .clickable { isPlaying = !isPlaying },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "پخش یا توقف",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            if (isPlaying) "در حال پخش کلیپ روم عسل آباد..." else "پخش متوقف شد",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "0:${String.format("%02d", progressSeconds)} / 0:28",
                            fontSize = 11.sp,
                            color = Color(0xFFD6BCFA)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("بستن پلیر", color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}
