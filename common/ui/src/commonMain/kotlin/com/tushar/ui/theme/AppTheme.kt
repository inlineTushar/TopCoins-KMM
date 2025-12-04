package com.tushar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Purple200 = Color(0xFFBB86FC)
private val Purple500 = Color(0xFF6200EE)
private val Purple700 = Color(0xFF3700B3)
private val Teal200 = Color(0xFF03DAC5)
private val Teal700 = Color(0xFF018786)
private val Black = Color(0xFF000000)
private val White = Color(0xFFFFFFFF)


private val ErrorLight = Color(0xFFB00020)
private val ErrorDark = Color(0xFFCF6679)
private val SurfaceLight = Color(0xFFFAFAFA)
private val SurfaceDark = Color(0xFF121212)
private val BackgroundLight = Color(0xFFFFFFFF)
private val BackgroundDark = Color(0xFF121212)

private val DarkColorScheme = darkColorScheme(
    primary = Purple200,
    onPrimary = Black,
    primaryContainer = Purple700,
    onPrimaryContainer = Purple200,

    secondary = Teal200,
    onSecondary = Black,
    secondaryContainer = Teal200,
    onSecondaryContainer = Black,

    tertiary = Teal700,
    onTertiary = White,
    tertiaryContainer = Teal200,
    onTertiaryContainer = Black,

    background = BackgroundDark,
    onBackground = White,

    surface = SurfaceDark,
    onSurface = White,
    surfaceVariant = Color(0xFF1E1E1E),
    onSurfaceVariant = Color(0xFFCACACA),

    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),

    error = ErrorDark,
    onError = Black,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF313033),
    inversePrimary = Purple500,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple500,
    onPrimary = White,
    primaryContainer = Purple700,
    onPrimaryContainer = White,

    secondary = Teal200,
    onSecondary = Black,
    secondaryContainer = Teal700,
    onSecondaryContainer = White,

    tertiary = Teal700,
    onTertiary = White,
    tertiaryContainer = Teal200,
    onTertiaryContainer = Black,

    background = BackgroundLight,
    onBackground = Black,

    surface = SurfaceLight,
    onSurface = Black,
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),

    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),

    error = ErrorLight,
    onError = White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Purple200,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}