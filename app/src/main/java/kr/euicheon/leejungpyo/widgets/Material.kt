package kr.euicheon.leejungpyo.widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import kr.euicheon.leejungpyo.R
import kr.euicheon.leejungpyo.data.LeeActions
import java.time.YearMonth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MaterialHeader(
    month: YearMonth,
    todayMonth: YearMonth,
    actions: LeeActions,
    titleContainer: @Composable (@Composable () -> Unit) -> Unit = { it() }
) {
    val isCurrentMonth = todayMonth == month
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { actions.onClickedPreviousMonth() },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Left"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        titleContainer {
            DefaultMonthTitle(
                month = month,
                isCurrentMonth = isCurrentMonth,
                fontFamily = FontFamily(Font(R.font.suite_bold))
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { actions.onClickedNextMonth() },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_right),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Right"
            )
        }
    }
}