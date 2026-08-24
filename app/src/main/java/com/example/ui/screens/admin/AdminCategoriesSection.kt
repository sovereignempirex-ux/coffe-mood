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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
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

@Composable
fun AdminCategoriesSection(
    categories: List<CategoryEntity>,
    products: List<ProductEntity>,
    onAddCategory: (String, String, String) -> Unit,
    onUpdateCategory: (CategoryEntity) -> Unit,
    onDeleteCategory: (Long) -> Unit,
    onToggleVisibility: (CategoryEntity) -> Unit,
    onReorderCategory: (CategoryEntity, Boolean) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<CategoryEntity?>(null) }
    var categoryToDelete by remember { mutableStateOf<CategoryEntity?>(null) }

    val sortedCategories = categories.sortedBy { it.sortOrder }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "إدارة أقسام وتصنيفات المنيو",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodCreamText
                    )
                    Text(
                        text = "يمكنك ترتيب الأقسام وإخفائها أو إضافة تصنيفات جديدة",
                        fontSize = 12.sp,
                        color = MoodMutedText
                    )
                }

                GoldGradientButton(
                    text = "إضافة قسم جديد +",
                    onClick = { showAddDialog = true }
                )
            }
        }

        if (sortedCategories.isEmpty()) {
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
                        Text("لا توجد أقسام مسجلة. أضف قسماً جديداً للبدء!", color = MoodMutedText, fontSize = 13.sp)
                    }
                }
            }
        } else {
            itemsIndexed(sortedCategories, key = { _, cat -> cat.id }) { index, cat ->
                val count = products.count { it.category == cat.code }
                val isFirst = index == 0
                val isLast = index == sortedCategories.size - 1

                AdminCategoryCard(
                    category = cat,
                    productCount = count,
                    isFirst = isFirst,
                    isLast = isLast,
                    onMoveUp = { onReorderCategory(cat, true) },
                    onMoveDown = { onReorderCategory(cat, false) },
                    onToggleVisibility = { onToggleVisibility(cat) },
                    onEdit = { categoryToEdit = cat },
                    onDelete = { categoryToDelete = cat }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Add Category Dialog
    if (showAddDialog) {
        CategoryFormDialog(
            initialCategory = null,
            onDismiss = { showAddDialog = false },
            onSave = { titleAr, titleEn, emoji ->
                onAddCategory(titleAr, titleEn, emoji)
                showAddDialog = false
            }
        )
    }

    // Edit Category Dialog
    if (categoryToEdit != null) {
        CategoryFormDialog(
            initialCategory = categoryToEdit,
            onDismiss = { categoryToEdit = null },
            onSave = { titleAr, titleEn, emoji ->
                categoryToEdit?.let {
                    onUpdateCategory(
                        it.copy(
                            titleAr = titleAr,
                            titleEn = titleEn,
                            iconEmoji = emoji
                        )
                    )
                }
                categoryToEdit = null
            }
        )
    }

    // Delete Confirmation
    if (categoryToDelete != null) {
        val cat = categoryToDelete!!
        val count = products.count { it.category == cat.code }
        AlertDialog(
            onDismissRequest = { categoryToDelete = null },
            containerColor = MoodDarkCard,
            title = { Text("تأكيد حذف القسم", color = MoodCreamText, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = if (count > 0) {
                        "تنبيه: يوجد $count منتج يتبع هذا القسم (${cat.titleAr}). هل أنت متأكد من الحذف؟"
                    } else {
                        "هل أنت متأكد من حذف قسم '${cat.titleAr}'؟"
                    },
                    color = MoodMutedText
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteCategory(cat.id)
                        categoryToDelete = null
                    }
                ) {
                    Text("حذف نهائي", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDelete = null }) {
                    Text("إلغاء", color = MoodMutedText)
                }
            }
        )
    }
}

@Composable
private fun AdminCategoryCard(
    category: CategoryEntity,
    productCount: Int,
    isFirst: Boolean,
    isLast: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onToggleVisibility: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = if (category.isVisible) MoodDarkCard else MoodDarkCardElevated.copy(alpha = 0.5f),
        borderColor = if (category.isVisible) MoodGlassBorder else Color.DarkGray
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reorder Arrows (Up / Down)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                IconButton(
                    onClick = onMoveUp,
                    enabled = !isFirst,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowUpward,
                        contentDescription = "تحريك للأعلى",
                        tint = if (!isFirst) MoodAmberPrimary else MoodMutedText.copy(alpha = 0.3f),
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = onMoveDown,
                    enabled = !isLast,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowDownward,
                        contentDescription = "تحريك للأسفل",
                        tint = if (!isLast) MoodAmberPrimary else MoodMutedText.copy(alpha = 0.3f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Icon / Emoji
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MoodDarkCardElevated)
                    .border(1.dp, MoodGlassBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = category.iconEmoji, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = category.titleAr,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (category.isVisible) MoodCreamText else MoodMutedText
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${category.titleEn})",
                        fontSize = 11.sp,
                        color = MoodTealGlow
                    )
                }

                Text(
                    text = "$productCount منتج في هذا القسم • ترتيب #${category.sortOrder}",
                    fontSize = 11.sp,
                    color = MoodMutedText
                )
            }

            // Visibility & Action Buttons
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (category.isVisible) "ظاهر" else "مخفي",
                        fontSize = 10.sp,
                        color = if (category.isVisible) Color(0xFF10B981) else Color(0xFFFF5252),
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = category.isVisible,
                        onCheckedChange = { onToggleVisibility() },
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
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(MoodDarkCardElevated)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = MoodAmberPrimary, modifier = Modifier.size(14.dp))
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(MoodDarkCardElevated)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "حذف", tint = Color(0xFFFF5252), modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryFormDialog(
    initialCategory: CategoryEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    val isEdit = initialCategory != null
    var titleAr by remember { mutableStateOf(initialCategory?.titleAr ?: "") }
    var titleEn by remember { mutableStateOf(initialCategory?.titleEn ?: "") }
    var iconEmoji by remember { mutableStateOf(initialCategory?.iconEmoji ?: "☕") }

    val emojis = listOf("☕", "🫖", "✨", "🧊", "🍹", "🍰", "🥐", "🥪", "🥤", "🍩")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MoodDarkCard,
        title = {
            Text(
                text = if (isEdit) "تعديل قسم: ${initialCategory?.titleAr}" else "إضافة قسم جديد للمنيو",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MoodCreamText
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = titleAr,
                    onValueChange = { titleAr = it },
                    label = { Text("اسم القسم بالعربية (مثال: مشروبات مميزة)", color = MoodMutedText, fontSize = 12.sp) },
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
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text("اسم القسم بالإنجليزية (English)", color = MoodMutedText, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MoodAmberPrimary,
                        unfocusedBorderColor = MoodGlassBorder,
                        focusedTextColor = MoodCreamText,
                        unfocusedTextColor = MoodCreamText
                    )
                )

                Text("اختر أيقونة القسم:", color = MoodMutedText, fontSize = 12.sp)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    emojis.take(6).forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (iconEmoji == emoji) MoodAmberPrimary else MoodDarkCardElevated)
                                .border(1.dp, if (iconEmoji == emoji) MoodAmberPrimary else MoodGlassBorder, RoundedCornerShape(10.dp))
                                .clickable { iconEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emoji, fontSize = 18.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (titleAr.isNotBlank()) {
                        onSave(titleAr, titleEn.ifBlank { titleAr }, iconEmoji)
                    }
                }
            ) {
                Text("حفظ القسم ✔", color = MoodAmberPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = MoodMutedText)
            }
        }
    )
}
