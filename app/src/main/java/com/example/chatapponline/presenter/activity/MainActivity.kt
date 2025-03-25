package com.example.chatapponline.presenter.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.chatapponline.R
import com.example.chatapponline.repository.Repository
import com.example.chatapponline.sourse.data_class.User
import com.example.chatapponline.sourse.localStoraga.LocalStorage


class MainActivity : AppCompatActivity() {
    private val repository = Repository.getInstance()
    private val localStorage = LocalStorage.instance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        window.statusBarColor = ContextCompat.getColor(this, R.color.header_bg);
//        repository.updateOnline(localStorage.getUserEmailId() ?: "null", true)
        repository._chatUser.value = User("", "", "","",true)
    }

    override fun onStop() {
        super.onStop()
        repository._chatUser.value = User("", "", "","",false)

//        repository.updateOnline(localStorage.getUserEmailId() ?: "null", false)
    }
}