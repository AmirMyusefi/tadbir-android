package ir.tadbir.app.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Standard dimensions ported from Daneshyar Final-4.
 */
object Spacing {
    val xxs = 4.dp
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 20.dp
    val xl = 24.dp
    val xxl = 32.dp

    val page = 20.dp
    val item = 12.dp
    val section = 12.dp
}

object Radii {
    val chip = 10.dp
    val control = 14.dp
    val row = 16.dp
    val card = 20.dp
    val hero = 24.dp
    val sheet = 28.dp
}

object AcademyShapes {
    val chip = RoundedCornerShape(Radii.chip)
    val control = RoundedCornerShape(Radii.control)
    val row = RoundedCornerShape(Radii.row)
    val card = RoundedCornerShape(Radii.card)
    val hero = RoundedCornerShape(Radii.hero)
    val sheet = RoundedCornerShape(Radii.sheet)

    val material = Shapes(
        extraSmall = RoundedCornerShape(8.dp),
        small = chip,
        medium = row,
        large = card,
        extraLarge = sheet,
    )
}

object Sizes {
    val touch = 48.dp
    val button = 52.dp
    val input = 56.dp
    val listRow = 64.dp
    val iconSmall = 16.dp
    val icon = 20.dp
    val iconLarge = 24.dp
    val iconTile = 44.dp
    val topBar = 64.dp
    val bottomBarEstimate = 100.dp
}

object Borders {
    val hairline = 1.dp
    const val subtleAlpha = 0.35f
}

object AppEasing {
    val emphasized = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    val emphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
    val emphasizedAccelerate = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
    val standard = CubicBezierEasing(0.2f, 0f, 0f, 1f)
}
