package com.example.chatapponline.app

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.chatapponline.sourse.localStoraga.LocalStorage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AppLifecycleObserver(private val firestore: Firebase) :
    DefaultLifecycleObserver {
        val localStorage = LocalStorage.instance

    override fun onStart(owner: LifecycleOwner) {

        firestore.firestore
            .collection("userData")
            .document(localStorage.getUserEmailId().toString())
            .update("userOnline", true)
            .addOnFailureListener { error -> Log.e("FirestoreError", error.message ?: "Unknown error") }
    }

    override fun onStop(owner: LifecycleOwner) {
        firestore.firestore
            .collection("userData")
            .document(localStorage.getUserEmailId().toString())
            .update("userOnline", false)
            .addOnFailureListener { error -> Log.e("FirestoreError", error.message ?: "Unknown error") }
    }

}
