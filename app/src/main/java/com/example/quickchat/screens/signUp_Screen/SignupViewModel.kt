package com.example.quickchat.screens.signUp_Screen

import android.app.Activity
import android.net.Uri
import android.nfc.Tag
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quickchat.data.UserData
import com.example.quickchat.util.navigateTo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import java.util.concurrent.CancellationException
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
) : ViewModel() {

    val state = MutableLiveData(1)
    val signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)  // our UserData class (ModelClass)
    private val currentUser = auth.currentUser


    init {
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)     // this function work like to set userdata value but its name is totally confusion
//            Log.d("data", "Current Data Value is : ${data.toString()}")
        }
    }


    fun signUp(name: String, number: String, email: String, password: String) {

        state.postValue(0)

        /*
        // we will check here the current number is first available in the chat or not

        database.collection("user").whereEqualTo("number", number).get().addOnSuccessListener { it ->
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { user->
                    if (user.isSuccessful) {
                        createAndUpdateProfile(name = name, number = number)
                        signIn.value = true
                    }
                }.addOnFailureListener {
                    state.postValue(3)
                }
            }
        }.addOnFailureListener {
            state.postValue(3)
        }

         */

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                state.postValue(2)
                createAndUpdateProfile(name, number)
                Log.d("Name", "Values : $name $number")
                signIn.value = true
            }
        }.addOnFailureListener {
            state.postValue(3)
            Log.d("error", "Error: ${it.toString()}")
        }

    }



     private fun createAndUpdateProfile(
        name: String? = null,
        number: String? = null,
        photoUrl: String? = null
    ) {

        val userId = auth.currentUser?.uid
        // i am just trying to make this with out map our UserData model class as shown in video

        val currentUserData = UserData(
            userId = userId,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            photoUrl = photoUrl ?: userData.value?.photoUrl,
        )


        userId?.let {

            state.postValue(0)
            database.collection("user").document(userId).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                   //  database.collection("user").document(userId).set(currentUserData)/

                    } else {
                        database.collection("user").document(userId).set(currentUserData)
                        state.postValue(1)
                        getUserData(it.id)   // i have to get this data in init block
                    }

                }.addOnFailureListener {
                    state.postValue(3)
                }
        }


    }

    fun getUserData(userId: String) {

        state.postValue(0)

        database.collection("user").document(userId).addSnapshotListener { value, error ->
            if (value != null) {
                val user = value.toObject<UserData>()
                Log.d("getUserData", "getUserData: ${user?.name} ${user?.number}")
                userData.value = user
                state.postValue(1)
            }

            if (error != null) {
                Log.d("signUpFailed", "getUserData: ${error.toString()}")
                state.postValue(3)
            }

        }
    }


}