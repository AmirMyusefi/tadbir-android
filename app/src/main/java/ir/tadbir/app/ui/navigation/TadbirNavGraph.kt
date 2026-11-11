package ir.tadbir.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ir.tadbir.app.ui.components.glass.NavigationTab
import ir.tadbir.app.ui.components.glass.TadbirGlassBottomBar
import ir.tadbir.app.ui.features.analytics.AnalyticsRoute
import ir.tadbir.app.ui.features.daily.DailyRoute
import ir.tadbir.app.ui.features.matrix.MatrixRoute
import ir.tadbir.app.ui.features.pomodoro.PomodoroRoute
import ir.tadbir.app.ui.features.routines.RoutinesRoute

sealed class Screen(val route: String) {
    data object Daily : Screen("daily")
    data object Routines : Screen("routines")
    data object Pomodoro : Screen("pomodoro")
    data object Matrix : Screen("matrix")
    data object Analytics : Screen("analytics")
}

val BottomNavTabs = listOf(
    NavigationTab(Screen.Daily.route, "امروز", Icons.Rounded.CalendarToday),
    NavigationTab(Screen.Routines.route, "روتین‌ها", Icons.Rounded.Repeat),
    NavigationTab(Screen.Pomodoro.route, "پومودورو", Icons.Rounded.Timer),
    NavigationTab(Screen.Matrix.route, "ماتریس", Icons.Rounded.GridView),
    NavigationTab(Screen.Analytics.route, "آمار", Icons.Rounded.BarChart)
)

@Composable
fun TadbirAppShell() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Daily.route

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Daily.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Daily.route) {
                DailyRoute()
            }
            composable(Screen.Routines.route) {
                RoutinesRoute()
            }
            composable(Screen.Pomodoro.route) {
                PomodoroRoute()
            }
            composable(Screen.Matrix.route) {
                MatrixRoute()
            }
            composable(Screen.Analytics.route) {
                AnalyticsRoute()
            }
        }

        // Floating Glassmorphic Bottom Navigation Bar (Overlays content like in Daneshyar)
        TadbirGlassBottomBar(
            tabs = BottomNavTabs,
            currentRoute = currentRoute,
            onTabSelected = { tab ->
                navController.navigate(tab.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
