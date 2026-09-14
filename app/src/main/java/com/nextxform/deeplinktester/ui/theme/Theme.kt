package com.nextxform.deeplinktester.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8B83FF),
    onPrimary = Color.White,

    primaryContainer = Color(0xFF292554),
    onPrimaryContainer = Color(0xFFE8E6FF),

    secondary = Color(0xFF6C63FF),
    onSecondary = Color.White,

    tertiary = Color(0xFFB8B3FF),
    onTertiary = Color(0xFF211E3D),

    background = Color(0xFF121212),
    onBackground = Color(0xFFF2F0F5),

    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFF2F0F5),

    outline = Color(0xFF48454E),
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF5B52D6),
    onPrimary = Color.White,

    primaryContainer = Color(0xFFE8E6FF),
    onPrimaryContainer = Color(0xFF211B5F),

    secondary = Color(0xFF6C63FF),
    onSecondary = Color.White,

    tertiary = Color(0xFF403E8E),
    onTertiary = Color.White,

    background = Color(0xFFF8F9FB),
    onBackground = Color(0xFF1A1A1A),

    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),

    outline = Color(0xFFE0DDE5),
)

@Composable
fun DeeplinkTesterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}