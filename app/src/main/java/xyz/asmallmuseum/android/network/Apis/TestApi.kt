package xyz.asmallmuseum.android.network.Apis

import android.content.Context
import xyz.asmallmuseum.android.network.HttpClient

class TestApi(context: Context){
    val httpClient = HttpClient()


    inline fun <reified T> getTest(callback : ResponseCallback<T>) {
        httpClient.getString("", HashMap<String, String>(), object : ResponseHandler<T> {
            override fun onSuccess(result: T) {
                callback.onSuccess(result)
            }

            override fun onFailure(code: Int, result: String) {
                callback.onFailure(code)
            }
        })
    }
}