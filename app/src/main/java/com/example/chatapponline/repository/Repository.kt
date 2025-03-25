package com.example.chatapponline.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatapponline.sourse.data_class.Message
import com.example.chatapponline.sourse.data_class.User
import com.example.chatapponline.sourse.localStoraga.LocalStorage
import com.example.chatapponline.utils.toMessage
import com.google.android.play.integrity.internal.u
import com.google.firebase.Firebase
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.firestore
import kotlin.math.log

class Repository private constructor() {
    companion object {
        private lateinit var instance: Repository

        fun getInstance(): Repository {
            if (!::instance.isInitialized) {
                instance = Repository()
            }
            return instance
        }
    }

    private val db = Firebase.firestore
    private val localStorage: LocalStorage = LocalStorage.instance

    private val _message: MutableLiveData<List<Message>> = MutableLiveData<List<Message>>()
    val message : LiveData<List<Message>> = _message
    val messageDatabase = db.collection("messageData")

    //ERROR
    private val userMessage = MutableLiveData<ArrayList<Message>>()

    private val chatUser = MutableLiveData<User>()
    private val chatUserList = MutableLiveData<ArrayList<User>>()


    val _userMessage = userMessage
    val _chatUserList = chatUserList
    val _chatUser = chatUser

    init {
        updateChat()
    }

    private fun updateChat() {
        db.collection("userData")
            .where(
                Filter.or(
                    Filter.and(
                        Filter.notEqualTo(
                            (User::userEmail.name),
                            localStorage.getUserEmailId().toString()
                        )
                    )
                )
            )

            .addSnapshotListener { query, _ ->
                val ls = ArrayList<User>()
                query?.forEach {
                    ls.add(
                        User(
                            userId = it.id,
                            userName = it.getString(User::userName.name) ?: "none",
                            userEmail = it.getString(User::userEmail.name) ?: "none",
                            userPassword = it.getString(User::userPassword.name)
                                ?: "none",
                            userOnline = it.getBoolean(User::userOnline.name) ?: false,
                            userImg = it.getString(User::userImg.name) ?: "none"
                        )
                    )
                }

                chatUserList.value = ls
            }
    }

    fun addUser(
        user: User,
        onSuccess: () -> Unit,
        onFiled: (Exception) -> Unit
    ) {
        val map = mapOf(
            User::userName.name to user.userName,
            User::userEmail.name to user.userEmail,
            User::userPassword.name to user.userPassword,
            User::userImg.name to user.userImg,
            User::userOnline.name to user.userOnline,
            User::userChats.name to user.userChats,
        )

        db.collection("userData")
            .document(user.userEmail)
            .set(map)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFiled)

        localStorage.setUserEmailId(user.userEmail)
        updateChat()
        localStorage.setUserPassword(user.userPassword)
        localStorage.setUserName(user.userName)
    }

    // ERROR
    fun checkEmailByData(
        userEmail: String,
        onExist: (bool: Boolean) -> Unit
    ) {
        db.collection("userData")
            .where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo(
                            (User::userEmail.name), userEmail
                        )
                    )
                )
            )
            .addSnapshotListener { query, _ ->
                query?.forEach {
                    if ((it.getString(User::userPassword.name) ?: "none") != "none") {
                        localStorage.setUserEmailId(userEmail)
                        updateChat()
                        onExist.invoke(true)
                    } else {
                        onExist.invoke(false)
                    }
                }
            }
    }

    // FIX ERROR
    fun checkEmailPassword(
        userEmail: String,
        password: String,
        onExist: (bool: Boolean) -> Unit
    ) {
        db.collection("userData")
            .where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo(
                            (User::userEmail.name), userEmail
                        )
                    )
                )
            )
            .addSnapshotListener { query, _ ->
                query?.forEach {
                    Log.d("HHH", "Pas $it")
                    if (it.getString(User::userPassword.name).toString() == password) {
                        localStorage.setUserEmailId(userEmail)
                        updateChat()
                        onExist.invoke(true)
                    } else {
                        onExist.invoke(false)
                    }
                }
            }
    }


    //Commented by Sanjar
    fun getUserMessage(
        senderId: String,
        receiverId: String
    ): MutableLiveData<List<Message>> {
//
        messageDatabase.where(
            Filter.or(
                Filter.and(
                    Filter.equalTo(Message::senderId.name, senderId),
                    Filter.equalTo(Message::receiverId.name, receiverId)
                ),
                Filter.and(
                    Filter.equalTo(Message::senderId.name, receiverId),
                    Filter.equalTo(Message::receiverId.name, senderId)
                )
            )

        ).addSnapshotListener { querySnapshot, _ ->
            val ls = ArrayList<Message>()
            querySnapshot?.map { ls.add(it.toMessage()) }
            _message.value = ls
        }
        return _message
    }

    fun addMessage(message: Message, onSuccess: () -> Unit) {
        val map = mapOf(
            "messageId" to message.messageId,
            "receiverId" to message.receiverId,
            "senderId" to message.senderId,
            "message" to message.message,
            "messageTime" to message.messageTime,
            "isRead" to message.isRead

        )
        messageDatabase.document(System.currentTimeMillis().toString()).set(map)
            .addOnSuccessListener { onSuccess() }
    }

    //Bug
    fun sendUserMessage(
        message: Message
    ) {
        val map = mapOf(
            Message::messageId.name to message.messageId,
            Message::receiverId.name to message.receiverId,
            Message::senderId.name to message.senderId,
            Message::messageTime.name to message.messageTime,
            Message::message.name to message.message,
            Message::isRead.name to message.isRead,
        )
        db.collection("messageData")
            .add(map)
    }

    fun loadChatUserData(userEmail: String) {
        db.collection("userData")
            .where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo(
                            (User::userEmail.name), userEmail
                        )
                    )
                )
            )
            .addSnapshotListener { query, _ ->
                query?.forEach {
                    chatUser.value = User(
                        userId = it.id,
                        userName = it.getString(User::userName.name) ?: "none",
                        userEmail = it.getString(User::userEmail.name) ?: "none",
                        userPassword = "***",
                        userOnline = it.getBoolean(User::userOnline.name) ?: false,
                        userImg = it.getString(User::userImg.name) ?: "none"
                    )
                }
            }
    }


//fun updateUserData(
//        userEmail: String,
//        userName: String,
//        userPassword: String,
////        userImg: String = "none"
//    ) {
////        val map = if (userImg != "none") {
////            mapOf(User::userName.name to userName, User::userPassword.name to userPassword)
////        } else {
//           val map = mapOf(
//                User::userName.name to userName,
//                User::userPassword.name to userPassword
////                User::userImg.name to userImg
//            )
////        }
//
//        db.collection("userData")
//            .whereEqualTo("userEmail", userEmail)
//            .get()
//            .addOnCompleteListener {
//                db.collection("userData")
//                    .document(it.result.documents[0].id)
//                    .update(map)
//            }
//    }

//    fun updateOnline(
//        userEmail: String,
//        userOnline: Boolean
//    ) {
//        val map = mapOf(User::userOnline.name to userOnline)
//
//        db.collection("userData")
//            .whereEqualTo("userEmail", userEmail)
//            .get()
//            .addOnCompleteListener {
//                db.collection("userData")
//                    .document(it.result.documents[0].id)
//                    .update(map)
//            }
//    }

//    fun getUserBySearchText(
//        userName: String
//    ) {
//        db.collection("userData")
//            .whereEqualTo("userEmail", userName)
//            .get()
//            .addOnCompleteListener { query ->
//                val ls = ArrayList<User>()
//                query.result.let {
//                    for (it in query.result) {
//                        ls.add(
//                            User(
//                                userId = it.id,
//                                userName = it.getString(User::userName.name) ?: "none",
//                                userEmail = it.getString(User::userEmail.name) ?: "none",
//                                userPassword = it.getString(User::userPassword.name)
//                                    ?: "none",
//                                userOnline = it.getBoolean(User::userOnline.name) ?: false,
//                                userImg = it.getString(User::userImg.name) ?: "none"
//                            )
//                        )
//                    }
//                }
//                chatUserListBySearch.value = ls
//            }
//
//        fun checkEmail(
//            email: String,
//            password: String,
//            onExist: (bool: Boolean) -> Unit
//        ) {
//            db.collection("userData")
//                .whereEqualTo("userEmail", email)
//                .get()
//                .addOnCompleteListener { query ->
//                    if (query.isSuccessful) {
//                        if (query.result.documents.isNotEmpty()) {
//                            if (query.result.documents[0][User::userPassword.name].toString() == password) {
////                                localStorage.setUserEmailId(
////                                    query.result.documents[0].getString("userEmail")
////                                        ?: "Don't found"
////                                )
//                            }
//                            onExist.invoke(query.result.documents[0][User::userPassword.name].toString() == password)
//                        } else {
//                            onExist.invoke(false)
//                        }
//                    }
//                }
//        }
//
//    }

//    fun getChatData(id: String) {
//        db.collection("userData")
//            .whereEqualTo("userEmail", id)
//            .addSnapshotListener { query, _ ->
//                query?.forEach {
//                    chatUser.value = User(
//                        userId = it.id,
//                        userName = it.getString(User::userName.name) ?: "none",
//                        userEmail = it.getString(User::userEmail.name) ?: "none",
//                        userPassword = it.getString(User::userPassword.name) ?: "none",
//                        userOnline = it.getBoolean(User::userOnline.name) ?: false,
//                        userImg = it.getString(User::userImg.name) ?: "none"
//                    )
//                }
//            }
//    }

}
