    package com.example.chatapponline.sourse.data_class

    import java.io.Serializable

    data class User(
        val userId: String = "null",
        val userName: String,
        val userEmail: String,
        val userPassword: String,
        val userOnline: Boolean = false,
        val userImg: String = "",
        val userChats: Map<String, String> = mapOf()
    ): Serializable