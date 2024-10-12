package com.example.quickchat.screens.status_Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.quickchat.screens.bottomNavigationMenu.BottomNavigationMenu
import com.example.quickchat.util.NavRouts
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusScreen(navController: NavController) {

    var selectedRoute = remember { mutableStateOf<NavRouts.Destination>(NavRouts.Destination.StatusList) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Status",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
            })
        },
        bottomBar = {
            BottomNavigationMenu(navController = navController, selectedRoute = selectedRoute)
        }
    ) {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "No status available")

            }


        }
    }
}