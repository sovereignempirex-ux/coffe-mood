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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.OrderEntity
import com.example.data.model.OrderStatus
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminUsersSection(
    users: List<UserEntity>,
    orders: List<OrderEntity>,
    onToggleRole: (UserEntity) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedUserForHistory by remember { mutableStateOf<UserEntity?>(null) }

    val filteredUsers = users.filter { u ->
        searchQuery.isBlank() ||
                u.name.contains(searchQuery, ignoreCase = true) ||
                u.email.contains(searchQuery, ignoreCase = true) ||
                u.phone.contains(searchQuery)
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
                        text = "إدارة حسابات ومستخدمي كافيه مزاج",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodCreamText
                    )
                    Text(
                        text = "متابعة بيانات العملاء، إجمالي المشتريات، وتعيين صلاحيات المديرين",
                        fontSize = 12.sp,
                        color = MoodMutedText
                    )
                }

                GlowBadge(
                    text = "${users.size} مستخدم",
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
                placeholder = { Text("ابحث باسم المستخدم، البريد، أو رقم الهاتف...", fontSize = 13.sp, color = MoodMutedText) },
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

        // Users List
        if (filteredUsers.isEmpty()) {
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
                        Text("لا يوجد مستخدمين مطابقين للبحث", color = MoodMutedText, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(filteredUsers, key = { it.id }) { user ->
                val userOrders = orders.filter { it.customerEmail == user.email || it.customerPhone == user.phone }
                val totalSpent = userOrders.filter { it.status != OrderStatus.CANCELLED.name }.sumOf { it.totalAmount }

                AdminUserCard(
                    user = user,
                    orderCount = userOrders.size,
                    totalSpent = totalSpent,
                    onViewHistory = { selectedUserForHistory = user },
                    onToggleRole = { onToggleRole(user) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // User Order History Dialog
    if (selectedUserForHistory != null) {
        val user = selectedUserForHistory!!
        val userOrders = orders.filter { it.customerEmail == user.email || it.customerPhone == user.phone }

        UserOrderHistoryDialog(
            user = user,
            orders = userOrders,
            onDismiss = { selectedUserForHistory = null }
        )
    }
}

@Composable
private fun AdminUserCard(
    user: UserEntity,
    orderCount: Int,
    totalSpent: Double,
    onViewHistory: () -> Unit,
    onToggleRole: () -> Unit
) {
    val isAdmin = user.role == UserRole.ADMIN.name

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = MoodDarkCard,
        borderColor = if (isAdmin) MoodAmberPrimary.copy(alpha = 0.5f) else MoodGlassBorder
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (isAdmin) MoodAmberPrimary else MoodDarkCardElevated)
                        .border(1.dp, if (isAdmin) MoodAmberPrimary else MoodGlassBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.take(1).uppercase(Locale.getDefault()),
                        color = if (isAdmin) Color(0xFF0D0B08) else MoodCreamText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MoodCreamText
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        GlowBadge(
                            text = if (isAdmin) "مدير (ADMIN) 👑" else "عميل (USER)",
                            isTeal = !isAdmin
                        )
                    }

                    Text(
                        text = "${user.email} • ${user.phone}",
                        fontSize = 11.sp,
                        color = MoodMutedText
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats Row & Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MoodDarkCardElevated)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        Text("الطلبات", fontSize = 10.sp, color = MoodMutedText)
                        Text("$orderCount طلب", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MoodCreamText)
                    }

                    Column {
                        Text("إجمالي المنفق", fontSize = 10.sp, color = MoodMutedText)
                        Text("${totalSpent.toInt()} ج.م", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MoodGoldPrimary)
                    }
                }

                // Action Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // History Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MoodTealGlow.copy(alpha = 0.15f))
                            .border(1.dp, MoodTealGlow.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable(onClick = onViewHistory)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.History, contentDescription = "السجل", tint = MoodTealGlow, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("السجل", color = MoodTealGlow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Role Toggle Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isAdmin) Color(0xFFFF5252).copy(alpha = 0.15f) else MoodAmberPrimary.copy(alpha = 0.2f))
                            .border(1.dp, if (isAdmin) Color(0xFFFF5252) else MoodAmberPrimary, RoundedCornerShape(8.dp))
                            .clickable(onClick = onToggleRole)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = "الرتبة",
                                tint = if (isAdmin) Color(0xFFFF5252) else MoodAmberPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isAdmin) "تجريد الرتبة" else "ترقية لمدير",
                                color = if (isAdmin) Color(0xFFFF5252) else MoodAmberPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserOrderHistoryDialog(
    user: UserEntity,
    orders: List<OrderEntity>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MoodDarkCard,
        title = {
            Column {
                Text(
                    text = "سجل طلبات: ${user.name}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodCreamText
                )
                Text(
                    text = "${orders.size} طلبات مسجلة بإجمالي ${orders.sumOf { it.totalAmount }.toInt()} ج.م",
                    fontSize = 11.sp,
                    color = MoodMutedText
                )
            }
        },
        text = {
            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("لا توجد طلبات سابقة لهذا العميل بعد", color = MoodMutedText, fontSize = 12.sp)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(orders, key = { it.id }) { order ->
                        val currentStatusEnum = OrderStatus.values().find { it.name == order.status } ?: OrderStatus.PENDING
                        val dateStr = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ar")).format(Date(order.createdAt))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MoodDarkCardElevated)
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("#${order.orderCode}", fontWeight = FontWeight.Bold, color = MoodGoldPrimary, fontSize = 12.sp)
                                    Text(
                                        currentStatusEnum.textAr,
                                        color = Color(currentStatusEnum.colorHex),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(order.itemsSummary, fontSize = 11.sp, color = MoodCreamText, maxLines = 2)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(dateStr, fontSize = 9.sp, color = MoodMutedText)
                                    Text("${order.totalAmount.toInt()} ج.م", fontSize = 11.sp, color = MoodAmberPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("إغلاق", color = MoodAmberPrimary, fontWeight = FontWeight.Bold)
            }
        }
    )
}
