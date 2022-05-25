package xyz.asmallmuseum.android.persistence

import android.content.Context
import okhttp3.Cookie

class CustomPreferences(context: Context) {
    val preferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)

    fun setUserTokens(aToken: String, aExpire: String, rToken: String, rExpire: String) {

    }

}