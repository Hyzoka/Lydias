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
import com.test.component.color.errorColor
import com.test.component.color.errorColorDark
import com.test.component.color.errorContainerColor
import com.test.component.color.errorContainerColorDark
import com.test.component.color.onErrorColor
import com.test.component.color.onErrorColorDark
import com.test.component.color.onErrorContainerColor
import com.test.component.color.onErrorContainerColorDark
import com.test.component.color.onPrimaryContainerColor
import com.test.component.color.onPrimaryContainerColorDark
import com.test.component.color.onSecondaryContainerColor
import com.test.component.color.onSecondaryContainerColorDark
import com.test.component.color.onSurfaceColor
import com.test.component.color.onSurfaceColorDark
import com.test.component.color.onTertiaryContainerColor
import com.test.component.color.onTertiaryContainerColorDark
import com.test.component.color.primaryColor
import com.test.component.color.primaryColorDark
import com.test.component.color.primaryContainerColor
import com.test.component.color.primaryContainerColorDark
import com.test.component.color.secondaryColor
import com.test.component.color.secondaryColorDark
import com.test.component.color.secondaryContainerColor
import com.test.component.color.secondaryContainerColorDark
import com.test.component.color.surfaceColor
import com.test.component.color.surfaceColorDark
import com.test.component.color.tertiaryColor
import com.test.component.color.tertiaryColorDark
import com.test.component.color.tertiaryContainerColor
import com.test.component.color.tertiaryContainerColorDark


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
    dynamicColor: Boolean = false,
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