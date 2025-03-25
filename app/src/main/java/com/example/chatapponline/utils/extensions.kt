package com.example.chatapponline.utils

import com.example.chatapponline.sourse.data_class.Message
import com.google.firebase.firestore.QueryDocumentSnapshot

fun QueryDocumentSnapshot.toMessage() = Message(
    messageId = getString(Message::messageId.name).toString(),
    receiverId = getString(Message::receiverId.name).toString(),
    senderId = getString(Message::senderId.name).toString(),
    message = getString(Message::message.name).toString(),
    messageTime = getLong(Message::messageTime.name) ?: 0, // null berishi mumkin!
    isRead = getBoolean(Message::isRead.name) ?: false
)