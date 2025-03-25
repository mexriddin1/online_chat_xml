package com.example.chatapponline.presenter.screen.chat


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.example.chatapponline.repository.Repository
import com.example.chatapponline.sourse.data_class.Message
import com.example.chatapponline.sourse.data_class.User

class ChatViewModelImpl : ViewModel(), ChatViewModel {
    private val repository: Repository = Repository.getInstance()
    override val user = MutableLiveData<User>()
    override val messageList = repository.getUserMessage("","")
//    override val messageList = repository.getUserMessage()

    init {
//        repository._userMessage.observeForever {
//            messageList.value = it
//        }

        repository._chatUser.observeForever {
            user.value = it
        }

    }
//    init {
//        repository.message.observeForever {
//            messageList.value = it
//        }
//    }

    override fun getMessageByUser(senderId: String, readerId: String) {
        repository.getUserMessage(senderId, readerId)
    }

    override fun sentMessageByUser(message: Message,onSuccess: ()->Unit) {
        repository.addMessage(message){
            onSuccess()
        }
    }

    override fun loadChatUser(userId: String) {
        repository.loadChatUserData(userId)
    }
}