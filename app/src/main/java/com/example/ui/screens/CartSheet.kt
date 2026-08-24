package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.model.CartItem
import com.example.ui.components.GlassCard
import com.example.ui.components.GoldGradientButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartSheet(
    cartItems: List<CartItem>,
    onUpdateQuantity: (Int, Int) -> Unit,
    onRemoveItem: (Int) -> Unit,
    onClearCart: () -> Unit,
    onCheckout: (notes: String) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var notes by remember { mutableStateOf("") }
    val total = cartItems.sumOf { it.totalPrice }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MoodDarkEspresso,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .background(MoodDarkEspresso.copy(alpha = 0.95f))
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = MoodGoldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "سلة طلباتك (${cartItems.sumOf { it.quantity }})",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodCreamText
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (cartItems.isNotEmpty()) {
                        IconButton(
                            onClick = onClearCart,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MoodGlassSurfaceElevated)
                                .border(BorderStroke(0.8.dp, MoodGlassBorder), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "تفريغ السلة",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MoodGlassSurfaceElevated)
                            .border(BorderStroke(0.8.dp, MoodGlassBorder), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = MoodCreamText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "☕", fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "سلتك فارغة حالياً",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MoodCreamText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "اختر مشروبك المفضل من قائمة مزاج وأضفه هنا",
                            fontSize = 13.sp,
                            color = MoodMutedText
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(cartItems) { index, item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { onUpdateQuantity(index, 1) },
                            onDecrease = { onUpdateQuantity(index, -1) },
                            onRemove = { onRemoveItem(index) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("ملاحظات إضافية (مثال: سكر خفيف، بدون ثلج)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MoodGoldPrimary,
                                unfocusedBorderColor = MoodGlassBorder,
                                focusedLabelColor = MoodGoldPrimary,
                                unfocusedLabelColor = MoodMutedText,
                                focusedTextColor = MoodCreamText,
                                unfocusedTextColor = MoodCreamText,
                                focusedContainerColor = MoodGlassSurfaceElevated,
                                unfocusedContainerColor = MoodGlassSurfaceElevated
                            ),
                            shape = RoundedCornerShape(14.dp),
                            maxLines = 2
                        )
                    }
                }

                HorizontalDivider(
                    color = MoodGlassBorder,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // Summary Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "الإجمالي الكلي",
                            fontSize = 13.sp,
                            color = MoodMutedText
                        )
                        Text(
                            text = "${total.toInt()} جنيه مصري",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = MoodGoldPrimary
                        )
                    }

                    GoldGradientButton(
                        text = "إرسال للواتساب 💬",
                        onClick = { onCheckout(notes) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(BorderStroke(1.dp, MoodGlassBorder), RoundedCornerShape(16.dp))
            .background(MoodGlassSurfaceElevated)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = getDrinkEmoji(item.product.nameAr),
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column {
                    Text(
                        text = item.product.nameAr,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodCreamText
                    )
                    Text(
                        text = "${item.selectedSize}${if (item.selectedAddons.isNotEmpty()) " • " + item.selectedAddons.joinToString("+") else ""}",
                        fontSize = 11.sp,
                        color = MoodGoldPrimary
                    )
                    Text(
                        text = "${item.totalPrice.toInt()} جنيه",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodGoldPrimary
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(MoodDarkEspresso)
                        .border(BorderStroke(0.8.dp, MoodGlassBorder), CircleShape)
                ) {
                    Icon(
                        imageVector = if (item.quantity == 1) Icons.Default.DeleteOutline else Icons.Default.Remove,
                        contentDescription = "تقليل",
                        tint = if (item.quantity == 1) Color(0xFFFF5252) else MoodCreamText,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "${item.quantity}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodCreamText,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(MoodGoldPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "زيادة",
                        tint = MoodDarkInk,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
