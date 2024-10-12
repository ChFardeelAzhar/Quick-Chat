package com.example.quickchat.screens.profile_Screen

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quickchat.data.UserData
import com.example.quickchat.screens.signUp_Screen.SignupViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : ViewModel() {


    val userData = mutableStateOf<UserData?>(null)
    val selectedImage = mutableStateOf<Uri?>(null)
    val state = MutableLiveData(0)
    val authUserData = auth.currentUser


    init {

    }


    fun uploadProfileImage(uri: Uri) {
        uploadImageToFirebase(uri) { uri ->
//            selectedImage.value = uri
            createAndUpdateProfile(photoUrl = uri.toString())
        }
    }

     fun uploadImageToFirebase(uri: Uri, onSuccess: (Uri) -> Unit) {
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)

        state.postValue(0)

        uploadTask.addOnSuccessListener {

            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener { uri ->
                onSuccess(uri)
//                selectedImage.value = uri
                createAndUpdateProfile(photoUrl = uri.toString())
                Log.d("PhotoUrl", "ImageUrl:$uri")
            }
            // result?.addOnSuccessListener(onSuccess)
        }

    }

     fun createAndUpdateProfile(
        name: String? = null,
        number: String? = null,
        email: String? = null,
        photoUrl: String? = null
    ) {

        val userId = auth.currentUser?.uid

        // i am just trying to make this with out map our UserData model class as shown in video

        val currentUserData = UserData(
            userId = userId,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            email = email ?: userData.value?.email,
            photoUrl = photoUrl ?: userData.value?.photoUrl,
        )

        // this block is for updating and creation of new profile
        userId?.let {
            state.postValue(0)
            database.collection("user").document(userId).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        getUserData(it.id)
//                        createAndUpdateProfile(photoUrl = photoUrl.toString())

                    } else {
                        database.collection("user").document(userId).set(currentUserData)
                        getUserData(userId)
                        state.postValue(1)
                    }

                }.addOnFailureListener {
                    Log.d("imageUploadFailed", "Uploading Failed...: ${it.toString()}")
                    state.postValue(3)
                }
        }


    }

    fun getUserData(userId: String) {

        state.postValue(0)
        database.collection("user").document(userId).addSnapshotListener { value, error ->

            if (value != null) {
                value?.let {
                    val userdata = value.toObject<UserData>()
                    userData.value = userdata

                    Log.d("LoginUserData", "getUserData: ${userdata?.name} ${userdata?.number}")
                    state.postValue(1)
                }
            }

            if (error != null) {
                error?.let {
                    state.postValue(3)
                }
            }
        }

    }


}