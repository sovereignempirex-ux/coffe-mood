package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DefaultMenuData
import com.example.data.model.CartItem
import com.example.data.model.LegendArtist
import com.example.data.model.ProductBadge
import com.example.data.model.ProductCategory
import com.example.data.model.ProductEntity
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
import com.example.ui.theme.MoodGlassSurface
import com.example.ui.theme.MoodGlassSurfaceElevated
import com.example.ui.theme.MoodGoldDark
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodGoldSecondary
import com.example.ui.theme.MoodMutedText
import com.example.ui.theme.MoodTealGlow
import com.example.ui.theme.MoodTealNeon
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MoodCafeViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MoodCafeViewModel
) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val products by viewModel.filteredProducts.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val cafeSettings by viewModel.cafeSettings.collectAsState()
    val selectedProductForModal by viewModel.selectedProductForModal.collectAsState()
    val isCartOpen by viewModel.isCartOpen.collectAsState()

    var isSearchExpanded by remember { mutableStateOf(false) }

    // Intercept back presses when modals or sheets are open
    if (isCartOpen) {
        BackHandler { viewModel.closeCart() }
    } else if (selectedProductForModal != null) {
        BackHandler { viewModel.closeProductModal() }
    } else if (isSearchExpanded) {
        BackHandler { isSearchExpanded = false }
    }

    Scaffold(
        containerColor = MoodDarkInk,
        floatingActionButton = {
            if (cartItems.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { viewModel.openCart() },
                    containerColor = MoodGoldPrimary,
                    contentColor = MoodDarkInk,
                    shape = CircleShape,
                    modifier = Modifier.shadow(12.dp, CircleShape, spotColor = MoodGoldSecondary)
                ) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = Color(0xFFFF5252),
                                contentColor = Color.White
                            ) {
                                Text("${cartItems.sumOf { it.quantity }}")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "السلة",
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background 3D Canvas
            CafeWorld3DCanvas(motionEnabled = cafeSettings.motionEnabled)

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Top Navbar
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeTopBar(
                        cafeNameAr = cafeSettings.cafeNameAr,
                        cafeNameEn = cafeSettings.cafeNameEn,
                        userRole = currentUser?.role ?: UserRole.USER.name,
                        onSearchToggle = { isSearchExpanded = !isSearchExpanded },
                        onProfileClick = { viewModel.navigateTo(AppScreen.USER_PROFILE) },
                        onAdminClick = { viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD) }
                    )
                }

                // Search Bar (if expanded)
                if (isSearchExpanded) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text("ابحث عن مشروبك المفضل...", color = MoodMutedText) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = MoodGoldPrimary)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                        Icon(Icons.Default.Close, contentDescription = "مسح", tint = MoodCreamText)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MoodGoldPrimary,
                                unfocusedBorderColor = MoodGlassBorder,
                                focusedTextColor = MoodCreamText,
                                unfocusedTextColor = MoodCreamText,
                                focusedContainerColor = MoodDarkCard,
                                unfocusedContainerColor = MoodDarkCard
                            ),
                            singleLine = true
                        )
                    }
                }

                // 2. Cinematic Hero Section
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeHeroSection(
                        title = cafeSettings.heroTitle,
                        tagline = cafeSettings.heroTagline,
                        onExploreMenu = { viewModel.selectCategory(ProductCategory.ALL) },
                        onOpenCart = { viewModel.openCart() }
                    )
                }

                // 3. Category Pills
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CategoryPillsRow(
                        selectedCategory = selectedCategory,
                        onSelectCategory = { viewModel.selectCategory(it) }
                    )
                }

                // 4. Products Grid Title
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "قائمة المشروبات (${products.size})",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MoodGoldPrimary
                        )
                        GlowBadge(text = selectedCategory.titleAr, isTeal = true)
                    }
                }

                // 5. Product Cards
                items(products, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        isFavorite = favoriteIds.contains(product.id),
                        onToggleFavorite = { viewModel.toggleFavorite(product) },
                        onCardClick = { viewModel.openProductModal(product) },
                        onQuickAdd = { viewModel.addToCart(product) }
                    )
                }

                // 6. Music Corner ("ركن الطرب")
                item(span = { GridItemSpan(maxLineSpan) }) {
                    MusicCornerSection(legends = DefaultMenuData.legends)
                }

                // 7. Cafe Info & Location Card
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CafeInfoCard(
                        address = cafeSettings.locationAddress,
                        phone = cafeSettings.phoneNumber,
                        hours = cafeSettings.workingHours
                    )
                }

                // Bottom spacer for FAB
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }

            // Product Detail Modal
            selectedProductForModal?.let { product ->
                ProductDetailModal(
                    product = product,
                    isFavorite = favoriteIds.contains(product.id),
                    onToggleFavorite = { viewModel.toggleFavorite(product) },
                    onAddToCart = { p, size, addons -> viewModel.addToCart(p, size, addons) },
                    onOrderDirect = { p -> viewModel.directOrderSingleProduct(context, p) },
                    onDismiss = { viewModel.closeProductModal() }
                )
            }

            // Cart Sheet
            if (isCartOpen) {
                CartSheet(
                    cartItems = cartItems,
                    onUpdateQuantity = { index, delta -> viewModel.updateCartItemQuantity(index, delta) },
                    onRemoveItem = { index -> viewModel.removeCartItem(index) },
                    onClearCart = { viewModel.clearCart() },
                    onCheckout = { notes -> viewModel.checkoutWhatsApp(context, notes) },
                    onDismiss = { viewModel.closeCart() }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeTopBar(
    cafeNameAr: String,
    cafeNameEn: String,
    userRole: String,
    onSearchToggle: () -> Unit,
    onProfileClick: () -> Unit,
    onAdminClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(BorderStroke(1.dp, MoodGlassBorder), RoundedCornerShape(20.dp))
            .background(MoodGlassSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo with secret long click for Admin Access
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.combinedClickable(
                    onClick = {},
                    onLongClick = onAdminClick
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(listOf(MoodGoldPrimary, MoodGoldDark))
                        )
                        .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = MoodGoldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("☕", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = cafeNameAr,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = MoodCreamText
                    )
                    Text(
                        text = cafeNameEn.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodGoldPrimary,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            // Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onSearchToggle,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MoodGlassSurfaceElevated)
                        .border(BorderStroke(0.8.dp, MoodGlassBorder), CircleShape)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "بحث", tint = MoodCreamText, modifier = Modifier.size(18.dp))
                }

                if (userRole == UserRole.ADMIN.name) {
                    IconButton(
                        onClick = onAdminClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MoodGoldPrimary.copy(alpha = 0.18f))
                            .border(BorderStroke(0.8.dp, MoodGoldPrimary.copy(alpha = 0.5f)), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            contentDescription = "لوحة الإدارة",
                            tint = MoodGoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MoodGlassSurfaceElevated)
                        .border(BorderStroke(0.8.dp, MoodGlassBorder), CircleShape)
                ) {
                    Icon(Icons.Default.Person, contentDescription = "الحساب", tint = MoodGoldPrimary, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun HomeHeroSection(
    title: String,
    tagline: String,
    onExploreMenu: () -> Unit,
    onOpenCart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(BorderStroke(1.dp, MoodGlassBorder), RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        MoodGlassSurfaceElevated,
                        MoodGlassSurface
                    )
                )
            )
            .padding(22.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            GlowBadge(text = "✦ قهوة وطرب استثنائي ✦")

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MoodCreamText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = tagline,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = MoodMutedText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GoldGradientButton(
                    text = "استكشف المنيو ✨",
                    onClick = onExploreMenu
                )
                GoldGradientButton(
                    text = "السلة 🛒",
                    isOutline = true,
                    onClick = onOpenCart
                )
            }
        }
    }
}

@Composable
private fun CategoryPillsRow(
    selectedCategory: ProductCategory,
    onSelectCategory: (ProductCategory) -> Unit
) {
    val categories = ProductCategory.values()
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { cat ->
            val isSelected = selectedCategory == cat
            val shape = RoundedCornerShape(16.dp)
            Box(
                modifier = Modifier
                    .clip(shape)
                    .border(
                        BorderStroke(
                            1.dp,
                            if (isSelected) MoodGoldPrimary else MoodGlassBorder
                        ),
                        shape
                    )
                    .background(
                        if (isSelected) MoodGoldPrimary else MoodGlassSurface
                    )
                    .clickable { onSelectCategory(cat) }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = cat.icon, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = cat.titleAr,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) MoodDarkInk else MoodMutedText
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductEntity,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onCardClick: () -> Unit,
    onQuickAdd: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Top Badge & Favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (product.badge != ProductBadge.NONE.name) {
                    GlowBadge(text = product.badge)
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MoodGlassSurfaceElevated)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "مفضلة",
                        tint = if (isFavorite) Color(0xFFFF5252) else MoodMutedText,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Drink Emoji / Visual Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(MoodGoldPrimary.copy(alpha = 0.15f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getDrinkEmoji(product.nameAr),
                    fontSize = 44.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Name
            Text(
                text = product.nameAr,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MoodCreamText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = product.nameEn,
                fontSize = 11.sp,
                color = MoodGoldPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.description,
                fontSize = 11.sp,
                color = MoodMutedText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Price & Add Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${product.price.toInt()} ج.م",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = MoodGoldPrimary
                )

                IconButton(
                    onClick = onQuickAdd,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MoodGoldPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "أضف للسلة",
                        tint = MoodDarkInk,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MusicCornerSection(legends: List<LegendArtist>) {
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = MoodTealGlow,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ركن الطرب الأصيل",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodGoldPrimary
                )
            }

            Text(
                text = "استمتع بمشروبك مع أروع كلاسيكيات الزمن الجميل في أجواء راقية",
                fontSize = 12.sp,
                color = MoodMutedText,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(legends) { artist ->
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.dp, MoodGlassBorder), RoundedCornerShape(16.dp))
                            .background(MoodGlassSurfaceElevated)
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(artist.iconEmoji, fontSize = 24.sp)
                                GlowBadge(text = artist.tag, isTeal = true)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = artist.nameAr,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MoodCreamText
                            )
                            Text(
                                text = artist.title,
                                fontSize = 11.sp,
                                color = MoodGoldPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "\"${artist.quote}\"",
                                fontSize = 11.sp,
                                color = MoodMutedText,
                                maxLines = 3,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CafeInfoCard(
    address: String,
    phone: String,
    hours: String
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "📍 فروعنا ومواعيد العمل",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MoodGoldPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MoodTealGlow, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = address, fontSize = 13.sp, color = MoodCreamText)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = MoodTealGlow, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = phone, fontSize = 13.sp, color = MoodCreamText)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "⏰ $hours",
                fontSize = 12.sp,
                color = MoodMutedText
            )
        }
    }
}
