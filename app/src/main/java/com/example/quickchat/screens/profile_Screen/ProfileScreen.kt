package com.example.quickchat.screens.profile_Screen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.quickchat.screens.bottomNavigationMenu.BottomNavigationMenu
import com.example.quickchat.screens.login_Screen.LoginViewModel
import com.example.quickchat.screens.signUp_Screen.SignupViewModel
import com.example.quickchat.util.NavRouts
import com.example.quickchat.viewModel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    signupViewModel: SignupViewModel = hiltViewModel()
) {

    val userData = signupViewModel.userData.value
    Log.d(
        "userDataCurrent",
        "Current user data checking : ${userData?.name.toString()} ${userData?.number.toString()}"
    )

    var name by remember { mutableStateOf(TextFieldValue(userData?.name ?: "")) }
    var number by remember { mutableStateOf(TextFieldValue(userData?.number ?: "")) }

    val auth = FirebaseAuth.getInstance()

    var selectedRoute =
        remember { mutableStateOf<NavRouts.Destination>(NavRouts.Destination.Profile) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.uploadProfileImage(uri)
                viewModel.createAndUpdateProfile(photoUrl = uri.toString())
                Log.d("uri", "ProfileScreen: ${uri.toString()}")
            }
        }

    val imageUrl = viewModel.userData.value?.photoUrl.toString()
    Log.d("imgUrl", "Uploaded photoUrl: ${imageUrl.toString()}")

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 3.dp)
                        .clickable {
                            navController.navigate(NavRouts.Destination.ChatList.route) {
                                navController.popBackStack()
                            }
                        }
                )


                TextButton(
                    onClick = {
                        viewModel.createAndUpdateProfile(
                            name = userData?.name.toString(),
                            number = userData?.number.toString()
                        )
                        navController.navigate(NavRouts.Destination.ChatList.route) {
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text(
                        text = "Save",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        },
        bottomBar = {
            BottomNavigationMenu(navController = navController, selectedRoute = selectedRoute)
        }
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            launcher.launch(
                                "image/*"
                            )
                        }, horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Card(
                        shape = CircleShape, modifier = Modifier
                            .padding(8.dp)
                            .size(120.dp)
                    )
                    {
                        viewModel.userData.value?.photoUrl.let {

                            CommonImage(dataUri = viewModel.userData.value?.photoUrl.toString())
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "${auth.currentUser?.email.toString()}",
                        style = MaterialTheme.typography.titleMedium
                    )


                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                label = { Text(text = "Name") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                )

            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = number,
                onValueChange = { number = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                label = { Text(text = "Number") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedButton(onClick = {
                auth.signOut()
                viewModel.userData.value = null
                navController.navigate(NavRouts.Destination.SignUp.route) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            }) {
                Text(text = "Logout", fontWeight = FontWeight.Bold)
            }

        }


    }
}


@Composable
fun CommonImage(
    dataUri: String?,
) {

    Image(
        painter = rememberImagePainter(data = dataUri),
        contentDescription = null,
        modifier = Modifier
            .wrapContentSize(),
        contentScale = ContentScale.Crop,
    )

}