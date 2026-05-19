package ru.kpfu.itis.designSystem.Theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import ru.kpfu.itis.designSystem.BackgroundDark
import ru.kpfu.itis.designSystem.BackgroundLight
import ru.kpfu.itis.designSystem.NdaNotRequiredBg
import ru.kpfu.itis.designSystem.NdaNotRequiredBgDark
import ru.kpfu.itis.designSystem.NdaNotRequiredText
import ru.kpfu.itis.designSystem.NdaRequiredBg
import ru.kpfu.itis.designSystem.NdaRequiredBgDark
import ru.kpfu.itis.designSystem.NdaRequiredText
import ru.kpfu.itis.designSystem.OnSurfaceDark
import ru.kpfu.itis.designSystem.OnSurfaceLight
import ru.kpfu.itis.designSystem.OnSurfaceVariantDark
import ru.kpfu.itis.designSystem.OnSurfaceVariantLight
import ru.kpfu.itis.designSystem.OutlineDark
import ru.kpfu.itis.designSystem.OutlineLight
import ru.kpfu.itis.designSystem.Primary
import ru.kpfu.itis.designSystem.SurfaceDark
import ru.kpfu.itis.designSystem.SurfaceLight

data class ExtendedColors(
    val ndaRequiredBg: Color,
    val ndaRequiredText: Color,
    val ndaNotRequiredBg: Color,
    val ndaNotRequiredText: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        ndaRequiredBg = NdaRequiredBg,
        ndaRequiredText = NdaRequiredText,
        ndaNotRequiredBg = NdaNotRequiredBg,
        ndaNotRequiredText = NdaNotRequiredText
    )
}

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineLight
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineDark
)

@Composable
fun CaseCareerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val extendedColors = if (darkTheme) {
        ExtendedColors(
            ndaRequiredBg = NdaRequiredBgDark,
            ndaRequiredText = NdaRequiredText,
            ndaNotRequiredBg = NdaNotRequiredBgDark,
            ndaNotRequiredText = NdaNotRequiredText
        )
    } else {
        ExtendedColors(
            ndaRequiredBg = NdaRequiredBg,
            ndaRequiredText = NdaRequiredText,
            ndaNotRequiredBg = NdaNotRequiredBg,
            ndaNotRequiredText = NdaNotRequiredText
        )
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
