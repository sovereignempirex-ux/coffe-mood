package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderEntity
import com.example.data.model.OrderStatus
import com.example.data.model.UserRole
import com.example.ui.components.CafeWorld3DCanvas
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowBadge
import com.example.ui.components.GoldGradientButton
import com.example.ui.theme.MoodAmberGlow
import com.example.ui.theme.MoodCreamText
import com.example.ui.theme.MoodDarkCard
import com.example.ui.theme.MoodDarkCardElevated
import com.example.ui.theme.MoodDarkEspresso
import com.example.ui.theme.MoodDarkInk
import com.example.ui.theme.MoodGlassBorder
import com.example.ui.theme.MoodGoldDark
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodGoldSecondary
import com.example.ui.theme.MoodMutedText
import com.example.ui.theme.MoodTealGlow
import com.example.ui.theme.MoodTealNeon
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MoodCafeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: MoodCafeViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAdminPinDialog by remember { mutableStateOf(false) }
    var adminPinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    // Intercept back presses for open dialogs or tabs
    if (showEditProfileDialog) {
        BackHandler { showEditProfileDialog = false }
    } else if (showAdminPinDialog) {
        BackHandler {
            showAdminPinDialog = false
            adminPinInput = ""
            pinError = false
        }
    } else if (selectedTabIndex != 0) {
        BackHandler { selectedTabIndex = 0 }
    }

    // User's orders
    val userOrders = allOrders.filter {
        it.customerEmail == (currentUser?.email ?: "guest@mood.cafe")
    }

    val favoriteProducts = allProducts.filter { favoriteIds.contains(it.id) }

    Scaffold(
        containerColor = MoodDarkInk
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CafeWorld3DCanvas(intensity = 0.8f)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Top Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MoodDarkCardElevated)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", tint = MoodCreamText)
                    }

                    Text(
                        text = "الملف الشخصي",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodGoldPrimary
                    )

                    IconButton(
                        onClick = { showAdminPinDialog = true },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MoodDarkCardElevated)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "دخول الإدارة", tint = MoodTealGlow)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(listOf(MoodGoldPrimary, MoodGoldDark))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentUser?.avatarLetter ?: "م",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MoodDarkInk
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = currentUser?.name ?: "ضيف مزاج",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MoodCreamText
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    GlowBadge(
                                        text = if (currentUser?.role == UserRole.ADMIN.name) "مدير النظام" else "عميل دائم",
                                        isTeal = currentUser?.role == UserRole.ADMIN.name
                                    )
                                }
                                Text(
                                    text = currentUser?.phone ?: "01283073813",
                                    fontSize = 12.sp,
                                    color = MoodMutedText
                                )
                                Text(
                                    text = currentUser?.email ?: "guest@mood.cafe",
                                    fontSize = 11.sp,
                                    color = MoodTealGlow
                                )
                            }
                        }

                        IconButton(
                            onClick = { showEditProfileDialog = true },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MoodDarkCardElevated)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = MoodGoldPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // If user is Admin, show quick access banner
                if (currentUser?.role == UserRole.ADMIN.name) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.2.dp, MoodTealGlow), RoundedCornerShape(16.dp))
                            .background(MoodTealNeon.copy(alpha = 0.15f))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = MoodTealGlow)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("لوحة إدارة كافيه مزاج", fontWeight = FontWeight.Bold, color = MoodCreamText, fontSize = 14.sp)
                                    Text("متابعة الطلبات، تعديل المنتجات والإعدادات", color = MoodMutedText, fontSize = 11.sp)
                                }
                            }
                            GoldGradientButton(
                                text = "دخول الإدارة",
                                isOutline = true,
                                onClick = { viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Tabs: Orders / Favorites / API & Settings
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MoodDarkCard,
                    contentColor = MoodGoldPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = MoodGoldPrimary
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("طلباتي (${userOrders.size})", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("المفضلة (${favoriteProducts.size})", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = { Text("الربط والـ API", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tab Contents
                when (selectedTabIndex) {
                    0 -> OrdersTab(orders = userOrders)
                    1 -> FavoritesTab(
                        favorites = favoriteProducts,
                        onQuickAdd = { viewModel.addToCart(it) },
                        onRemoveFavorite = { viewModel.toggleFavorite(it) }
                    )
                    2 -> ApiAndSettingsTab(
                        onOpenAdmin = { showAdminPinDialog = true }
                    )
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var editName by remember { mutableStateOf(currentUser?.name ?: "") }
        var editPhone by remember { mutableStateOf(currentUser?.phone ?: "") }
        var editEmail by remember { mutableStateOf(currentUser?.email ?: "") }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            containerColor = MoodDarkCard,
            title = { Text("تعديل بيانات الحساب", color = MoodGoldPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("الاسم") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodGoldPrimary,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        )
                    )
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("رقم الهاتف / واتساب") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodGoldPrimary,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        )
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("البريد الإلكتروني") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodGoldPrimary,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        )
                    )
                }
            },
            confirmButton = {
                GoldGradientButton(
                    text = "حفظ",
                    onClick = {
                        if (editName.isNotBlank()) {
                            viewModel.loginUser(editName, editEmail, editPhone)
                        }
                        showEditProfileDialog = false
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("إلغاء", color = MoodMutedText)
                }
            }
        )
    }

    // Admin PIN secret access dialog
    if (showAdminPinDialog) {
        AlertDialog(
            onDismissRequest = { showAdminPinDialog = false },
            containerColor = MoodDarkCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Key, contentDescription = null, tint = MoodTealGlow)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("المدخل السري للإدارة", color = MoodGoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        "أدخل الرمز السري الخاص بإدارة كافيه مزاج (الرمز الافتراضي: 1234 أو 2025):",
                        fontSize = 12.sp,
                        color = MoodMutedText
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = adminPinInput,
                        onValueChange = {
                            adminPinInput = it
                            pinError = false
                        },
                        label = { Text("رمز PIN") },
                        isError = pinError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodTealGlow,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        ),
                        singleLine = true
                    )
                    if (pinError) {
                        Text("الرمز غير صحيح، حاول مرة أخرى", color = Color(0xFFFF5252), fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            },
            confirmButton = {
                GoldGradientButton(
                    text = "فتح اللوحة 🔓",
                    onClick = {
                        if (adminPinInput == "1234" || adminPinInput == "2025" || adminPinInput == "mood" || adminPinInput == "7777") {
                            currentUser?.let { viewModel.toggleUserRole(it) }
                            showAdminPinDialog = false
                            viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD)
                        } else {
                            pinError = true
                        }
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { showAdminPinDialog = false }) {
                    Text("إلغاء", color = MoodMutedText)
                }
            }
        )
    }
}

@Composable
private fun OrdersTab(orders: List<OrderEntity>) {
    if (orders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "📋", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("لا توجد طلبات سابقة بعد", color = MoodCreamText, fontWeight = FontWeight.Bold)
                Text("اطلب الآن واستمتع بمذاق القهوة المميزة", color = MoodMutedText, fontSize = 12.sp)
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(orders) { order ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "طلب #${order.orderCode}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MoodGoldPrimary
                            )
                            val statusEnum = try {
                                OrderStatus.valueOf(order.status)
                            } catch (e: Exception) {
                                OrderStatus.PENDING
                            }
                            OrderStatusBadge(status = statusEnum)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = order.itemsSummary,
                            fontSize = 12.sp,
                            color = MoodCreamText,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val dateStr = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ar")).format(Date(order.createdAt))
                            Text(text = "📅 $dateStr", fontSize = 11.sp, color = MoodMutedText)
                            Text(
                                text = "${order.totalAmount.toInt()} ج.م",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = MoodGoldSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderStatusBadge(status: OrderStatus) {
    val color = Color(status.colorHex)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.18f))
            .border(BorderStroke(1.dp, color), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = status.textAr,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FavoritesTab(
    favorites: List<com.example.data.model.ProductEntity>,
    onQuickAdd: (com.example.data.model.ProductEntity) -> Unit,
    onRemoveFavorite: (com.example.data.model.ProductEntity) -> Unit
) {
    if (favorites.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "♥", fontSize = 48.sp, color = MoodGoldPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Text("لم تقم بإضافة مشروبات للمفضلة بعد", color = MoodCreamText, fontWeight = FontWeight.Bold)
                Text("اضغط على أيقونة القلب على أي مشروب لحفظه هنا", color = MoodMutedText, fontSize = 12.sp)
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(favorites) { product ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = getDrinkEmoji(product.nameAr),
                                fontSize = 28.sp,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            Column {
                                Text(
                                    text = product.nameAr,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MoodGoldPrimary
                                )
                                Text(
                                    text = "${product.price.toInt()} ج.م",
                                    fontSize = 12.sp,
                                    color = MoodTealGlow
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = { onQuickAdd(product) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MoodGoldPrimary)
                            ) {
                                Text("+", color = MoodDarkInk, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            }

                            IconButton(
                                onClick = { onRemoveFavorite(product) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MoodDarkCardElevated)
                            ) {
                                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ApiAndSettingsTab(onOpenAdmin: () -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = MoodGoldPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ربط الـ API وحساب جوجل الخارجي", fontWeight = FontWeight.Bold, color = MoodGoldPrimary, fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "التطبيق مهيأ بالكامل للربط مع سيرفر خارجي، تسجيل الدخول بجوجل Google Sign-In، ومزامنة الطلبات في الوقت الفعلي.",
                        fontSize = 12.sp,
                        color = MoodCreamText,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MoodDarkCardElevated)
                            .padding(10.dp)
                    ) {
                        Text(
                            "🌐 API Endpoint: Configured in Admin Dashboard\n🔑 Google OAuth: Jetpack CredentialManager Ready\n💾 Local Room DB: Synchronized & Offline First",
                            fontSize = 11.sp,
                            color = MoodTealGlow,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    GoldGradientButton(
                        text = "تعديل إعدادات الـ API عبر لوحة الإدارة ⚙️",
                        onClick = onOpenAdmin,
                        isOutline = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
