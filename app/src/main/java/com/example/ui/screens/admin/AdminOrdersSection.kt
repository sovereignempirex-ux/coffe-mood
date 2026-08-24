package com.example.ui.screens.admin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderEntity
import com.example.data.model.OrderStatus
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowBadge
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminOrdersSection(
    orders: List<OrderEntity>,
    onUpdateStatus: (Long, OrderStatus, String) -> Unit
) {
    val context = LocalContext.current
    var selectedStatusFilter by remember { mutableStateOf<String?>("ALL") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredOrders = orders.filter { order ->
        val matchesStatus = selectedStatusFilter == "ALL" || order.status == selectedStatusFilter
        val matchesSearch = searchQuery.isBlank() ||
                order.orderCode.contains(searchQuery, ignoreCase = true) ||
                order.customerName.contains(searchQuery, ignoreCase = true) ||
                order.customerPhone.contains(searchQuery)
        matchesStatus && matchesSearch
    }

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
                        text = "إدارة ومتابعة طلبات العملاء",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodCreamText
                    )
                    Text(
                        text = "متابعة وتحديث حالات الطلبات والتواصل الفوري مع العميل",
                        fontSize = 12.sp,
                        color = MoodMutedText
                    )
                }

                GlowBadge(
                    text = "${filteredOrders.size} طلب",
                    isTeal = true
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("ابحث برقم الطلب #MZ، اسم العميل، أو رقم الهاتف...", fontSize = 13.sp, color = MoodMutedText) },
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

        // Status Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    StatusFilterChip(
                        label = "الكل (${orders.size})",
                        isSelected = selectedStatusFilter == "ALL",
                        color = MoodAmberPrimary,
                        onClick = { selectedStatusFilter = "ALL" }
                    )
                }

                OrderStatus.values().forEach { st ->
                    val count = orders.count { it.status == st.name }
                    item {
                        StatusFilterChip(
                            label = "${st.textAr} ($count)",
                            isSelected = selectedStatusFilter == st.name,
                            color = Color(st.colorHex),
                            onClick = { selectedStatusFilter = st.name }
                        )
                    }
                }
            }
        }

        // Orders List
        if (filteredOrders.isEmpty()) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = MoodDarkCard
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("لا توجد طلبات مطابقة للفلتر المحدد", color = MoodMutedText, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(filteredOrders, key = { it.id }) { order ->
                AdminOrderCard(
                    order = order,
                    onUpdateStatus = { newSt ->
                        onUpdateStatus(order.id, newSt, order.orderCode)
                    },
                    onOpenWhatsApp = { phone, msg ->
                        openWhatsAppChat(context, phone, msg)
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
private fun StatusFilterChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) color.copy(alpha = 0.25f) else MoodDarkCard)
            .border(1.dp, if (isSelected) color else MoodGlassBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) color else MoodCreamText,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold
        )
    }
}

@Composable
private fun AdminOrderCard(
    order: OrderEntity,
    onUpdateStatus: (OrderStatus) -> Unit,
    onOpenWhatsApp: (String, String) -> Unit
) {
    val currentStatusEnum = OrderStatus.values().find { it.name == order.status } ?: OrderStatus.PENDING
    val dateStr = SimpleDateFormat("yyyy/MM/dd - HH:mm", Locale("ar")).format(Date(order.createdAt))
    var statusMenuExpanded by remember { mutableStateOf(false) }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = MoodDarkCard,
        borderColor = Color(currentStatusEnum.colorHex).copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Code, Date & Status Dropdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "#${order.orderCode}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = MoodGoldPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = dateStr,
                            fontSize = 10.sp,
                            color = MoodMutedText
                        )
                    }
                }

                // Interactive Status Pill
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(currentStatusEnum.colorHex).copy(alpha = 0.2f))
                            .border(1.dp, Color(currentStatusEnum.colorHex), RoundedCornerShape(10.dp))
                            .clickable { statusMenuExpanded = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${currentStatusEnum.textAr} ▾",
                            color = Color(currentStatusEnum.colorHex),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DropdownMenu(
                        expanded = statusMenuExpanded,
                        onDismissRequest = { statusMenuExpanded = false },
                        modifier = Modifier.background(MoodDarkCardElevated)
                    ) {
                        OrderStatus.values().forEach { st ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "${st.textAr} (${st.textEn})",
                                        color = Color(st.colorHex),
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    onUpdateStatus(st)
                                    statusMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Customer Info & Quick WhatsApp Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = MoodTealGlow, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = order.customerName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodCreamText
                    )
                    if (order.customerPhone.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = order.customerPhone,
                            fontSize = 11.sp,
                            color = MoodMutedText
                        )
                    }
                }

                if (order.customerPhone.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF25D366).copy(alpha = 0.2f))
                            .border(1.dp, Color(0xFF25D366), RoundedCornerShape(8.dp))
                            .clickable {
                                val msg = "مرحباً ${order.customerName}، بخصوص طلبك #${order.orderCode} من كافيه مزاج، أصبح الآن: ${currentStatusEnum.textAr}."
                                onOpenWhatsApp(order.customerPhone, msg)
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Chat, contentDescription = "واتساب", tint = Color(0xFF25D366), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("واتساب", color = Color(0xFF25D366), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Items Summary Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MoodDarkCardElevated)
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "تفاصيل الأصناف المطلوبة:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodAmberPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = order.itemsSummary,
                        fontSize = 12.sp,
                        color = MoodCreamText,
                        lineHeight = 18.sp
                    )

                    if (order.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "ملاحظات العميل: ${order.notes}",
                            fontSize = 11.sp,
                            color = Color(0xFFFFC107),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total & Fast Advance Action Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "الإجمالي: ", fontSize = 13.sp, color = MoodMutedText)
                    Text(
                        text = "${order.totalAmount.toInt()} ج.م",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = MoodGoldPrimary
                    )
                }

                // Fast next status buttons
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    when (currentStatusEnum) {
                        OrderStatus.PENDING -> {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF2196F3))
                                    .clickable { onUpdateStatus(OrderStatus.PREPARING) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("بدء التحضير 👨‍🍳", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        OrderStatus.PREPARING -> {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF4CAF50))
                                    .clickable { onUpdateStatus(OrderStatus.READY) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("جاهز للاستلام ✨", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        OrderStatus.READY -> {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MoodAmberPrimary)
                                    .clickable { onUpdateStatus(OrderStatus.COMPLETED) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("تم التسليم ✔", color = Color(0xFF0D0B08), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

private fun openWhatsAppChat(context: Context, phone: String, message: String) {
    val cleanPhone = phone.replace("+", "").replace(" ", "").trim()
    val encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
    val uri = Uri.parse("https://wa.me/$cleanPhone?text=$encodedMsg")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "تعذر فتح تطبيق واتساب", Toast.LENGTH_SHORT).show()
    }
}
