package kr.euicheon.leejungpyo.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kr.euicheon.leejungpyo.LeeViewModel

@Composable
fun PostScreen(navController: NavController, vm: LeeViewModel) {
    Text(text = "Post Screen")
}