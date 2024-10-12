package com.example.quickchat.util


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.quickchat.screens.signUp_Screen.SignupViewModel
import com.example.quickchat.viewModel.MainViewModel


@Composable
fun CheckUserSignedIn(viewModel: SignupViewModel, navController: NavController) {

    val alreadySignIn = remember { mutableStateOf(false) }
    val signIn = viewModel.signIn.value


    if (signIn && !alreadySignIn.value) {
        alreadySignIn.value = true

        navController.navigate(NavRouts.Destination.ChatList.route) {
            navController.popBackStack()
        }

    }

}