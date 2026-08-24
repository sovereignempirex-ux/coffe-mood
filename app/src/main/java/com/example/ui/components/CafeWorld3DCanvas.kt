package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import com.example.ui.theme.MoodAmberDark
import com.example.ui.theme.MoodAmberDeep
import com.example.ui.theme.MoodAmberGlow
import com.example.ui.theme.MoodAmberPrimary
import com.example.ui.theme.MoodDarkInk
import com.example.ui.theme.MoodGoldPrimary
import com.example.ui.theme.MoodGoldSecondary
import com.example.ui.theme.MoodTealGlow
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CafeWorld3DCanvas(
    modifier: Modifier = Modifier,
    motionEnabled: Boolean = true,
    intensity: Float = 1.0f
) {
    if (!motionEnabled) {
        // Ultra-lightweight Static Mode for Low-End Devices (Zero CPU/GPU animation load)
        Canvas(modifier = modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            drawRect(color = MoodDarkInk)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MoodAmberDark.copy(alpha = 0.18f * intensity),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.15f, height * 0.1f),
                    radius = width * 0.7f
                ),
                radius = width * 0.7f,
                center = Offset(width * 0.15f, height * 0.1f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MoodAmberDeep.copy(alpha = 0.22f * intensity),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.85f, height * 0.9f),
                    radius = width * 0.65f
                ),
                radius = width * 0.65f,
                center = Offset(width * 0.85f, height * 0.9f)
            )
        }
        return
    }

    val infiniteTransition = rememberInfiniteTransition(label = "CafeWorldAnimation")
    
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WorldTime"
    )

    val steamWave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SteamWave"
    )

    var touchOffsetX by remember { mutableFloatStateOf(0f) }
    var touchOffsetY by remember { mutableFloatStateOf(0f) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        touchOffsetX = (touchOffsetX + dragAmount.x * 0.04f).coerceIn(-30f, 30f)
                        touchOffsetY = (touchOffsetY + dragAmount.y * 0.04f).coerceIn(-30f, 30f)
                    },
                    onDragEnd = {
                        touchOffsetX = 0f
                        touchOffsetY = 0f
                    }
                )
            }
    ) {
        val width = size.width
        val height = size.height

        // 1. Pure Obsidian Base
        drawRect(color = MoodDarkInk)

        // 2. Immersive Ambient Glowing Spheres (from design: #D97706/20 top-left, #78350F/30 bottom-right)
        val sphere1X = width * 0.1f + (cos(Math.toRadians(time.toDouble())) * 25f).toFloat() + touchOffsetX
        val sphere1Y = height * 0.05f + (sin(Math.toRadians(time.toDouble())) * 20f).toFloat() + touchOffsetY
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    MoodAmberDark.copy(alpha = 0.22f * intensity),
                    MoodAmberPrimary.copy(alpha = 0.08f * intensity),
                    Color.Transparent
                ),
                center = Offset(sphere1X, sphere1Y),
                radius = width * 0.75f
            ),
            radius = width * 0.75f,
            center = Offset(sphere1X, sphere1Y)
        )

        val sphere2X = width * 0.9f - touchOffsetX
        val sphere2Y = height * 0.92f - touchOffsetY
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    MoodAmberDeep.copy(alpha = 0.32f * intensity),
                    MoodAmberDark.copy(alpha = 0.12f * intensity),
                    Color.Transparent
                ),
                center = Offset(sphere2X, sphere2Y),
                radius = width * 0.7f
            ),
            radius = width * 0.7f,
            center = Offset(sphere2X, sphere2Y)
        )

        // 3. Subtle Dot Matrix Grid Pattern (radial dots spaced ~24dp)
        val step = 28f
        val dotRadius = 1.0f
        var gx = 0f
        while (gx < width) {
            var gy = 0f
            while (gy < height) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.04f * intensity),
                    radius = dotRadius,
                    center = Offset(gx, gy)
                )
                gy += step
            }
            gx += step
        }

        // 4. Subtle Rising Coffee Steam Wave
        drawCoffeeSteam(width, height, steamWave, intensity, touchOffsetX)

        // 5. Ambient Micro Amber Floating Particles
        val particleCount = if (width < 600) 20 else 35
        for (i in 0 until particleCount) {
            val seed = i * 53.7f
            val px = (width * ((seed % 100) / 100f) + sin(Math.toRadians((time * 2f + seed).toDouble())) * 25f).toFloat() + touchOffsetX * 0.6f
            val py = ((height - ((time * (24f + (i % 4) * 8f) + seed * 16f) % height))) + touchOffsetY * 0.6f
            val radius = (1.2f + (i % 3) * 1.0f)
            val alpha = (0.2f + ((i % 4) * 0.12f)) * intensity

            drawCircle(
                color = MoodAmberPrimary.copy(alpha = alpha),
                radius = radius,
                center = Offset(px.coerceIn(0f, width), py.coerceIn(0f, height))
            )
        }
    }
}

private fun DrawScope.drawCoffeeSteam(width: Float, height: Float, wave: Float, intensity: Float, touchX: Float) {
    val steamCenterX = width * 0.5f + touchX * 0.5f
    val steamBaseY = height * 0.62f
    
    val path1 = Path().apply {
        moveTo(steamCenterX - 25f, steamBaseY)
        cubicTo(
            steamCenterX - 45f + sin(wave.toDouble()).toFloat() * 20f, steamBaseY - 90f,
            steamCenterX - 10f - cos(wave.toDouble()).toFloat() * 25f, steamBaseY - 180f,
            steamCenterX - 35f + sin(wave.toDouble()).toFloat() * 30f, steamBaseY - 280f
        )
    }

    drawPath(
        path = path1,
        color = MoodAmberPrimary.copy(alpha = 0.12f * intensity),
        style = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 3.5f,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    )
}

