package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ProductCategory(val titleAr: String, val titleEn: String, val icon: String) {
    ALL("الكل", "All", "✨"),
    HOT_COFFEE("قهوة ساخنة", "Hot Coffee", "☕"),
    TEA_HERBS("شاي وأعشاب", "Tea & Herbs", "🍵"),
    SPECIAL("مشروبات خاصة", "Special Drinks", "⭐"),
    COLD("مشروبات باردة", "Cold Drinks", "🧊"),
    JUICE("عصائر وسموزي", "Juice & Smoothie", "🥤"),
    DESSERT("حلى ومخبوزات", "Desserts", "🍰")
}

enum class ProductBadge(val textAr: String, val textEn: String) {
    NONE("", ""),
    POPULAR("الأكثر طلباً", "POPULAR"),
    BEST_SELLER("الأعلى مبيعاً", "BEST SELLER"),
    SPECIAL("مميز", "SPECIAL"),
    NEW("جديد", "NEW")
}

enum class OrderStatus(val textAr: String, val textEn: String, val colorHex: Long) {
    PENDING("قيد الانتظار", "Pending", 0xFFFFC107),
    PREPARING("جاري التحضير", "Preparing", 0xFF2196F3),
    READY("جاهز للاستلام", "Ready", 0xFF4CAF50),
    COMPLETED("تم التسليم", "Completed", 0xFF9E9E9E),
    CANCELLED("ملغي", "Cancelled", 0xFFE91E63)
}

enum class UserRole {
    USER,
    ADMIN
}

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameAr: String,
    val nameEn: String,
    val description: String,
    val price: Double,
    val category: String, // from ProductCategory name
    val badge: String = ProductBadge.NONE.name,
    val imageResName: String = "",
    val isAvailable: Boolean = true,
    val rating: Double = 4.9,
    val isCustom: Boolean = false
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderCode: String,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String = "",
    val itemsSummary: String,
    val totalAmount: Double,
    val status: String = OrderStatus.PENDING.name,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val role: String = UserRole.USER.name,
    val avatarLetter: String = "م",
    val joinedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites", primaryKeys = ["productId", "userEmail"])
data class FavoriteEntity(
    val productId: Long,
    val userEmail: String
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String, // e.g. "HOT_COFFEE", "TEA_HERBS", etc.
    val titleAr: String,
    val titleEn: String,
    val iconEmoji: String = "☕",
    val sortOrder: Int = 0,
    val isVisible: Boolean = true
)

@Entity(tableName = "settings")
data class CafeSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val cafeNameAr: String = "مزاج",
    val cafeNameEn: String = "Mood",
    val heroTitle: String = "اشرب قهوتك على مزاجك",
    val heroTagline: String = "Drink Your Coffee With The Legends",
    val whatsappNumber: String = "201283073813",
    val phoneNumber: String = "+20 128 307 3813",
    val locationAddress: String = "كفر الشيخ - شارع النبوي المهندس",
    val workingHours: String = "يومياً من 5:00 عصراً حتى 2:00 صباحاً",
    val instagramUrl: String = "https://instagram.com/moodcafe",
    val facebookUrl: String = "https://facebook.com/moodcafe",
    val mapsUrl: String = "https://maps.google.com",
    val adminPin: String = "7777",
    val primaryGoldHex: String = "#F59E0B",
    val accentTealHex: String = "#38BDF8",
    val glowColorHex: String = "#FBBF24",
    val backgroundDarkHex: String = "#050402",
    val buttonCornerRadiusDp: Int = 16,
    val buttonStyle: String = "GRADIENT", // GRADIENT, SOLID, OUTLINE
    val motionEnabled: Boolean = true,
    val storeStatus: String = "OPEN", // OPEN, BUSY, CLOSED
    val apiBaseUrl: String = "",
    val googleClientId: String = "",
    val enableGoogleLogin: Boolean = false,
    val isLiteMode: Boolean = false,
    val isSoundEnabled: Boolean = true
)

data class AppNotification(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = "ORDER" // "ORDER", "STATUS", "SYSTEM"
)

data class CartItem(
    val product: ProductEntity,
    var quantity: Int = 1,
    val selectedSize: String = "وسط (Medium)",
    val selectedAddons: List<String> = emptyList(),
    val sweetnessLevel: String = "مضبوط",
    val milkType: String = "عادي",
    val iceLevel: String = "معتدل",
    val notes: String = ""
) {
    val unitPrice: Double
        get() {
            var p = product.price
            if (selectedSize.contains("كبير") || selectedSize.contains("Large")) p += 10.0
            p += selectedAddons.size * 5.0
            return p
        }

    val totalPrice: Double
        get() = unitPrice * quantity
}

data class LegendArtist(
    val nameAr: String,
    val nameEn: String,
    val title: String,
    val quote: String,
    val tag: String,
    val iconEmoji: String
)
