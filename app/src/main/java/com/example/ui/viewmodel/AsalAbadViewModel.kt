package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatMessage
import com.example.data.model.ChatRoom
import com.example.data.model.GroupMarketItem
import com.example.data.model.PurchaseItem
import com.example.data.model.ShopPackage
import com.example.data.model.UserProfile
import com.example.data.model.WheelSector
import com.example.data.repository.AsalAbadRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val message: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

enum class MainTab {
    HOME,
    MESSENGER,
    SHOP,
    PROFILE,
    STATS
}

enum class ActiveGame {
    NONE,
    ROCK_PAPER_SCISSORS,
    NUMBER_GUESS,
    WORD_GUESS,
    SNAKES_LADDERS
}

enum class RpsChoice {
    ROCK, PAPER, SCISSORS
}

data class WordGuessState(
    val secretWord: String = "عسل",
    val hint: String = "شیرین‌ترین خوراکی کندو 🍯",
    val guessedLetters: Set<Char> = emptySet(),
    val attemptsLeft: Int = 6,
    val isFinished: Boolean = false,
    val isWon: Boolean = false
)

data class NumberGuessState(
    val targetNumber: Int = Random.nextInt(1, 101),
    val attemptsLeft: Int = 7,
    val guesses: List<Pair<Int, String>> = emptyList(),
    val isFinished: Boolean = false,
    val isWon: Boolean = false
)

class AsalAbadViewModel(
    private val repository: AsalAbadRepository = AsalAbadRepository()
) : ViewModel() {

    val currentUser: StateFlow<UserProfile> = repository.currentUser
    val creatorProfile: UserProfile = repository.creatorProfile
    val chatRooms: StateFlow<List<ChatRoom>> = repository.chatRooms
    val messagesMap: StateFlow<Map<String, List<ChatMessage>>> = repository.messagesMap
    val shopPackages: List<ShopPackage> = repository.shopPackages
    val purchaseList: StateFlow<List<PurchaseItem>> = repository.purchaseList
    val groupMarketListings: List<GroupMarketItem> = repository.groupMarketListings
    val wheelSectors: List<WheelSector> = repository.wheelSectors
    val leaderboardUsers = repository.leaderboardDaily
    val lotteryWinners = repository.lotteryYesterdayWinners

    // UI Navigation State
    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _activeChatRoomId = MutableStateFlow<String?>(null)
    val activeChatRoomId: StateFlow<String?> = _activeChatRoomId.asStateFlow()

    private val _activeGame = MutableStateFlow(ActiveGame.NONE)
    val activeGame: StateFlow<ActiveGame> = _activeGame.asStateFlow()

    // Dialogs
    val showWheelDialog = MutableStateFlow(false)
    val showLotteryDialog = MutableStateFlow(false)
    val showGroupMarketDialog = MutableStateFlow(false)
    val showMergeGroupsDialog = MutableStateFlow(false)
    val showJanousConvertDialog = MutableStateFlow(false)
    val showPurchaseCheckDialog = MutableStateFlow(false)
    val showPurchaseListDialog = MutableStateFlow(false)
    val showVipInfoDialog = MutableStateFlow(false)
    val showShopPackagesDialog = MutableStateFlow(false)
    val showBioEditDialog = MutableStateFlow(false)
    val showTariffDialog = MutableStateFlow(false)
    val showAdminPanelDialog = MutableStateFlow(false)

    // Call and Media States
    val showBeautyVideoCallDialog = MutableStateFlow(false)
    val activeVideoCallRoom = MutableStateFlow<ChatRoom?>(null)
    val selectedBeautyFilter = MutableStateFlow(com.example.data.model.BeautyFilter.GLAMOUR)

    val showGroupVoiceCallDialog = MutableStateFlow(false)
    val activeVoiceCallRoom = MutableStateFlow<ChatRoom?>(null)

    // Lucky Wheel 3-hour countdown
    val isFreeSpinAvailable = MutableStateFlow(true)
    val wheelCountdownSeconds = MutableStateFlow(3 * 3600 - 15 * 60) // 2h 45m remaining

    // Bank & Payment Gateway States
    val bankAccount = repository.bankAccount
    val selectedPackage = MutableStateFlow<ShopPackage?>(null)
    val showPaymentMethodDialog = MutableStateFlow(false)
    val showShaparakGatewayDialog = MutableStateFlow(false)
    val showCardToCardDialog = MutableStateFlow(false)
    val showEditBankCardDialog = MutableStateFlow(false)
    val paymentSuccessReceipt = MutableStateFlow<PurchaseItem?>(null)
    val showCreateGroupDialog = MutableStateFlow(false)
    val activeInAppNotification = MutableStateFlow<com.example.data.model.InAppNotification?>(null)
    private var appContext: android.content.Context? = null

    fun setApplicationContext(context: android.content.Context) {
        this.appContext = context.applicationContext
        com.example.util.AccountAge.days(context) // ثبت روز اول (سن = ۰)
        com.example.util.NotificationHelper.initNotificationChannel(context)
    }

    fun postNotification(
        title: String,
        message: String,
        iconEmoji: String,
        type: com.example.data.model.NotificationType,
        targetRoomId: String? = null,
        targetToken: String? = null
    ) {
        val notification = com.example.data.model.InAppNotification(
            title = title,
            message = message,
            iconEmoji = iconEmoji,
            type = type,
            targetRoomId = targetRoomId,
            targetToken = targetToken
        )
        activeInAppNotification.value = notification

        // System notification
        appContext?.let { ctx ->
            when (type) {
                com.example.data.model.NotificationType.CHAT_MESSAGE -> {
                    com.example.util.NotificationHelper.showChatNotification(
                        context = ctx,
                        senderName = title,
                        messageText = message,
                        roomId = targetRoomId ?: "room_dark_land"
                    )
                }
                com.example.data.model.NotificationType.PURCHASE_SUCCESS -> {
                    com.example.util.NotificationHelper.showPurchaseNotification(
                        context = ctx,
                        packageTitle = title,
                        priceToman = 0L,
                        diamondsGranted = 0L,
                        token = targetToken ?: ""
                    )
                }
                com.example.data.model.NotificationType.WALLET_UPDATE -> {
                    // Handled in-app
                }
            }
        }

        viewModelScope.launch {
            delay(4500)
            if (activeInAppNotification.value?.id == notification.id) {
                activeInAppNotification.value = null
            }
        }
    }

    fun dismissNotification() {
        activeInAppNotification.value = null
    }

    fun handleNotificationClick(notification: com.example.data.model.InAppNotification) {
        activeInAppNotification.value = null
        when (notification.type) {
            com.example.data.model.NotificationType.CHAT_MESSAGE -> {
                if (notification.targetRoomId != null) {
                    _currentTab.value = MainTab.MESSENGER
                    _activeChatRoomId.value = notification.targetRoomId
                }
            }
            com.example.data.model.NotificationType.PURCHASE_SUCCESS -> {
                _currentTab.value = MainTab.SHOP
                showPurchaseListDialog.value = true
            }
            com.example.data.model.NotificationType.WALLET_UPDATE -> {
                _currentTab.value = MainTab.HOME
            }
        }
    }

    // Wheel Spin State
    private val _wheelRotation = MutableStateFlow(0f)
    val wheelRotation: StateFlow<Float> = _wheelRotation.asStateFlow()

    private val _isSpinning = MutableStateFlow(false)
    val isSpinning: StateFlow<Boolean> = _isSpinning.asStateFlow()

    private val _wheelRewardMessage = MutableStateFlow<String?>(null)
    val wheelRewardMessage: StateFlow<String?> = _wheelRewardMessage.asStateFlow()

    // Rock Paper Scissors Game State
    private val _rpsPlayerChoice = MutableStateFlow<RpsChoice?>(null)
    val rpsPlayerChoice: StateFlow<RpsChoice?> = _rpsPlayerChoice.asStateFlow()

    private val _rpsBotChoice = MutableStateFlow<RpsChoice?>(null)
    val rpsBotChoice: StateFlow<RpsChoice?> = _rpsBotChoice.asStateFlow()

    private val _rpsResultText = MutableStateFlow<String?>("انتخاب خود را لمس کنید")
    val rpsResultText: StateFlow<String?> = _rpsResultText.asStateFlow()

    private val _rpsPlayerScore = MutableStateFlow(0)
    val rpsPlayerScore: StateFlow<Int> = _rpsPlayerScore.asStateFlow()

    private val _rpsBotScore = MutableStateFlow(0)
    val rpsBotScore: StateFlow<Int> = _rpsBotScore.asStateFlow()

    // Number Guess Game State
    private val _numberGuessState = MutableStateFlow(NumberGuessState())
    val numberGuessState: StateFlow<NumberGuessState> = _numberGuessState.asStateFlow()

    // Word Guess Game State
    private val wordsPool = listOf(
        Pair("عسل", "شیرین‌ترین خوراکی کندوی زنبور 🍯"),
        Pair("پریا", "نام زیبای سازنده و مدیر عسل آباد 👑"),
        Pair("الماس", "گوهر درخشان و پول درون برنامه عسل آباد 💎"),
        Pair("دوستی", "ارمغان باارزش چت کردن با همدیگه 💜"),
        Pair("کندو", "خانه زنبورهای پرتلاش عسل آباد 🐝"),
        Pair("ستاره", "در آسمان شب عسل آباد می‌درخشد ✨")
    )
    private val _wordGuessState = MutableStateFlow(
        WordGuessState(secretWord = wordsPool[0].first, hint = wordsPool[0].second)
    )
    val wordGuessState: StateFlow<WordGuessState> = _wordGuessState.asStateFlow()

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
        _activeChatRoomId.value = null
        _activeGame.value = ActiveGame.NONE
    }

    fun openChatRoom(roomId: String) {
        _activeChatRoomId.value = roomId
    }

    fun closeChatRoom() {
        _activeChatRoomId.value = null
    }

    fun openGame(game: ActiveGame) {
        _activeGame.value = game
        if (game == ActiveGame.NUMBER_GUESS) {
            resetNumberGuessGame()
        } else if (game == ActiveGame.WORD_GUESS) {
            resetWordGuessGame()
        }
    }

    fun rewardDiamonds(amount: Long) {
        repository.addDiamonds(amount)
    }

    fun closeGame() {
        _activeGame.value = ActiveGame.NONE
    }

    // Chat Actions
    fun sendChatMessage(roomId: String, text: String, stickerType: String? = null) {
        if (text.isBlank() && stickerType == null) return
        repository.sendMessage(roomId, text, stickerType)

        // Realistic Simulated Reply
        viewModelScope.launch {
            delay(1200)
            val (replyText, senderName, isCreator, emoji) = when (roomId) {
                "room_paria" -> {
                    val replies = listOf(
                        "عزیزم پیامت رسید! خوشحالم از حضورت در عسل آباد 💜",
                        "امروز گردونه شانس رو چرخوندی؟ جوایز خوبی داره!",
                        "عسل آباد با حضور شما کاربران گل زنده است 👑🐝",
                        "هر نظر یا پیشنهادی برای بهتر شدن عسل آباد داری بگو!"
                    )
                    listOf(replies.random(), "پریا چش رنگی 👑", "true", "👑")
                }
                "room_dark_land" -> {
                    val replies = listOf(
                        "به به پرچمدار اومد 🔥",
                        "سلام به همه اعضای خفن دارک لند 🕊️",
                        "کی الماس میخواد؟",
                        "بریم مسابقه سنگ کاغذ قیچی!"
                    )
                    val senders = listOf("شاهین", "آریا", "نگین", "رضا")
                    listOf(replies.random(), senders.random(), "false", "🔥")
                }
                "room_friends_special" -> {
                    val replies = listOf(
                        "سلام دوستان گل 🌸",
                        "کی پایه بازی حدس عدد یا کلمه هست؟",
                        "من امروز رتبه ۵ جدول شدم!"
                    )
                    val senders = listOf("سحر", "کیان", "مونا")
                    listOf(replies.random(), senders.random(), "false", "✨")
                }
                "room_asal_gap" -> {
                    val replies = listOf(
                        "عسل آبادی‌های گل درود 💜",
                        "امروز قرعه کشی روزانه الماس ثبت نام کردید؟",
                        "چت روم شلوغه کیف میده!"
                    )
                    val senders = listOf("امیر", "لیلا", "امید")
                    listOf(replies.random(), senders.random(), "false", "🐝")
                }
                "room_mahsa" -> {
                    val replies = listOf(
                        "جان دلم عزیزم ❤️",
                        "خوبی؟ روزت چطور بود؟",
                        "همیشه به یادتم قشنگم 🌸"
                    )
                    listOf(replies.random(), "مهسا ❤️", "false", "❤️")
                }
                "room_tak" -> {
                    val replies = listOf(
                        "عشقی به مولا ⚡",
                        "ناز شستت داداش",
                        "بریم دست بعدی!"
                    )
                    listOf(replies.random(), "TAK 惚", "false", "⚡")
                }
                "room_pani" -> {
                    val replies = listOf(
                        "عزیز دلمی 💜 مرسی که هوامو داری!",
                        "پایتم حسابی! هر بازی یا گپی بزنی من هستم 🦋",
                        "عسل آباد با تو خیلی قشنگ‌تره 🌸✨",
                        "بریم بقیه بچه‌ها رو هم بیاریم تو گروه چت کنیم!"
                    )
                    listOf(replies.random(), "پانی 🌸", "false", "🌸")
                }
                else -> {
                    val replies = listOf(
                        "ایول گروه چقدر خوبه! 🎉",
                        "من اینجام رفقا 🦋",
                        "سلام به همگی، گروهتون مبارک باشه 💜",
                        "بریم گردونه شانس بزنیم یا سنگ کاغذ قیچی!"
                    )
                    listOf(replies.random(), "پانی 🌸", "false", "🌸")
                }
            }

            repository.addReplyFromOpponent(
                roomId,
                replyText,
                senderName,
                isCreator = isCreator == "true"
            )

            postNotification(
                title = senderName,
                message = replyText,
                iconEmoji = emoji,
                type = com.example.data.model.NotificationType.CHAT_MESSAGE,
                targetRoomId = roomId
            )
        }
    }

    fun sendMediaMessage(roomId: String, type: com.example.data.model.AttachmentType, mediaUri: String, caption: String) {
        repository.sendMediaMessage(roomId, type, mediaUri, caption)

        viewModelScope.launch {
            delay(1500)
            val replyText = if (type == com.example.data.model.AttachmentType.IMAGE) {
                listOf(
                    "وااای چه عکس خوشگلی! 😍 عسل آباد به این عکس‌های قشنگ نیاز داره 🌸",
                    "عالیه عزیز دلم! چقدر با کیفیته 📸✨ لایک داری!",
                    "به به! خیلی زیبا و باحاله 💜"
                ).random()
            } else {
                listOf(
                    "عجب ویدیوی باحالی بود! مرسی که فرستادی 🎬✨",
                    "کیفیت فیلم عالی بود، دمت گرم رفیق 🕊️",
                    "خیلی خفن بود 🔥 دوباره بفرست!"
                ).random()
            }
            val sender = if (roomId == "room_paria") "پریا چش رنگی 👑" else "پانی 🌸"
            repository.addReplyFromOpponent(roomId, replyText, sender, isCreator = roomId == "room_paria")
            postNotification(
                title = sender,
                message = replyText,
                iconEmoji = "📷",
                type = com.example.data.model.NotificationType.CHAT_MESSAGE,
                targetRoomId = roomId
            )
        }
    }

    fun sendVoiceMessage(roomId: String, durationSeconds: Int) {
        repository.sendVoiceMessage(roomId, durationSeconds)

        viewModelScope.launch {
            delay(1500)
            val replies = listOf(
                "صدات خیلی دلنشینه عزیز دلم 🎙️💜 مرسی از ویس قشنگت!",
                "ویس رو گوش دادم، عشقی به مولا ⚡",
                "ای جان چه صدای نازی داری! خوشحالم ویس دادی 🌸",
                "شنیدم صداتو رفیق، پایتم حسابی 🦋"
            )
            val sender = if (roomId == "room_paria") "پریا چش رنگی 👑" else "پانی 🌸"
            val text = replies.random()
            repository.addReplyFromOpponent(roomId, text, sender, isCreator = roomId == "room_paria")
            postNotification(
                title = sender,
                message = text,
                iconEmoji = "🎙️",
                type = com.example.data.model.NotificationType.CHAT_MESSAGE,
                targetRoomId = roomId
            )
        }
    }

    fun grantOwnerFreeDiamonds() {
        repository.grantFreeDiamondsToOwner(100000L)
        postNotification(
            title = "الماس نامحدود مالک 👑",
            message = "۱۰۰,۰۰۰ الماس رایگان به حساب مالک (پریا چش رنگی) افزوده شد!",
            iconEmoji = "💎",
            type = com.example.data.model.NotificationType.WALLET_UPDATE
        )
    }

    fun updateProfileInfo(
        name: String,
        username: String,
        bio: String,
        avatarEmoji: String,
        avatarImageUri: String?,
        status: com.example.data.model.UserStatus,
        tick: com.example.data.model.VerificationTick
    ) {
        repository.updateFullProfile(name, username, bio, avatarEmoji, avatarImageUri, status, tick)
        showBioEditDialog.value = false
    }

    fun setPresenceStatus(status: com.example.data.model.UserStatus) {
        repository.setPresenceStatus(status)
        postNotification(
            title = "وضعیت آنلاین تغییر کرد",
            message = "وضعیت شما به «${status.label}» تغییر یافت.",
            iconEmoji = when(status) {
                com.example.data.model.UserStatus.ONLINE -> "🟢"
                com.example.data.model.UserStatus.IN_GAME -> "🟣"
                com.example.data.model.UserStatus.BUSY -> "🟡"
                com.example.data.model.UserStatus.OFFLINE -> "⚫"
            },
            type = com.example.data.model.NotificationType.WALLET_UPDATE
        )
    }

    fun startVideoCall(room: ChatRoom) {
        activeVideoCallRoom.value = room
        showBeautyVideoCallDialog.value = true
    }

    fun startVoiceCall(room: ChatRoom) {
        activeVoiceCallRoom.value = room
        showGroupVoiceCallDialog.value = true
    }

    // Wheel Spin
    fun spinWheel() {
        if (_isSpinning.value) return
        _isSpinning.value = true
        _wheelRewardMessage.value = null

        viewModelScope.launch {
            val fullRotations = (5..8).random() * 360f
            val sectorIndex = (0 until wheelSectors.size).random()
            val sectorDegrees = 360f / wheelSectors.size
            val targetAngle = fullRotations + (sectorIndex * sectorDegrees) + (sectorDegrees / 2)

            _wheelRotation.value += targetAngle

            // Wait for smooth spin duration
            delay(3500)
            _isSpinning.value = false

            val winningSector = wheelSectors[sectorIndex]
            repository.addDiamonds(winningSector.diamonds.toLong())
            repository.addJanous(winningSector.janous.toLong())
            _wheelRewardMessage.value = "تبریک! برنده ${winningSector.diamonds} الماس 💎 و ${winningSector.janous} جانوس 🟣 شدی!"
        }
    }

    // Rock Paper Scissors Game
    fun playRps(choice: RpsChoice) {
        _rpsPlayerChoice.value = choice
        val botChoice = RpsChoice.entries.random()
        _rpsBotChoice.value = botChoice

        if (choice == botChoice) {
            _rpsResultText.value = "مساوی شد! هردو یک انتخاب داشتید 🤝"
        } else if (
            (choice == RpsChoice.ROCK && botChoice == RpsChoice.SCISSORS) ||
            (choice == RpsChoice.PAPER && botChoice == RpsChoice.ROCK) ||
            (choice == RpsChoice.SCISSORS && botChoice == RpsChoice.PAPER)
        ) {
            _rpsPlayerScore.value += 1
            repository.addDiamonds(25)
            _rpsResultText.value = "آفرین! شما برنده شدید! (+۲۵ الماس 💎)"
        } else {
            _rpsBotScore.value += 1
            _rpsResultText.value = "حریف برنده شد! دوباره تلاش کن 💪"
        }
    }

    fun resetRps() {
        _rpsPlayerChoice.value = null
        _rpsBotChoice.value = null
        _rpsResultText.value = "انتخاب خود را لمس کنید"
        _rpsPlayerScore.value = 0
        _rpsBotScore.value = 0
    }

    // Number Guess Game
    fun makeNumberGuess(guess: Int) {
        val state = _numberGuessState.value
        if (state.isFinished) return

        val target = state.targetNumber
        val newAttempts = state.attemptsLeft - 1

        val feedback = when {
            guess == target -> "تبریک! دقیقاً درست حدس زدی 🎉 (+۱۰۰ الماس)"
            guess < target -> "عدد مخفی بزرگتر از $guess است ⬆️"
            else -> "عدد مخفی کوچکتر از $guess است ⬇️"
        }

        val won = guess == target
        val finished = won || newAttempts <= 0

        if (won) {
            repository.addDiamonds(100)
        }

        _numberGuessState.value = state.copy(
            attemptsLeft = newAttempts,
            guesses = state.guesses + Pair(guess, feedback),
            isFinished = finished,
            isWon = won
        )
    }

    fun resetNumberGuessGame() {
        _numberGuessState.value = NumberGuessState(targetNumber = Random.nextInt(1, 101))
    }

    // Word Guess Game
    fun guessWordLetter(letter: Char) {
        val state = _wordGuessState.value
        if (state.isFinished || state.guessedLetters.contains(letter)) return

        val newGuessed = state.guessedLetters + letter
        val isCorrect = state.secretWord.contains(letter)
        val newAttempts = if (isCorrect) state.attemptsLeft else state.attemptsLeft - 1

        val allGuessed = state.secretWord.all { newGuessed.contains(it) }
        val finished = allGuessed || newAttempts <= 0

        if (allGuessed) {
            repository.addDiamonds(150)
        }

        _wordGuessState.value = state.copy(
            guessedLetters = newGuessed,
            attemptsLeft = newAttempts,
            isFinished = finished,
            isWon = allGuessed
        )
    }

    fun resetWordGuessGame() {
        val nextWord = wordsPool.random()
        _wordGuessState.value = WordGuessState(
            secretWord = nextWord.first,
            hint = nextWord.second
        )
    }

    // Shop & Bank Payment Operations
    fun startPaymentFlow(pkg: ShopPackage) {
        selectedPackage.value = pkg
        showPaymentMethodDialog.value = true
    }

    fun startVipPurchase(planName: String, priceToman: Long) {
        val formatted = java.text.NumberFormat.getInstance(java.util.Locale.US).format(priceToman) + " تومان"
        startPaymentFlow(
            ShopPackage(
                id = "vip_plan_$planName",
                originalDiamonds = 0L,
                doubledDiamonds = 0L,
                priceToman = priceToman,
                formattedPrice = formatted,
                hasBonusWheel = false
            )
        )
    }

    fun choosePaymentMethod(isOnlineGateway: Boolean) {
        showPaymentMethodDialog.value = false
        if (isOnlineGateway) {
            showShaparakGatewayDialog.value = true
        } else {
            showCardToCardDialog.value = true
        }
    }

    fun completeShaparakPayment(pkg: ShopPackage, cardNumber: String): PurchaseItem {
        val receipt = repository.recordBankPurchase(
            packageName = if (pkg.id.startsWith("vip_plan_")) "حساب ${pkg.id.removePrefix("vip_plan_")}" else "بسته ${pkg.doubledDiamonds} الماس 2X",
            diamonds = pkg.doubledDiamonds,
            priceToman = pkg.priceToman,
            paymentMethod = "درگاه شاپرک (کارت ...${cardNumber.takeLast(4)})"
        )
        showShaparakGatewayDialog.value = false
        paymentSuccessReceipt.value = receipt
        if (pkg.id.startsWith("vip_plan_")) repository.activatePlan(pkg.id.removePrefix("vip_plan_"))

        postNotification(
            title = "خرید موفق ${pkg.doubledDiamonds} الماس 💎",
            message = "پرداخت آنلاین شاپرک تایید شد. ${pkg.doubledDiamonds} الماس به حسابتان افزوده شد (توکن: ${receipt.token})",
            iconEmoji = "💳",
            type = com.example.data.model.NotificationType.PURCHASE_SUCCESS,
            targetToken = receipt.token
        )
        return receipt
    }

    fun completeCardToCardPayment(pkg: ShopPackage, trackingCode: String): PurchaseItem {
        val receipt = repository.recordBankPurchase(
            packageName = if (pkg.id.startsWith("vip_plan_")) "حساب ${pkg.id.removePrefix("vip_plan_")} (کارت به کارت)" else "بسته ${pkg.doubledDiamonds} الماس 2X (کارت به کارت)",
            diamonds = pkg.doubledDiamonds,
            priceToman = pkg.priceToman,
            paymentMethod = "کارت به کارت به حساب پریا چش رنگی",
            customRefId = trackingCode.ifBlank { null }
        )
        showCardToCardDialog.value = false
        paymentSuccessReceipt.value = receipt
        if (pkg.id.startsWith("vip_plan_")) repository.activatePlan(pkg.id.removePrefix("vip_plan_"))

        postNotification(
            title = "خرید موفق کارت‌به‌کارت 💳",
            message = "رسید شما ثبت گردید. ${pkg.doubledDiamonds} الماس به کیفتان اضافه شد (توکن: ${receipt.token})",
            iconEmoji = "✅",
            type = com.example.data.model.NotificationType.PURCHASE_SUCCESS,
            targetToken = receipt.token
        )
        return receipt
    }

    fun updateDestinationBankCard(cardNumber: String, cardHolderName: String, bankName: String, iban: String) {
        repository.updateBankAccount(cardNumber, cardHolderName, bankName, iban)
        showEditBankCardDialog.value = false
    }

    fun searchPurchase(tokenOrRef: String): PurchaseItem? {
        return repository.findPurchase(tokenOrRef)
    }

    fun clearPurchases() {
        repository.clearPurchaseHistory()
    }

    fun donatePoints(diamonds: Long): Pair<Boolean, Long> {
        return repository.donateGroupPoints(diamonds)
    }

    fun buyShopPackage(pkg: ShopPackage): String {
        return repository.recordPurchase(
            "بسته ${pkg.doubledDiamonds} الماس 2X",
            pkg.doubledDiamonds,
            pkg.priceToman
        )
    }

    fun convertDiamondsToJanous(diamonds: Long): Boolean {
        val success = repository.convertDiamondsToJanous(diamonds)
        if (success) {
            postNotification(
                title = "تبدیل موفق به جانوس 🟣",
                message = "${diamonds / 1000} جانوس به موجودی شما افزوده شد.",
                iconEmoji = "🟣",
                type = com.example.data.model.NotificationType.WALLET_UPDATE
            )
        }
        return success
    }

    fun joinDailyLottery(): Boolean {
        val success = repository.spendDiamonds(500)
        return success
    }

    fun buyVipAccount(): Boolean {
        val success = repository.spendDiamonds(2000)
        if (success) {
            repository.activateVip()
        }
        return success
    }

    fun createNewGroup(title: String, description: String, avatarEmoji: String, withPani: Boolean): String {
        val newRoom = repository.createGroup(title, description, avatarEmoji, withPani)
        showCreateGroupDialog.value = false
        _currentTab.value = MainTab.MESSENGER
        _activeChatRoomId.value = newRoom.id

        if (withPani) {
            postNotification(
                title = "پانی 🌸 (ادمین گروه)",
                message = "سلام سلام! منم به گروه «${newRoom.title}» پیوستم 🦋🎉",
                iconEmoji = "🌸",
                type = com.example.data.model.NotificationType.CHAT_MESSAGE,
                targetRoomId = newRoom.id
            )
        }
        return newRoom.id
    }

    fun setDefaultAvatar(female: Boolean) {
        repository.setDefaultAvatar(if (female) "default:female" else "default:male")
    }

    fun setGalleryAvatar(path: String): Boolean = repository.setGalleryAvatar(path, 10L)

    fun restoreProfileBackup(name: String, username: String, bio: String, emoji: String) {
        repository.restoreProfileBackup(name, username, bio, emoji)
    }

    fun deleteAccount(context: android.content.Context) {
        try { java.io.File(context.filesDir, "avatars").deleteRecursively() } catch (_: Throwable) {}
        com.example.util.AccountAge.reset(context)
        signOut()
    }

    fun updateBio(newBio: String, newBadge: String) {
        repository.updateProfile(newBio, newBadge)
    }

    // --- Firebase Auth & Session Methods ---
    private val firebaseAuth: FirebaseAuth? = try {
        FirebaseAuth.getInstance()
    } catch (_: Throwable) {
        null
    }

    val authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val isUserLoggedIn = MutableStateFlow(false)

    init {
        try {
            val user = firebaseAuth?.currentUser
            if (user != null) {
                isUserLoggedIn.value = true
                repository.setAuthenticatedUser(user.uid, user.email, user.displayName)
            }
        } catch (_: Throwable) {}
    }

    fun signInWithEmail(email: String, pass: String) {
        val cleanEmail = email.trim()
        if (cleanEmail.isEmpty() || pass.isEmpty()) {
            authUiState.value = AuthUiState.Error("لطفاً ایمیل و رمز عبور را به صورت کامل وارد کنید")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            authUiState.value = AuthUiState.Error("فرمت آدرس ایمیل وارد شده نامعتبر است")
            return
        }

        authUiState.value = AuthUiState.Loading

        val auth = firebaseAuth
        if (auth == null) {
            val displayName = cleanEmail.substringBefore("@")
            repository.setAuthenticatedUser("local_" + java.util.UUID.randomUUID().toString().take(8), cleanEmail, displayName)
            isUserLoggedIn.value = true
            authUiState.value = AuthUiState.Success("خوش آمدید! ورود موفق به عسل آباد 🍯")
            return
        }

        auth.signInWithEmailAndPassword(cleanEmail, pass)
            .addOnSuccessListener { result ->
                val user = result.user
                val displayName = user?.displayName ?: cleanEmail.substringBefore("@")
                repository.setAuthenticatedUser(user?.uid ?: "user_me", user?.email, displayName)
                isUserLoggedIn.value = true
                authUiState.value = AuthUiState.Success("ورود موفقیت‌آمیز به عسل آباد 🍯")
            }
            .addOnFailureListener { error ->
                val errorMsg = error.message ?: ""
                val friendly = when {
                    errorMsg.contains("no user record", ignoreCase = true) ||
                    errorMsg.contains("user-not-found", ignoreCase = true) ->
                        "حسابی با این ایمیل یافت نشد. لطفاً در بخش ثبت‌نام حساب بسازید."
                    errorMsg.contains("wrong-password", ignoreCase = true) ||
                    errorMsg.contains("invalid-credential", ignoreCase = true) ->
                        "رمز عبور وارد شده اشتباه است."
                    errorMsg.contains("network", ignoreCase = true) ->
                        "خطای شبکه اینترنت! لطفاً ارتباط را بررسی کنید."
                    else -> error.localizedMessage ?: "خطایی در ورود رخ داد. لطفاً مجدداً امتحان کنید."
                }
                authUiState.value = AuthUiState.Error(friendly)
            }
    }

    fun signUpWithEmail(email: String, pass: String, confirmPass: String, displayName: String) {
        val cleanEmail = email.trim()
        val cleanName = displayName.trim()
        if (cleanEmail.isEmpty() || pass.isEmpty()) {
            authUiState.value = AuthUiState.Error("لطفاً فیلدهای ایمیل و رمز عبور را تکمیل کنید")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            authUiState.value = AuthUiState.Error("فرمت ایمیل نامعتبر است")
            return
        }
        if (pass.length < 6) {
            authUiState.value = AuthUiState.Error("رمز عبور باید حداقل شامل ۶ کاراکتر باشد")
            return
        }
        if (pass != confirmPass) {
            authUiState.value = AuthUiState.Error("تکرار رمز عبور با رمز وارد شده مطابقت ندارد")
            return
        }

        authUiState.value = AuthUiState.Loading

        val auth = firebaseAuth
        if (auth == null) {
            val name = if (cleanName.isNotBlank()) cleanName else cleanEmail.substringBefore("@")
            repository.setAuthenticatedUser("local_" + java.util.UUID.randomUUID().toString().take(8), cleanEmail, name)
            isUserLoggedIn.value = true
            authUiState.value = AuthUiState.Success("ثبت‌نام موفق! به عسل آباد خوش آمدید 🐝")
            return
        }

        auth.createUserWithEmailAndPassword(cleanEmail, pass)
            .addOnSuccessListener { result ->
                val user = result.user
                val name = if (cleanName.isNotBlank()) cleanName else cleanEmail.substringBefore("@")
                try {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user?.updateProfile(profileUpdates)
                } catch (_: Throwable) {}

                repository.setAuthenticatedUser(user?.uid ?: "user_me", user?.email, name)
                isUserLoggedIn.value = true
                authUiState.value = AuthUiState.Success("ثبت‌نام موفق در عسل آباد! 🐝💜")
            }
            .addOnFailureListener { error ->
                val errorMsg = error.message ?: ""
                val friendly = when {
                    errorMsg.contains("already in use", ignoreCase = true) ||
                    errorMsg.contains("email-already-in-use", ignoreCase = true) ->
                        "این ایمیل قبلاً در عسل آباد ثبت شده است. لطفاً وارد شوید."
                    errorMsg.contains("weak-password", ignoreCase = true) ->
                        "رمز عبور ضعیف است. لطفاً از رمز قوی‌تری استفاده کنید."
                    errorMsg.contains("network", ignoreCase = true) ->
                        "خطای شبکه اینترنت! لطفاً ارتباط را بررسی کنید."
                    else -> error.localizedMessage ?: "خطا در ثبت‌نام. لطفاً مجدداً امتحان کنید."
                }
                authUiState.value = AuthUiState.Error(friendly)
            }
    }

    fun signInAsGuest(customName: String? = null) {
        val name = customName?.takeIf { it.isNotBlank() } ?: "کاربر مهمان عسل‌آباد 🐝"
        repository.setAuthenticatedUser("guest_" + java.util.UUID.randomUUID().toString().take(6), null, name)
        isUserLoggedIn.value = true
        authUiState.value = AuthUiState.Success("با موفقیت به عنوان مهمان وارد شدید 🌸")
    }

    fun signOut() {
        try {
            firebaseAuth?.signOut()
        } catch (_: Throwable) {}
        repository.resetUser()
        isUserLoggedIn.value = false
        authUiState.value = AuthUiState.Idle
    }

    fun clearAuthError() {
        if (authUiState.value is AuthUiState.Error) {
            authUiState.value = AuthUiState.Idle
        }
    }
}

