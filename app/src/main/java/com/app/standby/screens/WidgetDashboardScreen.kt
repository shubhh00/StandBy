package com.app.standby.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun WidgetDashboardScreen() {

    var leftIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    var rightIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    var elapsedSeconds by rememberSaveable {
        mutableLongStateOf(0L)
    }

    var isTimerRunning by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            delay(1_000.milliseconds)
            elapsedSeconds++
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        LeftWidgetPanel(
            currentIndex = leftIndex,
            onClick = {
                leftIndex = (leftIndex + 1) % 2
            },
            modifier = Modifier.weight(1f)
        )

        RightWidgetPanel(
            currentIndex = rightIndex,

            elapsedSeconds = elapsedSeconds,
            isTimerRunning = isTimerRunning,

            onStartPause = {
                isTimerRunning = !isTimerRunning
            },

            onReset = {
                isTimerRunning = false
                elapsedSeconds = 0L
            },

            onClick = {
                rightIndex = (rightIndex + 1) % 2
            },

            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LeftWidgetPanel(
    currentIndex: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = currentIndex,
            label = "left_widget_animation"
        ) { index ->

            when (index) {

                0 -> ClockWidget(
                    modifier = Modifier.fillMaxSize(),
                    timeFontSize = 72.sp
                )

                1 -> CalendarWidget(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun RightWidgetPanel(
    currentIndex: Int,

    elapsedSeconds: Long,
    isTimerRunning: Boolean,

    onStartPause: () -> Unit,
    onReset: () -> Unit,

    onClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = currentIndex,
            label = "right_widget_animation"
        ) { index ->

            when (index) {

                0 -> BatteryWidget(
                    modifier = Modifier.fillMaxSize()
                )

                1 -> TimerWidget(
                    elapsedSeconds = elapsedSeconds,
                    isRunning = isTimerRunning,
                    onStartPause = onStartPause,
                    onReset = onReset,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}