package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SparkCrimson,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF881337),
    onPrimaryContainer = SparkRoseLight,
    secondary = SparkSunset,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF7C2D12),
    onSecondaryContainer = Color(0xFFFFEDD5),
    tertiary = SparkGold,
    onTertiary = Color.Black,
    background = Slate900,
    surface = Slate800,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate200
)

private val LightColorScheme = lightColorScheme(
    primary = SparkCrimson,
    onPrimary = Color.White,
    primaryContainer = SparkRoseLight,
    onPrimaryContainer = Color(0xFF881337),
    secondary = SparkSunset,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFEDD5),
    onSecondaryContainer = Color(0xFF7C2D12),
    tertiary = SparkGold,
    onTertiary = Color.White,
    tertiaryContainer = SparkGoldLight,
    onTertiaryContainer = Color(0xFF78350F),
    background = Color(0xFFFAFAFA),
    surface = Color.White,
    onBackground = Slate900,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700
)

@Composable
fun SparkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep intentional branding for Spark
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
