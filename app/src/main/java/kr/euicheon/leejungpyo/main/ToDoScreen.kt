package kr.euicheon.leejungpyo.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kr.euicheon.leejungpyo.LeeViewModel
import kr.euicheon.leejungpyo.data.LeeDate

@Composable
fun ToDoScreen(navController: NavController, vm: LeeViewModel, date: LeeDate) {

    Column() {
        //use lambda LeeDate
        Text(text = "something")
    }
}