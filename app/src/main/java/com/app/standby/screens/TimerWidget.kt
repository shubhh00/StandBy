package com.app.standby.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.standby.ui.theme.StandByColors

@Composable
fun TimerWidget(
    elapsedSeconds: Long,
    isRunning: Boolean,
    onStartPause: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60

    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formattedTime,
            color = StandByColors.Primary,
            fontSize = 64.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(
                onClick = onStartPause
            ) {
                Text(
                    text = if (isRunning) "Pause" else "Start"
                )
            }

            TextButton(
                onClick = onReset
            ) {
                Text("Reset")
            }
        }
    }
}