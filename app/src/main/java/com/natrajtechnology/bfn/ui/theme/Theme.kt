package com.natrajtechnology.bfn.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.natrajtechnology.bfn.presentation.theme.BFNColors

private val DarkColorScheme = darkColorScheme(
    primary = BFNColors.PrimaryDark,
    onPrimary = Color.White,
    secondary = BFNColors.SecondaryDark,
    onSecondary = Color.White,
    tertiary = BFNColors.Accent,
    onTertiary = Color.White,
    background = Color(0xFF18181B),
    onBackground = Color.White,
    surface = Color(0xFF232326),
    onSurface = Color.White,
    error = BFNColors.Error,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BFNColors.Primary,
    onPrimary = Color.White,
    secondary = BFNColors.Secondary,
    onSecondary = Color.White,
    tertiary = BFNColors.Accent,
    onTertiary = Color.White,
    background = BFNColors.Background,
    onBackground = BFNColors.OnSurface,
    surface = BFNColors.Surface,
    onSurface = BFNColors.OnSurface,
    error = BFNColors.Error,
    onError = Color.White
)

@Composable
fun BFNTheme(
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