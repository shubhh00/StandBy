package com.app.standby.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.app.standby.ui.theme.StandByColors
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun rememberCurrentTime(): LocalTime {
    var time by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = LocalTime.now()
            time = now
            val msToNextMinute = 60_000L - (now.second * 1000L + now.nano / 1_000_000L)
            delay(msToNextMinute.milliseconds)
        }
    }
    return time
}
@Composable
fun ClockWidget(
    modifier: Modifier = Modifier,
    timeFontSize: TextUnit = 72.sp
) {
    val time = rememberCurrentTime()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time.format(
                DateTimeFormatter.ofPattern("hh:mm")
            ),
            color = StandByColors.Primary,
            fontSize = timeFontSize,
            fontWeight = FontWeight.Light
        )

        Text(
            text = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEE, d MMM")
            ),
            color = StandByColors.Secondary,
            fontSize = 16.sp
        )
    }
}