package com.example.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CafeWorld3DCanvas
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowBadge
import com.example.ui.components.GoldGradientButton
import com.example.ui.theme.MoodAmberDark
import com.example.ui.theme.MoodAmberPrimary
import com.example.ui.theme.MoodCreamText
import com.example.ui.theme.MoodDarkCard
import com.example.ui.theme.MoodDarkCardElevated
import com.example.ui.theme.MoodDarkInk
import com.example.ui.theme.MoodGlassBorder
import com.example.ui.theme.MoodGlassSurface
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodMutedText
import com.example.ui.theme.MoodTealGlow
import com.example.ui.theme.MoodTealNeon
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MoodCafeViewModel

@Composable
fun AdminSecurityGate(
    viewModel: MoodCafeViewModel,
    onSuccess: () -> Unit
) {
    var enteredPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MoodDarkInk),
        contentAlignment = Alignment.Center
    ) {
        CafeWorld3DCanvas(intensity = 0.8f)

        // Top Back Action
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(AppScreen.HOME) },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MoodDarkCardElevated)
                    .border(1.dp, MoodGlassBorder, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "رجوع للقائمة", tint = MoodCreamText)
            }
        }

        // Security Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(28.dp),
            backgroundColor = MoodDarkCard.copy(alpha = 0.85f),
            borderColor = MoodGlassBorder
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Shield Icon
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(MoodAmberPrimary.copy(alpha = 0.25f), Color.Transparent)
                            )
                        )
                        .border(1.5.dp, MoodAmberPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "أمان الإدارة",
                        tint = MoodAmberPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "بوابة الإدارة الآمنة",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MoodCreamText
                )

                Text(
                    text = "MOOD ADMIN SECURITY GATE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoodTealGlow,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "يرجى إدخال رمز PIN السري للوصول إلى لوحة التحكم والعمليات الإدارية.",
                    fontSize = 12.sp,
                    color = MoodMutedText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // PIN Digits Display (4 Circles)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 4) {
                        val isFilled = i < enteredPin.length
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isFilled) MoodAmberPrimary else MoodDarkCardElevated
                                )
                                .border(
                                    1.dp,
                                    if (isFilled) MoodAmberPrimary else MoodGlassBorder,
                                    CircleShape
                                )
                        )
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = errorMessage ?: "",
                        fontSize = 12.sp,
                        color = Color(0xFFFF5252),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Keypad
                val keypad = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("C", "0", "OK")
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    keypad.forEach { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            row.forEach { key ->
                                Box(
                                    modifier = Modifier
                                        .size(62.dp, 50.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            when (key) {
                                                "OK" -> MoodAmberPrimary
                                                "C" -> MoodDarkCardElevated
                                                else -> MoodGlassSurface
                                            }
                                        )
                                        .border(
                                            1.dp,
                                            if (key == "OK") MoodAmberPrimary else MoodGlassBorder,
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            errorMessage = null
                                            when (key) {
                                                "C" -> {
                                                    enteredPin = ""
                                                }
                                                "OK" -> {
                                                    if (enteredPin.length == 4) {
                                                        if (viewModel.unlockAdmin(enteredPin)) {
                                                            onSuccess()
                                                        } else {
                                                            errorMessage = "رمز PIN غير صحيح!"
                                                            enteredPin = ""
                                                        }
                                                    } else {
                                                        errorMessage = "أدخل 4 أرقام"
                                                    }
                                                }
                                                else -> {
                                                    if (enteredPin.length < 4) {
                                                        enteredPin += key
                                                        if (enteredPin.length == 4) {
                                                            if (viewModel.unlockAdmin(enteredPin)) {
                                                                onSuccess()
                                                            } else {
                                                                errorMessage = "رمز PIN غير صحيح!"
                                                                enteredPin = ""
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = key,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (key == "OK") MoodDarkInk else MoodCreamText
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Fast Quick Unlock for Demo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            enteredPin = "7777"
                            if (viewModel.unlockAdmin("7777")) {
                                onSuccess()
                            }
                        }
                    ) {
                        Text(
                            text = "استخدام الرمز الافتراضي (7777)",
                            color = MoodTealGlow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(
                        onClick = { viewModel.navigateTo(AppScreen.HOME) }
                    ) {
                        Text(
                            text = "إلغاء",
                            color = MoodMutedText,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
