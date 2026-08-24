package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.data.model.UserRole
import com.example.ui.components.CafeWorld3DCanvas
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowBadge
import com.example.ui.screens.admin.AdminCategoriesSection
import com.example.ui.screens.admin.AdminNotificationsModal
import com.example.ui.screens.admin.AdminOrdersSection
import com.example.ui.screens.admin.AdminOverviewSection
import com.example.ui.screens.admin.AdminProductsSection
import com.example.ui.screens.admin.AdminSecurityGate
import com.example.ui.screens.admin.AdminSettingsSection
import com.example.ui.screens.admin.AdminThemeSection
import com.example.ui.screens.admin.AdminUsersSection
import com.example.ui.theme.MoodAmberDark
import com.example.ui.theme.MoodAmberPrimary
import com.example.ui.theme.MoodCreamText
import com.example.ui.theme.MoodDarkCard
import com.example.ui.theme.MoodDarkCardElevated
import com.example.ui.theme.MoodDarkInk
import com.example.ui.theme.MoodGlassBorder
import com.example.ui.theme.MoodGlassSurface
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodMutedText
import com.example.ui.theme.MoodTealGlow
import com.example.ui.theme.MoodTealNeon
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MoodCafeViewModel
import kotlinx.coroutines.launch

data class AdminNavSection(
    val id: Int,
    val titleAr: String,
    val titleEn: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: MoodCafeViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isAdminUnlocked by viewModel.isAdminUnlocked.collectAsState()
    val cafeSettings by viewModel.cafeSettings.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val allCategories by viewModel.allCategories.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    val pendingOrdersCount = allOrders.count { it.status == OrderStatus.PENDING.name }
    val unreadNotificationsCount = notifications.count { !it.isRead }

    // Security Gate Check: if not unlocked and user is not admin
    val isAuthorized = isAdminUnlocked || currentUser?.role == UserRole.ADMIN.name

    var selectedSectionIndex by remember { mutableIntStateOf(0) }
    var showNotificationsModal by remember { mutableStateOf(false) }

    val navSections = listOf(
        AdminNavSection(0, "نظرة عامة والإحصائيات", "Overview", Icons.Default.Dashboard),
        AdminNavSection(1, "إدارة المنتجات والمنيو", "Products", Icons.Default.LocalCafe),
        AdminNavSection(2, "إدارة الأقسام والتصنيفات", "Categories", Icons.Default.Category),
        AdminNavSection(3, "إدارة ومتابعة الطلبات", "Orders", Icons.Default.ReceiptLong, badgeCount = pendingOrdersCount),
        AdminNavSection(4, "إدارة المستخدمين والعملاء", "Users", Icons.Default.Group),
        AdminNavSection(5, "إعدادات الكافيه والموقع", "Settings", Icons.Default.Settings),
        AdminNavSection(6, "تخصيص المظهر والثيم", "Theme", Icons.Default.Palette)
    )

    if (!isAuthorized) {
        AdminSecurityGate(
            viewModel = viewModel,
            onSuccess = { /* Automatically refreshes state */ }
        )
        return
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Back handlers for nested states
    if (drawerState.isOpen) {
        BackHandler { coroutineScope.launch { drawerState.close() } }
    } else if (showNotificationsModal) {
        BackHandler { showNotificationsModal = false }
    } else if (selectedSectionIndex != 0) {
        BackHandler { selectedSectionIndex = 0 }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MoodDarkInk)
    ) {
        val isWideScreen = maxWidth >= 750.dp

        if (isWideScreen) {
            // Tablet & Desktop Layout: Permanent Glassmorphism Sidebar
            Row(modifier = Modifier.fillMaxSize()) {
                // Permanent Sidebar
                GlassAdminSidebar(
                    sections = navSections,
                    selectedSection = selectedSectionIndex,
                    onSelectSection = { selectedSectionIndex = it },
                    onNavigateHome = { viewModel.navigateTo(AppScreen.HOME) },
                    onLockAdmin = { viewModel.lockAdmin() },
                    modifier = Modifier
                        .width(260.dp)
                        .fillMaxHeight()
                )

                // Main Content View
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    AdminTopHeader(
                        currentSectionTitle = navSections[selectedSectionIndex].titleAr,
                        unreadNotificationsCount = unreadNotificationsCount,
                        onOpenNotifications = { showNotificationsModal = true },
                        onNavigateHome = { viewModel.navigateTo(AppScreen.HOME) },
                        onToggleDrawer = null // Permanent on desktop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AdminSectionContent(
                        selectedIndex = selectedSectionIndex,
                        viewModel = viewModel,
                        allOrders = allOrders,
                        allProducts = allProducts,
                        allCategories = allCategories,
                        allUsers = allUsers,
                        cafeSettings = cafeSettings,
                        onNavigateToSection = { selectedSectionIndex = it }
                    )
                }
            }
        } else {
            // Mobile Layout: ModalNavigationDrawer
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = MoodDarkCard,
                        modifier = Modifier.width(280.dp)
                    ) {
                        GlassAdminSidebar(
                            sections = navSections,
                            selectedSection = selectedSectionIndex,
                            onSelectSection = {
                                selectedSectionIndex = it
                                coroutineScope.launch { drawerState.close() }
                            },
                            onNavigateHome = {
                                coroutineScope.launch { drawerState.close() }
                                viewModel.navigateTo(AppScreen.HOME)
                            },
                            onLockAdmin = {
                                coroutineScope.launch { drawerState.close() }
                                viewModel.lockAdmin()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            ) {
                Scaffold(
                    containerColor = MoodDarkInk,
                    topBar = {
                        AdminTopHeader(
                            currentSectionTitle = navSections[selectedSectionIndex].titleAr,
                            unreadNotificationsCount = unreadNotificationsCount,
                            onOpenNotifications = { showNotificationsModal = true },
                            onNavigateHome = { viewModel.navigateTo(AppScreen.HOME) },
                            onToggleDrawer = {
                                coroutineScope.launch {
                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                }
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        CafeWorld3DCanvas(motionEnabled = cafeSettings.motionEnabled, intensity = 0.5f)

                        AdminSectionContent(
                            selectedIndex = selectedSectionIndex,
                            viewModel = viewModel,
                            allOrders = allOrders,
                            allProducts = allProducts,
                            allCategories = allCategories,
                            allUsers = allUsers,
                            cafeSettings = cafeSettings,
                            onNavigateToSection = { selectedSectionIndex = it }
                        )
                    }
                }
            }
        }

        // Real-time Notifications Dialog
        if (showNotificationsModal) {
            AdminNotificationsModal(
                notifications = notifications,
                onMarkAsRead = { viewModel.markNotificationAsRead(it) },
                onClearAll = { viewModel.clearAllNotifications() },
                onDismiss = { showNotificationsModal = false }
            )
        }
    }
}

@Composable
private fun AdminTopHeader(
    currentSectionTitle: String,
    unreadNotificationsCount: Int,
    onOpenNotifications: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleDrawer: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onToggleDrawer != null) {
                IconButton(
                    onClick = onToggleDrawer,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MoodDarkCardElevated)
                        .border(1.dp, MoodGlassBorder, CircleShape)
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "القائمة الجانبية", tint = MoodAmberPrimary)
                }
                Spacer(modifier = Modifier.width(10.dp))
            } else {
                IconButton(
                    onClick = onNavigateHome,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MoodDarkCardElevated)
                        .border(1.dp, MoodGlassBorder, CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", tint = MoodCreamText)
                }
                Spacer(modifier = Modifier.width(10.dp))
            }

            Column {
                Text(
                    text = currentSectionTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = MoodCreamText
                )
                Text(
                    text = "MOOD ADMIN PANEL ✦",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodTealGlow,
                    letterSpacing = 1.sp
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Live Notification Bell
            IconButton(
                onClick = onOpenNotifications,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MoodDarkCardElevated)
                    .border(1.dp, MoodGlassBorder, CircleShape)
            ) {
                if (unreadNotificationsCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = Color(0xFFFF5252),
                                contentColor = Color.White
                            ) {
                                Text("$unreadNotificationsCount")
                            }
                        }
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "الإشعارات", tint = MoodAmberPrimary)
                    }
                } else {
                    Icon(Icons.Default.Notifications, contentDescription = "الإشعارات", tint = MoodMutedText)
                }
            }

            // Quick Store Back
            if (onToggleDrawer != null) {
                IconButton(
                    onClick = onNavigateHome,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MoodDarkCardElevated)
                        .border(1.dp, MoodGlassBorder, CircleShape)
                ) {
                    Icon(Icons.Default.Storefront, contentDescription = "واجهة الكافيه", tint = MoodTealGlow)
                }
            }
        }
    }
}

@Composable
private fun GlassAdminSidebar(
    sections: List<AdminNavSection>,
    selectedSection: Int,
    onSelectSection: (Int) -> Unit,
    onNavigateHome: () -> Unit,
    onLockAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MoodDarkCard)
            .border(1.dp, MoodGlassBorder, RoundedCornerShape(0.dp))
            .padding(16.dp)
    ) {
        // Logo & Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 20.dp, top = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(MoodAmberPrimary, MoodAmberDark)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("☕", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "كافيه مَزاج",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = MoodAmberPrimary
                )
                Text(
                    text = "لوحة الإدارة المتكاملة",
                    fontSize = 11.sp,
                    color = MoodMutedText
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MoodGlassBorder)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Items
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            sections.forEach { sec ->
                val isSelected = selectedSection == sec.id
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isSelected) MoodAmberPrimary.copy(alpha = 0.2f) else Color.Transparent
                        )
                        .border(
                            1.dp,
                            if (isSelected) MoodAmberPrimary.copy(alpha = 0.6f) else Color.Transparent,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { onSelectSection(sec.id) }
                        .padding(horizontal = 14.dp, vertical = 11.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = sec.icon,
                                contentDescription = sec.titleAr,
                                tint = if (isSelected) MoodAmberPrimary else MoodMutedText,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = sec.titleAr,
                                color = if (isSelected) MoodCreamText else MoodMutedText,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                            )
                        }

                        if (sec.badgeCount > 0) {
                            GlowBadge(text = "${sec.badgeCount}", isTeal = false)
                        }
                    }
                }
            }
        }

        // Bottom Actions: Back to Store & Lock
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MoodGlassBorder)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MoodDarkCardElevated)
                .clickable(onClick = onNavigateHome)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Storefront, contentDescription = null, tint = MoodTealGlow, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("العودة للمنيو الرئيسي", fontSize = 12.sp, color = MoodCreamText, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFF5252).copy(alpha = 0.15f))
                .border(1.dp, Color(0xFFFF5252).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clickable(onClick = onLockAdmin)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("قفل لوحة الإدارة 🔒", fontSize = 12.sp, color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AdminSectionContent(
    selectedIndex: Int,
    viewModel: MoodCafeViewModel,
    allOrders: List<com.example.data.model.OrderEntity>,
    allProducts: List<com.example.data.model.ProductEntity>,
    allCategories: List<com.example.data.model.CategoryEntity>,
    allUsers: List<com.example.data.model.UserEntity>,
    cafeSettings: com.example.data.model.CafeSettingsEntity,
    onNavigateToSection: (Int) -> Unit
) {
    AnimatedContent(
        targetState = selectedIndex,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "AdminSectionsTransition"
    ) { sectionIndex ->
        when (sectionIndex) {
            0 -> AdminOverviewSection(
                orders = allOrders,
                products = allProducts,
                users = allUsers,
                settings = cafeSettings,
                onNavigateToSection = onNavigateToSection,
                onUpdateStoreStatus = { status ->
                    viewModel.saveSettings(cafeSettings.copy(storeStatus = status))
                },
                onQuickUpdateOrderStatus = { id, st, code ->
                    viewModel.updateOrderStatus(id, st, code)
                }
            )
            1 -> AdminProductsSection(
                products = allProducts,
                categories = allCategories,
                onAddProduct = { viewModel.addProduct(it) },
                onUpdateProduct = { viewModel.updateProduct(it) },
                onDeleteProduct = { viewModel.deleteProduct(it) }
            )
            2 -> AdminCategoriesSection(
                categories = allCategories,
                products = allProducts,
                onAddCategory = { titleAr, titleEn, emoji ->
                    viewModel.addCategory(titleAr, titleEn, emoji)
                },
                onUpdateCategory = { viewModel.updateCategory(it) },
                onDeleteCategory = { viewModel.deleteCategory(it) },
                onToggleVisibility = { viewModel.toggleCategoryVisibility(it) },
                onReorderCategory = { cat, moveUp -> viewModel.reorderCategory(cat, moveUp) }
            )
            3 -> AdminOrdersSection(
                orders = allOrders,
                onUpdateStatus = { id, st, code ->
                    viewModel.updateOrderStatus(id, st, code)
                }
            )
            4 -> AdminUsersSection(
                users = allUsers,
                orders = allOrders,
                onToggleRole = { viewModel.toggleUserRole(it) }
            )
            5 -> AdminSettingsSection(
                settings = cafeSettings,
                onSaveSettings = { viewModel.saveSettings(it) }
            )
            6 -> AdminThemeSection(
                settings = cafeSettings,
                onSaveTheme = { pri, sec, glow, bg, rad, sty, mot ->
                    viewModel.updateThemeColors(pri, sec, glow, bg, rad, sty, mot)
                }
            )
        }
    }
}
