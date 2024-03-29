package kr.euicheon.leejungpyo.data

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import java.time.DayOfWeek
import java.time.YearMonth

/**
 * Describes the various widget types which can be rendered within Calpose
 *
 * @property header Widget which defines the header of the calendar (excl. the DayOfWeek row)
 *
 * @property headerDayRow Widget which defines the DayOfWeek row
 *
 * @property day Widget which defines the look of a day item in the currently selected month
 *
 * @property priorMonthDay Widget which defines the look of a day item of the previous month
 *
 * @property nextMonthDay Widget which defines the look of a day item of the next month
 *
 * @property headerContainer Widget which can define a container layout for the *header* & *headerDayRow*.
 *                           Useful if you want to place the header inside of a *Card* f.e.
 *
 * @property monthContainer Widget which can define a container layout for the month overview.
 *                          Can be used to change the background color, change animation of the
 *                          month overview
 *
 * @property weekContainer Widget which can define a container layout for the week. Can be
 *                          used to change the background color of the week or make dividers between them.
 */
data class LeeWidgets(
    val header: @Composable (month: YearMonth, todayMonth: YearMonth, actions: LeeActions) -> Unit,
    val headerDayRow: @Composable (headerDayList: Set<DayOfWeek>) -> Unit,
    val day: @Composable RowScope.(dayDate: CalendarDate, todayDate: CalendarDate) -> Unit,
    val priorMonthDay: @Composable RowScope.(dayDate: CalendarDate) -> Unit,
    val nextMonthDay: @Composable RowScope.(dayDate: CalendarDate) -> Unit = priorMonthDay,
    val headerContainer: @Composable (@Composable () -> Unit) -> Unit = { it() },
    val monthContainer: @Composable (@Composable () -> Unit) -> Unit = { it() },
    val weekContainer: @Composable (@Composable () -> Unit) -> Unit = { it() }
)