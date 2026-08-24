package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryEntity
import com.example.data.model.ProductBadge
import com.example.data.model.ProductCategory
import com.example.data.model.ProductEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowBadge
import com.example.ui.components.GoldGradientButton
import com.example.ui.theme.MoodAmberDark
import com.example.ui.theme.MoodAmberPrimary
import com.example.ui.theme.MoodCreamText
import com.example.ui.theme.MoodDarkCard
import com.example.ui.theme.MoodDarkCardElevated
import com.example.ui.theme.MoodGlassBorder
import com.example.ui.theme.MoodGlassSurface
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodMutedText
import com.example.ui.theme.MoodTealGlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductsSection(
    products: List<ProductEntity>,
    categories: List<CategoryEntity>,
    onAddProduct: (ProductEntity) -> Unit,
    onUpdateProduct: (ProductEntity) -> Unit,
    onDeleteProduct: (Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }
    var showAddDialog by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<ProductEntity?>(null) }
    var productToDelete by remember { mutableStateOf<ProductEntity?>(null) }

    val filtered = products.filter { p ->
        val matchesCategory = selectedCategoryFilter == "ALL" || p.category == selectedCategoryFilter
        val matchesQuery = searchQuery.isBlank() ||
                p.nameAr.contains(searchQuery, ignoreCase = true) ||
                p.nameEn.contains(searchQuery, ignoreCase = true) ||
                p.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header & Add Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "إدارة قائمة المشروبات والمأكولات",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodCreamText
                    )
                    Text(
                        text = "${products.size} صنف مسجل في القائمة",
                        fontSize = 12.sp,
                        color = MoodMutedText
                    )
                }

                GoldGradientButton(
                    text = "إضافة صنف جديد +",
                    onClick = { showAddDialog = true }
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("ابحث بالاسم العربي، الإنجليزي أو الوصف...", fontSize = 13.sp, color = MoodMutedText) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث", tint = MoodAmberPrimary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "مسح", tint = MoodMutedText)
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MoodAmberPrimary,
                    unfocusedBorderColor = MoodGlassBorder,
                    focusedContainerColor = MoodDarkCard,
                    unfocusedContainerColor = MoodDarkCard,
                    focusedTextColor = MoodCreamText,
                    unfocusedTextColor = MoodCreamText
                ),
                singleLine = true
            )
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    CategoryChip(
                        title = "الكل (${products.size})",
                        isSelected = selectedCategoryFilter == "ALL",
                        onClick = { selectedCategoryFilter = "ALL" }
                    )
                }
                items(categories, key = { it.id }) { cat ->
                    val count = products.count { it.category == cat.code }
                    CategoryChip(
                        title = "${cat.iconEmoji} ${cat.titleAr} ($count)",
                        isSelected = selectedCategoryFilter == cat.code,
                        onClick = { selectedCategoryFilter = cat.code }
                    )
                }
            }
        }

        // Products Count
        item {
            Text(
                text = "النتائج المعروضة (${filtered.size})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MoodGoldPrimary
            )
        }

        // Products List
        if (filtered.isEmpty()) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = MoodDarkCard
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("لا توجد أصناف تطابق البحث الحالي", color = MoodMutedText, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(filtered, key = { it.id }) { product ->
                AdminProductCard(
                    product = product,
                    onEdit = { productToEdit = product },
                    onDelete = { productToDelete = product },
                    onToggleAvailability = { onUpdateProduct(product.copy(isAvailable = !product.isAvailable)) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Add / Edit Dialog
    if (showAddDialog || productToEdit != null) {
        ProductFormDialog(
            initialProduct = productToEdit,
            categories = categories,
            onDismiss = {
                showAddDialog = false
                productToEdit = null
            },
            onSave = { updatedOrNew ->
                if (productToEdit != null) {
                    onUpdateProduct(updatedOrNew)
                } else {
                    onAddProduct(updatedOrNew)
                }
                showAddDialog = false
                productToEdit = null
            }
        )
    }

    // Delete Confirmation Dialog
    if (productToDelete != null) {
        val prod = productToDelete!!
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            containerColor = MoodDarkCard,
            title = {
                Text("تأكيد حذف الصنف", color = MoodCreamText, fontWeight = FontWeight.Bold)
            },
            text = {
                Text("هل أنت متأكد من رغبتك في حذف '${prod.nameAr}' نهائياً من قائمة مزاج؟", color = MoodMutedText)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteProduct(prod.id)
                        productToDelete = null
                    }
                ) {
                    Text("حذف نهائي", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("إلغاء", color = MoodMutedText)
                }
            }
        )
    }
}

@Composable
private fun CategoryChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) MoodAmberPrimary else MoodDarkCard
            )
            .border(
                1.dp,
                if (isSelected) MoodAmberPrimary else MoodGlassBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color(0xFF0D0B08) else MoodCreamText,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold
        )
    }
}

@Composable
private fun AdminProductCard(
    product: ProductEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleAvailability: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = if (product.isAvailable) MoodDarkCard else MoodDarkCardElevated.copy(alpha = 0.6f),
        borderColor = if (product.isAvailable) MoodGlassBorder else Color.DarkGray
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Icon / Emoji
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MoodDarkCardElevated)
                    .border(1.dp, MoodGlassBorder, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (product.category) {
                        "HOT_COFFEE" -> "☕"
                        "TEA_HERBS" -> "🫖"
                        "SPECIAL" -> "✨"
                        "COLD" -> "🧊"
                        "JUICE" -> "🍹"
                        "DESSERT" -> "🍰"
                        else -> "☕"
                    },
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.nameAr,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (product.isAvailable) MoodCreamText else MoodMutedText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (product.badge != ProductBadge.NONE.name) {
                        GlowBadge(
                            text = when (product.badge) {
                                ProductBadge.NEW.name -> "جديد ✨"
                                ProductBadge.POPULAR.name -> "الأكثر طلباً 🔥"
                                ProductBadge.BEST_SELLER.name -> "الأعلى مبيعاً ⭐"
                                ProductBadge.SPECIAL.name -> "خاص ✦"
                                else -> product.badge
                            },
                            isTeal = product.badge == ProductBadge.NEW.name
                        )
                    }
                }

                Text(
                    text = "${product.nameEn} • ${product.price.toInt()} ج.م",
                    fontSize = 12.sp,
                    color = MoodAmberPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = product.description,
                    fontSize = 11.sp,
                    color = MoodMutedText,
                    maxLines = 1
                )
            }

            // Availability Switch & Action Buttons
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (product.isAvailable) "متوفر" else "غير متاح",
                        fontSize = 10.sp,
                        color = if (product.isAvailable) Color(0xFF10B981) else Color(0xFFFF5252),
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = product.isAvailable,
                        onCheckedChange = { onToggleAvailability() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MoodDarkCard,
                            checkedTrackColor = Color(0xFF10B981),
                            uncheckedThumbColor = MoodMutedText,
                            uncheckedTrackColor = MoodDarkCardElevated
                        ),
                        modifier = Modifier.size(38.dp, 24.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MoodDarkCardElevated)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = MoodAmberPrimary, modifier = Modifier.size(16.dp))
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MoodDarkCardElevated)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "حذف", tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductFormDialog(
    initialProduct: ProductEntity?,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (ProductEntity) -> Unit
) {
    val isEdit = initialProduct != null
    var nameAr by remember { mutableStateOf(initialProduct?.nameAr ?: "") }
    var nameEn by remember { mutableStateOf(initialProduct?.nameEn ?: "") }
    var description by remember { mutableStateOf(initialProduct?.description ?: "") }
    var priceStr by remember { mutableStateOf(initialProduct?.price?.toInt()?.toString() ?: "50") }
    var selectedCategory by remember { mutableStateOf(initialProduct?.category ?: "HOT_COFFEE") }
    var selectedBadge by remember { mutableStateOf(initialProduct?.badge ?: ProductBadge.NONE.name) }
    var isAvailable by remember { mutableStateOf(initialProduct?.isAvailable ?: true) }

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var badgeDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MoodDarkCard,
        title = {
            Text(
                text = if (isEdit) "تعديل صنف: ${initialProduct?.nameAr}" else "إضافة صنف جديد للمنيو",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MoodCreamText
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nameAr,
                    onValueChange = { nameAr = it },
                    label = { Text("الاسم بالعربية", color = MoodMutedText, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MoodAmberPrimary,
                        unfocusedBorderColor = MoodGlassBorder,
                        focusedTextColor = MoodCreamText,
                        unfocusedTextColor = MoodCreamText
                    )
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("الاسم بالإنجليزية (English)", color = MoodMutedText, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MoodAmberPrimary,
                        unfocusedBorderColor = MoodGlassBorder,
                        focusedTextColor = MoodCreamText,
                        unfocusedTextColor = MoodCreamText
                    )
                )

                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("السعر (جنيه مصري)", color = MoodMutedText, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MoodAmberPrimary,
                        unfocusedBorderColor = MoodGlassBorder,
                        focusedTextColor = MoodCreamText,
                        unfocusedTextColor = MoodCreamText
                    )
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("وصف المشروب والمكونات", color = MoodMutedText, fontSize = 12.sp) },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MoodAmberPrimary,
                        unfocusedBorderColor = MoodGlassBorder,
                        focusedTextColor = MoodCreamText,
                        unfocusedTextColor = MoodCreamText
                    )
                )

                // Category Selector Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                ) {
                    val catTitle = categories.find { it.code == selectedCategory }?.let { "${it.iconEmoji} ${it.titleAr}" } ?: selectedCategory
                    OutlinedTextField(
                        value = catTitle,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("القسم التصنيفي", color = MoodMutedText, fontSize = 12.sp) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodAmberPrimary,
                            unfocusedBorderColor = MoodGlassBorder,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("${cat.iconEmoji} ${cat.titleAr} (${cat.titleEn})") },
                                onClick = {
                                    selectedCategory = cat.code
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Badge Selector Dropdown
                ExposedDropdownMenuBox(
                    expanded = badgeDropdownExpanded,
                    onExpandedChange = { badgeDropdownExpanded = !badgeDropdownExpanded }
                ) {
                    val badgeLabel = when (selectedBadge) {
                        ProductBadge.NONE.name -> "بدون شارة (عادي)"
                        ProductBadge.NEW.name -> "جديد ✨ (New)"
                        ProductBadge.POPULAR.name -> "الأكثر طلباً 🔥 (Popular)"
                        ProductBadge.BEST_SELLER.name -> "الأعلى مبيعاً ⭐ (Best Seller)"
                        ProductBadge.SPECIAL.name -> "خاص ✦ (Special)"
                        else -> selectedBadge
                    }
                    OutlinedTextField(
                        value = badgeLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("شارة المنتج (Badge)", color = MoodMutedText, fontSize = 12.sp) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = badgeDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodAmberPrimary,
                            unfocusedBorderColor = MoodGlassBorder,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = badgeDropdownExpanded,
                        onDismissRequest = { badgeDropdownExpanded = false }
                    ) {
                        listOf(
                            ProductBadge.NONE.name to "بدون شارة",
                            ProductBadge.NEW.name to "جديد ✨ (New)",
                            ProductBadge.POPULAR.name to "الأكثر طلباً 🔥 (Popular)",
                            ProductBadge.BEST_SELLER.name to "الأعلى مبيعاً ⭐ (Best Seller)",
                            ProductBadge.SPECIAL.name to "خاص ✦ (Special)"
                        ).forEach { (code, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedBadge = code
                                    badgeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Availability Toggle in Form
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("إتاحة الصنف للطلب المباشر", color = MoodCreamText, fontSize = 13.sp)
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = { isAvailable = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MoodDarkCard,
                            checkedTrackColor = Color(0xFF10B981)
                        )
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val price = priceStr.toDoubleOrNull() ?: 50.0
                    val entity = if (isEdit && initialProduct != null) {
                        initialProduct.copy(
                            nameAr = nameAr.ifBlank { "مشروب مزاج" },
                            nameEn = nameEn.ifBlank { "Mood Drink" },
                            description = description,
                            price = price,
                            category = selectedCategory,
                            badge = selectedBadge,
                            isAvailable = isAvailable
                        )
                    } else {
                        ProductEntity(
                            nameAr = nameAr.ifBlank { "مشروب جديد" },
                            nameEn = nameEn.ifBlank { "New Drink" },
                            description = description.ifBlank { "مشروب مميز محضر بأعلى معايير الجودة." },
                            price = price,
                            category = selectedCategory,
                            badge = selectedBadge,
                            rating = 4.8,
                            isAvailable = isAvailable
                        )
                    }
                    onSave(entity)
                }
            ) {
                Text("حفظ التغييرات ✔", color = MoodAmberPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = MoodMutedText)
            }
        }
    )
}
