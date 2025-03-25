package com.example.chatapponline.sourse.localStoraga

import android.content.Context
import android.content.SharedPreferences
import com.example.chatapponline.app.App

class LocalStorage private constructor(){
    private val memory: SharedPreferences =
        App.context.getSharedPreferences("Memory", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = memory.edit()

    companion object{
        lateinit var instance: LocalStorage

        fun init(){
            if (!(::instance.isInitialized)){
                instance = LocalStorage()
            }
        }
    }

    private val FIRST = "first enter"

    fun getUserEmailId(): String? {
        return memory.getString("email", "don't found")
    }

    fun setUserEmailId(email: String) {
        editor.putString("email", email).apply()
    }
    fun setFirstTime(boolean: Boolean) {
        editor.putBoolean(FIRST, boolean).apply()
    }

    fun getFirstTime(): Boolean {
        return memory.getBoolean(FIRST, false)
    }

    fun setUserName(name: String){
        editor.putString("name", name).apply()
    }

    fun getUserName(): String? {
        return memory.getString("name", "")
    }
    fun setUserPassword(name: String){
        editor.putString("name", name).apply()
    }

    fun getUserPassword(): String? {
        return memory.getString("name", "")
    }


}