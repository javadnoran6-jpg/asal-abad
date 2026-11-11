package com.example.data.repository

import com.example.data.model.ChatMessage
import com.example.data.model.ChatRoom
import com.example.data.model.GroupMarketItem
import com.example.data.model.LeaderboardUser
import com.example.data.model.PurchaseItem
import com.example.data.model.ShopPackage
import com.example.data.model.UserProfile
import com.example.data.model.WheelSector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AsalAbadRepository {

    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "user_me",
            name = "کاربر عسل‌آبادی",
            username = "@user_asal",
            level = 4,
            diamonds = 1250,
            janous = 1604,
            ageDays = 105,
            bio = "عاشق چت و بازی در عسل آباد 💜 خوشحال میشم دوست شیم!",
            isVip = true,
            vipRemainingText = "۵ روز و ۱۸ ساعت باقی مانده",
            specialGiftsCount = 2,
            pointsGiftCount = 14419,
            badgeSymbol = "👑",
            chatBubbleColorHex = "#7E22CE"
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    val creatorProfile = UserProfile(
        id = "paria_creator",
        name = "پریا چش رنگی",
        username = "@Paria_ChessRangi",
        level = 10,
        diamonds = 100111,
        janous = 9999,
        ageDays = 720,
        bio = "👑 سازنده عسل آباد | چت، دوستی، سرگرمی 💜 به شهر عسل آباد خوش آمدید!",
        isVip = true,
        vipRemainingText = "دائمی (مالک و توسعه‌دهنده)",
        specialGiftsCount = 999,
        pointsGiftCount = 99999,
        badgeSymbol = "👑",
        chatBubbleColorHex = "#D946EF"
    )

    private val _chatRooms = MutableStateFlow(
        listOf(
            ChatRoom(
                id = "room_dark_land",
                title = "DARK LAND 🕊️",
                subtitle = "به نام خداوند DARK LAND 🕊️ به ...",
                memberCount = 11252,
                onlineCount = 842,
                lastMessage = "برید تبلیغات شلوغ بشخ",
                lastMessageTime = "۰۰:۴۴",
                unreadCount = 3,
                isGroup = true,
                avatarEmoji = "🦅"
            ),
            ChatRoom(
                id = "room_paria",
                title = "پریا چش رنگی 👑 (سازنده)",
                subtitle = "آنلاین • پاسخگوی کاربران",
                memberCount = 1,
                onlineCount = 1,
                lastMessage = "سلام عزیزم! به عسل آباد خوش اومدی 💜",
                lastMessageTime = "۲۱:۴۵",
                unreadCount = 1,
                isGroup = false,
                isCreatorChat = true,
                avatarEmoji = "👑"
            ),
            ChatRoom(
                id = "room_friends_special",
                title = "دوستان خاص",
                subtitle = "گروه عمومی • ۲۴۶ عضو",
                memberCount = 246,
                onlineCount = 38,
                lastMessage = "کی میاد مار و پله بزنیم؟ 🎲",
                lastMessageTime = "۲۱:۴۲",
                unreadCount = 2,
                isGroup = true,
                avatarEmoji = "✨"
            ),
            ChatRoom(
                id = "room_asal_gap",
                title = "عسل آباد گپ 🐝",
                subtitle = "دورهمی شبانه عسل آبادی‌ها",
                memberCount = 178,
                onlineCount = 45,
                lastMessage = "گردونه امروز ۲۰۰ تا الماس داد بهم!",
                lastMessageTime = "۲۱:۳۰",
                unreadCount = 0,
                isGroup = true,
                avatarEmoji = "🐝"
            ),
            ChatRoom(
                id = "room_mahsa",
                title = "مهسا ❤️",
                subtitle = "در حال بازی در لابی عسل آباد 🎲",
                memberCount = 1,
                onlineCount = 1,
                lastMessage = "سلام عزیزم کجایی؟",
                lastMessageTime = "۲۱:۴۰",
                unreadCount = 1,
                isGroup = false,
                avatarEmoji = "🌸",
                contactStatus = com.example.data.model.UserStatus.IN_GAME
            ),
            ChatRoom(
                id = "room_pani",
                title = "پانی 🌸",
                subtitle = "آنلاین • رفیق صمیمی",
                memberCount = 1,
                onlineCount = 1,
                lastMessage = "سلام عشقم! گروه جدید ساختی منم ببر داخلش 💜",
                lastMessageTime = "۱۰:۱۵",
                unreadCount = 1,
                isGroup = false,
                avatarEmoji = "🦋",
                contactStatus = com.example.data.model.UserStatus.ONLINE
            ),
            ChatRoom(
                id = "room_tak",
                title = "TAK 惚",
                subtitle = "آفلاین • آخرین بازدید دیروز",
                memberCount = 1,
                onlineCount = 0,
                lastMessage = "شات بباز",
                lastMessageTime = "۰۹:۲۶",
                unreadCount = 0,
                isGroup = false,
                avatarEmoji = "⚡",
                contactStatus = com.example.data.model.UserStatus.OFFLINE
            )
        )
    )
    val chatRooms: StateFlow<List<ChatRoom>> = _chatRooms.asStateFlow()

    private val _messagesMap = MutableStateFlow<Map<String, List<ChatMessage>>>(
        mapOf(
            "room_dark_land" to listOf(
                ChatMessage(
                    id = "m1",
                    senderId = "user_deram",
                    senderName = "deram",
                    text = "مرسی گل",
                    timestamp = "۰۰:۴۲",
                    isMe = false,
                    badge = "کاربر فعال"
                ),
                ChatMessage(
                    id = "m2",
                    senderId = "user_fadat",
                    senderName = "فدات",
                    text = "فدات شم عزیز دلم",
                    timestamp = "۰۰:۴۲",
                    isMe = false
                ),
                ChatMessage(
                    id = "m3",
                    senderId = "user_malek",
                    senderName = "مالک",
                    text = "برید تبلیغات شلوغ بشخ",
                    timestamp = "۰۰:۴۴",
                    isMe = false,
                    badge = "مالک",
                    replyToText = "deram: مرسی گل"
                )
            ),
            "room_paria" to listOf(
                ChatMessage(
                    id = "p1",
                    senderId = "paria_creator",
                    senderName = "پریا چش رنگی 👑",
                    text = "سلام به عسل آباد خوش اومدی عزیز دلم! 💜🐝",
                    timestamp = "۲۱:۴۰",
                    isMe = false,
                    isCreator = true,
                    badge = "سازنده عسل آباد"
                ),
                ChatMessage(
                    id = "p2",
                    senderId = "paria_creator",
                    senderName = "پریا چش رنگی 👑",
                    text = "این برنامه با کلی عشق برای شما ساخته شده. می‌تونی چت کنی، بازی کنی و در گردونه شانس شرکت کنی!",
                    timestamp = "۲۱:۴۱",
                    isMe = false,
                    isCreator = true,
                    badge = "سازنده عسل آباد"
                )
            ),
            "room_tak" to listOf(
                ChatMessage(
                    id = "t1",
                    senderId = "tak_user",
                    senderName = "TAK 惚",
                    text = "ناز ناز و خودمی که",
                    timestamp = "۲۰:۳۰",
                    isMe = false,
                    stickerType = "cat_wink"
                ),
                ChatMessage(
                    id = "t2",
                    senderId = "user_me",
                    senderName = "من",
                    text = "قلبم",
                    timestamp = "۲۳:۱۱",
                    isMe = true
                ),
                ChatMessage(
                    id = "t3",
                    senderId = "tak_user",
                    senderName = "TAK 惚",
                    text = "جونم",
                    timestamp = "۰۱:۱۱",
                    isMe = false
                ),
                ChatMessage(
                    id = "t4",
                    senderId = "user_me",
                    senderName = "من",
                    text = "شات بباز",
                    timestamp = "۰۹:۲۶",
                    isMe = true
                )
            ),
            "room_friends_special" to listOf(
                ChatMessage(
                    id = "fs1",
                    senderId = "user_sahar",
                    senderName = "سحر",
                    text = "سلام بچه‌ها کیا بیدارن؟",
                    timestamp = "۲۱:۳۰",
                    isMe = false
                ),
                ChatMessage(
                    id = "fs2",
                    senderId = "user_kian",
                    senderName = "کیان",
                    text = "کی میاد مار و پله یا حدس کلمه بزنیم؟ 🎲",
                    timestamp = "۲۱:۴۲",
                    isMe = false,
                    badge = "همبازی"
                )
            ),
            "room_asal_gap" to listOf(
                ChatMessage(
                    id = "ag1",
                    senderId = "user_amir",
                    senderName = "امیر",
                    text = "عسل آبادی‌های گل درود 💜",
                    timestamp = "۲۱:۲۰",
                    isMe = false
                ),
                ChatMessage(
                    id = "ag2",
                    senderId = "user_leila",
                    senderName = "لیلا",
                    text = "گردونه امروز ۲۰۰ تا الماس داد بهم! شانس آوردم 💎",
                    timestamp = "۲۱:۳۰",
                    isMe = false
                )
            ),
            "room_mahsa" to listOf(
                ChatMessage(
                    id = "m_1",
                    senderId = "user_mahsa",
                    senderName = "مهسا ❤️",
                    text = "سلام عزیزم کجایی؟ دلم برات تنگ شده 🌸",
                    timestamp = "۲۱:۴۰",
                    isMe = false
                )
            ),
            "room_pani" to listOf(
                ChatMessage(
                    id = "pani_1",
                    senderId = "user_pani",
                    senderName = "پانی 🌸",
                    text = "سلام عزیز دلم! چطوری؟ شنیدم می‌خوای گروه جدید بسازی 💜",
                    timestamp = "۱۰:۱۴",
                    isMe = false,
                    badge = "رفیق صمیمی"
                ),
                ChatMessage(
                    id = "pani_2",
                    senderId = "user_pani",
                    senderName = "پانی 🌸",
                    text = "گروه ساختی من رو حتماً ادمین کن باهم مدیریت کنیم و بترکونیم 🦋✨",
                    timestamp = "۱۰:۱۵",
                    isMe = false,
                    badge = "رفیق صمیمی"
                )
            )
        )
    )
    val messagesMap: StateFlow<Map<String, List<ChatMessage>>> = _messagesMap.asStateFlow()

    val shopPackages = listOf(
        ShopPackage("pkg_1", 1400, 2800, 14000, "۱۴,۰۰۰ ت"),
        ShopPackage("pkg_2", 2800, 5600, 28000, "۲۸,۰۰۰ ت"),
        ShopPackage("pkg_3", 5600, 11200, 56000, "۵۶,۰۰۰ ت"),
        ShopPackage("pkg_4", 8400, 16800, 84000, "۸۴,۰۰۰ ت"),
        ShopPackage("pkg_5", 50000, 100000, 499000, "۴۹۹,۰۰۰ ت"),
        ShopPackage("pkg_6", 100000, 200000, 999000, "۹۹۹,۰۰۰ ت")
    )

    private val _purchaseList = MutableStateFlow(
        listOf(
            PurchaseItem(
                id = "p1",
                title = "اختصاصی",
                date = "۹ آبان ۱۴۰۵ ۱۷:۱۳",
                status = "تایید شده",
                token = "TOKEN-883921",
                priceToman = 50000L,
                diamondsGranted = 2000L,
                refId = "14050809171388",
                paymentMethod = "درگاه پرداخت شاپرک"
            ),
            PurchaseItem(
                id = "p2",
                title = "حساب کاربری اختصاصی ۱۴ روزه",
                date = "۳ آبان ۱۴۰۵ ۱۲:۰۵",
                status = "فعال",
                token = "TOKEN-120485",
                priceToman = 25000L,
                diamondsGranted = 1000L,
                refId = "6037997511223344",
                paymentMethod = "کارت‌به‌کارت"
            )
        )
    )
    val purchaseList: StateFlow<List<PurchaseItem>> = _purchaseList.asStateFlow()

    val groupMarketListings = listOf(
        GroupMarketItem("g1", "مفت تر از این نی", 54800, 1100000, "۱,۱۰۰,۰۰۰ تومان", "۵۴,۸۰۰"),
        GroupMarketItem("g2", "فروشی ۲۵۵ کا ، با پول ۹۴۵ تومن", 23825, 260000, "۲۶۰,۰۰۰ تومان", "۲۳,۸۲۵"),
        GroupMarketItem("g3", "فروشی ۲۵۴ کا ، با پول ۹۴۰ تومن", 23665, 257000, "۲۵۷,۰۰۰ تومان", "۲۳,۶۶۵"),
        GroupMarketItem("g4", "فروشی فوری مفت 290k 💎✨", 24017, 270000, "۲۷۰,۰۰۰ تومان", "۲۴,۰۱۷"),
        GroupMarketItem("g5", "مشهدسیتی‌تهران‌سیتی", 1207804, 35000000, "۳۵,۰۰۰,۰۰۰ تومان", "۱,۲۰۷,۸۰۴"),
        GroupMarketItem("g6", "گروه چت شبانه عسل آباد", 22146, 229000, "۲۲۹,۰۰۰ تومان", "۲۲,۱۴۶")
    )

    val wheelSectors = listOf(
        WheelSector(0, "۳۰۰ 💎 + ۱۵ 🟣", 300, 15, 0xFF9370DB),
        WheelSector(1, "۲۵۰ 💎 + ۱۶ 🟣", 250, 16, 0xFFA78BFA),
        WheelSector(2, "۲۰۰ 💎 + ۱۷ 🟣", 200, 17, 0xFF8B5CF6),
        WheelSector(3, "۱۵۰ 💎 + ۱۷ 🟣", 150, 17, 0xFF9333EA),
        WheelSector(4, "۱۰۰ 💎 + ۲۰ 🟣", 100, 20, 0xFF7E22CE),
        WheelSector(5, "۱۷۰ 💎 + ۱۴ 🟣", 170, 14, 0xFF9370DB),
        WheelSector(6, "۲۲۰ 💎 + ۱۳ 🟣", 220, 13, 0xFFA78BFA),
        WheelSector(7, "۲۷۰ 💎 + ۱۲ 🟣", 270, 12, 0xFF8B5CF6)
    )

    val leaderboardDaily = listOf(
        LeaderboardUser(1, "ALI_KURD", 1057, 0xFFFFD700),
        LeaderboardUser(2, "Roya ⚖️", 830, 0xFFC0C0C0),
        LeaderboardUser(3, "Boy_special", 640, 0xFFCD7F32),
        LeaderboardUser(4, "somaye ⚖️", 490, 0xFF8B5CF6),
        LeaderboardUser(5, "Nazanin 💖", 372, 0xFFEC4899),
        LeaderboardUser(6, "تلاشگر", 352, 0xFF3B82F6),
        LeaderboardUser(7, "یوسف بلوچ", 362, 0xFF10B981),
        LeaderboardUser(8, "shima", 310, 0xFFA855F7)
    )

    val lotteryYesterdayWinners = listOf(
        "رضا رمیس (۲۴,۳۷۳ امتیاز)",
        "سازنده ممنون (۲۴,۳۷۳ امتیاز)",
        "ابوالفضل عقاب 🦅 (۲۴,۳۷۳ امتیاز)"
    )

    fun sendMessage(roomId: String, text: String, stickerType: String? = null) {
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val newMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = _currentUser.value.id,
            senderName = _currentUser.value.name,
            text = text,
            timestamp = timeNow,
            isMe = true,
            badge = _currentUser.value.badgeSymbol,
            stickerType = stickerType
        )

        _messagesMap.update { currentMap ->
            val list = currentMap[roomId]?.toMutableList() ?: mutableListOf()
            list.add(newMessage)
            currentMap + (roomId to list)
        }

        // Update room's last message
        _chatRooms.update { rooms ->
            rooms.map { room ->
                if (room.id == roomId) {
                    room.copy(
                        lastMessage = if (stickerType != null) "[استیکر]" else text,
                        lastMessageTime = timeNow
                    )
                } else room
            }
        }
    }

    fun addReplyFromOpponent(roomId: String, replyText: String, senderName: String, isCreator: Boolean = false) {
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val reply = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = if (isCreator) "paria_creator" else "bot_${System.currentTimeMillis()}",
            senderName = senderName,
            text = replyText,
            timestamp = timeNow,
            isMe = false,
            isCreator = isCreator,
            badge = if (isCreator) "سازنده عسل آباد 👑" else "عضو فعال"
        )
        _messagesMap.update { currentMap ->
            val list = currentMap[roomId]?.toMutableList() ?: mutableListOf()
            list.add(reply)
            currentMap + (roomId to list)
        }
        _chatRooms.update { rooms ->
            rooms.map { room ->
                if (room.id == roomId) {
                    room.copy(
                        lastMessage = replyText,
                        lastMessageTime = timeNow
                    )
                } else room
            }
        }
    }

    fun createGroup(title: String, description: String, avatarEmoji: String, withPani: Boolean): ChatRoom {
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val newGroupId = "group_" + UUID.randomUUID().toString().take(8)
        val initialMembers = if (withPani) 2 else 1
        val newRoom = ChatRoom(
            id = newGroupId,
            title = title.ifBlank { "گروه با پانی 🌸" },
            subtitle = description.ifBlank { "گروه دوستانه و صمیمی عسل آباد" },
            memberCount = initialMembers,
            onlineCount = initialMembers,
            lastMessage = if (withPani) "پانی به گروه پیوست 🎉" else "گروه با موفقیت ساخته شد",
            lastMessageTime = timeNow,
            unreadCount = 0,
            isGroup = true,
            avatarEmoji = avatarEmoji.ifBlank { "🌸" }
        )

        val welcomeMessages = mutableListOf(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = _currentUser.value.id,
                senderName = _currentUser.value.name,
                text = "سلام به همه! به گروه جدید «${newRoom.title}» خوش اومدید 💜🐝",
                timestamp = timeNow,
                isMe = true,
                badge = "مالک گروه 👑"
            )
        )

        if (withPani) {
            welcomeMessages.add(
                ChatMessage(
                    id = UUID.randomUUID().toString(),
                    senderId = "user_pani",
                    senderName = "پانی 🌸",
                    text = "سلام سلام! منم عضو شدم 🦋 مرسی که منو به گروهت اضافه کردی رفیق، بترکونیم! 🎉💜",
                    timestamp = timeNow,
                    isMe = false,
                    badge = "ادمین گروه 🌸"
                )
            )
        }

        _messagesMap.update { currentMap ->
            currentMap + (newGroupId to welcomeMessages)
        }

        _chatRooms.update { currentRooms ->
            listOf(newRoom) + currentRooms
        }

        return newRoom
    }

    fun addDiamonds(amount: Long) {
        _currentUser.update { it.copy(diamonds = it.diamonds + amount) }
    }

    fun addJanous(amount: Long) {
        _currentUser.update { it.copy(janous = it.janous + amount) }
    }

    fun spendDiamonds(amount: Long): Boolean {
        if (_currentUser.value.diamonds >= amount) {
            _currentUser.update { it.copy(diamonds = it.diamonds - amount) }
            return true
        }
        return false
    }

    fun convertDiamondsToJanous(diamondsCount: Long): Boolean {
        if (diamondsCount >= 1000 && _currentUser.value.diamonds >= diamondsCount) {
            val janousGained = diamondsCount / 1000
            _currentUser.update {
                it.copy(
                    diamonds = it.diamonds - (janousGained * 1000),
                    janous = it.janous + janousGained
                )
            }
            return true
        }
        return false
    }

    private val _bankAccount = MutableStateFlow(
        com.example.data.model.BankAccountInfo(
            cardNumber = "6037-9975-8421-3965",
            cardHolderName = "پریا چش رنگی",
            bankName = "بانک ملی ایران",
            iban = "IR120170000000123456789012"
        )
    )
    val bankAccount: StateFlow<com.example.data.model.BankAccountInfo> = _bankAccount.asStateFlow()

    fun updateBankAccount(cardNumber: String, cardHolderName: String, bankName: String, iban: String) {
        _bankAccount.value = com.example.data.model.BankAccountInfo(
            cardNumber = cardNumber.ifBlank { "6037-9975-8421-3965" },
            cardHolderName = cardHolderName.ifBlank { "پریا چش رنگی" },
            bankName = bankName.ifBlank { "بانک ملی ایران" },
            iban = iban.ifBlank { "IR120170000000123456789012" }
        )
    }

    fun recordPurchase(packageName: String, diamonds: Long, priceToman: Long): String {
        return recordBankPurchase(packageName, diamonds, priceToman, "درگاه شاپرک").token
    }

    fun recordBankPurchase(
        packageName: String,
        diamonds: Long,
        priceToman: Long,
        paymentMethod: String,
        customRefId: String? = null
    ): PurchaseItem {
        val token = "TOKEN-" + (100000..999999).random()
        val refId = customRefId ?: ("RRN-" + (10000000..99999999).random())
        val timeNow = SimpleDateFormat("d MMMM yyyy - HH:mm", Locale("fa", "IR")).format(Date())
        val newItem = PurchaseItem(
            id = UUID.randomUUID().toString(),
            title = packageName,
            date = timeNow,
            status = "تایید و واریز شد",
            token = token,
            priceToman = priceToman,
            diamondsGranted = diamonds,
            refId = refId,
            paymentMethod = paymentMethod
        )
        _purchaseList.update { listOf(newItem) + it }
        addDiamonds(diamonds)
        return newItem
    }

    fun findPurchase(query: String): PurchaseItem? {
        val cleanQuery = query.trim().lowercase()
        return _purchaseList.value.find {
            it.token.lowercase().contains(cleanQuery) ||
            it.refId.lowercase().contains(cleanQuery) ||
            cleanQuery.contains(it.token.lowercase().replace("token-", ""))
        }
    }

    fun clearPurchaseHistory() {
        _purchaseList.value = emptyList()
    }

    fun donateGroupPoints(diamonds: Long): Pair<Boolean, Long> {
        if (_currentUser.value.diamonds < diamonds || diamonds <= 0) {
            return Pair(false, 0L)
        }
        val points = when {
            diamonds <= 10000 -> diamonds / 5
            diamonds <= 20000 -> 2000 + ((diamonds - 10000) / 50)
            else -> 2200 + ((diamonds - 20000) / 100)
        }
        spendDiamonds(diamonds)
        _currentUser.update { it.copy(pointsGiftCount = it.pointsGiftCount + points.toInt()) }
        return Pair(true, points)
    }

    fun sendMediaMessage(roomId: String, type: com.example.data.model.AttachmentType, mediaUri: String, caption: String) {
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val newMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = _currentUser.value.id,
            senderName = _currentUser.value.name,
            text = caption,
            timestamp = timeNow,
            isMe = true,
            badge = _currentUser.value.badgeSymbol,
            attachmentType = type,
            mediaUri = mediaUri,
            mediaCaption = caption.ifBlank { null }
        )

        _messagesMap.update { currentMap ->
            val list = currentMap[roomId]?.toMutableList() ?: mutableListOf()
            list.add(newMessage)
            currentMap + (roomId to list)
        }

        val preview = if (type == com.example.data.model.AttachmentType.IMAGE) "📷 [عکس]" else "🎬 [فیلم]"
        _chatRooms.update { rooms ->
            rooms.map { room ->
                if (room.id == roomId) {
                    room.copy(lastMessage = preview, lastMessageTime = timeNow)
                } else room
            }
        }
    }

    fun sendVoiceMessage(roomId: String, durationSeconds: Int) {
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val newMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = _currentUser.value.id,
            senderName = _currentUser.value.name,
            text = "پیام صوتی ($durationSeconds ثانیه)",
            timestamp = timeNow,
            isMe = true,
            badge = _currentUser.value.badgeSymbol,
            attachmentType = com.example.data.model.AttachmentType.VOICE,
            voiceDurationSeconds = durationSeconds
        )

        _messagesMap.update { currentMap ->
            val list = currentMap[roomId]?.toMutableList() ?: mutableListOf()
            list.add(newMessage)
            currentMap + (roomId to list)
        }

        _chatRooms.update { rooms ->
            rooms.map { room ->
                if (room.id == roomId) {
                    room.copy(lastMessage = "🎙️ [پیام صوتی]", lastMessageTime = timeNow)
                } else room
            }
        }
    }

    fun grantFreeDiamondsToOwner(amount: Long = 100000L) {
        _currentUser.update { it.copy(diamonds = it.diamonds + amount) }
    }

    fun updateFullProfile(
        name: String,
        username: String,
        bio: String,
        avatarEmoji: String,
        avatarImageUri: String?,
        status: com.example.data.model.UserStatus,
        tick: com.example.data.model.VerificationTick
    ) {
        _currentUser.update {
            it.copy(
                name = name.ifBlank { it.name },
                username = username.ifBlank { it.username },
                bio = bio.ifBlank { it.bio },
                avatarEmoji = avatarEmoji.ifBlank { it.avatarEmoji },
                avatarImageUri = if (avatarEmoji.isNotBlank() && avatarEmoji != it.avatarEmoji) null
                    else (avatarImageUri ?: it.avatarImageUri),
                status = status,
                verificationTick = tick,
                statusCustomText = "${status.label} • با مدیریت پریا چش رنگی 👑"
            )
        }
    }

    fun restoreProfileBackup(name: String, username: String, bio: String, emoji: String) {
        _currentUser.update {
            it.copy(
                name = name.ifBlank { it.name },
                username = username.ifBlank { it.username },
                bio = bio.ifBlank { it.bio },
                avatarEmoji = emoji.ifBlank { it.avatarEmoji }
            )
        }
    }

    /** عکس پیش‌فرض (خانم/آقا) — رایگان */
    fun setDefaultAvatar(marker: String) {
        _currentUser.update { it.copy(avatarImageUri = marker) }
    }

    /** عکس گالری — ۱۰ الماس کسر می‌شود. اگر الماس کافی نباشد false برمی‌گردد. */
    fun setGalleryAvatar(path: String, cost: Long): Boolean {
        if (_currentUser.value.diamonds < cost) return false
        _currentUser.update { it.copy(avatarImageUri = path, diamonds = it.diamonds - cost) }
        return true
    }

    fun updateProfile(bio: String, badge: String) {
        _currentUser.update { it.copy(bio = bio, badgeSymbol = badge) }
    }

    fun setPresenceStatus(status: com.example.data.model.UserStatus) {
        _currentUser.update {
            it.copy(
                status = status,
                statusCustomText = "${status.label} • با مدیریت پریا چش رنگی 👑"
            )
        }
    }

    fun activateVip() {
        _currentUser.update {
            it.copy(
                isVip = true,
                vipRemainingText = "۱۴ روز باقی مانده",
                badgeSymbol = "👑"
            )
        }
    }

    fun setAuthenticatedUser(uid: String, email: String?, displayName: String?) {
        _currentUser.update { current ->
            val cleanName = displayName?.takeIf { it.isNotBlank() }
                ?: email?.substringBefore("@")
                ?: current.name
            val cleanUsername = if (email != null) {
                "@" + email.substringBefore("@").replace("[^a-zA-Z0-9_]".toRegex(), "_")
            } else {
                current.username
            }
            current.copy(
                id = uid,
                email = email,
                name = cleanName,
                username = cleanUsername,
                status = com.example.data.model.UserStatus.ONLINE,
                statusCustomText = "آنلاین • کاربر تایید شده عسل آباد 🐝"
            )
        }
    }

    fun resetUser() {
        _currentUser.value = UserProfile(
            id = "user_guest",
            name = "کاربر مهمان",
            username = "@guest_asal",
            diamonds = 500,
            janous = 200,
            bio = "کاربر تازه وارد به دنیای پرشور عسل آباد 💜"
        )
    }
}

