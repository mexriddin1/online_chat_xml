package com.example.chatapponline.sourse.data_class

data class Message(
    val messageId: String,
    val receiverId: String,
    val senderId: String,
    val message: String,
    val messageTime: Long,
    val isRead: Boolean = false
)