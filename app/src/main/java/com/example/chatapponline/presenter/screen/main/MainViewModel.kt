package com.example.chatapponline.presenter.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatapponline.sourse.data_class.User

interface MainViewModel {
//    val updateProfile: MutableLiveData<User>
//    fun updateProfileClick()
    val chatsList: MutableLiveData<ArrayList<User>>
}