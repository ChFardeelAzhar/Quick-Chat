package com.example.quickchat.screens.chatList_Screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quickchat.data.ChatData
import com.example.quickchat.data.ChatUser
import com.example.quickchat.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val database: FirebaseFirestore,
    val auth : FirebaseAuth
) : ViewModel() {

    val chats = mutableStateOf<List<ChatData>>(emptyList())
    val userData = mutableStateOf<UserData?>(null)
    val state = MutableLiveData(1)

    fun onAddChat(number: String) {
        state.postValue(0)
        if (number.isEmpty() or !number.isDigitsOnly()) {
            state.postValue(3) // toast enter the correct number
        } else {

            database.collection("chats").where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userData.value?.number)
                    ),
                    Filter.and(
                        Filter.equalTo("user1.number", userData.value?.number),
                        Filter.and(Filter.equalTo("user2.number", number))
                    )
                )
            ).get().addOnSuccessListener {

                if (it.isEmpty) {

                    //     First i will check the current entered number is first entered or not
                    database.collection("user").whereEqualTo("number", number).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                state.postValue(5) // toast number not found
                            } else {

                                // we're creating a chat room here

                                // database se query laga ke data ko access kya ha to is lye data array ki sorat me aye ga is lye iski 0th index li ha
                                val chatPartner =
                                    it.toObjects<UserData>()[0]       // firebse se data objects ki form me atta ha is lye usko type cast karna parta ha
                                val id = database.collection("chats")
                                    .document().id         // is id pe ham apne data ko send karen gay jo abi idar create karen gay
                                val chat = ChatData(
                                    chatId = id,
                                    ChatUser(
                                        chatId = userData.value?.userId,
                                        name = userData.value?.name,
                                        number = userData.value?.number,
                                        photoUrl = userData.value?.photoUrl,
                                    ),
                                    ChatUser(
                                        chatId = chatPartner.userId,
                                        name = chatPartner.name,
                                        number = chatPartner.number,
                                        photoUrl = chatPartner.photoUrl,
                                    )
                                )
                                database.collection("chats").document(id).set(chat)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            state.postValue(2)
                                        }
                                    }.addOnFailureListener {
                                        state.postValue(6)
                                    }

                            }
                        }.addOnFailureListener {
                            state.postValue(6) // toast for this error Failed to set data in data base
                            Log.d("Failed", "onAddChat: ${it.toString()}")
                        }

                } else {
                    state.postValue(4) // toast chat already exist
                }
            }

        }

    }

    fun populateChat() {


        state.postValue(1) //show progressbar

        database.collection("chats").where(
            Filter.or(
                Filter.equalTo("user1.id", userData.value?.userId),
                Filter.equalTo("user2.id", userData.value?.userId)
            )

        ).addSnapshotListener { value, error ->

            if (value != null) {
                val currentUserChat = value.toObjects<ChatData>()
                chats.value = currentUserChat
            }


            if (value != null) {
                chats.value = value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
            }
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
                    populateChat()
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