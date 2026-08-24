package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MoodAmberDark
import com.example.ui.theme.MoodAmberDeep
import com.example.ui.theme.MoodAmberPrimary
import com.example.ui.theme.MoodDarkCard
import com.example.ui.theme.MoodDarkEspresso
import com.example.ui.theme.MoodDarkInk
import com.example.ui.theme.MoodGlassBorder
import com.example.ui.theme.MoodGlassHighlight
import com.example.ui.theme.MoodGlassSurface
import com.example.ui.theme.MoodGlassSurfaceElevated
import com.example.ui.theme.MoodGoldDark
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodGoldSecondary
import com.example.ui.theme.MoodTealGlow
import com.example.ui.theme.MoodTealNeon

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = MoodGlassSurface,
    borderColor: Color = MoodGlassBorder,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 6.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                spotColor = MoodAmberPrimary.copy(alpha = 0.15f),
                ambientColor = Color.Black
            )
            .border(
                border = BorderStroke(borderWidth, borderColor),
                shape = shape
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        onClick = onClick ?: {}
    ) {
        content()
    }
}

@Composable
fun GoldGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    isOutline: Boolean = false,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(16.dp)
    val backgroundBrush = if (isOutline) {
        Brush.linearGradient(listOf(MoodGlassSurfaceElevated, MoodGlassSurface))
    } else {
        Brush.linearGradient(listOf(MoodAmberPrimary, MoodAmberDark))
    }

    val borderStroke = if (isOutline) {
        BorderStroke(1.dp, MoodGlassBorder)
    } else {
        BorderStroke(1.dp, MoodAmberPrimary.copy(alpha = 0.6f))
    }

    val contentColor = if (isOutline) MoodAmberPrimary else MoodDarkInk

    Box(
        modifier = modifier
            .clip(shape)
            .border(borderStroke, shape)
            .background(backgroundBrush)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.invoke()
            if (icon != null) {
                Box(modifier = Modifier.padding(start = 8.dp))
            }
            Text(
                text = text,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun GlowBadge(
    text: String,
    modifier: Modifier = Modifier,
    isTeal: Boolean = false
) {
    val bgColor = if (isTeal) MoodTealNeon.copy(alpha = 0.15f) else MoodAmberPrimary.copy(alpha = 0.2f)
    val borderColor = if (isTeal) MoodTealGlow.copy(alpha = 0.6f) else MoodAmberPrimary.copy(alpha = 0.5f)
    val textColor = if (isTeal) MoodTealGlow else MoodAmberPrimary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(0.8.dp, borderColor, RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

