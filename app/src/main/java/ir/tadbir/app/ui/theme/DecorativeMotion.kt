package ir.tadbir.app.ui.theme

import androidx.compose.runtime.mutableIntStateOf

/**
 * Ported from Daneshyar Final-4:
 * Freezes continuous background loops while the user is actively touching / dragging
 * a glass control, avoiding redundant full-screen HWUI rasterization.
 */
object DecorativeMotion {
    private val holds = mutableIntStateOf(0)

    val isHeld: Boolean
        get() = holds.intValue > 0

    fun hold() {
        holds.intValue++
    }

    fun release() {
        if (holds.intValue > 0) {
            holds.intValue--
        }
    }
}
