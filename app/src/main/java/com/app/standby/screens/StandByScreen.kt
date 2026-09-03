package com.app.standby.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.app.standby.utils.burnInProtection

@Composable
fun StandByScreen() {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .burnInProtection()
        ) { page ->
            when (page) {

                0 -> WidgetDashboardScreen()

                1 -> ClockWidget(
                    modifier = Modifier.fillMaxSize(),
                    timeFontSize = 172.sp
                )
            }
        }
    }
}