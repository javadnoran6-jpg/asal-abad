package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AsalAbadBottomNav
import com.example.ui.components.AsalAbadTopHeader
import com.example.ui.components.BeautyVideoCallDialog
import com.example.ui.components.CardToCardDialog
import com.example.ui.components.CreateGroupDialog
import com.example.ui.components.DailyLotteryDialog
import com.example.ui.components.EditBankCardDialog
import com.example.ui.components.GroupMarketDialog
import com.example.ui.components.GroupVoiceCallDialog
import com.example.ui.components.InAppNotificationBanner
import com.example.ui.components.JanousConvertDialog
import com.example.ui.components.LuckyWheelDialog
import com.example.ui.components.MergeGroupsDialog
import com.example.ui.components.OwnerAdminPanelDialog
import com.example.ui.components.PaymentMethodDialog
import com.example.ui.components.PaymentSuccessReceiptDialog
import com.example.ui.components.PreviousWinnersDialog
import com.example.ui.components.ShaparakGatewayDialog
import com.example.ui.components.TariffDialog
import com.example.ui.components.VipInfoDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.MessengerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.SignInScreen
import com.example.ui.screens.games.NumberGuessGameScreen
import com.example.ui.screens.games.RockPaperScissorsGameScreen
import com.example.ui.screens.games.WordGuessGameScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ActiveGame
import com.example.ui.viewmodel.AsalAbadViewModel
import com.example.ui.viewmodel.MainTab
import com.example.util.NotificationHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        NotificationHelper.initNotificationChannel(this)
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    AsalAbadMainApp(intent = intent)
                }
            }
        }
    }
}

@Composable
fun AsalAbadMainApp(
    intent: Intent? = null,
    viewModel: AsalAbadViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.setApplicationContext(context)
        intent?.getStringExtra("EXTRA_TARGET_ROOM_ID")?.let { roomId ->
            viewModel.selectTab(MainTab.MESSENGER)
            viewModel.openChatRoom(roomId)
        }
        if (intent?.getStringExtra("EXTRA_TARGET_TAB") == "SHOP") {
            viewModel.selectTab(MainTab.SHOP)
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { /* Handled */ }

        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val activeInAppNotification by viewModel.activeInAppNotification.collectAsState()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val activeChatRoomId by viewModel.activeChatRoomId.collectAsState()
    val activeGame by viewModel.activeGame.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val chatRooms by viewModel.chatRooms.collectAsState()
    val messagesMap by viewModel.messagesMap.collectAsState()
    val purchaseList by viewModel.purchaseList.collectAsState()

    // Bank & Payment Gateway states
    val bankAccount by viewModel.bankAccount.collectAsState()
    val selectedPackage by viewModel.selectedPackage.collectAsState()
    val showPaymentMethodDialog by viewModel.showPaymentMethodDialog.collectAsState()
    val showShaparakGatewayDialog by viewModel.showShaparakGatewayDialog.collectAsState()
    val showCardToCardDialog by viewModel.showCardToCardDialog.collectAsState()
    val showEditBankCardDialog by viewModel.showEditBankCardDialog.collectAsState()
    val paymentSuccessReceipt by viewModel.paymentSuccessReceipt.collectAsState()

    // Dialog state collections
    val showWheelDialog by viewModel.showWheelDialog.collectAsState()
    val showLotteryDialog by viewModel.showLotteryDialog.collectAsState()
    val showGroupMarketDialog by viewModel.showGroupMarketDialog.collectAsState()
    val showMergeGroupsDialog by viewModel.showMergeGroupsDialog.collectAsState()
    val showJanousConvertDialog by viewModel.showJanousConvertDialog.collectAsState()
    val showVipInfoDialog by viewModel.showVipInfoDialog.collectAsState()
    val showTariffDialog by viewModel.showTariffDialog.collectAsState()
    val showCreateGroupDialog by viewModel.showCreateGroupDialog.collectAsState()
    val showAdminPanelDialog by viewModel.showAdminPanelDialog.collectAsState()
    val showBeautyVideoCallDialog by viewModel.showBeautyVideoCallDialog.collectAsState()
    val activeVideoCallRoom by viewModel.activeVideoCallRoom.collectAsState()
    val showGroupVoiceCallDialog by viewModel.showGroupVoiceCallDialog.collectAsState()
    val activeVoiceCallRoom by viewModel.activeVoiceCallRoom.collectAsState()

    var showPreviousWinnersDialog by remember { mutableStateOf(false) }

    // Wheel animation states
    val wheelRotation by viewModel.wheelRotation.collectAsState()
    val isSpinning by viewModel.isSpinning.collectAsState()
    val wheelRewardMessage by viewModel.wheelRewardMessage.collectAsState()

    // Games states
    val rpsPlayerChoice by viewModel.rpsPlayerChoice.collectAsState()
    val rpsBotChoice by viewModel.rpsBotChoice.collectAsState()
    val rpsResultText by viewModel.rpsResultText.collectAsState()
    val rpsPlayerScore by viewModel.rpsPlayerScore.collectAsState()
    val rpsBotScore by viewModel.rpsBotScore.collectAsState()

    val numberGuessState by viewModel.numberGuessState.collectAsState()
    val wordGuessState by viewModel.wordGuessState.collectAsState()

    // Back handling
    BackHandler(enabled = isUserLoggedIn && (activeGame != ActiveGame.NONE || activeChatRoomId != null || currentTab != MainTab.HOME)) {
        if (activeGame != ActiveGame.NONE) {
            viewModel.closeGame()
        } else if (activeChatRoomId != null) {
            viewModel.closeChatRoom()
        } else if (currentTab != MainTab.HOME) {
            viewModel.selectTab(MainTab.HOME)
        }
    }

    if (!isUserLoggedIn) {
        SignInScreen(
            viewModel = viewModel,
            onContinueAsGuest = {
                // State is updated in viewModel
            }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
        if (activeGame != ActiveGame.NONE) {
            // Active Game Full-Screen Overlays
            when (activeGame) {
                ActiveGame.ROCK_PAPER_SCISSORS -> {
                    RockPaperScissorsGameScreen(
                        playerChoice = rpsPlayerChoice,
                        botChoice = rpsBotChoice,
                        resultText = rpsResultText,
                        playerScore = rpsPlayerScore,
                        botScore = rpsBotScore,
                        onChoose = { viewModel.playRps(it) },
                        onReset = { viewModel.resetRps() },
                        onBack = { viewModel.closeGame() }
                    )
                }
                ActiveGame.NUMBER_GUESS -> {
                    NumberGuessGameScreen(
                        state = numberGuessState,
                        onGuess = { viewModel.makeNumberGuess(it) },
                        onReset = { viewModel.resetNumberGuessGame() },
                        onBack = { viewModel.closeGame() }
                    )
                }
                ActiveGame.WORD_GUESS -> {
                    WordGuessGameScreen(
                        state = wordGuessState,
                        onGuessLetter = { viewModel.guessWordLetter(it) },
                        onReset = { viewModel.resetWordGuessGame() },
                        onBack = { viewModel.closeGame() }
                    )
                }
                ActiveGame.SNAKES_LADDERS -> {
                    com.example.ui.screens.games.SnakesLaddersGameScreen(
                        onWin = { viewModel.rewardDiamonds(it) },
                        onBack = { viewModel.closeGame() }
                    )
                }
                ActiveGame.NONE -> {}
            }
        } else {

    Scaffold(
        topBar = {
            if (activeChatRoomId == null) {
                AsalAbadTopHeader(
                    user = currentUser,
                    onShopClick = { viewModel.selectTab(MainTab.SHOP) },
                    onJanousClick = { viewModel.showJanousConvertDialog.value = true },
                    onProfileClick = { viewModel.selectTab(MainTab.PROFILE) }
                )
            }
        },
        bottomBar = {
            if (activeChatRoomId == null) {
                AsalAbadBottomNav(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFE2D6F5),
                            Color(0xFFEDE4FA),
                            Color(0xFFF9F6FE)
                        )
                    )
                )
        ) {
            when (currentTab) {
                MainTab.HOME -> {
                    HomeScreen(
                        user = currentUser,
                        onOpenGame = { viewModel.openGame(it) },
                        onOpenWheel = { viewModel.showWheelDialog.value = true },
                        onOpenLottery = { viewModel.showLotteryDialog.value = true }
                    )
                }
                MainTab.MESSENGER -> {
                    val messages = if (activeChatRoomId != null) {
                        messagesMap[activeChatRoomId] ?: emptyList()
                    } else emptyList()

                    MessengerScreen(
                        rooms = chatRooms,
                        activeRoomId = activeChatRoomId,
                        messages = messages,
                        allMessagesMap = messagesMap,
                        onOpenRoom = { viewModel.openChatRoom(it) },
                        onCloseRoom = { viewModel.closeChatRoom() },
                        onSendMessage = { roomId, text, sticker ->
                            viewModel.sendChatMessage(roomId, text ?: "", sticker)
                        },
                        onSendMedia = { roomId, type, uri, caption ->
                            viewModel.sendMediaMessage(roomId, type, uri, caption)
                        },
                        onSendVoice = { roomId, durationSeconds ->
                            viewModel.sendVoiceMessage(roomId, durationSeconds)
                        },
                        onStartVideoCall = { room ->
                            viewModel.startVideoCall(room)
                        },
                        onStartVoiceCall = { room ->
                            viewModel.startVoiceCall(room)
                        },
                        onOpenMarket = { viewModel.showGroupMarketDialog.value = true },
                        onOpenMergeGroups = { viewModel.showMergeGroupsDialog.value = true },
                        onOpenCreateGroup = { viewModel.showCreateGroupDialog.value = true }
                    )
                }
                MainTab.SHOP -> {
                    ShopScreen(
                        packages = viewModel.shopPackages,
                        purchaseList = purchaseList,
                        bankInfo = bankAccount,
                        user = currentUser,
                        onStartPayment = { viewModel.startPaymentFlow(it) },
                        onOpenJanous = { viewModel.showJanousConvertDialog.value = true },
                        onOpenTariff = { viewModel.showTariffDialog.value = true },
                        onOpenEditBankCard = { viewModel.showEditBankCardDialog.value = true },
                        onSearchPurchase = { viewModel.searchPurchase(it) },
                        onClearHistory = { viewModel.clearPurchases() }
                    )
                }
                MainTab.PROFILE -> {
                    ProfileScreen(
                        user = currentUser,
                        creatorProfile = viewModel.creatorProfile,
                        onOpenVipModal = { viewModel.showVipInfoDialog.value = true },
                        onUpdateBio = { bio, badge -> viewModel.updateBio(bio, badge) },
                        onOpenPrivateChatWithCreator = {
                            viewModel.selectTab(MainTab.MESSENGER)
                            viewModel.openChatRoom("room_paria")
                        },
                        onOpenAdminPanel = { viewModel.showAdminPanelDialog.value = true },
                        onSetPresence = { viewModel.setPresenceStatus(it) },
                        onUpdateFullProfile = { name, username, bio, emoji, avatarUri, status, tick ->
                            viewModel.updateProfileInfo(name, username, bio, emoji, avatarUri, status, tick)
                        },
                        onSignOut = { viewModel.signOut() },
                        onSetDefaultAvatar = { viewModel.setDefaultAvatar(it) },
                        onPickGalleryAvatar = { viewModel.setGalleryAvatar(it) },
                        onSaveBio = { viewModel.updateBio(it, currentUser.badgeSymbol) },
                        onRestoreBackup = { n, u, b, e -> viewModel.restoreProfileBackup(n, u, b, e) },
                        onDeleteAccount = { viewModel.deleteAccount(context) }
                    )
                }
                MainTab.STATS -> {
                    LeaderboardScreen(users = viewModel.leaderboardUsers)
                }
            }
        }
    }
    }

    // Wheel Dialog
    if (showWheelDialog) {
        LuckyWheelDialog(
            sectors = viewModel.wheelSectors,
            rotationAngle = wheelRotation,
            isSpinning = isSpinning,
            rewardMessage = wheelRewardMessage,
            onSpinClick = { viewModel.spinWheel() },
            onDismiss = { viewModel.showWheelDialog.value = false }
        )
    }

    // Daily Lottery Dialog
    if (showLotteryDialog) {
        DailyLotteryDialog(
            onJoinClick = {
                viewModel.joinDailyLottery()
                viewModel.showLotteryDialog.value = false
            },
            onPreviousWinnersClick = {
                viewModel.showLotteryDialog.value = false
                showPreviousWinnersDialog = true
            },
            onDismiss = { viewModel.showLotteryDialog.value = false }
        )
    }

    if (showPreviousWinnersDialog) {
        PreviousWinnersDialog(
            winners = viewModel.lotteryWinners,
            onDismiss = { showPreviousWinnersDialog = false }
        )
    }

    // Janous Converter Dialog
    if (showJanousConvertDialog) {
        JanousConvertDialog(
            currentDiamonds = currentUser.diamonds,
            onConvert = { viewModel.convertDiamondsToJanous(it) },
            onDismiss = { viewModel.showJanousConvertDialog.value = false }
        )
    }

    // Group Market & Merge
    if (showGroupMarketDialog) {
        GroupMarketDialog(
            listings = viewModel.groupMarketListings,
            onDismiss = { viewModel.showGroupMarketDialog.value = false }
        )
    }

    if (showMergeGroupsDialog) {
        MergeGroupsDialog(
            onDismiss = { viewModel.showMergeGroupsDialog.value = false }
        )
    }

    // VIP Dialog
    if (showVipInfoDialog) {
        VipInfoDialog(
            onBuyPlan = { name, price ->
                viewModel.startVipPurchase(name, price)
                viewModel.showVipInfoDialog.value = false
            },
            onDismiss = { viewModel.showVipInfoDialog.value = false }
        )
    }

    // Tariff Dialog with Interactive Calculator
    if (showTariffDialog) {
        TariffDialog(
            currentDiamonds = currentUser.diamonds,
            onDonatePoints = { viewModel.donatePoints(it) },
            onDismiss = { viewModel.showTariffDialog.value = false }
        )
    }

    // Bank Payment Modals
    if (showPaymentMethodDialog && selectedPackage != null) {
        PaymentMethodDialog(
            pkg = selectedPackage!!,
            onSelectGateway = { viewModel.choosePaymentMethod(isOnlineGateway = true) },
            onSelectCardToCard = { viewModel.choosePaymentMethod(isOnlineGateway = false) },
            onDismiss = { viewModel.showPaymentMethodDialog.value = false }
        )
    }

    if (showShaparakGatewayDialog && selectedPackage != null) {
        ShaparakGatewayDialog(
            pkg = selectedPackage!!,
            onPaymentSuccess = { cardNumber ->
                viewModel.completeShaparakPayment(selectedPackage!!, cardNumber)
            },
            onDismiss = { viewModel.showShaparakGatewayDialog.value = false }
        )
    }

    if (showCardToCardDialog && selectedPackage != null) {
        CardToCardDialog(
            pkg = selectedPackage!!,
            bankInfo = bankAccount,
            onEditCardClick = {
                viewModel.showCardToCardDialog.value = false
                viewModel.showEditBankCardDialog.value = true
            },
            onSubmitReceipt = { trackingCode ->
                viewModel.completeCardToCardPayment(selectedPackage!!, trackingCode)
            },
            onDismiss = { viewModel.showCardToCardDialog.value = false }
        )
    }

    if (showEditBankCardDialog) {
        EditBankCardDialog(
            currentInfo = bankAccount,
            onSave = { cardNumber, holderName, bankName, iban ->
                viewModel.updateDestinationBankCard(cardNumber, holderName, bankName, iban)
            },
            onDismiss = { viewModel.showEditBankCardDialog.value = false }
        )
    }

    if (paymentSuccessReceipt != null) {
        PaymentSuccessReceiptDialog(
            receipt = paymentSuccessReceipt!!,
            onDismiss = { viewModel.paymentSuccessReceipt.value = null }
        )
    }

    if (showCreateGroupDialog) {
        CreateGroupDialog(
            onCreateGroup = { title, desc, emoji, withPani ->
                viewModel.createNewGroup(title, desc, emoji, withPani)
                viewModel.showCreateGroupDialog.value = false
            },
            onDismiss = { viewModel.showCreateGroupDialog.value = false }
        )
    }

    // Owner Admin Panel Dialog
    if (showAdminPanelDialog) {
        OwnerAdminPanelDialog(
            user = currentUser,
            bankInfo = bankAccount,
            onGrantDiamonds = { viewModel.grantOwnerFreeDiamonds() },
            onUpdateBankCard = {
                viewModel.showAdminPanelDialog.value = false
                viewModel.showEditBankCardDialog.value = true
            },
            onDismiss = { viewModel.showAdminPanelDialog.value = false }
        )
    }

    // Beauty Video Call Dialog (تماس تصویری با فیلتر خوشگل و آرایشی)
    if (showBeautyVideoCallDialog && activeVideoCallRoom != null) {
        BeautyVideoCallDialog(
            room = activeVideoCallRoom!!,
            onDismiss = {
                viewModel.showBeautyVideoCallDialog.value = false
                viewModel.activeVideoCallRoom.value = null
            }
        )
    }

    // Group Voice Call Dialog (ویسکال گروهی)
    if (showGroupVoiceCallDialog && activeVoiceCallRoom != null) {
        GroupVoiceCallDialog(
            room = activeVoiceCallRoom!!,
            onDismiss = {
                viewModel.showGroupVoiceCallDialog.value = false
                viewModel.activeVoiceCallRoom.value = null
            }
        )
    }

    // In-App Notification Heads-Up Banner at the top of the screen
    AnimatedVisibility(
        visible = activeInAppNotification != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = Modifier
            .align(Alignment.TopCenter)
            .statusBarsPadding()
            .padding(top = 8.dp)
            .zIndex(100f)
    ) {
        activeInAppNotification?.let { notif ->
            InAppNotificationBanner(
                notification = notif,
                onClick = { viewModel.handleNotificationClick(notif) },
                onDismiss = { viewModel.dismissNotification() }
            )
        }
    }
}
}
}

