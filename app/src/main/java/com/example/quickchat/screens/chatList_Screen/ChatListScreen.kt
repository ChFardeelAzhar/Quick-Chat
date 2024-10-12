package com.example.quickchat.screens.chatList_Screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quickchat.screens.bottomNavigationMenu.BottomNavigationMenu
import com.example.quickchat.screens.signUp_Screen.SignupViewModel
import com.example.quickchat.util.NavRouts

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatScreenViewModel = hiltViewModel(),
    signupViewModel: SignupViewModel = hiltViewModel()
) {


    var selectedRoute =
        remember { mutableStateOf<NavRouts.Destination>(NavRouts.Destination.ChatList) }
    var showDialog by remember { mutableStateOf(false) }
    var addChatNumber by remember { mutableStateOf("") }
    var showProgressBar by remember { mutableStateOf(false) }
    val onAddChat: (String) -> Unit = {
        viewModel.onAddChat(it)
    }
    val state = viewModel.state.observeAsState()
    val context = LocalContext.current
    val chats = viewModel.chats.value
    val userdata = signupViewModel.userData.value

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Chats",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier
                    .padding(7.dp)
                    .padding(bottom = 20.dp),
                containerColor = MaterialTheme.colorScheme.tertiary,
            ) {
                Image(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.background)
                )
            }
        },
        bottomBar = {
            BottomNavigationMenu(navController = navController, selectedRoute)
        }
    ) {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), contentAlignment = Alignment.Center
        ) {

            if (chats.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Chats Available", style = MaterialTheme.typography.titleMedium)
                    Image(imageVector = Icons.Filled.Chat, contentDescription = null)
                }
            } else {

                LazyColumn(modifier = Modifier.padding(4.dp)) {
                    items(chats) { chat ->
                        val chatUser =
                            if (chat.user1.chatId == userdata?.userId) {
                                chat.user1
                            } else {
                                chat.user2
                            }

                    }
                }
            }


        }



        when (state.value) {

            0 -> {
                showProgressBar = true
            }

            1 -> {

            }

            2 -> {
                LaunchedEffect(key1 = true) {
                    Toast.makeText(context, "Chat Added Successfully...", Toast.LENGTH_SHORT).show()
                    showDialog = false
                }
            }

            3 -> {
                LaunchedEffect(key1 = true) {
                    Toast.makeText(context, "Enter the correct Number", Toast.LENGTH_SHORT).show()
                    showProgressBar = false
                }
            }

            4 -> {
                LaunchedEffect(key1 = true) {
                    Toast.makeText(context, "Chat already exist", Toast.LENGTH_SHORT).show()
                    showProgressBar = false

                }
            }

            5 -> {
                LaunchedEffect(key1 = true) {
                    Toast.makeText(context, "Number not found", Toast.LENGTH_SHORT).show()
                    showProgressBar = false

                }
            }

            6 -> {
                LaunchedEffect(key1 = true) {
                    Toast.makeText(context, "Failed to set data in database", Toast.LENGTH_SHORT)
                        .show()
                    showProgressBar = false

                }
            }

        }


        if (showProgressBar) {

            Dialog(onDismissRequest = { }) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

        }

        if (showDialog) {
            AlertDialog(
                title = { Text(text = "Add Chat", fontWeight = FontWeight.Bold) },
                onDismissRequest = {
                    showDialog = false
                    addChatNumber = ""
                },
                text = {
                    OutlinedTextField(
                        value = addChatNumber,
                        onValueChange = { addChatNumber = it },
                        label = { Text(text = "Add Number") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),


                        )
                },
                confirmButton = {
                    OutlinedButton(
                        onClick = {
                            onAddChat(addChatNumber)
                            addChatNumber = ""
                            showDialog = false
                        }) {
                        Text(text = "save")
                    }
                })
        }


    }


}