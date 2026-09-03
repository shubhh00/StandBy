package com.app.standby.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.offset
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Modifier.burnInProtection(): Modifier {

    var positionIndex by remember {
        mutableIntStateOf(0)
    }

    val density = LocalDensity.current

    val positions = remember {
        listOf(
            0 to 0,
            3 to 2,
            -2 to 3,
            -3 to -2,
            2 to -3
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000.milliseconds)

            positionIndex =
                (positionIndex + 1) % positions.size
        }
    }

    val position = positions[positionIndex]

    return this.offset {
        with(density) {
            IntOffset(
                x = position.first.dp.roundToPx(),
                y = position.second.dp.roundToPx()
            )
        }
    }
}