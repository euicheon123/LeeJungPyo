package kr.euicheon.leejungpyo.main

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kr.euicheon.leejungpyo.DestinationScreen
import kr.euicheon.leejungpyo.LeeViewModel
import kr.euicheon.leejungpyo.R
import kr.euicheon.leejungpyo.data.LeeActions
import kr.euicheon.leejungpyo.data.CalendarDate
import kr.euicheon.leejungpyo.data.LeeProperties
import kr.euicheon.leejungpyo.data.LeeWidgets
import kr.euicheon.leejungpyo.widgets.DefaultDay
import kr.euicheon.leejungpyo.widgets.MaterialHeader
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

const val WEIGHT_7DAY_WEEK = 1 / 7f

@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(navController: NavController, vm: LeeViewModel) {
    var month by remember { mutableStateOf(YearMonth.now()) }

    MaterialCalendar(
        month = month,
        actions = LeeActions(
            onClickedPreviousMonth = { month = month.minusMonths(1) },
            onClickedNextMonth = { month = month.plusMonths(1) },
        ),
        navController = navController
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MaterialCalendar(
    month: YearMonth,
    actions: LeeActions,
    navController: NavController,
) {
    LeeCalendar(
        month = month,
        actions = actions,

        widgets = LeeWidgets(
            header = { month, todayMonth, actions ->
                MaterialHeader(month, todayMonth, actions)
            },
            headerDayRow = { headerDayList -> HeaderDayRow(headerDayList = headerDayList) },
            day = { dayDate, todayDate ->
                Day(
                    dayDate = dayDate,
                    navController = navController
                )
            },
            priorMonthDay = { dayDate -> PriorMonthDay(dayDate = dayDate) },
        )
    )
}


@Composable
fun HeaderDayRow(
    headerDayList: Set<DayOfWeek>,
) {
    val koreanDayNames = listOf("월", "화", "수", "목", "금", "토", "일")
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(vertical = 8.dp),
    ) {
        headerDayList.forEachIndexed { index, _ ->
            DefaultDay(
                text = koreanDayNames[index],
                modifier = Modifier
                    .weight(WEIGHT_7DAY_WEEK)
                    .alpha(.6f),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = FontFamily(Font(R.font.suite_bold))
                )
            )
        }
    }
}

@Composable
fun RowScope.Day(
    dayDate: CalendarDate,
    navController: NavController
) {

    val widget: @Composable () -> Unit = {
        DefaultDay(
            text = dayDate.day.toString(),
            modifier = Modifier
                .padding(4.dp)
                .weight(WEIGHT_7DAY_WEEK)
                .fillMaxWidth(),
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily =  FontFamily(Font(R.font.suite_light)
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .weight(WEIGHT_7DAY_WEEK)
            .clickable { navigateTo(navController, DestinationScreen.ToDo, NavParam.ParcelableParam("todo", dayDate)) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
            ) {
                widget()
            }
        Image(
            painter = painterResource(id = R.drawable.lee_logo),
            contentDescription = null
        )
    }
}

@Composable
fun RowScope.PriorMonthDay(
    dayDate: CalendarDate,
) {
    DefaultDay(
        text = dayDate.day.toString(),
        style = TextStyle(color = MaterialTheme.colorScheme.background),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .weight(WEIGHT_7DAY_WEEK)
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeeCalendar(
    modifier: Modifier = Modifier,
    month: YearMonth,
    actions: LeeActions,
    widgets: LeeWidgets,
    properties: LeeProperties = LeeProperties(),
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
) {
    Crossfade(
        targetState = month,
        animationSpec = properties.changeMonthAnimation
    ) {
        LeeStatic(
            modifier = modifier,
            month = it,
            actions = actions,
            widgets = widgets,
            firstDayOfWeek = firstDayOfWeek
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeeStatic(
    modifier: Modifier = Modifier,
    month: YearMonth,
    actions: LeeActions,
    widgets: LeeWidgets,
    properties: LeeProperties = LeeProperties(),
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
) {
    val todayMonth = remember { YearMonth.now() }
    val weekStartOffset = firstDayOfWeek.value - 1
    Column(
        modifier = modifier.draggable(
            orientation = Orientation.Horizontal,
            state = DraggableState {},
            onDragStopped = { velocity ->
                if (velocity > properties.changeMonthSwipeTriggerVelocity) {
                    actions.onSwipedPreviousMonth()
                } else if (velocity < -properties.changeMonthSwipeTriggerVelocity) {
                    actions.onSwipedNextMonth()
                }
            })
    ) {
        LeeHeader(month, todayMonth, actions, widgets, weekStartOffset)
        widgets.monthContainer { LeeMonth(month, todayMonth, widgets, weekStartOffset) }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeeHeader(
    month: YearMonth,
    todayMonth: YearMonth,
    actions: LeeActions,
    widgets: LeeWidgets,
    weekStartOffset: Int,
) {
    widgets.headerContainer {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            widgets.header(month, todayMonth, actions)
            widgets.headerDayRow((0..6).map { DayOfWeek.of((it + weekStartOffset) % 7 + 1) }
                .toSet())
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeeMonth(month: YearMonth, todayMonth: YearMonth, widgets: LeeWidgets, weekStartOffset: Int) {
    var firstDayOffset = month.atDay(1).dayOfWeek.ordinal - weekStartOffset
    if (firstDayOffset < 1) firstDayOffset += 7
    if (firstDayOffset > 7) firstDayOffset -= 7
    if (firstDayOffset == 7) firstDayOffset = 0
    val monthLength = month.lengthOfMonth()
    val priorMonthLength = month.minusMonths(1).lengthOfMonth()
    val lastDayCount = (monthLength + firstDayOffset) % 7
    val weekCount = (firstDayOffset + monthLength) / 7
    val today = SimpleDateFormat("dd").format(Date(System.currentTimeMillis())).toInt()

    for (i in 0..weekCount) {
        widgets.weekContainer {
            LeeWeek(
                startDayOffSet = firstDayOffset,
                endDayCount = lastDayCount,
                monthWeekNumber = i,
                weekCount = weekCount,
                priorMonthLength = priorMonthLength,
                today = CalendarDate(
                    day = today,
                    dayOfWeek = todayMonth.atDay(today).dayOfWeek,
                    month = todayMonth
                ),
                month = month,
                widgets = widgets,
                weekStartOffset = weekStartOffset
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeeWeek(
    startDayOffSet: Int,
    endDayCount: Int,
    monthWeekNumber: Int,
    weekCount: Int,
    priorMonthLength: Int,
    today: CalendarDate,
    month: YearMonth,
    widgets: LeeWidgets,
    weekStartOffset: Int,
) {
    Row {
        var dayOfWeekOrdinal = weekStartOffset
        if (monthWeekNumber == 0) {
            for (i in 1..startDayOffSet) {
                val priorDay = (priorMonthLength - (startDayOffSet - i))
                widgets.priorMonthDay(
                    this,
                      CalendarDate(
                        priorDay,
                        DayOfWeek.of(dayOfWeekOrdinal++ % 7 + 1),
                        month.minusMonths(1)
                    )
                )
            }
        }

        val endDay = when (monthWeekNumber) {
            0 -> 7 - startDayOffSet
            weekCount -> endDayCount
            else -> 7
        }

        for (i in 1..endDay) {
            val day = if (monthWeekNumber == 0) i else (i + (7 * monthWeekNumber) - startDayOffSet)
            widgets.day(
                this,
                CalendarDate(day, DayOfWeek.of(dayOfWeekOrdinal++ % 7 + 1), month),
                today
            )
        }

        if (monthWeekNumber == weekCount && endDayCount > 0) {
            for (i in 0 until (7 - endDayCount)) {
                val nextMonthDay = i + 1
                widgets.nextMonthDay(
                    this, CalendarDate(
                        nextMonthDay,
                        DayOfWeek.of(dayOfWeekOrdinal++ % 7 + 1),
                        month.plusMonths(1)
                    )
                )
            }
        }
    }
}








