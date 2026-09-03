package com.app.standby.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.app.standby.ui.theme.StandByColors

data class BatteryState(
    val percent: Int = 0,
    val isCharging: Boolean = false
)

@Composable
fun rememberBatteryState(): BatteryState {
    val context = LocalContext.current
    var state by remember { mutableStateOf(BatteryState()) }

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                intent ?: return
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)

                if (level >= 0 && scale > 0) {
                    state = BatteryState(
                        percent = (level * 100) / scale,
                        isCharging = plugged != 0
                    )
                }
            }
        }

        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        onDispose { context.unregisterReceiver(receiver) }
    }

    return state
}


@Composable
fun BatteryWidget(
    modifier: Modifier = Modifier
) {
    val battery = rememberBatteryState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${battery.percent}%",
            color = StandByColors.Primary,
            fontSize = 64.sp,
            fontWeight = FontWeight.Light
        )

        Text(
            text = if (battery.isCharging) {
                "Charging"
            } else {
                "On battery"
            },
            color = StandByColors.Secondary,
            fontSize = 16.sp
        )
    }
}