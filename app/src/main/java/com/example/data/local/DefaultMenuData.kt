package com.example.data.local

import com.example.data.model.CafeSettingsEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.LegendArtist
import com.example.data.model.ProductBadge
import com.example.data.model.ProductCategory
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole

object DefaultMenuData {
    val initialCategories = listOf(
        CategoryEntity(id = 1, code = "HOT_COFFEE", titleAr = "قهوة ساخنة", titleEn = "Hot Coffee", iconEmoji = "☕", sortOrder = 1, isVisible = true),
        CategoryEntity(id = 2, code = "TEA_HERBS", titleAr = "شاي وأعشاب", titleEn = "Tea & Herbs", iconEmoji = "🍵", sortOrder = 2, isVisible = true),
        CategoryEntity(id = 3, code = "SPECIAL", titleAr = "مشروبات خاصة", titleEn = "Special Drinks", iconEmoji = "⭐", sortOrder = 3, isVisible = true),
        CategoryEntity(id = 4, code = "COLD", titleAr = "مشروبات باردة", titleEn = "Cold Drinks", iconEmoji = "🧊", sortOrder = 4, isVisible = true),
        CategoryEntity(id = 5, code = "JUICE", titleAr = "عصائر وسموزي", titleEn = "Juice & Smoothie", iconEmoji = "🥤", sortOrder = 5, isVisible = true),
        CategoryEntity(id = 6, code = "DESSERT", titleAr = "حلى ومخبوزات", titleEn = "Desserts", iconEmoji = "🍰", sortOrder = 6, isVisible = true)
    )

    val initialProducts = listOf(
        // Hot Coffee (1-10)
        ProductEntity(
            id = 1,
            nameAr = "قهوة تركي",
            nameEn = "Turkish Coffee",
            description = "سادة - مظبوط - زيادة - على الريحة، ببن فاخر ومطحون طازج على الرمالة",
            price = 30.0,
            category = ProductCategory.HOT_COFFEE.name,
            badge = ProductBadge.POPULAR.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 2,
            nameAr = "اسبريسو",
            nameEn = "Espresso",
            description = "شوت قهوة مركز وقوي مع كريما ذهبية غنية ونكهة عميقة",
            price = 40.0,
            category = ProductCategory.HOT_COFFEE.name,
            badge = ProductBadge.BEST_SELLER.name,
            rating = 4.8
        ),
        ProductEntity(
            id = 3,
            nameAr = "امريكان كوفي",
            nameEn = "Americano",
            description = "اسبريسو أصلي + ماء ساخن نقي لتجربة سلسة وممتدة",
            price = 45.0,
            category = ProductCategory.HOT_COFFEE.name,
            badge = ProductBadge.NONE.name,
            rating = 4.7
        ),
        ProductEntity(
            id = 4,
            nameAr = "لاتيه",
            nameEn = "Latte",
            description = "اسبريسو غني + حليب مبخر كريمي مع رسمة آرت مميزة",
            price = 55.0,
            category = ProductCategory.HOT_COFFEE.name,
            badge = ProductBadge.POPULAR.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 5,
            nameAr = "كابتشينو",
            nameEn = "Cappuccino",
            description = "اسبريسو إيطالي + حليب متوازن + طبقة فوم كثيفة وناعمة",
            price = 55.0,
            category = ProductCategory.HOT_COFFEE.name,
            badge = ProductBadge.BEST_SELLER.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 6,
            nameAr = "موكا",
            nameEn = "Mocha",
            description = "اسبريسو + صوص شوكولاتة بلجيكية غنية + حليب مبخر",
            price = 60.0,
            category = ProductCategory.HOT_COFFEE.name,
            badge = ProductBadge.SPECIAL.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 7,
            nameAr = "فلات وايت",
            nameEn = "Flat White",
            description = "دبل شوت اسبريسو مع مايكروفوم حريري وقوام مركز",
            price = 60.0,
            category = ProductCategory.HOT_COFFEE.name,
            badge = ProductBadge.SPECIAL.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 8,
            nameAr = "ماكياتو",
            nameEn = "Macchiato",
            description = "شوت اسبريسو مع لمسة فوم حليب مخملي",
            price = 50.0,
            category = ProductCategory.HOT_COFFEE.name,
            badge = ProductBadge.NONE.name,
            rating = 4.7
        ),
        ProductEntity(
            id = 9,
            nameAr = "قهوة عربي",
            nameEn = "Arabic Coffee",
            description = "بالهيل والزعفران الفاخر، تقدم مع حبات التمر الأصيل",
            price = 35.0,
            category = ProductCategory.HOT_COFFEE.name,
            badge = ProductBadge.SPECIAL.name,
            rating = 5.0
        ),
        ProductEntity(
            id = 10,
            nameAr = "قهوة فرنساوي",
            nameEn = "French Coffee",
            description = "بالبندق المحمص أو الكراميل مع الحليب الطبيعي",
            price = 45.0,
            category = ProductCategory.HOT_COFFEE.name,
            badge = ProductBadge.POPULAR.name,
            rating = 4.8
        ),

        // Tea & Herbs (11-15)
        ProductEntity(
            id = 11,
            nameAr = "شاي بلبن",
            nameEn = "Tea with Milk",
            description = "شاي كرك مضبوط ومغلي على الفحم مع بهارات دافئة",
            price = 25.0,
            category = ProductCategory.TEA_HERBS.name,
            badge = ProductBadge.POPULAR.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 12,
            nameAr = "شاي اخضر",
            nameEn = "Green Tea",
            description = "شاي أخضر نقي بالنعناع الطازج أو شرائح الليمون",
            price = 20.0,
            category = ProductCategory.TEA_HERBS.name,
            badge = ProductBadge.NONE.name,
            rating = 4.6
        ),
        ProductEntity(
            id = 13,
            nameAr = "ينسون",
            nameEn = "Anise",
            description = "أعشاب ينسون طبيعية مهدئة ومريحة",
            price = 20.0,
            category = ProductCategory.TEA_HERBS.name,
            badge = ProductBadge.NONE.name,
            rating = 4.6
        ),
        ProductEntity(
            id = 14,
            nameAr = "كركديه سخن",
            nameEn = "Hot Hibiscus",
            description = "كركديه أسواني طبيعي ساخن غني بمضادات الأكسدة",
            price = 25.0,
            category = ProductCategory.TEA_HERBS.name,
            badge = ProductBadge.NONE.name,
            rating = 4.8
        ),
        ProductEntity(
            id = 15,
            nameAr = "قرفة باللبن",
            nameEn = "Cinnamon Milk",
            description = "قرفة سيلانية فاخرة + حليب ساخن + رشة مكسرات محمصة",
            price = 25.0,
            category = ProductCategory.TEA_HERBS.name,
            badge = ProductBadge.SPECIAL.name,
            rating = 4.8
        ),

        // Special Drinks (16-20)
        ProductEntity(
            id = 16,
            nameAr = "هوت شوكليت",
            nameEn = "Hot Chocolate",
            description = "شوكولاتة ساخنة كريمية فاخرة مع طبقة كريمة ورشة كاكاو",
            price = 50.0,
            category = ProductCategory.SPECIAL.name,
            badge = ProductBadge.POPULAR.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 17,
            nameAr = "سحلب",
            nameEn = "Sahlab",
            description = "سحلب بلدي بالمكسرات المحمصة وجوز الهند والقرفة العطرية",
            price = 40.0,
            category = ProductCategory.SPECIAL.name,
            badge = ProductBadge.BEST_SELLER.name,
            rating = 5.0
        ),
        ProductEntity(
            id = 18,
            nameAr = "سبانيش لاتيه",
            nameEn = "Spanish Latte",
            description = "لاتيه مميز بالحليب المكثف المحلى ونكهة اسبريسو مخملية",
            price = 65.0,
            category = ProductCategory.SPECIAL.name,
            badge = ProductBadge.BEST_SELLER.name,
            rating = 5.0
        ),
        ProductEntity(
            id = 19,
            nameAr = "بيستاشيو لاتيه",
            nameEn = "Pistachio Latte",
            description = "اسبريسو فاخر + صوص فستق طبيعي + حليب كريمي ناعم",
            price = 70.0,
            category = ProductCategory.SPECIAL.name,
            badge = ProductBadge.NEW.name,
            rating = 5.0
        ),
        ProductEntity(
            id = 20,
            nameAr = "كوفي لافيرز",
            nameEn = "Coffee Lovers",
            description = "مزيج حصري من القوة والشوكولاتة والكريمة المخفوقة",
            price = 65.0,
            category = ProductCategory.SPECIAL.name,
            badge = ProductBadge.SPECIAL.name,
            rating = 4.9
        ),

        // Cold Drinks (21-25)
        ProductEntity(
            id = 21,
            nameAr = "ايس كوفي",
            nameEn = "Iced Coffee",
            description = "قهوة مثلجة منعشة مع حليب بارد ونكهة سكر خفيفة",
            price = 55.0,
            category = ProductCategory.COLD.name,
            badge = ProductBadge.POPULAR.name,
            rating = 4.8
        ),
        ProductEntity(
            id = 22,
            nameAr = "ايس لاتيه",
            nameEn = "Iced Latte",
            description = "لاتيه مثلج بارد ومنعش مع طبقات الحليب والاسبريسو",
            price = 60.0,
            category = ProductCategory.COLD.name,
            badge = ProductBadge.BEST_SELLER.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 23,
            nameAr = "فرابتشينو",
            nameEn = "Frappuccino",
            description = "مخفوق قهوة مثلجة ومجروشة مع الكريمة وصوص الشوكولاتة",
            price = 70.0,
            category = ProductCategory.COLD.name,
            badge = ProductBadge.POPULAR.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 24,
            nameAr = "موهيتو",
            nameEn = "Mojito",
            description = "نعناع طازج + شرائح ليمون + سيرب التوت أو البلو + صودا مثلجة",
            price = 45.0,
            category = ProductCategory.COLD.name,
            badge = ProductBadge.POPULAR.name,
            rating = 4.8
        ),
        ProductEntity(
            id = 25,
            nameAr = "كركديه ساقع",
            nameEn = "Iced Hibiscus",
            description = "كركديه مثلج منعش مع مكعبات الثلج ورشة ماء ورد",
            price = 25.0,
            category = ProductCategory.COLD.name,
            badge = ProductBadge.NONE.name,
            rating = 4.7
        ),

        // Juices & Smoothies (26-30)
        ProductEntity(
            id = 26,
            nameAr = "سموزي فراولة",
            nameEn = "Strawberry Smoothie",
            description = "فريش وطبيعي 100% مع قطع الفراولة الطازجة",
            price = 65.0,
            category = ProductCategory.JUICE.name,
            badge = ProductBadge.BEST_SELLER.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 27,
            nameAr = "سموزي مانجو",
            nameEn = "Mango Smoothie",
            description = "مانجو طبيعية مثلجة مع قوام سموثي غني وزبادي خفيف",
            price = 65.0,
            category = ProductCategory.JUICE.name,
            badge = ProductBadge.POPULAR.name,
            rating = 5.0
        ),
        ProductEntity(
            id = 28,
            nameAr = "ليمون بالنعناع",
            nameEn = "Lemon Mint",
            description = "ليمون طازج معصور مع أوراق النعناع وثلج مجروش",
            price = 30.0,
            category = ProductCategory.JUICE.name,
            badge = ProductBadge.BEST_SELLER.name,
            rating = 4.9
        ),
        ProductEntity(
            id = 29,
            nameAr = "عصير برتقال",
            nameEn = "Orange Juice",
            description = "برتقال طبيعي 100% معصور طازج عند الطلب بدون سكر",
            price = 35.0,
            category = ProductCategory.JUICE.name,
            badge = ProductBadge.NONE.name,
            rating = 4.8
        ),
        ProductEntity(
            id = 30,
            nameAr = "مياه معدنية",
            nameEn = "Mineral Water",
            description = "مياه معدنية نقية وباردة 500 مل",
            price = 10.0,
            category = ProductCategory.COLD.name,
            badge = ProductBadge.NONE.name,
            rating = 4.5
        )
    )

    val legends = listOf(
        LegendArtist(
            nameAr = "أم كلثوم",
            nameEn = "Umm Kulthum",
            title = "كوكب الشرق",
            quote = "وعايزنا نرجع زي زمان.. قول للزمان ارجع يا زمان",
            tag = "الست",
            iconEmoji = "👑"
        ),
        LegendArtist(
            nameAr = "عبد الحليم حافظ",
            nameEn = "Abdel Halim Hafez",
            title = "العندليب الأسمر",
            quote = "على حسب وداد قلبي يا بوي.. راح أقول لعينيك أمانة",
            tag = "العندليب",
            iconEmoji = "🕊️"
        ),
        LegendArtist(
            nameAr = "جورج وسوف",
            nameEn = "George Wassouf",
            title = "سلطان الطرب",
            quote = "طبيب جراح.. قلوب الناس أداويها وياما جراح سهرت الليل أداريها",
            tag = "أبو وديع",
            iconEmoji = "🎙️"
        ),
        LegendArtist(
            nameAr = "فيروز",
            nameEn = "Fairuz",
            title = "جارة القمر",
            quote = "قهوة ع المفرق.. وطريق مودي لبعيد",
            tag = "الصباح",
            iconEmoji = "🌙"
        )
    )

    val defaultSettings = CafeSettingsEntity()

    val demoUsers = listOf(
        UserEntity(
            id = 1,
            name = "مدير كافيه مزاج",
            email = "admin@mood.cafe",
            phone = "01283073813",
            role = UserRole.ADMIN.name,
            avatarLetter = "م"
        ),
        UserEntity(
            id = 2,
            name = "ضيف مزاج الدائم",
            email = "guest@mood.cafe",
            phone = "01000000000",
            role = UserRole.USER.name,
            avatarLetter = "ض"
        )
    )
}
