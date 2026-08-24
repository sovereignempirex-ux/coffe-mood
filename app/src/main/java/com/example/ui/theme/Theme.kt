package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MoodGoldPrimary,
    onPrimary = MoodDarkInk,
    primaryContainer = MoodGoldDark,
    onPrimaryContainer = MoodGoldPrimary,
    secondary = MoodTealNeon,
    onSecondary = MoodDarkInk,
    secondaryContainer = MoodTealGlow,
    onSecondaryContainer = MoodDarkInk,
    tertiary = MoodBerryRose,
    onTertiary = MoodDarkInk,
    background = MoodDarkInk,
    onBackground = MoodCreamText,
    surface = MoodDarkEspresso,
    onSurface = MoodCreamText,
    surfaceVariant = MoodDarkCard,
    onSurfaceVariant = MoodMutedText,
    outline = MoodGlassBorder,
    outlineVariant = MoodDarkCardElevated
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

