package kr.euicheon.leejungpyo.widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kr.euicheon.leejungpyo.R
import kr.euicheon.leejungpyo.data.LeeActions
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DefaultHeader(
    month: YearMonth,
    todayMonth: YearMonth,
    actions: LeeActions,
) {
    val isCurrentMonth = todayMonth == month
    Row {
        IconButton(
            onClick = { actions.onClickedPreviousMonth() },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                contentDescription = "Left"
            )
        }

        Spacer(modifier = Modifier.weight(1f))


        DefaultMonthTitle(
            month = month,
            isCurrentMonth = isCurrentMonth,
            fontFamily = FontFamily(Font(R.font.suite_bold))
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { actions.onClickedNextMonth() },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_right),
                contentDescription = "Right"
            )
        }
    }
}

@Composable
fun DefaultDay(
    text: String,
    modifier: Modifier = Modifier.padding(4.dp),
    style: TextStyle = TextStyle(),
) {
    Text(
        text,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = style,
    )

}
