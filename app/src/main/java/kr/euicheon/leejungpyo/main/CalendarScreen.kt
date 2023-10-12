package kr.euicheon.leejungpyo.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.navigation.NavController
import kr.euicheon.leejungpyo.LeeViewModel

@Composable
fun CalendarScreen(navController: NavController, vm: LeeViewModel) {
    val days = listOf("월", "화", "수", "목", "금", "토", "일")
    //val dates = vm.dates.value // This assumes that your ViewModel provides dates in a LiveData or State object

}

@Composable
fun CalendarView(month: List<String>, days: List<String>, dates: List<Int>, onDateClick: (Int) -> Unit) {
    // Month and Year row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "2023년 7월")
        Icon(imageVector = Icons.Default.Search, contentDescription = null)
    }

    // Days row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            Text(text = day, modifier = Modifier.weight(1f))
        }
    }

    // Dates grid
    dates.chunked(7).forEach { week ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            week.forEach { date ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onDateClick(date) }
                ) {
                    if (date != 0) Text(text = date.toString())
                    // Here, you can also add the image for specific dates using the `Image` composable
                }
            }
        }
    }
}

//Commit point
//Commit point2

