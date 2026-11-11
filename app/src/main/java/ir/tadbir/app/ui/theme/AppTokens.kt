package ir.tadbir.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AppMotion {
    const val instant = 120
    const val fast = 180
    const val medium = 240
    const val slow = 300
    const val screen = 300
    const val staggerStep = 55L
}

val MinTouchTarget = 48.dp

@Immutable
data class AppColorTokens(
    val success: Color = Color(0xFF10B981),
    val warning: Color = Color(0xFFF59E0B),
    val error: Color = Color(0xFFEF4444),
    val glassTint: Color = Color(0x991E293B),
    val glassBorder: Color = Color(0x22FFFFFF),
    val frogHighlight: Color = Color(0xFF1E3A2F),
    val streakGold: Color = Color(0xFFF59E0B)
)

val LocalAppColorTokens = staticCompositionLocalOf { AppColorTokens() }
