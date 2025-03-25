package com.example.chatapponline.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.chatapponline.sourse.localStoraga.LocalStorage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class App : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        LocalStorage.init()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(Firebase))

    }
}