package com.example.quickchat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickchat.screens.bottomNavigationMenu.BottomNavigationMenu
import com.example.quickchat.screens.chatList_Screen.ChatListScreen
import com.example.quickchat.screens.login_Screen.LoginScreen
import com.example.quickchat.screens.profile_Screen.ProfileScreen
import com.example.quickchat.screens.signUp_Screen.SignupScreen
import com.example.quickchat.screens.status_Screen.StatusScreen
import com.example.quickchat.util.NavRouts
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        val navController = rememberNavController()

        Scaffold(

        ) {

            NavHost(
                navController = navController,
                startDestination = NavRouts.Destination.SignUp.route
            ) {

                composable(NavRouts.Destination.SignUp.route) {
                    SignupScreen(navController)
                }

                composable(NavRouts.Destination.Login.route) {
                    LoginScreen(navController = navController)
                }

                composable(NavRouts.Destination.ChatList.route) {
                    ChatListScreen(navController)
                }

                composable(NavRouts.Destination.StatusList.route) {
                    StatusScreen(navController)
                }

                composable(NavRouts.Destination.Profile.route) {
                    ProfileScreen(navController )
                }

            }


        }


    }


}
