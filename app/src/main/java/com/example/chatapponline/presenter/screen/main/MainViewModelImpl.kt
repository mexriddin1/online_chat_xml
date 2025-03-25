package com.example.chatapponline.presenter.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapponline.repository.Repository
import com.example.chatapponline.sourse.data_class.User

class MainViewModelImpl : ViewModel(), MainViewModel {
    private val repository: Repository = Repository.getInstance()
    override val chatsList = MutableLiveData<ArrayList<User>>()
    val updateLiveData: MutableLiveData<User> = MutableLiveData<User>()

    init {
        repository._chatUserList.observeForever {
            chatsList.value = it
        }
    }

    fun updateProfile(user: User) {
//        repository.updateUserData(user.userEmail, user.userName, user.userPassword)
        updateLiveData.value = user
    }

}