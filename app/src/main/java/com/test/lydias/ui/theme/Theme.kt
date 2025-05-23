package com.test.lydias.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = primaryColorDark,
    primaryContainer = primaryContainerColorDark,
    onPrimaryContainer = onPrimaryContainerColorDark,
    secondary = secondaryColorDark,
    secondaryContainer = secondaryContainerColorDark,
    onSecondaryContainer = onSecondaryContainerColorDark,
    tertiary = tertiaryColorDark,
    tertiaryContainer = tertiaryContainerColorDark,
    onTertiaryContainer = onTertiaryContainerColorDark,
    error = errorColorDark,
    onError = onErrorColorDark,
    errorContainer = errorContainerColorDark,
    onErrorContainer = onErrorContainerColorDark,
    surface = surfaceColorDark,
    onSurface = onSurfaceColorDark
)

private val LightColorScheme = lightColorScheme(
    primary = primaryColor,
    primaryContainer = primaryContainerColor,
    onPrimaryContainer = onPrimaryContainerColor,
    secondary = secondaryColor,
    secondaryContainer = secondaryContainerColor,
    onSecondaryContainer = onSecondaryContainerColor,
    tertiary = tertiaryColor,
    tertiaryContainer = tertiaryContainerColor,
    onTertiaryContainer = onTertiaryContainerColor,
    error = errorColor,
    onError = onErrorColor,
    errorContainer = errorContainerColor,
    onErrorContainer = onErrorContainerColor,
    surface = surfaceColor,
    onSurface = onSurfaceColor
)

@Composable
fun LydiasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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