package com.example.quickchat.viewModel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quickchat.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    val state = MutableLiveData(1)
    val signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)  // our UserData class (ModelClass)

    val selectedImage = mutableStateOf<Uri?>(null)

    private val currentUser = auth.currentUser


    init {
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)     // this function work like to set userdata value but its name is totally confusion
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
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                state.postValue(2)
                createAndUpdateProfile(name, number)
                signIn.value = true
            }
        }.addOnFailureListener {
            state.postValue(3)
        }

    }


    fun createAndUpdateProfile(
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


        // this block is for updating and creation of new profile
        userId?.let {

            state.postValue(0)
            database.collection("user").document(userId).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        // update profile

                    } else {
                        database.collection("user").document(userId).set(currentUserData)
                        state.postValue(1)
                        getUserData(it.id)
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
                userData.value = user

                Log.d("userInfo", "getUserDataValue: ${user?.name} ${user?.number}")
                state.postValue(1)
            }

            if (error != null) {
                state.postValue(3)
            }

        }
    }


    fun login(email: String, password: String) {

        state.postValue(0)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                state.postValue(0)

                if (it.isSuccessful) {
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                }

            }.addOnFailureListener {
                state.postValue(3)
            }

    }


    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) { uri ->
            selectedImage.value = uri
            createAndUpdateProfile(photoUrl = uri.toString())
        }
    }

    private fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)

        state.postValue(0)

        uploadTask.addOnSuccessListener {

            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener { uri ->
                onSuccess(uri)
                selectedImage.value = uri
                Log.d("PhotoUrl", "ImageUrl:$uri")
            }
            // result?.addOnSuccessListener(onSuccess)
        }

    }

}