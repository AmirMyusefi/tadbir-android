package ir.tadbir.app.ui.features.pomodoro

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.tadbir.app.core.util.JalaliDate
import ir.tadbir.app.ui.components.pageBottomInset
import ir.tadbir.app.ui.theme.EmeraldPrimary
import ir.tadbir.app.ui.theme.RoseRed

@Composable
fun PomodoroScreen(
    state: PomodoroUiState,
    onIntent: (PomodoroIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(targetValue = state.progressFraction, label = "pomoProgress")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Mode Selector Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    PomodoroMode.values().forEach { mode ->
                        val isSelected = state.mode == mode
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) EmeraldPrimary else Color.Transparent)
                                .clickable { onIntent(PomodoroIntent.ChangeMode(mode)) }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mode.titleFa,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // Circular Timer Visual
        Box(
            modifier = Modifier.size(280.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background Track
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface,
                strokeWidth = 14.dp
            )

            // Dynamic Progress Arc
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = if (state.mode == PomodoroMode.FOCUS) EmeraldPrimary else RoseRed,
                strokeWidth = 14.dp
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = JalaliDate.toFa(state.formattedTime),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = state.mode.titleFa,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }

        // Action Controls & Session Counter
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = pageBottomInset())
        ) {
            Text(
                text = "جلسات انجام شده امروز: ${JalaliDate.toFa(state.completedSessionsCount)} 🍅",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Button
                IconButton(
                    onClick = { onIntent(PomodoroIntent.ResetTimer) },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        Icons.Rounded.Refresh,
                        contentDescription = "بازنشانی",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Play / Pause Big Button
                Surface(
                    onClick = { onIntent(PomodoroIntent.ToggleTimer) },
                    shape = CircleShape,
                    color = if (state.isRunning) RoseRed else EmeraldPrimary,
                    shadowElevation = 8.dp,
                    modifier = Modifier.size(76.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (state.isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = if (state.isRunning) "توقف" else "شروع",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }
    }
}
