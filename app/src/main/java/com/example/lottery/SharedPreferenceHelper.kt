package com.example.lottery

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(context: Context) {
    companion object {
        private const val PREF_NAME = "LoginPreference"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }
}