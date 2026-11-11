package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BankAccountInfo
import com.example.data.model.PurchaseItem
import com.example.data.model.ShopPackage
import com.example.data.model.UserProfile
import com.example.ui.theme.AsalGold
import com.example.ui.theme.AsalGoldLight
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurpleLight
import com.example.ui.theme.AsalPurplePrimary

@Composable
fun ShopScreen(
    packages: List<ShopPackage>,
    purchaseList: List<PurchaseItem>,
    bankInfo: BankAccountInfo,
    user: UserProfile = UserProfile(),
    onStartPayment: (ShopPackage) -> Unit,
    onOpenJanous: () -> Unit,
    onOpenTariff: () -> Unit,
    onOpenEditBankCard: () -> Unit,
    onSearchPurchase: (String) -> PurchaseItem?,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedShopTab by remember { mutableIntStateOf(0) } // 0: بسته‌ها, 1: واریز دستی, 2: پیگیری و رسیدها
    var showPurchaseCheckDialog by remember { mutableStateOf(false) }
    var selectedReceiptItem by remember { mutableStateOf<PurchaseItem?>(null) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // ==========================================
            // HEADER & WALLET BALANCE SUMMARY CARD
            // ==========================================
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AsalGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF2E1065), Color(0xFF581C87), Color(0xFF7E22CE))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(AsalGold),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("👑", fontSize = 20.sp)
                                }
                                Column {
                                    Text(
                                        text = "فروشگاه الماس عسل آباد 🛒",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "با مدیریت و تضمین پریا چش رنگی 👑",
                                        fontSize = 10.sp,
                                        color = AsalGoldLight
                                    )
                                }
                            }

                            // VIP Badge
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AsalGold)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("⭐ VIP", fontSize = 11.sp, color = AsalGoldLight, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Current User Balances Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Diamonds Balance Box
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("💎", fontSize = 22.sp)
                                    Column {
                                        Text("موجودی الماس", fontSize = 10.sp, color = Color(0xFFDDD2F5))
                                        Text(
                                            text = "${user.diamonds}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            // Janous Balance Box
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("🟣", fontSize = 22.sp)
                                    Column {
                                        Text("موجودی جانوس", fontSize = 10.sp, color = Color(0xFFDDD2F5))
                                        Text(
                                            text = "${user.janous}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AsalGoldLight
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // SHOP SECTION NAVIGATION TABS
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    "💎 خرید بسته‌ها",
                    "🏦 واریز دستی",
                    "📋 پیگیری خریدها"
                ).forEachIndexed { index, title ->
                    val isSelected = selectedShopTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) AsalPurplePrimary else Color.Transparent)
                            .clickable { selectedShopTab = index }
                            .padding(vertical = 8.dp)
                            .testTag("shop_tab_$index"),
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

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // TAB 0: DIAMOND PACKAGES (بسته‌های خرید الماس)
            // ==========================================
            if (selectedShopTab == 0) {
                // Banner for 2X Diamonds Promo
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("🎡", fontSize = 26.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "طرح شگفت‌انگیز فروشگاه 2X عسل آباد",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "با خرید هر بسته، تعداد الماس‌ها ۲ برابر و ۱ گردونه شانس هدیه داده می‌شود!",
                                fontSize = 10.sp,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Packages Grid (3 rows x 2 columns)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val chunkedPackages = packages.chunked(2)
                    chunkedPackages.forEach { rowPackages ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowPackages.forEach { pkg ->
                                DiamondPackageCard(
                                    pkg = pkg,
                                    modifier = Modifier.weight(1f),
                                    onBuyClick = { onStartPayment(pkg) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Link to Manual Transfer
                Surface(
                    onClick = { selectedShopTab = 1 },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDD2F5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🏦", fontSize = 22.sp)
                            Column {
                                Text(
                                    text = "ترجیح می‌دهید کارت‌به‌کارت واریز کنید؟",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AsalPurpleDark
                                )
                                Text(
                                    text = "مشاهده شماره کارت مستقیم پریا چش رنگی و ثبت فیش",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Text("‹", fontSize = 20.sp, color = AsalPurplePrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ==========================================
            // TAB 1: MANUAL BANK CARD TRANSFER (کارت به کارت)
            // ==========================================
            if (selectedShopTab == 1) {
                // Realistic Bank Card Visual Representation
                BankCardVisualItem(
                    bankInfo = bankInfo,
                    onCopyCard = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("شماره کارت", bankInfo.cardNumber.replace("-", "").replace(" ", ""))
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "شماره کارت با موفقیت کپی شد: ${bankInfo.cardNumber}", Toast.LENGTH_SHORT).show()
                    },
                    onEditCard = onOpenEditBankCard
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Transfer Guide & Package Direct Selection
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("📌", fontSize = 18.sp)
                            Text(
                                text = "راهنمای واریز دستی (کارت به کارت)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val steps = listOf(
                            "۱. مبلغ بسته انتخابی را با همراه بانک یا خودپرداز به شماره کارت بالا انتقال دهید.",
                            "۲. کد پیگیری، شماره ارجاع یا ۶ رقم آخر رسید واریز را یادداشت کنید.",
                            "۳. یکی از بسته‌های زیر را انتخاب و شماره پیگیری را ثبت کنید تا الماس‌ها آنی شارژ شوند."
                        )
                        steps.forEach { step ->
                            Text(
                                text = step,
                                fontSize = 11.sp,
                                color = Color(0xFF4B5563),
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "انتخاب بسته واریز شده جهت ثبت فیش:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AsalPurplePrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Compact Package List for Card to Card Registration
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            packages.forEach { pkg ->
                                Surface(
                                    onClick = { onStartPayment(pkg) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFAF5FF),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text("💎", fontSize = 18.sp)
                                            Column {
                                                Text(
                                                    text = "بسته ${pkg.doubledDiamonds} الماس 2X",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AsalPurpleDark
                                                )
                                                Text(
                                                    text = "مبلغ: ${pkg.formattedPrice}",
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF059669),
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                        }

                                        Button(
                                            onClick = { onStartPayment(pkg) },
                                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(34.dp)
                                        ) {
                                            Text("ثبت فیش ✍️", fontSize = 10.sp, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ==========================================
            // TAB 2: PURCHASE LIST & TRACKING (پیگیری و رسیدها)
            // ==========================================
            if (selectedShopTab == 2) {
                // Tracking Search Bar
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🔍", fontSize = 18.sp)
                            Text(
                                text = "پیگیری سریع خرید با توکن یا شماره مرجع",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        var searchInput by remember { mutableStateOf("") }
                        var searchResult by remember { mutableStateOf<PurchaseItem?>(null) }
                        var hasSearched by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = searchInput,
                                onValueChange = { searchInput = it },
                                placeholder = { Text("توکن خرید (TOKEN) یا مرجع (RRN)") },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsalPurplePrimary)
                            )

                            Button(
                                onClick = {
                                    hasSearched = true
                                    searchResult = onSearchPurchase(searchInput)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(52.dp)
                            ) {
                                Text("جستجو", color = Color.White)
                            }
                        }

                        if (hasSearched) {
                            Spacer(modifier = Modifier.height(10.dp))
                            if (searchResult != null) {
                                val item = searchResult!!
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFECFDF5),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("✅ خرید یافت و تایید شد", fontWeight = FontWeight.Bold, color = Color(0xFF065F46), fontSize = 12.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("عنوان: ${item.title}", fontSize = 11.sp, color = AsalPurpleDark)
                                        Text("توکن: ${item.token}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AsalPurplePrimary)
                                        Text("روش پرداخت: ${item.paymentMethod}", fontSize = 11.sp, color = Color.Gray)
                                        Text("تاریخ ثبت: ${item.date}", fontSize = 10.sp, color = Color.Gray)
                                    }
                                }
                            } else {
                                Text(
                                    text = "❌ خریدی با این کد یافت نشد.",
                                    color = Color.Red,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // History List
                Card(
                    shape = RoundedCornerShape(18.dp),
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
                            Text(
                                text = "تراکنش‌ها و خریدهای اخیر (${purchaseList.size})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsalPurpleDark
                            )
                            if (purchaseList.isNotEmpty()) {
                                TextButton(onClick = onClearHistory) {
                                    Text("پاک کردن تاریخچه", fontSize = 11.sp, color = Color.Red)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (purchaseList.isEmpty()) {
                            Text(
                                text = "هنوز خریدی ثبت نکرده‌اید.",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                purchaseList.forEach { item ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFF9FAFB),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedReceiptItem = item }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text("🧾", fontSize = 20.sp)
                                                Column {
                                                    Text(
                                                        text = item.title,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = AsalPurpleDark
                                                    )
                                                    Text(
                                                        text = "${item.paymentMethod} • ${item.date}",
                                                        fontSize = 10.sp,
                                                        color = Color.Gray
                                                    )
                                                }
                                            }

                                            Text(
                                                text = "مشاهده فاکتور 👈",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AsalPurplePrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // EXTRA ASAL ABAD SERVICES (جانوس و تعرفه)
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Janous converter shortcut
                Surface(
                    onClick = onOpenJanous,
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🟣", fontSize = 22.sp)
                        Column {
                            Text("تبدیل به جانوس", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                            Text("۱۰۰۰ الماس = ۱ جانوس", fontSize = 9.sp, color = Color.Gray)
                        }
                    }
                }

                // Tariff calculator shortcut
                Surface(
                    onClick = onOpenTariff,
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("📊", fontSize = 22.sp)
                        Column {
                            Text("جدول تعرفه و امتیاز", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                            Text("محاسبه جوایز گروهی", fontSize = 9.sp, color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Digital Receipt Modal
        if (selectedReceiptItem != null) {
            com.example.ui.components.PaymentSuccessReceiptDialog(
                receipt = selectedReceiptItem!!,
                onDismiss = { selectedReceiptItem = null }
            )
        }
    }
}

/**
 * Beautiful Diamond Package Card Component with 2X badges and instant Buy button.
 */
@Composable
private fun DiamondPackageCard(
    pkg: ShopPackage,
    modifier: Modifier = Modifier,
    onBuyClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3E8FF)),
        modifier = modifier
            .testTag("diamond_package_${pkg.id}")
            .clickable(onClick = onBuyClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = "2X دوبل 💎",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                Text("🎡", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Diamond Icon with radial soft aura
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFAF5FF)),
                contentAlignment = Alignment.Center
            ) {
                Text("💎", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Original Strikethrough amount
            Text(
                text = "${pkg.originalDiamonds} الماس",
                fontSize = 10.sp,
                color = Color.Red,
                textDecoration = TextDecoration.LineThrough
            )

            // Doubled Big Reward
            Text(
                text = "${pkg.doubledDiamonds} الماس",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = AsalPurplePrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Formatted Price Tag
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFECFDF5),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981))
            ) {
                Text(
                    text = pkg.formattedPrice,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF047857),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Button
            Button(
                onClick = onBuyClick,
                colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
            ) {
                Text("خرید سریع ⚡", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

/**
 * High-fidelity Iranian Debit Bank Card UI Component for manual transfer
 */
@Composable
fun BankCardVisualItem(
    bankInfo: BankAccountInfo,
    onCopyCard: () -> Unit,
    onEditCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, AsalGold),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF1E1035), Color(0xFF4C1D95), Color(0xFF6D28D9))
                    )
                )
                .padding(16.dp)
        ) {
            // Card Top Row: Bank name & Chip & Edit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp, 22.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Brush.linearGradient(listOf(AsalGold, AsalGoldLight))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("💳", fontSize = 12.sp)
                    }
                    Text(
                        text = bankInfo.bankName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    onClick = onEditCard,
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "تغییر کارت", tint = Color.White, modifier = Modifier.size(12.dp))
                        Text("تغییر شماره کارت", fontSize = 9.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 16-Digit Card Number (Large & Centered)
            Text(
                text = bankInfo.cardNumber,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Card Bottom Row: Holder Name & Shaba
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text("صاحب حساب:", fontSize = 9.sp, color = Color(0xFFDDD2F5))
                    Text(
                        text = bankInfo.cardHolderName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsalGoldLight
                    )
                    if (bankInfo.iban.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = bankInfo.iban,
                            fontSize = 9.sp,
                            color = Color(0xFFC4B5FD)
                        )
                    }
                }

                // Copy Action Button
                Button(
                    onClick = onCopyCard,
                    colors = ButtonDefaults.buttonColors(containerColor = AsalGold),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "کپی", tint = AsalPurpleDark, modifier = Modifier.size(14.dp))
                        Text("کپی شماره کارت", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                    }
                }
            }
        }
    }
}
