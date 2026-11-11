package com.example.data.model

enum class UserStatus(val label: String, val colorHex: Long) {
    ONLINE("آنلاین", 0xFF10B981), // Emerald green
    IN_GAME("در حال بازی 🎲", 0xFF8B5CF6), // Purple
    BUSY("مشغول", 0xFFF59E0B), // Amber
    OFFLINE("آفلاین", 0xFF9CA3AF) // Gray
}

enum class VerificationTick(val label: String, val colorHex: Long, val symbol: String) {
    NONE("بدون تیک", 0xFF9CA3AF, ""),
    BLUE("تیک آبی رسمی", 0xFF3B82F6, "✓"),
    GREEN("تیک سبز اعتبار", 0xFF10B981, "✓"),
    GOLD("تیک طلایی مالک 👑", 0xFFFFB703, "👑"),
    PURPLE("تیک بنفش VIP", 0xFFA855F7, "★"),
    RED("تیک قرمز محبوبیت", 0xFFEF4444, "♥")
}

enum class AttachmentType {
    NONE, IMAGE, VIDEO, VOICE
}

enum class BeautyFilter(val label: String, val emoji: String, val glowColorHex: Long) {
    NATURAL("طبیعی روتوش ✨", "✨", 0xFFFDE047),
    GLAMOUR("میکاپ و آرایشی 💄", "💄", 0xFFF43F5E),
    LILAC_ROSE("گل یاسی عسل آباد 🌸", "🌸", 0xFFA855F7),
    HEART_GLOW("هاله عشق و قلب ❤️", "❤️", 0xFFEF4444),
    STARLIGHT("ستاره‌های درخشان ⭐", "⭐", 0xFF38BDF8)
}

data class UserProfile(
    val id: String = "user_me",
    val email: String? = null,
    val name: String = "کاربر عسل‌آبادی",
    val username: String = "@user_asal",
    val level: Int = 4,
    val diamonds: Long = 1000,
    val janous: Long = 1604,
    val ageDays: Int = 105,
    val bio: String = "عاشق چت و بازی در عسل آباد 💜",
    val isVip: Boolean = true,
    val vipRemainingText: String = "۵ روز و ۱۸ ساعت باقی مانده",
    val specialGiftsCount: Int = 2,
    val pointsGiftCount: Int = 14419,
    val badgeSymbol: String = "👑",
    val chatBubbleColorHex: String = "#7E22CE",
    val status: UserStatus = UserStatus.ONLINE,
    val statusCustomText: String = "آنلاین • با مدیریت پریا چش رنگی 👑",
    val avatarEmoji: String = "🌸",
    val avatarImageUri: String? = null,
    val isCreatorOrManager: Boolean = false,
    val managementRole: String = "مدیریت عسل آباد: پریا چش رنگی 👑",
    val verifiedBadge: Boolean = true,
    val verificationTick: VerificationTick = VerificationTick.GOLD,
    val reactions: Map<String, Int> = mapOf(
        "💎" to 8,
        "☕" to 104,
        "❤️" to 17,
        "🤤" to 16,
        "🍓" to 5
    )
)

data class CandidateUser(
    val id: String,
    val name: String,
    val username: String,
    val avatarEmoji: String,
    val status: UserStatus = UserStatus.ONLINE,
    val roleTitle: String,
    val isManager: Boolean = false,
    val isPreselected: Boolean = false
)

data class BankAccountInfo(
    val cardNumber: String = "6037-9975-8421-3965",
    val cardHolderName: String = "پریا چش رنگی",
    val bankName: String = "بانک ملی ایران",
    val iban: String = "IR120170000000123456789012"
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isMe: Boolean,
    val isCreator: Boolean = false,
    val badge: String = "",
    val stickerType: String? = null,
    val reaction: String? = null,
    val replyToText: String? = null,
    val attachmentType: AttachmentType = AttachmentType.NONE,
    val mediaUri: String? = null,
    val mediaCaption: String? = null,
    val voiceDurationSeconds: Int = 0,
    val isPlayingVoice: Boolean = false
)

data class ChatRoom(
    val id: String,
    val title: String,
    val subtitle: String,
    val memberCount: Int = 1,
    val onlineCount: Int = 1,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isGroup: Boolean = true,
    val isCreatorChat: Boolean = false,
    val avatarEmoji: String = "🐝",
    val contactStatus: UserStatus = UserStatus.ONLINE
)

data class ShopPackage(
    val id: String,
    val originalDiamonds: Long,
    val doubledDiamonds: Long,
    val priceToman: Long,
    val formattedPrice: String,
    val hasBonusWheel: Boolean = true
)

data class PurchaseItem(
    val id: String,
    val title: String,
    val date: String,
    val status: String,
    val token: String,
    val priceToman: Long = 0L,
    val diamondsGranted: Long = 0L,
    val refId: String = "",
    val paymentMethod: String = "درگاه بانکی شاپرک"
)

data class GroupMarketItem(
    val id: String,
    val groupName: String,
    val score: Long,
    val priceToman: Long,
    val formattedPrice: String,
    val formattedScore: String
)

data class WheelSector(
    val index: Int,
    val label: String,
    val diamonds: Int,
    val janous: Int,
    val colorHex: Long = 0xFF9F7AEA
) {
    val amount: Int get() = diamonds
    val isJanous: Boolean get() = false
}

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val score: Int,
    val avatarColorHex: Long = 0xFF9C27B0
)

enum class NotificationType {
    CHAT_MESSAGE,
    PURCHASE_SUCCESS,
    WALLET_UPDATE
}

data class InAppNotification(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val iconEmoji: String,
    val type: NotificationType,
    val targetRoomId: String? = null,
    val targetToken: String? = null,
    val timestamp: String = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
)

