package kr.euicheon.leejungpyo.widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.euicheon.leejungpyo.R
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle


@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun DefaultMonthTitle(
    month: YearMonth,
    isCurrentMonth:Boolean = false,
    fontFamily: FontFamily
){
    val title = remember(month){
        val formatter = DateTimeFormatter.ofPattern("MMMM  yyyy")
        month.format(formatter)
    }

    Text(
        text = title,
        modifier = Modifier.padding(vertical = 8.dp),
        fontFamily = FontFamily(Font(R.font.suite_bold)),
        )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview("NonCurrentMonth",widthDp = 200,heightDp = 40)
@Composable
fun NonCurrentMonthPreview(){
    DefaultMonthTitle(month = YearMonth.of(2020,10), isCurrentMonth = false, fontFamily = FontFamily(Font(R.font.suite_bold)))
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview("CurrentMonth",widthDp = 200,heightDp = 40)
@Composable
fun CurrentMonthPreview(){
    DefaultMonthTitle(month = YearMonth.of(2020,8), isCurrentMonth = true, fontFamily = FontFamily(Font(R.font.suite_bold)))
}