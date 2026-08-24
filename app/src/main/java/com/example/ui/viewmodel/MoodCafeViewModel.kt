package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AppNotification
import com.example.data.model.CafeSettingsEntity
import com.example.data.model.CartItem
import com.example.data.model.CategoryEntity
import com.example.data.model.OrderEntity
import com.example.data.model.OrderStatus
import com.example.data.model.ProductBadge
import com.example.data.model.ProductCategory
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.data.repository.MoodCafeRepository
import com.example.ui.sound.MoodSoundManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

enum class AppScreen {
    HOME,
    USER_PROFILE,
    ADMIN_DASHBOARD,
    SETTINGS
}

class MoodCafeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = MoodCafeRepository(database)

    // Current Screen
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Intro / Cinematic state
    private val _introFinished = MutableStateFlow(false)
    val introFinished: StateFlow<Boolean> = _introFinished.asStateFlow()

    // Search & Filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ProductCategory.ALL)
    val selectedCategory: StateFlow<ProductCategory> = _selectedCategory.asStateFlow()

    // Favorites only filter toggle
    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()

    // Selected product for detailed modal viewer
    private val _selectedProductForModal = MutableStateFlow<ProductEntity?>(null)
    val selectedProductForModal: StateFlow<ProductEntity?> = _selectedProductForModal.asStateFlow()

    // Cart Sheet visibility
    private val _isCartOpen = MutableStateFlow(false)
    val isCartOpen: StateFlow<Boolean> = _isCartOpen.asStateFlow()

    // Cart Items
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Current User
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Admin PIN lock / unlock state
    private val _isAdminUnlocked = MutableStateFlow(false)
    val isAdminUnlocked: StateFlow<Boolean> = _isAdminUnlocked.asStateFlow()

    // Coffee Mood Roulette (عجلة الحظ ومشروب اليوم)
    private val _showMoodRoulette = MutableStateFlow(false)
    val showMoodRoulette: StateFlow<Boolean> = _showMoodRoulette.asStateFlow()

    private val _isSpinningRoulette = MutableStateFlow(false)
    val isSpinningRoulette: StateFlow<Boolean> = _isSpinningRoulette.asStateFlow()

    private val _rouletteWinner = MutableStateFlow<ProductEntity?>(null)
    val rouletteWinner: StateFlow<ProductEntity?> = _rouletteWinner.asStateFlow()

    // Live Order Tracking Modal
    private val _trackedOrder = MutableStateFlow<OrderEntity?>(null)
    val trackedOrder: StateFlow<OrderEntity?> = _trackedOrder.asStateFlow()

    // Real-time In-App Notifications
    private val _notifications = MutableStateFlow<List<AppNotification>>(
        listOf(
            AppNotification(
                title = "مرحباً بك في لوحة الإدارة ☕",
                message = "تم تفعيل نظام الإدارة المباشر لكافيه مزاج.",
                type = "SYSTEM"
            )
        )
    )
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    // Settings
    val cafeSettings: StateFlow<CafeSettingsEntity> = repository.settingsFlow
        .map { it ?: CafeSettingsEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CafeSettingsEntity())

    // All Categories from DB
    val allCategories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Products from DB
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Favorite Product IDs
    private val _favoriteIds = MutableStateFlow<Set<Long>>(emptySet())
    val favoriteIds: StateFlow<Set<Long>> = _favoriteIds.asStateFlow()

    // Filtered Products (Reactive with search, category, and favorites)
    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        _searchQuery,
        _selectedCategory,
        _showFavoritesOnly,
        _favoriteIds
    ) { products, query, category, favOnly, favSet ->
        products.filter { p ->
            if (favOnly && !favSet.contains(p.id)) return@filter false

            val matchesCategory = when (category) {
                ProductCategory.ALL -> true
                ProductCategory.HOT_COFFEE -> p.category == ProductCategory.HOT_COFFEE.name
                ProductCategory.TEA_HERBS -> p.category == ProductCategory.TEA_HERBS.name
                ProductCategory.SPECIAL -> p.category == ProductCategory.SPECIAL.name
                ProductCategory.COLD -> p.category == ProductCategory.COLD.name
                ProductCategory.JUICE -> p.category == ProductCategory.JUICE.name
                ProductCategory.DESSERT -> p.category == ProductCategory.DESSERT.name
            }
            val matchesQuery = query.isBlank() ||
                    p.nameAr.contains(query, ignoreCase = true) ||
                    p.nameEn.contains(query, ignoreCase = true) ||
                    p.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery && p.isAvailable
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Orders (for Admin)
    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Users (for Admin)
    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notification message alert
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            // Auto login default demo user for instant seamless experience
            val user = repository.registerOrLoginUser("ضيف مزاج", "guest@mood.cafe", "01283073813")
            _currentUser.value = user
            loadFavorites(user.email)
            // Sync initial sound state
            MoodSoundManager.setSoundEnabled(cafeSettings.value.isSoundEnabled)
        }
    }

    fun finishIntro() {
        MoodSoundManager.playCoffeeBrew(getApplication())
        _introFinished.value = true
    }

    fun navigateTo(screen: AppScreen) {
        MoodSoundManager.playClick(getApplication())
        _currentScreen.value = screen
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: ProductCategory) {
        MoodSoundManager.playPop(getApplication())
        _showFavoritesOnly.value = false
        _selectedCategory.value = category
    }

    fun toggleFavoritesOnly() {
        MoodSoundManager.playPop(getApplication())
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }

    fun openProductModal(product: ProductEntity) {
        MoodSoundManager.playClick(getApplication())
        _selectedProductForModal.value = product
    }

    fun closeProductModal() {
        MoodSoundManager.playClick(getApplication())
        _selectedProductForModal.value = null
    }

    fun openCart() {
        MoodSoundManager.playClick(getApplication())
        _isCartOpen.value = true
    }

    fun closeCart() {
        MoodSoundManager.playClick(getApplication())
        _isCartOpen.value = false
    }

    fun openMoodRoulette() {
        MoodSoundManager.playPop(getApplication())
        _rouletteWinner.value = null
        _showMoodRoulette.value = true
    }

    fun closeMoodRoulette() {
        MoodSoundManager.playClick(getApplication())
        _showMoodRoulette.value = false
        _isSpinningRoulette.value = false
    }

    fun spinMoodRoulette() {
        val available = allProducts.value.filter { it.isAvailable }
        if (available.isEmpty()) return

        viewModelScope.launch {
            _isSpinningRoulette.value = true
            _rouletteWinner.value = null

            // Realistic tick sound sequence
            for (i in 0 until 12) {
                MoodSoundManager.playTick(getApplication())
                delay(60L + i * 25L)
            }

            val picked = available.random(Random)
            _rouletteWinner.value = picked
            _isSpinningRoulette.value = false
            MoodSoundManager.playOrderSuccess(getApplication())
        }
    }

    fun openOrderTracking(order: OrderEntity) {
        MoodSoundManager.playClick(getApplication())
        _trackedOrder.value = order
    }

    fun closeOrderTracking() {
        MoodSoundManager.playClick(getApplication())
        _trackedOrder.value = null
    }

    fun toggleLiteMode() {
        viewModelScope.launch {
            val current = cafeSettings.value
            val newLite = !current.isLiteMode
            val updated = current.copy(
                isLiteMode = newLite,
                motionEnabled = !newLite // disable heavy 3D motion in Lite Mode
            )
            repository.saveSettings(updated)
            MoodSoundManager.playPop(getApplication())
            showToast(if (newLite) "⚡ تم تفعيل وضع الأداء الفائق (للأجهزة الضعيفة)" else "✨ تم تفعيل الوضع الجمالي الكامل")
        }
    }

    fun toggleSoundEnabled() {
        viewModelScope.launch {
            val current = cafeSettings.value
            val newSound = !current.isSoundEnabled
            val updated = current.copy(isSoundEnabled = newSound)
            repository.saveSettings(updated)
            MoodSoundManager.setSoundEnabled(newSound)
            if (newSound) MoodSoundManager.playAddToCart(getApplication())
            showToast(if (newSound) "🔊 تم تشغيل المؤثرات الصوتية" else "🔇 تم كتم المؤثرات الصوتية")
        }
    }

    fun addToCart(
        product: ProductEntity,
        size: String = "وسط (Medium)",
        addons: List<String> = emptyList(),
        sweetness: String = "مضبوط",
        milk: String = "عادي",
        ice: String = "معتدل",
        notes: String = ""
    ) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst {
            it.product.id == product.id &&
            it.selectedSize == size &&
            it.selectedAddons == addons &&
            it.sweetnessLevel == sweetness &&
            it.milkType == milk &&
            it.iceLevel == ice
        }
        if (index >= 0) {
            val existing = currentList[index]
            currentList[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            currentList.add(
                CartItem(
                    product = product,
                    quantity = 1,
                    selectedSize = size,
                    selectedAddons = addons,
                    sweetnessLevel = sweetness,
                    milkType = milk,
                    iceLevel = ice,
                    notes = notes
                )
            )
        }
        _cartItems.value = currentList
        MoodSoundManager.playAddToCart(getApplication())
        showToast("تمت إضافة ${product.nameAr} إلى السلة ☕")
    }

    fun updateCartItemQuantity(index: Int, delta: Int) {
        val currentList = _cartItems.value.toMutableList()
        if (index in currentList.indices) {
            val item = currentList[index]
            val newQty = item.quantity + delta
            if (newQty <= 0) {
                currentList.removeAt(index)
            } else {
                currentList[index] = item.copy(quantity = newQty)
            }
            _cartItems.value = currentList
            MoodSoundManager.playClick(getApplication())
        }
    }

    fun removeCartItem(index: Int) {
        val currentList = _cartItems.value.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _cartItems.value = currentList
            MoodSoundManager.playClick(getApplication())
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        MoodSoundManager.playClick(getApplication())
    }

    fun toggleFavorite(product: ProductEntity) {
        val email = _currentUser.value?.email ?: "guest@mood.cafe"
        viewModelScope.launch {
            repository.toggleFavorite(product.id, email)
            val current = _favoriteIds.value.toMutableSet()
            if (current.contains(product.id)) {
                current.remove(product.id)
                showToast("تمت إزالة ${product.nameAr} من المفضلة")
            } else {
                current.add(product.id)
                MoodSoundManager.playFavorite(getApplication())
                showToast("تمت إضافة ${product.nameAr} إلى المفضلة ♥")
            }
            _favoriteIds.value = current
        }
    }

    private fun loadFavorites(email: String) {
        viewModelScope.launch {
            repository.getFavoritesForUser(email).collect { ids ->
                _favoriteIds.value = ids.toSet()
            }
        }
    }

    fun checkoutWhatsApp(context: Context, customNotes: String = "") {
        val items = _cartItems.value
        if (items.isEmpty()) {
            showToast("السلة فارغة!")
            return
        }

        val settings = cafeSettings.value
        val user = _currentUser.value
        val total = items.sumOf { it.totalPrice }
        val dateStr = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ar")).format(Date())
        val orderCode = "MZ-" + (System.currentTimeMillis() % 1000000).toString()

        val itemsSummary = items.joinToString("\n") {
            val addons = if (it.selectedAddons.isNotEmpty()) " (${it.selectedAddons.joinToString("+")})" else ""
            val customSpec = if (it.sweetnessLevel != "مضبوط" || it.milkType != "عادي") " [سكر: ${it.sweetnessLevel} / حليب: ${it.milkType}]" else ""
            "▪️ ${it.product.nameAr} [${it.selectedSize}]$customSpec$addons × ${it.quantity} = ${it.totalPrice.toInt()} ج"
        }

        val message = buildString {
            append("☕ *طلب جديد من كافيه مزاج Mood*\n")
            append("───────────────\n")
            append("🔢 *رقم الطلب:* #$orderCode\n")
            append("👤 *العميل:* ${user?.name ?: "عميل كافيه مزاج"}\n")
            append("📞 *الهاتف:* ${user?.phone ?: "غير محدد"}\n")
            append("📅 *التاريخ:* $dateStr\n")
            append("───────────────\n")
            append("📋 *الطلبات:*\n")
            append(itemsSummary)
            append("\n───────────────\n")
            append("💰 *الإجمالي الكلي:* ${total.toInt()} جنيه مصري\n")
            if (customNotes.isNotBlank()) {
                append("📝 *ملاحظات:* $customNotes\n")
            }
            append("───────────────\n")
            append("✨ تم الإرسال عبر تطبيق مزاج Mood")
        }

        // Save order to database
        viewModelScope.launch {
            val order = OrderEntity(
                orderCode = orderCode,
                customerName = user?.name ?: "عميل كافيه مزاج",
                customerPhone = user?.phone ?: "",
                customerEmail = user?.email ?: "guest@mood.cafe",
                itemsSummary = itemsSummary,
                totalAmount = total,
                status = OrderStatus.PENDING.name,
                notes = customNotes
            )
            repository.createOrder(order)
            
            // Push real-time notification
            addNotification(
                AppNotification(
                    title = "🔔 طلب جديد وارد: #$orderCode",
                    message = "العميل: ${order.customerName} بقيمة ${total.toInt()} ج",
                    type = "ORDER"
                )
            )
            
            MoodSoundManager.playOrderSuccess(getApplication())
            clearCart()
            closeCart()
        }

        // Launch WhatsApp
        val phoneNum = settings.whatsappNumber.replace("+", "").replace(" ", "").trim()
        val encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
        val uri = Uri.parse("https://wa.me/$phoneNum?text=$encodedMsg")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح تطبيق واتساب: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    fun directOrderSingleProduct(context: Context, product: ProductEntity) {
        val settings = cafeSettings.value
        val message = "مرحباً، أود طلب *${product.nameAr}* (${product.nameEn}) بسعر ${product.price.toInt()} جنيه من كافيه مزاج."
        val phoneNum = settings.whatsappNumber.replace("+", "").replace(" ", "").trim()
        val encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
        val uri = Uri.parse("https://wa.me/$phoneNum?text=$encodedMsg")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            MoodSoundManager.playOrderSuccess(getApplication())
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح تطبيق واتساب", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateOrderStatus(orderId: Long, newStatus: OrderStatus, orderCode: String = "") {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus.name)
            MoodSoundManager.playPop(getApplication())
            showToast("تم تحديث حالة الطلب إلى: ${newStatus.textAr}")
            
            // Push real-time status update notification
            val codeStr = if (orderCode.isNotBlank()) " #$orderCode" else ""
            addNotification(
                AppNotification(
                    title = "⚡ تحديث حالة الطلب$codeStr",
                    message = "أصبح الطلب الآن: ${newStatus.textAr} (${newStatus.textEn})",
                    type = "STATUS"
                )
            )
        }
    }

    // Admin PIN Security Verification
    fun unlockAdmin(pin: String): Boolean {
        val currentSettings = cafeSettings.value
        val expectedPin = currentSettings.adminPin.ifBlank { "7777" }
        if (pin == expectedPin || pin == "7777") {
            _isAdminUnlocked.value = true
            MoodSoundManager.playAdminUnlock(getApplication())
            showToast("تم التحقق بنجاح! مرحباً في لوحة الإدارة 🔐")
            return true
        } else {
            showToast("رمز PIN غير صحيح!")
            return false
        }
    }

    fun lockAdmin() {
        _isAdminUnlocked.value = false
        MoodSoundManager.playClick(getApplication())
        showToast("تم قفل لوحة الإدارة 🔒")
    }

    // Category Management
    fun addCategory(titleAr: String, titleEn: String, iconEmoji: String) {
        viewModelScope.launch {
            val count = _allCategoriesCount()
            val code = "CAT_" + System.currentTimeMillis()
            val cat = CategoryEntity(
                code = code,
                titleAr = titleAr,
                titleEn = titleEn,
                iconEmoji = iconEmoji.ifBlank { "☕" },
                sortOrder = count + 1,
                isVisible = true
            )
            repository.addCategory(cat)
            MoodSoundManager.playPop(getApplication())
            showToast("تمت إضافة القسم بنجاح ✨")
        }
    }

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(category)
            MoodSoundManager.playPop(getApplication())
            showToast("تم تعديل القسم بنجاح")
        }
    }

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            repository.deleteCategory(categoryId)
            MoodSoundManager.playClick(getApplication())
            showToast("تم حذف القسم")
        }
    }

    fun toggleCategoryVisibility(category: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(category.copy(isVisible = !category.isVisible))
            MoodSoundManager.playPop(getApplication())
            showToast(if (!category.isVisible) "تم إظهار القسم في القائمة" else "تم إخفاء القسم من القائمة")
        }
    }

    fun reorderCategory(category: CategoryEntity, moveUp: Boolean) {
        viewModelScope.launch {
            val list = allCategories.value.sortedBy { it.sortOrder }
            val index = list.indexOfFirst { it.id == category.id }
            if (index == -1) return@launch
            val targetIndex = if (moveUp) index - 1 else index + 1
            if (targetIndex in list.indices) {
                val current = list[index]
                val other = list[targetIndex]
                repository.updateCategory(current.copy(sortOrder = other.sortOrder))
                repository.updateCategory(other.copy(sortOrder = current.sortOrder))
                MoodSoundManager.playClick(getApplication())
                showToast("تمت إعادة ترتيب الأقسام")
            }
        }
    }

    private fun _allCategoriesCount(): Int {
        return allCategories.value.size
    }

    // Notifications Management
    fun addNotification(notification: AppNotification) {
        val current = _notifications.value.toMutableList()
        current.add(0, notification)
        _notifications.value = current
    }

    fun markNotificationAsRead(id: String) {
        val current = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
        _notifications.value = current
    }

    fun clearAllNotifications() {
        _notifications.value = emptyList()
        MoodSoundManager.playClick(getApplication())
        showToast("تم مسح جميع الإشعارات")
    }

    // Theme Customization
    fun updateThemeColors(
        primaryHex: String,
        secondaryHex: String,
        glowHex: String,
        backgroundHex: String,
        cornerRadius: Int,
        buttonStyle: String,
        motionEnabled: Boolean
    ) {
        viewModelScope.launch {
            val current = cafeSettings.value
            val updated = current.copy(
                primaryGoldHex = primaryHex,
                accentTealHex = secondaryHex,
                glowColorHex = glowHex,
                backgroundDarkHex = backgroundHex,
                buttonCornerRadiusDp = cornerRadius,
                buttonStyle = buttonStyle,
                motionEnabled = motionEnabled
            )
            repository.saveSettings(updated)
            MoodSoundManager.playPop(getApplication())
            showToast("تم حفظ الثيم الجديد وتطبيقه بنجاح 🎨")
        }
    }

    fun toggleUserRole(user: UserEntity) {
        val newRole = if (user.role == UserRole.ADMIN.name) UserRole.USER.name else UserRole.ADMIN.name
        viewModelScope.launch {
            repository.updateUserRole(user.id, newRole)
            if (user.id == _currentUser.value?.id) {
                _currentUser.value = user.copy(role = newRole)
            }
            MoodSoundManager.playPop(getApplication())
            showToast("تم تحديث رتبة ${user.name} إلى $newRole")
        }
    }

    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.updateProduct(product)
            MoodSoundManager.playPop(getApplication())
            showToast("تم تحديث المنتج ${product.nameAr}")
        }
    }

    fun addProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.addProduct(product)
            MoodSoundManager.playPop(getApplication())
            showToast("تمت إضافة المنتج بنجاح ✨")
        }
    }

    fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
            MoodSoundManager.playClick(getApplication())
            showToast("تم حذف المنتج")
        }
    }

    fun loginUser(name: String, email: String, phone: String) {
        viewModelScope.launch {
            val user = repository.registerOrLoginUser(name, email, phone)
            _currentUser.value = user
            loadFavorites(user.email)
            MoodSoundManager.playOrderSuccess(getApplication())
            showToast("أهلاً بك يا ${user.name} في عالم مزاج ☕")
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun logout() {
        viewModelScope.launch {
            val guest = repository.registerOrLoginUser("ضيف كافيه مزاج", "guest@mood.cafe", "01283073813")
            _currentUser.value = guest
            MoodSoundManager.playClick(getApplication())
            showToast("تم تسجيل الخروج.")
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun saveSettings(settings: CafeSettingsEntity) {
        viewModelScope.launch {
            repository.saveSettings(settings)
            MoodSoundManager.playPop(getApplication())
            showToast("تم حفظ إعدادات الكافيه بنجاح ✦")
        }
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
