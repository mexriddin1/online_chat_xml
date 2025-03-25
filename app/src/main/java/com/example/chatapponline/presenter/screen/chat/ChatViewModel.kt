package com.example.chatapponline.presenter.screen.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatapponline.sourse.data_class.Message
import com.example.chatapponline.sourse.data_class.User

interface ChatViewModel {
    val messageList: LiveData<List<Message>>
    val user: LiveData<User>

    fun getMessageByUser(senderId: String, readerId: String)
    fun loadChatUser(userId:String)
    fun sentMessageByUser(message: Message, onSuccess: () -> Unit)
}