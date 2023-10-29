package kr.euicheon.leejungpyo.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kr.euicheon.leejungpyo.DestinationScreen
import kr.euicheon.leejungpyo.LeeViewModel
import kr.euicheon.leejungpyo.R
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import kr.euicheon.leejungpyo.data.CalendarDate
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxWidth



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ToDoScreen(navController: NavController, vm: LeeViewModel, dayDate: CalendarDate) {
    vm.getCalendarData(dayDate)
    val leeDate = vm.dateComponent.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // To Do header
        Text(
            text = "To Do",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.suite_extrabold)),
                fontSize = 36.sp
            ),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable {
                    navigateTo(
                        navController,
                        DestinationScreen.CreateToDo,
                        NavParam.ParcelableParam("createtodo", dayDate)
                    )
                }
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))


        leeDate?.todoList?.let { todoList ->
            LazyColumn(
                modifier = Modifier.padding(8.dp)
            ) {
                items(todoList) { todoItem ->

                    Text(
                        text = "• $todoItem",  // Added bullet point before each item
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.suite_bold))
                        ), // Adjusted font size
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    )
                }
            }
        }
    }
}




