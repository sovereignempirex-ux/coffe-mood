package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CafeWorld3DCanvas
import com.example.ui.components.GoldGradientButton
import com.example.ui.theme.MoodAmberGlow
import com.example.ui.theme.MoodCreamText
import com.example.ui.theme.MoodDarkEspresso
import com.example.ui.theme.MoodDarkInk
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodGoldSecondary
import com.example.ui.theme.MoodTealGlow
import kotlinx.coroutines.delay

@Composable
fun IntroCinematicOverlay(
    onEnterCafe: () -> Unit
) {
    var step by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(400)
        step = 1
        delay(1200)
        step = 2
    }

    val scale by animateFloatAsState(
        targetValue = if (step >= 1) 1.0f else 0.7f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "LogoScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (step >= 1) 1.0f else 0.0f,
        animationSpec = tween(durationMillis = 800),
        label = "LogoAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MoodDarkInk)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onEnterCafe
            ),
        contentAlignment = Alignment.Center
    ) {
        // 3D Canvas Background
        CafeWorld3DCanvas(intensity = 1.4f)

        // Vignette Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Transparent, MoodDarkInk.copy(alpha = 0.85f)),
                        radius = 900f
                    )
                )
        )

        Column(
            modifier = Modifier
                .padding(32.dp)
                .scale(scale)
                .alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Glowing Emblem
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(MoodGoldPrimary.copy(alpha = 0.35f), MoodAmberGlow.copy(alpha = 0.1f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "☕",
                    fontSize = 46.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "مزاج Mood",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = MoodGoldPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "CAFE & MUSIC LOUNGE",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MoodTealGlow,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "قهوة، طرب، وعالم له مزاجه الخاص",
                fontSize = 16.sp,
                color = MoodCreamText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            AnimatedVisibility(
                visible = step >= 2,
                enter = fadeIn(tween(600)),
                exit = fadeOut()
            ) {
                GoldGradientButton(
                    text = "دخول إلى الكافيه  ←",
                    onClick = onEnterCafe,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
