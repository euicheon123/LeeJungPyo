package kr.euicheon.leejungpyo.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import kr.euicheon.leejungpyo.DestinationScreen
import kr.euicheon.leejungpyo.LeeViewModel
import kr.euicheon.leejungpyo.R
import kr.euicheon.leejungpyo.data.LeeDate

@Composable
fun ToDoScreen(navController: NavController, vm: LeeViewModel, date: LeeDate) {

    Column() {
        Image(
            painter = painterResource(id = R.drawable.lee_logo),
            contentDescription = "For ToDo",
            modifier = Modifier
                .clickable { navigateTo(navController, DestinationScreen.CreateToDo, NavParam("createtodo", date)) }
        )

    }
}