package ir.tadbir.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ir.tadbir.app.ui.navigation.TadbirAppShell
import ir.tadbir.app.ui.theme.TadbirTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TadbirTheme {
                TadbirAppShell()
            }
        }
    }
}
