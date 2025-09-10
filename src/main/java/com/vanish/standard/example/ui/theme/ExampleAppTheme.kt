package com.vanish.standard.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ===== Light Theme（白背景・青系Primary） =====
private val LightColorScheme =
    lightColorScheme(
        primary = Color(0xFF1565C0),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFBBDEFB),
        onPrimaryContainer = Color(0xFF002F6C),
        secondary = Color(0xFF0288D1),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFB3E5FC),
        onSecondaryContainer = Color(0xFF004B6B),
        tertiary = Color(0xFF0097A7),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFB2EBF2),
        onTertiaryContainer = Color(0xFF004D54),
        error = Color(0xFFB00020),
        onError = Color.White,
        errorContainer = Color(0xFFFCD8DF),
        onErrorContainer = Color(0xFF370B12),
        background = Color(0xFFFFFFFF),
        onBackground = Color(0xFF1A1A1A),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF1A1A1A),
        surfaceVariant = Color(0xFFE1E3E6),
        onSurfaceVariant = Color(0xFF44474F),
        outline = Color(0xFF73777F),
        outlineVariant = Color(0xFFC5C6CA),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFF2F3033),
        inverseOnSurface = Color(0xFFF0F0F0),
        inversePrimary = Color(0xFF90CAF9),
    )

// ===== Dark Theme（黒背景・青系Primary） =====
private val DarkColorScheme =
    darkColorScheme(
        primary = Color(0xFF90CAF9),
        onPrimary = Color(0xFF003C8F),
        primaryContainer = Color(0xFF1565C0),
        onPrimaryContainer = Color(0xFFBBDEFB),
        secondary = Color(0xFF4DD0E1),
        onSecondary = Color(0xFF00363D),
        secondaryContainer = Color(0xFF006978),
        onSecondaryContainer = Color(0xFFB2EBF2),
        tertiary = Color(0xFF4DB6AC),
        onTertiary = Color(0xFF003732),
        tertiaryContainer = Color(0xFF005049),
        onTertiaryContainer = Color(0xFFB2DFDB),
        error = Color(0xFFF2B8B5),
        onError = Color(0xFF601410),
        errorContainer = Color(0xFF8C1D18),
        onErrorContainer = Color(0xFFF9DEDC),
        background = Color(0xFF121212),
        onBackground = Color(0xFFEDEDED),
        surface = Color(0xFF1F1F1F),
        onSurface = Color(0xFFEDEDED),
        surfaceVariant = Color(0xFF44474F),
        onSurfaceVariant = Color(0xFFC5C6CA),
        outline = Color(0xFF8F9199),
        outlineVariant = Color(0xFF44474F),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFFEDEDED),
        inverseOnSurface = Color(0xFF2F3033),
        inversePrimary = Color(0xFF1565C0),
    )

@Composable
fun ExampleAppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colors,
        content = content,
    )
}

@Composable
fun DefaultAppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) darkColorScheme() else lightColorScheme()
    MaterialTheme(
        colorScheme = colors,
        content = content,
    )
}
