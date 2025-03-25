package com.example.chatapponline.presenter.screen.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chatapponline.R
import com.example.chatapponline.databinding.ScreenRegisterBinding
import com.example.chatapponline.repository.Repository
import com.example.chatapponline.sourse.data_class.User
import com.example.chatapponline.sourse.localStoraga.LocalStorage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class RegisterScreen : Fragment(R.layout.screen_register) {
    private val binding: ScreenRegisterBinding by viewBinding(ScreenRegisterBinding::bind)
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private val repository = Repository.getInstance()
    private val localStorage = LocalStorage.instance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        binding.registerGoogleButton.setOnClickListener {
            signIn()
        }
        binding.registerButton.setOnClickListener {
            loadUserData()
        }
    }

    private fun loadUserData() {
        if (binding.name.text.toString().isBlank()) {
            Toast.makeText(requireContext(), "Fill in the blanks", Toast.LENGTH_SHORT)
                .show()
        } else if (binding.email.text.toString().isBlank()) {
            Toast.makeText(requireContext(), "Fill in the blanks", Toast.LENGTH_SHORT)
                .show()
        } else if (binding.password.text.toString().isBlank()) {
            Toast.makeText(requireContext(), "Fill in the blanks", Toast.LENGTH_SHORT)
                .show()
        } else {
            repository.checkEmailPassword(
                binding.email.text.toString(),
                binding.password.text.toString()
            ) { isExist ->
                if (isExist) {
                    findNavController().navigate(R.id.mainScreen)
                    localStorage.setFirstTime(true)
                    return@checkEmailPassword
                } else {
                    Toast.makeText(requireContext(), "User already exist", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            repository.addUser(
                User(
                    userName = binding.name.text.toString(),
                    userEmail = binding.email.text.toString(),
                    userPassword = binding.password.text.toString()
                ), {}, {}

            )

        }

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {


                Toast.makeText(
                    requireContext(),
                    "Welcome, ${account.displayName}!",
                    Toast.LENGTH_LONG
                ).show()

//                repository.checkEmailPassword(
//                    binding.email.text.toString(),
//                    binding.password.text.toString()
//                ) {
//                    if (!it) {
                repository.addUser(
                    User(
                        userName = account.displayName.toString(),
                        userEmail = account.email.toString(),
                        userPassword = System.currentTimeMillis().toString(),
                        userImg = account.photoUrl.toString()
                    ), {}, {}

                )
                findNavController().navigate(R.id.mainScreen)
                localStorage.setFirstTime(true)
//                        return@checkEmailPassword
//                    }
//                }

            }
        } catch (e: ApiException) {
            val errorCode = e.statusCode
            Toast.makeText(requireContext(), "Sign-in failed: ${e.message}", Toast.LENGTH_SHORT)
                .show()
            Log.e("GoogleSignIn", "Error code: $errorCode", e)
        }
    }

}
