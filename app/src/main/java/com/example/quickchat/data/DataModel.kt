package com.example.quickchat.data

data class UserData(
    var userId: String? = "",
    var name: String? = "",
    var number: String? = "",
    var email: String? = "",
    var photoUrl: String? = "",
) {
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "photoUrl" to photoUrl
    )
}


data class ChatData(
    val chatId: String? = "",
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser()
)


data class ChatUser(
    val chatId: String? = "",
    val name: String? = "",
    val number: String? = "",
    val photoUrl: String? = "",

    )
