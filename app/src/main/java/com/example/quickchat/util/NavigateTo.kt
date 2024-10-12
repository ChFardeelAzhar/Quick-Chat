package com.example.quickchat.util

import androidx.navigation.NavController
import com.example.quickchat.data.UserData

fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        launchSingleTop = true
    }
}


