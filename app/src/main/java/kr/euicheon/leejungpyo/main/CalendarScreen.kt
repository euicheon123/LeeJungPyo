package kr.euicheon.leejungpyo.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kr.euicheon.leejungpyo.LeeViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import kr.euicheon.leejungpyo.R
import java.util.Date

@Composable
fun CalendarScreen(navController: NavController, vm: LeeViewModel) {
    val events by vm.calendarEvents.isInitialized


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // CalendarView
            // You can use a Compose library for Calendar or build your own custom calendar view here.

            Spacer(modifier = Modifier.height(16.dp))

            // ImageViews and TextViews for each day
            // For simplicity, here's a single day's example:
            val currentDate = Date() // Example date. Replace this with your desired date.
            val currentEvent = events[currentDate]

            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lee_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clickable {
                            // Handle Image click, maybe navigate to another screen with details?
                        }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = currentEvent ?: "No event", // Show event if there's any, else show "No event".
                    modifier = Modifier.clickable {
                        // Handle Text click.
                        // For instance, you can set the selected date in the ViewModel.
                        vm.setSelectedDate(currentDate)
                    }
                )
            }

            // You can continue for other days, or use a loop if the data is dynamic.

        }
    }
}




