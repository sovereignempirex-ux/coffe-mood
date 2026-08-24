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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
fun AdminThemeSection(
    settings: CafeSettingsEntity,
    onSaveTheme: (
        primaryHex: String,
        secondaryHex: String,
        glowHex: String,
        backgroundHex: String,
        cornerRadius: Int,
        buttonStyle: String,
        motionEnabled: Boolean
    ) -> Unit
) {
    var primaryColorHex by remember(settings) { mutableStateOf(settings.primaryGoldHex) }
    var secondaryColorHex by remember(settings) { mutableStateOf(settings.accentTealHex) }
    var glowColorHex by remember(settings) { mutableStateOf(settings.glowColorHex) }
    var backgroundHex by remember(settings) { mutableStateOf(settings.backgroundDarkHex) }
    var buttonRadius by remember(settings) { mutableIntStateOf(settings.buttonCornerRadiusDp) }
    var buttonStyle by remember(settings) { mutableStateOf(settings.buttonStyle) }
    var motionEnabled by remember(settings) { mutableStateOf(settings.motionEnabled) }

    val colorPresets = listOf(
        Triple("#F59E0B", "ذهبي كهرماني (Mood Amber)", Color(0xFFF59E0B)),
        Triple("#10B981", "زمردي نيون (Emerald Glow)", Color(0xFF10B981)),
        Triple("#06B6D4", "سماوي سايبر (Cyan Frost)", Color(0xFF06B6D4)),
        Triple("#8B5CF6", "بنفسجي ملكي (Royal Violet)", Color(0xFF8B5CF6)),
        Triple("#F43F5E", "وردي مغروب (Rose Sunset)", Color(0xFFF43F5E)),
        Triple("#EF4444", "أحمر قرمزي (Crimson Ruby)", Color(0xFFEF4444))
    )

    val backgroundPresets = listOf(
        Pair("#050402", "سواد كوني (Obsidian Noir)"),
        Pair("#0D0B08", "إسبريسو داكن (Deep Espresso)"),
        Pair("#0F172A", "كربون أزرق (Carbon Slate)")
    )

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
                        text = "تخصيص الهوية البصرية والمظهر (Theme Customizer)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoodCreamText
                    )
                    Text(
                        text = "تحكم كامل بألوان التطبيق، انحناءات الأزرار، والـ 3D Canvas",
                        fontSize = 12.sp,
                        color = MoodMutedText
                    )
                }

                GoldGradientButton(
                    text = "تطبيق وحفظ الثيم 🎨",
                    onClick = {
                        onSaveTheme(
                            primaryColorHex,
                            secondaryColorHex,
                            glowColorHex,
                            backgroundHex,
                            buttonRadius,
                            buttonStyle,
                            motionEnabled
                        )
                    }
                )
            }
        }

        // Live Preview Box
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = MoodDarkCard,
                borderColor = MoodAmberPrimary.copy(alpha = 0.5f)
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
                        Text("معاينة تفاعلية حية (Live Preview)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MoodCreamText)
                        GlowBadge(text = "مباشر LIVE", isTeal = true)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Preview Item Sample
                    val primaryColor = runCatching { Color(android.graphics.Color.parseColor(primaryColorHex)) }.getOrDefault(MoodAmberPrimary)
                    val glowColor = runCatching { Color(android.graphics.Color.parseColor(glowColorHex)) }.getOrDefault(MoodTealGlow)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(buttonRadius.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(primaryColor, primaryColor.copy(alpha = 0.7f))
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("إسبريسو دبل فاخر ☕", color = Color(0xFF0D0B08), fontWeight = FontWeight.Black, fontSize = 15.sp)
                                Text("سعر العرض: 65 جنيه", color = Color(0xFF0D0B08).copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape((buttonRadius / 2).coerceAtLeast(6).dp))
                                    .background(Color(0xFF0D0B08))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("اطلب الآن ✦", color = primaryColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 1. Primary Accent Color
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
                        Icon(Icons.Default.Palette, contentDescription = null, tint = MoodAmberPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("اللون الأساسي للبراند (Primary Accent Color)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MoodCreamText)
                    }

                    // Preset Color Circles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        colorPresets.forEach { (hex, _, color) ->
                            val isSelected = primaryColorHex.equals(hex, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        2.dp,
                                        if (isSelected) MoodCreamText else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { primaryColorHex = hex },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = "محدد", tint = Color.Black, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }

                    // Custom Hex Input
                    OutlinedTextField(
                        value = primaryColorHex,
                        onValueChange = { primaryColorHex = it },
                        label = { Text("رمز اللون السداسي مخصص (HEX)", color = MoodMutedText, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(0.6f),
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

        // 2. Background Canvas Style
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
                        Icon(Icons.Default.Tune, contentDescription = null, tint = MoodTealGlow, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("درجة السواد للخلفية الزجاجية (Background Canvas)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MoodCreamText)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        backgroundPresets.forEach { (hex, title) ->
                            val isSelected = backgroundHex.equals(hex, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) MoodAmberPrimary.copy(alpha = 0.2f) else MoodDarkCardElevated)
                                    .border(1.dp, if (isSelected) MoodAmberPrimary else MoodGlassBorder, RoundedCornerShape(12.dp))
                                    .clickable { backgroundHex = hex }
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = title,
                                    color = if (isSelected) MoodAmberPrimary else MoodCreamText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Button Styles & Corner Radius
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
                    Text("انحناءات الأزرار والبطاقات (Corner Radius)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MoodCreamText)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(
                            Pair(8, "حواف ناعمة (8dp)"),
                            Pair(16, "دائري عصري (16dp)"),
                            Pair(28, "كبسولة كروية (28dp)")
                        ).forEach { (radius, label) ->
                            val isSelected = buttonRadius == radius
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) MoodAmberPrimary else MoodDarkCardElevated)
                                    .border(1.dp, if (isSelected) MoodAmberPrimary else MoodGlassBorder, RoundedCornerShape(12.dp))
                                    .clickable { buttonRadius = radius }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color(0xFF0D0B08) else MoodCreamText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 3D Motion Canvas Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("تأثيرات الجزيئات ثلاثية الأبعاد (3D Canvas)", color = MoodCreamText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("تشغيل أو إيقاف حركة الذرات العائمة في الخلفية لتوفير البطارية", color = MoodMutedText, fontSize = 11.sp)
                        }

                        Switch(
                            checked = motionEnabled,
                            onCheckedChange = { motionEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MoodDarkCard,
                                checkedTrackColor = Color(0xFF10B981)
                            )
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
