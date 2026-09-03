package com.app.standby.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.standby.ui.theme.StandByColors
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun CalendarWidget(
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val currentMonth = YearMonth.from(today)

    val daysInMonth = currentMonth.lengthOfMonth()

    // Monday = 1 ... Sunday = 7
    val firstDayOffset =
        currentMonth.atDay(1).dayOfWeek.value - 1

    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = currentMonth.format(
                DateTimeFormatter.ofPattern("MMMM yyyy")
            ),
            color = StandByColors.Primary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )

        WeekHeader()

        val totalCells = firstDayOffset + daysInMonth
        val numberOfWeeks = (totalCells + 6) / 7

        repeat(numberOfWeeks) { week ->

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                repeat(7) { dayOfWeek ->

                    val cellIndex =
                        week * 7 + dayOfWeek

                    val day =
                        cellIndex - firstDayOffset + 1

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {

                        if (day in 1..daysInMonth) {

                            val isToday =
                                day == today.dayOfMonth

                            Box(
                                modifier = if (isToday) {
                                    Modifier
                                        .background(
                                            Color.White,
                                            CircleShape
                                        )
                                        .padding(7.dp)
                                } else {
                                    Modifier.padding(7.dp)
                                },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (isToday) {
                                        Color.Black
                                    } else {
                                        Color.White
                                    },
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekHeader() {

    val days = listOf(
        "M", "T", "W", "T", "F", "S", "S"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {

        days.forEach { day ->

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
        }
    }
}