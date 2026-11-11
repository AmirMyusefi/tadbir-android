package ir.tadbir.app.ui.theme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

object ReduceMotion {
    private val _enabled = mutableStateOf(false)
    val enabled: Boolean
        get() = _enabled.value

    fun refresh(context: Context) {
        val durationScale = Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1.0f
        )
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val powerSave = powerManager?.isPowerSaveMode == true
        _enabled.value = durationScale == 0f || powerSave
    }
}

val LocalReduceMotion = staticCompositionLocalOf { false }

@Composable
fun ProvideReduceMotion(content: @Composable () -> Unit) {
    val context = LocalContext.current
    DisposableEffect(context) {
        ReduceMotion.refresh(context)
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) = ReduceMotion.refresh(context)
        }
        context.contentResolver.registerContentObserver(
            Settings.Global.getUriFor(Settings.Global.ANIMATOR_DURATION_SCALE),
            false,
            observer
        )

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) = ReduceMotion.refresh(context)
        }
        val filter = IntentFilter(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
        if (Build.VERSION.SDK_INT >= 33) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            context.registerReceiver(receiver, filter)
        }

        onDispose {
            context.contentResolver.unregisterContentObserver(observer)
            runCatching { context.unregisterReceiver(receiver) }
        }
    }

    CompositionLocalProvider(LocalReduceMotion provides ReduceMotion.enabled, content = content)
}
