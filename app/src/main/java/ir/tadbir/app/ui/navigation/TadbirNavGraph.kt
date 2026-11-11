package ir.tadbir.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import ir.tadbir.app.core.util.JalaliDate
import ir.tadbir.app.ui.components.BottomCoverage
import ir.tadbir.app.ui.components.LocalBottomCoverage
import ir.tadbir.app.ui.components.glass.LiquidBottomTab
import ir.tadbir.app.ui.components.glass.LiquidBottomTabs
import ir.tadbir.app.ui.features.analytics.AnalyticsRoute
import ir.tadbir.app.ui.features.daily.DailyRoute
import ir.tadbir.app.ui.features.goals.GoalsRoute
import ir.tadbir.app.ui.features.inbox.InboxRoute
import ir.tadbir.app.ui.features.matrix.MatrixRoute
import ir.tadbir.app.ui.features.pomodoro.PomodoroRoute
import ir.tadbir.app.ui.features.projects.ProjectsRoute
import ir.tadbir.app.ui.features.routines.RoutinesRoute
import ir.tadbir.app.ui.features.wellness.WellnessRoute
import ir.tadbir.app.ui.theme.EmeraldPrimary
import ir.tadbir.app.ui.theme.Sizes
import ir.tadbir.app.ui.theme.Spacing

sealed class Screen(val route: String) {
    data object Daily : Screen("daily")
    data object Routines : Screen("routines")
    data object Pomodoro : Screen("pomodoro")
    data object Matrix : Screen("matrix")
    data object Goals : Screen("goals")
    data object Projects : Screen("projects")
    data object Wellness : Screen("wellness")
    data object Inbox : Screen("inbox")
    data object Analytics : Screen("analytics")
}

data class AppTab(
    val route: String,
    val title: String,
    val icon: ImageVector
)

val AppTabs = listOf(
    AppTab(Screen.Daily.route, "امروز", Icons.Rounded.CalendarToday),
    AppTab(Screen.Goals.route, "اهداف", Icons.Rounded.TrackChanges),
    AppTab(Screen.Projects.route, "پروژه‌ها", Icons.Rounded.Folder),
    AppTab(Screen.Pomodoro.route, "پومودورو", Icons.Rounded.Timer),
    AppTab(Screen.Routines.route, "عادت‌ها", Icons.Rounded.Repeat),
    AppTab(Screen.Wellness.route, "سلامت", Icons.Rounded.Spa),
    AppTab(Screen.Matrix.route, "ماتریس", Icons.Rounded.GridView),
    AppTab(Screen.Inbox.route, "صندوق", Icons.Rounded.Inbox)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TadbirAppShell() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Daily.route
    val backdrop = rememberLayerBackdrop()

    val chosenIndex = remember(currentRoute) {
        AppTabs.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "تدبیر",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = JalaliDate.today().formatted(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = EmeraldPrimary.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.35f))
                        ) {
                            Text(
                                text = "تدبیر نسخه ۲",
                                color = EmeraldPrimary,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Layer 1: Page Content recorded in layerBackdrop
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .layerBackdrop(backdrop)
            ) {
                CompositionLocalProvider(
                    LocalBottomCoverage provides BottomCoverage { 88.dp }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Daily.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = padding.calculateTopPadding())
                    ) {
                        composable(Screen.Daily.route) { DailyRoute() }
                        composable(Screen.Goals.route) { GoalsRoute() }
                        composable(Screen.Projects.route) { ProjectsRoute() }
                        composable(Screen.Pomodoro.route) { PomodoroRoute() }
                        composable(Screen.Routines.route) { RoutinesRoute() }
                        composable(Screen.Wellness.route) { WellnessRoute() }
                        composable(Screen.Matrix.route) { MatrixRoute() }
                        composable(Screen.Inbox.route) { InboxRoute() }
                        composable(Screen.Analytics.route) { AnalyticsRoute() }
                    }
                }
            }

            // Layer 2: Authentic Daneshyar Final-4 Liquid Glass Bottom Bar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 8.dp, vertical = Spacing.xs)
            ) {
                LiquidBottomTabs(
                    selectedTabIndex = { chosenIndex },
                    onTabSelected = { index ->
                        val target = AppTabs[index].route
                        navController.navigate(target) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    backdrop = backdrop,
                    backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                    indicatorColor = EmeraldPrimary.copy(alpha = 0.25f),
                    activeIconColor = EmeraldPrimary,
                    tabsCount = AppTabs.size,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppTabs.forEachIndexed { index, tab ->
                        LiquidBottomTab(
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            selected = index == chosenIndex
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (index == chosenIndex) EmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                                modifier = Modifier.size(Sizes.icon)
                            )
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = if (index == chosenIndex) EmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                            )
                        }
                    }
                }
            }
        }
    }
}
