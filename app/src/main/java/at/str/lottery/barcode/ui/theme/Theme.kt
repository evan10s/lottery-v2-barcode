package at.str.lottery.barcode.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import at.str.lottery.barcode.ui.theme.blue500
import at.str.lottery.barcode.ui.theme.blueLight
import at.str.lottery.barcode.ui.theme.purple300

private val DarkColorPalette = darkColors(
    primary = blue500,
    primaryVariant = blueLight,
    secondary = purple300
)

private val LightColorPalette = lightColors(
    primary = blue500,
    primaryVariant = blueLight,
    secondary = purple300

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun LotteryBarcodeScannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}