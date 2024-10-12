package com.example.quickchat.screens.signUp_Screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quickchat.R
import com.example.quickchat.util.CheckUserSignedIn
import com.example.quickchat.util.NavRouts
import com.example.quickchat.util.navigateTo
import com.example.quickchat.viewModel.MainViewModel


@Composable
fun SignupScreen(navController: NavController, viewModel: SignupViewModel = hiltViewModel()) {


    var name by remember { mutableStateOf(TextFieldValue("")) }
    var number by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var showDialog = remember { mutableStateOf(false) }
    val state = viewModel.state.observeAsState()
    val context = LocalContext.current


    CheckUserSignedIn(viewModel = viewModel, navController = navController)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "Icon",
            modifier = Modifier.size(100.dp)
        )

        Text(
            text = "SignUp",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text(text = "Name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text(text = "Enter Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text(text = "Enter Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text(text = "Enter Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                viewModel.signUp(
                    name = name.text,
                    number = number.text,
                    email = email.text,
                    password = password.text
                )
            },
            shape = RoundedCornerShape(60.dp),
            enabled = !(name.text.isNullOrEmpty() || number.text.isNullOrEmpty() || email.text.isNullOrEmpty() || password.text.isNullOrEmpty())
        ) {
            Text(text = "SIGN UP")
        }

        Spacer(modifier = Modifier.height(30.dp))

        ClickableText(
            text = AnnotatedString("Already a User ? Go to Login -> "),
            onClick = {
                navigateTo(navController, NavRouts.Destination.Login.route)
            },
            style = TextStyle(color = MaterialTheme.colorScheme.primary)
        )

    }

    when (state.value) {

        0 -> {
            showDialog.value = true
        }

        1 -> {

        }

        2 -> {
            LaunchedEffect(key1 = true) {
                Toast.makeText(context, "sign up successfully...", Toast.LENGTH_SHORT).show()
            }
        }

        3 -> {
            LaunchedEffect(key1 = true) {
                Toast.makeText(context, "sign up failed ", Toast.LENGTH_SHORT).show()
                showDialog.value = false
            }
        }

    }

    if (showDialog.value) {

        Dialog(onDismissRequest = { }) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }

    }


}