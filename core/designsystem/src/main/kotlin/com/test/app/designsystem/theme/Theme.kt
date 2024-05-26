package com.test.app.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightDefaultColorScheme = lightColorScheme(
    primary = Teal40,
    onPrimary = Color.White,
    primaryContainer = Teal90,
    onPrimaryContainer = Teal10,
    secondary = Orange40,
    onSecondary = Color.White,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = Blue40,
    onTertiary = Color.White,
    tertiaryContainer = Blue90,
    onTertiaryContainer = Blue10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkTealGray99,
    onBackground = DarkTealGray10,
    surface = DarkTealGray99,
    onSurface = DarkTealGray10,
    surfaceVariant = TealGray90,
    onSurfaceVariant = TealGray30,
    inverseSurface = DarkTealGray20,
    inverseOnSurface = DarkTealGray95,
    outline = TealGray50,
)

val DarkDefaultColorScheme = darkColorScheme(
    primary = Teal80,
    onPrimary = Teal20,
    primaryContainer = Teal30,
    onPrimaryContainer = Teal90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = Blue80,
    onTertiary = Blue20,
    tertiaryContainer = Blue30,
    onTertiaryContainer = Blue90,
    error = Red40,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkTealGray10,
    onBackground = DarkTealGray90,
    surface = DarkTealGray10,
    onSurface = DarkTealGray90,
    surfaceVariant = TealGray30,
    onSurfaceVariant = TealGray80,
    inverseSurface = DarkTealGray90,
    inverseOnSurface = DarkTealGray10,
    outline = TealGray60,
)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    val colorScheme = if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
