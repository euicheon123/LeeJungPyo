package kr.euicheon.leejungpyo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.euicheon.leejungpyo.auth.LoginScreen
import kr.euicheon.leejungpyo.auth.SignupScreen
import kr.euicheon.leejungpyo.data.LeeDate
import kr.euicheon.leejungpyo.main.CalendarScreen
import kr.euicheon.leejungpyo.main.NotificationMessage
import kr.euicheon.leejungpyo.main.PostScreen
import kr.euicheon.leejungpyo.main.ToDoScreen
import kr.euicheon.leejungpyo.ui.theme.LeejungpyoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeejungpyoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LeeJungPyoApp()
                }
            }
        }
    }
}

sealed class DestinationScreen(val route: String) {
    object Signup : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Calendar: DestinationScreen("calendar")
    object ToDo: DestinationScreen("todo")
    object Post: DestinationScreen("post")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeeJungPyoApp() {

    val vm = hiltViewModel<LeeViewModel>()
    val navController = rememberNavController()

    
    NotificationMessage(vm = vm)


    NavHost(
        navController = navController, startDestination = DestinationScreen.Signup.route
    ) {
        composable(DestinationScreen.Signup.route) {
            SignupScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.Login.route) {
            LoginScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.Calendar.route) {
            CalendarScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.ToDo.route) {
            val leeDate =
                navController.previousBackStackEntry?.arguments?.getParcelable<LeeDate>("todo")
            leeDate?.let {
                ToDoScreen(navController = navController, vm = vm, date = leeDate)
            }
        }
        composable(DestinationScreen.Post.route) {
            PostScreen(navController = navController, vm = vm)
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LeejungpyoTheme {
        LeeJungPyoApp()
    }
}