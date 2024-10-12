package com.example.quickchat.util

object NavRouts{
    sealed class Destination(var route: String) {
        object SignUp     : Destination("SignUp")
        object Login      : Destination("Login")
        object Profile    : Destination("Profile")
        object ChatList   : Destination("ChatList")
        object StatusList : Destination("StatusList")

        object SingleChat : Destination("SingleChat/{chatId}") {
            fun createRoute(chatId: String) = "SingleChat/$chatId"
        }

        object SingleStatus : Destination("SingleStatus/{userId}") {
            fun createRoute(userId: String) = "SingleStatus/$userId"
        }

    }
}
