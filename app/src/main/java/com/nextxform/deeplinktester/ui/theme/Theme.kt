package com.nextxform.deeplinktester.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6C63FF),
    onPrimary = Color.White,

    primaryContainer = Color(0xffE8E6FF),

    secondary = Color(0xFF00BFA6),
    onSecondary = Color.Black,

    tertiary = Color(0xFF403E8E),

    background = Color(0xFF121212),
    onBackground = Color(0xFFEDEDED),

    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFEDEDED),

    outline = Color(0xFF3C3C3C),
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6C63FF),
    onPrimary = Color.White,

    primaryContainer = Color(0xffE8E6FF),

    secondary = Color(0xFF00BFA6),
    onSecondary = Color.Black,

    tertiary = Color(0xFF403E8E),

    background = Color(0xFFF8F9FB),
    onBackground = Color(0xFF1A1A1A),

    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),

    outline = Color(0xFFE0E0E0),
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