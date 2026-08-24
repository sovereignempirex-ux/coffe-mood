package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ProductBadge
import com.example.data.model.ProductEntity
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
import com.example.ui.theme.MoodGlassSurfaceElevated
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodGoldSecondary
import com.example.ui.theme.MoodMutedText
import com.example.ui.theme.MoodTealGlow
import com.example.ui.theme.MoodTealNeon

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailModal(
    product: ProductEntity,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onAddToCart: (ProductEntity, String, List<String>) -> Unit,
    onOrderDirect: (ProductEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedSize by remember { mutableStateOf("وسط (350ml)") }
    val selectedAddons = remember { mutableStateListOf<String>() }
    var quantity by remember { mutableIntStateOf(1) }

    val sizes = listOf("صغير (250ml)", "وسط (350ml)", "كبير (500ml)")
    val availableAddons = listOf(
        "شوت اسبريسو إضافي (+10 ج)",
        "صوص كراميل (+5 ج)",
        "صوص فستق (+10 ج)",
        "حليب شوفان (+10 ج)",
        "سكر مضبوط",
        "سكر زيادة",
        "بدون سكر (سادة)"
    )

    val basePrice = product.price
    val sizeExtra = if (selectedSize.contains("كبير")) 10.0 else 0.0
    val addonsExtra = selectedAddons.sumOf {
        when {
            it.contains("+10") -> 10.0
            it.contains("+5") -> 5.0
            else -> 0.0
        }
    }
    val unitPrice = basePrice + sizeExtra + addonsExtra
    val totalPrice = unitPrice * quantity

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(26.dp))
                .border(BorderStroke(1.dp, MoodGlassBorder), RoundedCornerShape(26.dp))
                .background(MoodDarkEspresso.copy(alpha = 0.95f))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Bar in Modal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MoodGlassSurfaceElevated)
                            .border(BorderStroke(0.8.dp, MoodGlassBorder), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = MoodCreamText
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (product.badge != ProductBadge.NONE.name) {
                            GlowBadge(text = product.badge, modifier = Modifier.padding(end = 8.dp))
                        }
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MoodGlassSurfaceElevated)
                                .border(BorderStroke(0.8.dp, MoodGlassBorder), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "المفضلة",
                                tint = if (isFavorite) Color(0xFFFF5252) else MoodMutedText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hero Graphic Placeholder for Drink
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.radialGradient(
                                listOf(MoodGoldPrimary.copy(alpha = 0.18f), MoodDarkCard, Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = getDrinkEmoji(product.nameAr),
                            fontSize = 62.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MoodGoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = " ${product.rating} • تقييم العملاء",
                                color = MoodGoldPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Product Title & English name
                Text(
                    text = product.nameAr,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodCreamText
                )
                Text(
                    text = product.nameEn,
                    fontSize = 14.sp,
                    color = MoodGoldPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    color = MoodMutedText,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Sizes Selector
                Text(
                    text = "الحجم المفضل (Size):",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodGoldPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sizes.forEach { size ->
                        val isSelected = selectedSize == size
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (isSelected) MoodGoldPrimary else MoodGlassBorder
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .background(if (isSelected) MoodGoldPrimary else MoodGlassSurfaceElevated)
                                .clickable { selectedSize = size }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = size,
                                fontSize = 12.sp,
                                color = if (isSelected) MoodDarkInk else MoodMutedText,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Addons & Sugar choices
                Text(
                    text = "الإضافات وتعديل النكهة:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodGoldPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableAddons.forEach { addon ->
                        val isSelected = selectedAddons.contains(addon)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    BorderStroke(
                                        0.8.dp,
                                        if (isSelected) MoodGoldPrimary else MoodGlassBorder
                                    ),
                                    RoundedCornerShape(10.dp)
                                )
                                .background(if (isSelected) MoodGoldPrimary.copy(alpha = 0.2f) else MoodGlassSurfaceElevated)
                                .clickable {
                                    if (isSelected) selectedAddons.remove(addon) else selectedAddons.add(addon)
                                }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = addon,
                                fontSize = 12.sp,
                                color = if (isSelected) MoodGoldPrimary else MoodMutedText,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Quantity & Price Summary Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MoodGlassSurfaceElevated)
                        .border(BorderStroke(1.dp, MoodGlassBorder), RoundedCornerShape(14.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "الإجمالي:",
                            fontSize = 12.sp,
                            color = MoodMutedText
                        )
                        Text(
                            text = "${totalPrice.toInt()} جنيه",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = MoodGoldPrimary
                        )
                    }

                    // Counter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MoodDarkEspresso)
                                .border(BorderStroke(0.8.dp, MoodGlassBorder), CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "تقليل", tint = MoodCreamText)
                        }
                        Text(
                            text = "$quantity",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MoodCreamText
                        )
                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MoodGoldPrimary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "زيادة", tint = MoodDarkInk)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GoldGradientButton(
                        text = "أضف للسلة 🛒",
                        onClick = {
                            repeat(quantity) {
                                onAddToCart(product, selectedSize, selectedAddons.toList())
                            }
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    )

                    GoldGradientButton(
                        text = "طلب فوري 📱",
                        isOutline = true,
                        onClick = {
                            onOrderDirect(product)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

fun getDrinkEmoji(name: String): String {
    return when {
        name.contains("تركي") || name.contains("عربي") -> "☕"
        name.contains("اسبريسو") || name.contains("ماكياتو") -> "☕"
        name.contains("لاتيه") || name.contains("كابتشينو") || name.contains("وايت") -> "🥛"
        name.contains("شاي") || name.contains("ينسون") || name.contains("كركديه") -> "🫖"
        name.contains("شوكليت") || name.contains("سحلب") -> "🍫"
        name.contains("ايس") || name.contains("فرابتشينو") || name.contains("موهيتو") -> "🧊"
        name.contains("سموزي") || name.contains("عصير") || name.contains("ليمون") -> "🥤"
        name.contains("مياه") -> "💧"
        else -> "☕"
    }
}
