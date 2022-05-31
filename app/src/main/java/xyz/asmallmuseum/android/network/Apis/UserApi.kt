package xyz.asmallmuseum.android.network.Apis

import android.content.Context
import android.content.SharedPreferences
import xyz.asmallmuseum.android.network.HttpClient
import xyz.asmallmuseum.android.persistence.UserPreference

class UserApi(context: Context) {
    var httpClient = HttpClient()
    var context = context

    inline fun <reified T> signIn(callback: ResponseCallback<T>) {

        val headers = HashMap<String, String>()
        headers.put("Authorization", UserPreference.getAccessToken(context))

        httpClient.postString("", headers, "", object: ResponseHandler<T> {
            override fun onSuccess(result: T) {
                TODO("Not yet implemented")
            }

            override fun onFailure(code: Int, result: String) {
                TODO("Not yet implemented")
            }

        })

    }

    inline fun <reified T> signUp(callback: ResponseCallback<T>) {

        val headers = HashMap<String, String>()
        headers.put("Authorization", UserPreference.getAccessToken(context))

        httpClient.postString("", headers, "", object: ResponseHandler<T> {
            override fun onSuccess(result: T) {
                callback.onSuccess(result)
            }

            override fun onFailure(code: Int, result: String) {
                TODO("Not yet implemented")
            }

        })
    }
}