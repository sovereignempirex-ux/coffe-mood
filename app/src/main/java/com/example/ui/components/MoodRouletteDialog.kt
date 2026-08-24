package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ProductEntity
import com.example.ui.theme.MoodAmberGlow
import com.example.ui.theme.MoodCreamText
import com.example.ui.theme.MoodDarkCard
import com.example.ui.theme.MoodDarkCardElevated
import com.example.ui.theme.MoodDarkInk
import com.example.ui.theme.MoodGlassBorder
import com.example.ui.theme.MoodGoldDark
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodGoldSecondary
import com.example.ui.theme.MoodMutedText
import com.example.ui.theme.MoodTealGlow
import com.example.ui.theme.MoodTealNeon

@Composable
fun MoodRouletteDialog(
    isSpinning: Boolean,
    winnerProduct: ProductEntity?,
    onSpin: () -> Unit,
    onAddToCart: (ProductEntity) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(24.dp, RoundedCornerShape(28.dp), spotColor = MoodGoldSecondary)
                .clip(RoundedCornerShape(28.dp))
                .background(MoodDarkCardElevated)
                .border(BorderStroke(1.5.dp, Brush.linearGradient(listOf(MoodGoldPrimary, MoodGlassBorder))), RoundedCornerShape(28.dp))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(MoodGoldPrimary, MoodGoldDark))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = MoodDarkInk,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "عجلة مزاج اليوم ☕",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MoodGoldPrimary
                            )
                            Text(
                                text = "محتار؟ دع المزاج يختار لك!",
                                fontSize = 12.sp,
                                color = MoodMutedText
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = MoodMutedText)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Center Roulette Area
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(MoodDarkInk)
                        .border(
                            BorderStroke(
                                3.dp,
                                Brush.sweepGradient(
                                    listOf(MoodGoldPrimary, MoodTealNeon, MoodGoldSecondary, MoodAmberGlow, MoodGoldPrimary)
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSpinning) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(54.dp),
                                color = MoodGoldPrimary,
                                strokeWidth = 4.dp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("جاري الاختيار...", fontSize = 12.sp, color = MoodGoldPrimary)
                        }
                    } else if (winnerProduct != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "✨",
                                fontSize = 24.sp
                            )
                            Text(
                                text = winnerProduct.nameAr,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MoodGoldPrimary,
                                textAlign = TextAlign.Center,
                                maxLines = 2
                            )
                            Text(
                                text = "${winnerProduct.price.toInt()} ج",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MoodTealNeon
                            )
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.LocalCafe,
                                contentDescription = null,
                                tint = MoodGoldPrimary,
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "اضغط للتدوير",
                                fontSize = 13.sp,
                                color = MoodCreamText,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Result Information
                AnimatedVisibility(
                    visible = winnerProduct != null && !isSpinning,
                    enter = fadeIn() + scaleIn()
                ) {
                    winnerProduct?.let { product ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MoodDarkCard)
                                .border(BorderStroke(1.dp, MoodGlassBorder), RoundedCornerShape(16.dp))
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎉 مشروبك المقترح لهذا اليوم:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MoodCreamText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = product.description,
                                fontSize = 12.sp,
                                color = MoodMutedText,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            GoldGradientButton(
                                text = "أضف للسلة (${product.price.toInt()} ج) 🛒",
                                onClick = {
                                    onAddToCart(product)
                                    onDismiss()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Spin Action Button
                GoldGradientButton(
                    text = if (isSpinning) "جاري السحب..." else if (winnerProduct != null) "جرّب مشروباً آخر 🎲" else "تدوير العجلة 🎲",
                    onClick = onSpin,
                    enabled = !isSpinning,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
