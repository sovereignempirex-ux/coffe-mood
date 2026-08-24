package com.example.ui.components

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.OrderEntity
import com.example.data.model.OrderStatus
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderTrackingDialog(
    order: OrderEntity,
    onDismiss: () -> Unit
) {
    val steps = listOf(
        TrackingStep(
            titleAr = "تم استلام الطلب",
            descAr = "وصل طلبك للمطبخ بنجاح",
            icon = Icons.Default.ReceiptLong,
            status = OrderStatus.PENDING
        ),
        TrackingStep(
            titleAr = "جاري التحضير",
            descAr = "الباريستا يقوم بتحضير مشروباتك بعناية",
            icon = Icons.Default.LocalCafe,
            status = OrderStatus.PREPARING
        ),
        TrackingStep(
            titleAr = "جاهز للاستلام",
            descAr = "طلبك جاهز على البار أو مع الكابتن",
            icon = Icons.Default.DeliveryDining,
            status = OrderStatus.READY
        ),
        TrackingStep(
            titleAr = "تم التسليم",
            descAr = "بالعافية عليك، نتشرف بك دائماً",
            icon = Icons.Default.Check,
            status = OrderStatus.COMPLETED
        )
    )

    val currentStepIndex = when (order.status) {
        OrderStatus.PENDING.name -> 0
        OrderStatus.PREPARING.name -> 1
        OrderStatus.READY.name -> 2
        OrderStatus.COMPLETED.name -> 3
        else -> 0
    }

    val dateFormatted = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar")).format(Date(order.createdAt))

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(24.dp, RoundedCornerShape(28.dp), spotColor = MoodGoldSecondary)
                .clip(RoundedCornerShape(28.dp))
                .background(MoodDarkCardElevated)
                .border(BorderStroke(1.5.dp, Brush.linearGradient(listOf(MoodGoldPrimary, MoodGlassBorder))), RoundedCornerShape(28.dp))
                .padding(22.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "تتبع الطلب",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MoodGoldPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "#${order.orderCode}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MoodTealNeon
                            )
                        }
                        Text(
                            text = dateFormatted,
                            fontSize = 11.sp,
                            color = MoodMutedText
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = MoodMutedText)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stepper Timeline
                Column(modifier = Modifier.fillMaxWidth()) {
                    steps.forEachIndexed { index, step ->
                        val isDone = index <= currentStepIndex
                        val isCurrent = index == currentStepIndex

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Indicator Icon & Line
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isCurrent) Brush.linearGradient(listOf(MoodGoldPrimary, MoodGoldDark))
                                            else if (isDone) Brush.linearGradient(listOf(MoodTealNeon, MoodTealGlow))
                                            else Brush.linearGradient(listOf(MoodDarkCard, MoodDarkInk))
                                        )
                                        .border(
                                            BorderStroke(
                                                1.5.dp,
                                                if (isDone) MoodGoldPrimary else MoodGlassBorder
                                            ),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = step.icon,
                                        contentDescription = null,
                                        tint = if (isDone) MoodDarkInk else MoodMutedText,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                if (index < steps.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(28.dp)
                                            .background(
                                                if (index < currentStepIndex) MoodGoldPrimary
                                                else MoodGlassBorder
                                            )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Step Text
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = if (index < steps.lastIndex) 14.dp else 0.dp)
                            ) {
                                Text(
                                    text = step.titleAr,
                                    fontSize = 14.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isCurrent) MoodGoldPrimary else if (isDone) MoodCreamText else MoodMutedText
                                )
                                Text(
                                    text = step.descAr,
                                    fontSize = 11.sp,
                                    color = MoodMutedText
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Order summary snippet
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MoodDarkCard)
                        .border(BorderStroke(1.dp, MoodGlassBorder), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("الإجمالي الكلي:", fontSize = 13.sp, color = MoodMutedText)
                            Text("${order.totalAmount.toInt()} ج", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MoodGoldPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                GoldGradientButton(
                    text = "حسناً، استمرار ✦",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private data class TrackingStep(
    val titleAr: String,
    val descAr: String,
    val icon: ImageVector,
    val status: OrderStatus
)
