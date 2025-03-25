package com.example.chatapponline.presenter.screen.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.chatapponline.R
import com.example.chatapponline.databinding.ScreenChatBinding
import com.example.chatapponline.presenter.screen.chat.adapter.MessageAdapter
import com.example.chatapponline.sourse.data_class.Message
import com.example.chatapponline.sourse.localStoraga.LocalStorage
import com.google.firebase.database.collection.LLRBNode.Color

class ChatScreen : Fragment(R.layout.screen_chat) {
    private val binding by viewBinding(ScreenChatBinding::bind)
    private val viewModel by viewModels<ChatViewModelImpl>()
    private val messageAdapter = MessageAdapter()

    private val localStorage = LocalStorage.instance
    private val args: ChatScreenArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.listMessage.setHasFixedSize(true)

        viewModel.getMessageByUser(localStorage.getUserEmailId() ?: "null", args.userEmail)
        viewModel.loadChatUser(args.userEmail)

        observerFunction()

        binding.listMessage.adapter = messageAdapter
        binding.listMessage.layoutManager.apply {
            binding.listMessage.smoothScrollToPosition(messageAdapter.currentList.size)
        }

        //bag
        binding.sentBnt.setOnClickListener {
            if (binding.message.text.toString().isNotEmpty()) {
                val message = Message(
                    messageId = System.currentTimeMillis().toString(),
                    messageTime = System.currentTimeMillis(),
                    message = binding.message.text.toString().trim(),
                    receiverId = args.userEmail,
                    senderId = localStorage.getUserEmailId() ?: "null"
                )
                viewModel.sentMessageByUser(message) {
//                    binding.listMessage.layoutManager?.scrollToPosition(binding.listMessage.adapter!!.itemCount)

                }
            }
            binding.message.setText("")
        }
        viewModel.messageList.observe(viewLifecycleOwner) { messages ->
            Log.d("TAG", "onViewCreated: $messages")
            messageAdapter.submitList(messages)

            if (messages.isNotEmpty()) {
                binding.listMessage.smoothScrollToPosition(messages.size - 1)
            }
        }


        binding.backBnt.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun observerFunction() {
        viewModel.messageList.observe(viewLifecycleOwner) {
            messageAdapter.submitList(it)
        }

//        viewModel.user.observe(viewLifecycleOwner) {
//
//            binding.nameTitle.text = it.userName
//            Glide.with(binding.root)
//                .load(it.userImg)
//                .into(binding.mainImg)
//
//            binding.userOnline.text = (if (it.userOnline) "Online" else "Offline")
//            binding.userOnline.setTextColor(
//                ContextCompat.getColor(
//                    requireContext(),
//                    if (it.userOnline) R.color.online else R.color.offline
//                )
//            )
//        }
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.nameTitle.text = user.userName
            Glide.with(binding.root)
                .load(user.userImg)
                .placeholder(R.raw.user)
                .into(binding.mainImg)

            if (user.userOnline) {
                binding.userOnline.text = "Online"
                binding.userOnline.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.online
                    )
                )
            } else {
//                val lastSeen = DateFormat.getDateTimeInstance().format(user.lastSeen.toDate())
                binding.userOnline.text = "Offline"
                binding.userOnline.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.offline
                    )
                )
            }
        }

    }


}