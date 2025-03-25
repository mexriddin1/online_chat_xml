package com.example.chatapponline.presenter.screen.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chatapponline.R
import com.example.chatapponline.databinding.SplashScreenBinding
import com.example.chatapponline.sourse.localStoraga.LocalStorage

@SuppressLint("CustomSplashScreen")
class SplashScreen: Fragment(R.layout.splash_screen) {
    private val binding: SplashScreenBinding by viewBinding(SplashScreenBinding::bind)
    private val localStorage = LocalStorage.instance
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        object : CountDownTimer(1000, 100){
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                if (localStorage.getFirstTime()){
                    findNavController().navigate(R.id.mainScreen,null,NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreen,true)
                        .build()
                    )
                }
                else{
                    findNavController().navigate(R.id.loginScreen,null,NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreen,true)
                        .build()
                    )
                }
            }

        }.start()
    }
}
// findNavController().navigate(
//    R.id.secondFragment,
//    null,
//    NavOptions.Builder()
//        .setPopUpTo(R.id.firstFragment, true)  // Указывает на удаление firstFragment из стека
//        .build()
//)