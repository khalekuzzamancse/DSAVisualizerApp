package com.khalekuzzamanjustcse.dsavisulizer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_primary,
    onPrimary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onPrimary,
    primaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_primaryContainer,
    onPrimaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onPrimaryContainer,
    secondary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_secondary,
    onSecondary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onSecondary,
    secondaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_secondaryContainer,
    onSecondaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onSecondaryContainer,
    tertiary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_tertiary,
    onTertiary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onTertiary,
    tertiaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_tertiaryContainer,
    onTertiaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onTertiaryContainer,
    error = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_error,
    errorContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_errorContainer,
    onError = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onError,
    onErrorContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onErrorContainer,
    background = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_background,
    onBackground = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onBackground,
    surface = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_surface,
    onSurface = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onSurface,
    surfaceVariant = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_surfaceVariant,
    onSurfaceVariant = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_onSurfaceVariant,
    outline = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_outline,
    inverseOnSurface = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_inverseOnSurface,
    inverseSurface = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_inverseSurface,
    inversePrimary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_inversePrimary,
    surfaceTint = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_surfaceTint,
    outlineVariant = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_outlineVariant,
    scrim = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_primary,
    onPrimary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onPrimary,
    primaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_primaryContainer,
    onPrimaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onPrimaryContainer,
    secondary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_secondary,
    onSecondary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onSecondary,
    secondaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_secondaryContainer,
    onSecondaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onSecondaryContainer,
    tertiary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_tertiary,
    onTertiary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onTertiary,
    tertiaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_tertiaryContainer,
    onTertiaryContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onTertiaryContainer,
    error = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_error,
    errorContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_errorContainer,
    onError = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onError,
    onErrorContainer = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onErrorContainer,
    background = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_background,
    onBackground = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onBackground,
    surface = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_surface,
    onSurface = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onSurface,
    surfaceVariant = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_surfaceVariant,
    onSurfaceVariant = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_onSurfaceVariant,
    outline = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_outline,
    inverseOnSurface = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_inverseOnSurface,
    inverseSurface = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_inverseSurface,
    inversePrimary = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_inversePrimary,
    surfaceTint = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_surfaceTint,
    outlineVariant = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_outlineVariant,
    scrim = com.khalekuzzamanjustcse.tree_visualization.ui.theme.md_theme_dark_scrim,
)

@Composable
fun DSAVisulizerTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
