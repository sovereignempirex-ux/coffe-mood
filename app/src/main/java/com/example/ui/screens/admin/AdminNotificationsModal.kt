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
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppNotification
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
fun AdminNotificationsModal(
    notifications: List<AppNotification>,
    onMarkAsRead: (String) -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit
) {
    val unreadCount = notifications.count { !it.isRead }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MoodDarkCard,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MoodAmberPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "مركز الإشعارات الحية",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodCreamText
                    )
                    if (unreadCount > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        GlowBadge(text = "$unreadCount جديد", isTeal = true)
                    }
                }

                if (notifications.isNotEmpty()) {
                    IconButton(onClick = onClearAll, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.ClearAll, contentDescription = "مسح الكل", tint = MoodMutedText)
                    }
                }
            }
        },
        text = {
            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "لا توجد إشعارات جديدة حالياً. كل التنبيهات المباشرة ستصلك فوراً!",
                        color = MoodMutedText,
                        fontSize = 12.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    items(notifications, key = { it.id }) { notif ->
                        val dateStr = SimpleDateFormat("HH:mm - MM/dd", Locale("ar")).format(Date(notif.timestamp))
                        val isOrder = notif.type == "ORDER"

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (!notif.isRead) MoodAmberPrimary.copy(alpha = 0.12f) else MoodDarkCardElevated
                                )
                                .border(
                                    1.dp,
                                    if (!notif.isRead) MoodAmberPrimary.copy(alpha = 0.4f) else MoodGlassBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onMarkAsRead(notif.id) }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isOrder) MoodAmberPrimary.copy(alpha = 0.2f) else MoodTealGlow.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isOrder) Icons.Default.ReceiptLong else Icons.Default.Update,
                                        contentDescription = null,
                                        tint = if (isOrder) MoodAmberPrimary else MoodTealGlow,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = notif.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = MoodCreamText
                                        )
                                        Text(
                                            text = dateStr,
                                            fontSize = 10.sp,
                                            color = MoodMutedText
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = notif.message,
                                        fontSize = 11.sp,
                                        color = MoodMutedText
                                    )
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
