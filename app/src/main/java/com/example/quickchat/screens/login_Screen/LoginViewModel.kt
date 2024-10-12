package com.example.quickchat.screens.login_Screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quickchat.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    val database : FirebaseFirestore
) : ViewModel() {

    val state = MutableLiveData(1)
    val userData = mutableStateOf<UserData?>(null)
    val currentUser = auth.currentUser

    init {
        currentUser?.uid?.let {
            getUserData(it)
        }
    }



    fun login(email : String, password : String){

        state.postValue(0)

        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
            state.postValue(0)

            if (it.isSuccessful){
                auth.currentUser?.uid?.let {
                    getUserData(it)
                    state.postValue(2)
                }
            }

        }.addOnFailureListener {
            state.postValue(3)
                Log.d("LoginFailed", "Login Failed: $it")
        }

    }


    fun getUserData(userId: String) {

        state.postValue(0)

        database.collection("user").document(userId).addSnapshotListener { value, error ->

            if (value != null) {                        // we will create a variable to store the value and convert it in object of type UserData
                val user = value.toObject<UserData>()   // we are just trying to typeCast the value in UserData object
                userData.value = user

                Log.d("MyUserLogin", "Login user: ${user?.name} ${user?.number}")
                state.postValue(2)
            }

            if (error != null) {
                state.postValue(3)
            }

        }
    }

}