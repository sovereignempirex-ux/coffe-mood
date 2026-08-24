package com.example.ui.screens.admin

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CafeSettingsEntity
import com.example.data.model.OrderEntity
import com.example.data.model.OrderStatus
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowBadge
import com.example.ui.components.GoldGradientButton
import com.example.ui.theme.MoodAmberDark
import com.example.ui.theme.MoodAmberGlow
import com.example.ui.theme.MoodAmberPrimary
import com.example.ui.theme.MoodCreamText
import com.example.ui.theme.MoodDarkCard
import com.example.ui.theme.MoodDarkCardElevated
import com.example.ui.theme.MoodDarkEspresso
import com.example.ui.theme.MoodGlassBorder
import com.example.ui.theme.MoodGlassSurface
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodMutedText
import com.example.ui.theme.MoodTealGlow
import com.example.ui.theme.MoodTealNeon
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminOverviewSection(
    orders: List<OrderEntity>,
    products: List<ProductEntity>,
    users: List<UserEntity>,
    settings: CafeSettingsEntity,
    onNavigateToSection: (Int) -> Unit,
    onUpdateStoreStatus: (String) -> Unit,
    onQuickUpdateOrderStatus: (Long, OrderStatus, String) -> Unit
) {
    val nonCancelledOrders = orders.filter { it.status != OrderStatus.CANCELLED.name }
    val totalRevenue = nonCancelledOrders.sumOf { it.totalAmount }
    val pendingOrders = orders.filter { it.status == OrderStatus.PENDING.name }
    val preparingOrders = orders.filter { it.status == OrderStatus.PREPARING.name }
    val readyOrders = orders.filter { it.status == OrderStatus.READY.name }
    val completedOrders = orders.filter { it.status == OrderStatus.COMPLETED.name }
    val cancelledOrders = orders.filter { it.status == OrderStatus.CANCELLED.name }
    val avgOrderValue = if (nonCancelledOrders.isNotEmpty()) totalRevenue / nonCancelledOrders.size else 0.0

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // 1. Store Live Status Banner & Quick Switcher
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = MoodDarkCard
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    when (settings.storeStatus) {
                                        "OPEN" -> Color(0xFF10B981).copy(alpha = 0.2f)
                                        "BUSY" -> Color(0xFFF59E0B).copy(alpha = 0.2f)
                                        else -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Store,
                                contentDescription = "حالة الكافيه",
                                tint = when (settings.storeStatus) {
                                    "OPEN" -> Color(0xFF10B981)
                                    "BUSY" -> Color(0xFFF59E0B)
                                    else -> Color(0xFFEF4444)
                                },
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "حالة استقبال الطلبات",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MoodCreamText
                            )
                            Text(
                                text = when (settings.storeStatus) {
                                    "OPEN" -> "🟢 مفتوح ويستقبل الطلبات الآن"
                                    "BUSY" -> "🟡 وقت الذروة (قد يستغرق وقتاً أطول)"
                                    else -> "🔴 مغلق حالياً"
                                },
                                fontSize = 12.sp,
                                color = MoodMutedText
                            )
                        }
                    }

                    // Status Switcher Pills
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            Triple("OPEN", "مفتوح", Color(0xFF10B981)),
                            Triple("BUSY", "ذروة", Color(0xFFF59E0B)),
                            Triple("CLOSED", "مغلق", Color(0xFFEF4444))
                        ).forEach { (code, label, color) ->
                            val isSelected = settings.storeStatus == code
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) color.copy(alpha = 0.25f) else MoodGlassSurface
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) color else MoodGlassBorder,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { onUpdateStoreStatus(code) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) color else MoodMutedText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Six High-Impact Metric Cards (Responsive FlowRow Grid)
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                maxItemsInEachRow = 3
            ) {
                MetricCard(
                    title = "إجمالي الإيرادات",
                    value = "${totalRevenue.toInt()} ج.م",
                    subtitle = "${nonCancelledOrders.size} طلب مكتمل ومؤكد",
                    icon = Icons.Default.AttachMoney,
                    color = MoodAmberPrimary,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "إجمالي الطلبات",
                    value = "${orders.size}",
                    subtitle = "${pendingOrders.size} بانتظار التأكيد",
                    icon = Icons.Default.ReceiptLong,
                    color = MoodTealGlow,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "متوسط قيمة الطلب",
                    value = "${avgOrderValue.toInt()} ج.م",
                    subtitle = "AOV لكل عميل",
                    icon = Icons.Default.Speed,
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                maxItemsInEachRow = 3
            ) {
                MetricCard(
                    title = "طلبات قيد التحضير",
                    value = "${preparingOrders.size + pendingOrders.size}",
                    subtitle = "تحتاج إلى متابعة المطبخ",
                    icon = Icons.Default.HourglassTop,
                    color = Color(0xFFFFB300),
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "العملاء المسجلين",
                    value = "${users.size}",
                    subtitle = "في قاعدة بيانات مزاج",
                    icon = Icons.Default.People,
                    color = Color(0xFFA78BFA),
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "الأصناف النشطة",
                    value = "${products.count { it.isAvailable }}",
                    subtitle = "من إجمالي ${products.size} صنف",
                    icon = Icons.Default.LocalCafe,
                    color = Color(0xFF34D399),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. Order Status Breakdown Visualizer
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = MoodDarkCard
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "توزيع حالات الطلبات",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MoodCreamText
                        )
                        Text(
                            text = "إجمالي: ${orders.size}",
                            fontSize = 12.sp,
                            color = MoodMutedText
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val totalOrdersCount = maxOf(orders.size, 1)

                    StatusProgressRow(
                        label = "قيد الانتظار (Pending)",
                        count = pendingOrders.size,
                        total = totalOrdersCount,
                        color = Color(0xFFFFC107)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StatusProgressRow(
                        label = "جاري التحضير (Preparing)",
                        count = preparingOrders.size,
                        total = totalOrdersCount,
                        color = Color(0xFF2196F3)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StatusProgressRow(
                        label = "جاهز للاستلام (Ready)",
                        count = readyOrders.size,
                        total = totalOrdersCount,
                        color = Color(0xFF4CAF50)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StatusProgressRow(
                        label = "تم التسليم (Completed)",
                        count = completedOrders.size,
                        total = totalOrdersCount,
                        color = Color(0xFF9E9E9E)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StatusProgressRow(
                        label = "ملغي (Cancelled)",
                        count = cancelledOrders.size,
                        total = totalOrdersCount,
                        color = Color(0xFFE91E63)
                    )
                }
            }
        }

        // 4. Recent Live Orders Stream (Fast Advance)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "أحدث الطلبات الواردة",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodCreamText
                )
                Text(
                    text = "عرض الكل (${orders.size}) ❯",
                    fontSize = 12.sp,
                    color = MoodTealGlow,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToSection(3) } // Go to Orders section
                )
            }
        }

        if (orders.isEmpty()) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = MoodDarkCard
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لا توجد طلبات مسجلة بعد. عند إتمام أي طلب ستظهر هنا مباشرة!",
                            color = MoodMutedText,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            val recentOrders = orders.take(4)
            items(recentOrders.size) { idx ->
                val order = recentOrders[idx]
                QuickOrderCard(
                    order = order,
                    onAdvanceStatus = { nextStatus ->
                        onQuickUpdateOrderStatus(order.id, nextStatus, order.orderCode)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        backgroundColor = MoodDarkCard,
        borderColor = color.copy(alpha = 0.25f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color.copy(alpha = 0.15f))
                        .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(18.dp))
                }

                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = MoodMutedText,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                color = MoodCreamText
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MoodMutedText,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun StatusProgressRow(
    label: String,
    count: Int,
    total: Int,
    color: Color
) {
    val progress = (count.toFloat() / total.toFloat()).coerceIn(0f, 1f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 12.sp, color = MoodCreamText)
            Text("$count (${(progress * 100).toInt()}%)", fontSize = 12.sp, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MoodDarkCardElevated
        )
    }
}

@Composable
private fun QuickOrderCard(
    order: OrderEntity,
    onAdvanceStatus: (OrderStatus) -> Unit
) {
    val currentStatusEnum = OrderStatus.values().find { it.name == order.status } ?: OrderStatus.PENDING
    val dateStr = SimpleDateFormat("HH:mm - yyyy/MM/dd", Locale("ar")).format(Date(order.createdAt))

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MoodDarkCard
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "#${order.orderCode}",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = MoodGoldPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    GlowBadge(
                        text = currentStatusEnum.textAr,
                        isTeal = currentStatusEnum == OrderStatus.READY || currentStatusEnum == OrderStatus.PREPARING
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${order.customerName} • ${order.totalAmount.toInt()} ج.م",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodCreamText
                )

                Text(
                    text = dateStr,
                    fontSize = 10.sp,
                    color = MoodMutedText
                )
            }

            // Fast Advance Button
            when (currentStatusEnum) {
                OrderStatus.PENDING -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF2196F3).copy(alpha = 0.2f))
                            .border(1.dp, Color(0xFF2196F3), RoundedCornerShape(10.dp))
                            .clickable { onAdvanceStatus(OrderStatus.PREPARING) }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text("بدء التحضير 👨‍🍳", color = Color(0xFF2196F3), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                OrderStatus.PREPARING -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF4CAF50).copy(alpha = 0.2f))
                            .border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(10.dp))
                            .clickable { onAdvanceStatus(OrderStatus.READY) }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text("جاهز للاستلام ✨", color = Color(0xFF4CAF50), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                OrderStatus.READY -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(MoodAmberPrimary.copy(alpha = 0.2f))
                            .border(1.dp, MoodAmberPrimary, RoundedCornerShape(10.dp))
                            .clickable { onAdvanceStatus(OrderStatus.COMPLETED) }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text("تم التسليم ✔", color = MoodAmberPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                else -> {
                    Text(
                        text = currentStatusEnum.textAr,
                        color = Color(currentStatusEnum.colorHex),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
