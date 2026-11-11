package ir.tadbir.app.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import ir.tadbir.app.ui.theme.Spacing

fun interface BottomCoverage {
    fun overlayHeight(page: ViewModelStoreOwner?): Dp?
}

val LocalBottomCoverage = staticCompositionLocalOf { BottomCoverage { null } }

@Composable
fun pageBottomInset(): Dp {
    val overlay = LocalBottomCoverage.current.overlayHeight(LocalViewModelStoreOwner.current)
    return if (overlay != null) overlay + Spacing.md
    else WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + Spacing.xl
}
