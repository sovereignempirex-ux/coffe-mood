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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CafeSettingsEntity
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
fun AdminSettingsSection(
    settings: CafeSettingsEntity,
    onSaveSettings: (CafeSettingsEntity) -> Unit
) {
    var cafeNameAr by remember(settings) { mutableStateOf(settings.cafeNameAr) }
    var cafeNameEn by remember(settings) { mutableStateOf(settings.cafeNameEn) }
    var heroTitle by remember(settings) { mutableStateOf(settings.heroTitle) }
    var heroTagline by remember(settings) { mutableStateOf(settings.heroTagline) }
    var whatsappNumber by remember(settings) { mutableStateOf(settings.whatsappNumber) }
    var phoneNumber by remember(settings) { mutableStateOf(settings.phoneNumber) }
    var locationAddress by remember(settings) { mutableStateOf(settings.locationAddress) }
    var workingHours by remember(settings) { mutableStateOf(settings.workingHours) }
    var instagramUrl by remember(settings) { mutableStateOf(settings.instagramUrl) }
    var facebookUrl by remember(settings) { mutableStateOf(settings.facebookUrl) }
    var mapsUrl by remember(settings) { mutableStateOf(settings.mapsUrl) }
    var adminPin by remember(settings) { mutableStateOf(settings.adminPin) }
    var storeStatus by remember(settings) { mutableStateOf(settings.storeStatus) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
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
                        text = "إعدادات الموقع ومعلومات الكافيه",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodCreamText
                    )
                    Text(
                        text = "تعديل تفاصيل الاتصال، الروابط، ساعات العمل ورمز الأمان",
                        fontSize = 12.sp,
                        color = MoodMutedText
                    )
                }

                GoldGradientButton(
                    text = "حفظ التعديلات ✔",
                    onClick = {
                        onSaveSettings(
                            settings.copy(
                                cafeNameAr = cafeNameAr,
                                cafeNameEn = cafeNameEn,
                                heroTitle = heroTitle,
                                heroTagline = heroTagline,
                                whatsappNumber = whatsappNumber,
                                phoneNumber = phoneNumber,
                                locationAddress = locationAddress,
                                workingHours = workingHours,
                                instagramUrl = instagramUrl,
                                facebookUrl = facebookUrl,
                                mapsUrl = mapsUrl,
                                adminPin = adminPin,
                                storeStatus = storeStatus
                            )
                        )
                    }
                )
            }
        }

        // 1. Cafe Identity & Hero Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = MoodDarkCard
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Store, contentDescription = null, tint = MoodAmberPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("هوية الكافيه والشعار الترحيبي", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MoodCreamText)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = cafeNameAr,
                            onValueChange = { cafeNameAr = it },
                            label = { Text("اسم الكافيه (عربي)", color = MoodMutedText, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MoodAmberPrimary,
                                unfocusedBorderColor = MoodGlassBorder,
                                focusedTextColor = MoodCreamText,
                                unfocusedTextColor = MoodCreamText
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = cafeNameEn,
                            onValueChange = { cafeNameEn = it },
                            label = { Text("الاسم (English)", color = MoodMutedText, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MoodAmberPrimary,
                                unfocusedBorderColor = MoodGlassBorder,
                                focusedTextColor = MoodCreamText,
                                unfocusedTextColor = MoodCreamText
                            ),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = heroTitle,
                        onValueChange = { heroTitle = it },
                        label = { Text("عنوان الترحيب الرئيسي في الصفحة الأولى", color = MoodMutedText, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodAmberPrimary,
                            unfocusedBorderColor = MoodGlassBorder,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = heroTagline,
                        onValueChange = { heroTagline = it },
                        label = { Text("الشعار الفرعي (Tagline)", color = MoodMutedText, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodAmberPrimary,
                            unfocusedBorderColor = MoodGlassBorder,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // 2. Contact & Hours Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = MoodDarkCard
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = MoodTealGlow, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("بيانات التواصل وساعات العمل", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MoodCreamText)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = whatsappNumber,
                            onValueChange = { whatsappNumber = it },
                            label = { Text("رقم الواتساب لاستقبال الطلبات", color = MoodMutedText, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MoodAmberPrimary,
                                unfocusedBorderColor = MoodGlassBorder,
                                focusedTextColor = MoodCreamText,
                                unfocusedTextColor = MoodCreamText
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("رقم هاتف الاتصال المباشر", color = MoodMutedText, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MoodAmberPrimary,
                                unfocusedBorderColor = MoodGlassBorder,
                                focusedTextColor = MoodCreamText,
                                unfocusedTextColor = MoodCreamText
                            ),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = locationAddress,
                        onValueChange = { locationAddress = it },
                        label = { Text("العنوان الفعلي (مثال: المنصورة، شارع المشاية)", color = MoodMutedText, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodAmberPrimary,
                            unfocusedBorderColor = MoodGlassBorder,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = workingHours,
                        onValueChange = { workingHours = it },
                        label = { Text("مواعيد العمل (مثال: يومياً من 8 صباحاً حتى 2 بعد منتصف الليل)", color = MoodMutedText, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodAmberPrimary,
                            unfocusedBorderColor = MoodGlassBorder,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // 3. Social Media & Maps Links
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = MoodDarkCard
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Link, contentDescription = null, tint = MoodGoldPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("روابط التواصل الاجتماعي والخرائط", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MoodCreamText)
                    }

                    OutlinedTextField(
                        value = instagramUrl,
                        onValueChange = { instagramUrl = it },
                        label = { Text("رابط حساب انستجرام (Instagram URL)", color = MoodMutedText, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodAmberPrimary,
                            unfocusedBorderColor = MoodGlassBorder,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = facebookUrl,
                        onValueChange = { facebookUrl = it },
                        label = { Text("رابط صفحة فيسبوك (Facebook URL)", color = MoodMutedText, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodAmberPrimary,
                            unfocusedBorderColor = MoodGlassBorder,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = mapsUrl,
                        onValueChange = { mapsUrl = it },
                        label = { Text("رابط موقع الكافيه على خرائط جوجل (Google Maps)", color = MoodMutedText, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodAmberPrimary,
                            unfocusedBorderColor = MoodGlassBorder,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // 4. Admin Security PIN Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = MoodDarkCard,
                borderColor = MoodAmberPrimary.copy(alpha = 0.3f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = MoodAmberPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("رمز PIN السري لحماية لوحة الإدارة", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MoodCreamText)
                    }

                    Text(
                        text = "يستخدم هذا الرمز لحماية الوصول إلى لوحة الإدارة ومنع الدخول غير المصرح به.",
                        fontSize = 11.sp,
                        color = MoodMutedText
                    )

                    OutlinedTextField(
                        value = adminPin,
                        onValueChange = { adminPin = it },
                        label = { Text("رمز PIN السري (4 أرقام)", color = MoodMutedText, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(0.5f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MoodAmberPrimary,
                            unfocusedBorderColor = MoodGlassBorder,
                            focusedTextColor = MoodCreamText,
                            unfocusedTextColor = MoodCreamText
                        ),
                        singleLine = true
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
