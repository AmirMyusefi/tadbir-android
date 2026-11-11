package ir.tadbir.app.ui.components.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

internal val LocalLiquidBottomTabScale = staticCompositionLocalOf { { 1f } }
internal val LocalLiquidStaticIndicator = staticCompositionLocalOf { Color.Transparent }

@Composable
fun RowScope.LiquidBottomTab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    weight: Float = 1f,
    selected: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val scale = LocalLiquidBottomTabScale.current
    Column(
        modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) LocalLiquidStaticIndicator.current else Color.Transparent)
            .semantics { this.selected = selected }
            .clickable(
                interactionSource = null,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            )
            .fillMaxHeight()
            .weight(weight)
            .graphicsLayer {
                val s = scale()
                scaleX = s
                scaleY = s
            },
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}
