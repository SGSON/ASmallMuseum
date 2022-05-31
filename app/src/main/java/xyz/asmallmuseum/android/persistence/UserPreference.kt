package xyz.asmallmuseum.android.persistence

import android.content.Context
import android.content.SharedPreferences
import org.joda.time.DateTime

class UserPreference(pref: SharedPreferences) {
    companion object {
        private val aToken = "ACCESS_TOKEN"
        private val aExpire = "ACCESS_TOKEN_EXPIRE"
        private val rToken = "REFRESH_TOKEN"
        private val rExpire = "REFRESH_TOKEN_EXPIRE"

//        var preference : SharedPreferences? = null

        fun setUserTokens(context: Context, aToken: String, aExpire: String, rToken: String, rExpire: String) {
            val preference = context.getSharedPreferences("USER", Context.MODE_PRIVATE)

            val edit = preference.edit()

            edit.putString(this.aToken, aToken)
            edit.putString(this.aExpire, aExpire)
            edit.putString(this.rToken, rToken)
            edit.putString(this.rExpire, rExpire)

            edit.apply()
        }

        fun getAccessToken(context: Context) : String {
            val preference = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            val result : String = preference.getString(aToken, "a")!!
            return result
        }

        fun isLogin(context: Context) : Boolean {
            val preference = context.getSharedPreferences("USER", Context.MODE_PRIVATE)

            var result = false

            if (!preference.getString(aToken, "").equals("")) {
                var date = getDate(preference, aExpire)
                if (date.isAfterNow) {
                    result = true
                }
                else {
                    date = getDate(preference, rExpire)
                    if (date.isAfterNow){
                        result = true
                        requestAccessToken()
                    }
                }
            }

            return result
        }

        private fun getDate(pref: SharedPreferences, date : String) : DateTime {
            var exp = pref.getString(date, DateTime.now().toString())
            return DateTime(exp)
        }

        private fun requestAccessToken() {

        }
    }


}