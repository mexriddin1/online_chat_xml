package com.example.chatapponline.presenter.screen.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chatapponline.R
import com.example.chatapponline.databinding.DialogUpdateUserBinding
import com.example.chatapponline.databinding.ScreenUserBinding
import com.example.chatapponline.presenter.screen.main.adapter.MainItemAdapter
import com.example.chatapponline.sourse.data_class.User
import com.example.chatapponline.sourse.localStoraga.LocalStorage
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainScreen : Fragment(R.layout.screen_user) {
    private val binding by viewBinding(ScreenUserBinding::bind)
    private val viewModel by viewModels<MainViewModelImpl>()
    private val adapter = MainItemAdapter()
    private val localStorage = LocalStorage.instance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.itemList.adapter = adapter

        observerFunction()
        mainFunction()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    private fun mainFunction(){
        adapter.setItemClickListener {
            findNavController().navigate(MainScreenDirections.toChatScreen(it))
        }

//        binding.profileButton.setOnClickListener {
//            val user = User(
//                userName = localStorage.getUserName()
//                    ?: "defaultName",   // Method to get user's name
//                userEmail = localStorage.getUserEmailId()
//                    ?: "defaultEmail", // Method to get user's email
//                userPassword = localStorage.getUserPassword()
//                    ?: "defaultPassword" // Method to get user's password
//            )
//            viewModel.updateProfile(user)
//        }


    }

    private fun showUpdateDialog(){
        val dialogBinding = DialogUpdateUserBinding.inflate(layoutInflater, null, false)
        val alert = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialogBinding.updateButton.setOnClickListener {
            viewModel.updateProfile(
                User(
                    userName = localStorage.getUserEmailId() ?: "null",
                    userEmail = dialogBinding.editEmail.text.toString(),
                    userPassword = dialogBinding.editPassword.text.toString()
                )
            )
            alert.dismiss()
        }
        dialogBinding.cancelBnt.setOnClickListener {
            alert.dismiss()
        }
        alert.show()
    }

    private fun observerFunction() {
        viewModel.chatsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.updateLiveData.observe(viewLifecycleOwner){
            showUpdateDialog()
        }
    }
}