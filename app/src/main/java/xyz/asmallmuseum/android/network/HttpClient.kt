package xyz.asmallmuseum.android.network

import com.google.android.gms.common.util.SharedPreferencesUtils
import okhttp3.*
import okhttp3.Headers.Companion.toHeaders
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import xyz.asmallmuseum.android.network.Apis.ResponseHandler
import java.io.File

class HttpClient {
    val client : OkHttpClient = OkHttpClient()
    val url : String = ""

    inline fun <reified T> getString(end_point: String, headers: Map<String, String>?, callback: ResponseHandler<T>) {
        var requestHeaders = headers
        if (requestHeaders == null) {
            requestHeaders = HashMap<String, String>()
        }

        val request = Request.Builder()
            .url(url + end_point)
            .headers(requestHeaders.toHeaders())
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw okio.IOException()

                    var result: T = T::class.java.newInstance()
                    callback.onSuccess(result)

                }
            }
        })
    }

    inline fun <reified T> postString(end_point: String, headers: Map<String, String>?, json: String, callback: ResponseHandler<T>) {
        var requestHeaders = headers
        if (requestHeaders == null) {
            requestHeaders = HashMap<String, String>()
        }

        val request = Request.Builder()
            .url(url + end_point)
            .headers(requestHeaders.toHeaders())
            .post(json.toRequestBody())
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException()

            var result : T = T::class.java.newInstance()
            callback.onSuccess(result)

        }
    }

    fun postStream(end_point: String) {

    }

    inline fun <reified T> postMultipart(end_point: String, headers: Map<String, String>?, callback: ResponseHandler<T>) {
        var requestHeaders = headers
        if (requestHeaders == null) {
            requestHeaders = HashMap<String, String>()
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("", "")
            .addFormDataPart("", "", File("").asRequestBody())
            .build()

        val request = Request.Builder()
            .headers(requestHeaders.toHeaders())
            .url(url + end_point)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException()

            var result: T = T::class.java.newInstance()
            callback.onSuccess(result)

        }
    }

    inline fun <reified T> put(end_point: String, json:String, headers: Map<String, String>?, callback: ResponseHandler<T>) {
        var requestHeaders = headers
        if (requestHeaders == null) {
            requestHeaders = HashMap<String, String>()
        }

         val request = Request.Builder()
             .url(url + end_point)
             .headers(requestHeaders.toHeaders())
             .put(json.toRequestBody())
             .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException()

            var result : T = T::class.java.newInstance()
            callback.onSuccess(result)
        }
    }

    inline fun <reified  T> delete(end_point: String, json: String, headers: Map<String, String>?, callback: ResponseHandler<T>) {
        var requestHeaders = headers
        if (requestHeaders == null) {
            requestHeaders = HashMap<String, String>()
        }

        val request = Request.Builder()
            .url(url + end_point)
            .headers(requestHeaders.toHeaders())
            .delete(json.toRequestBody())
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException()

            var result: T = T::class.java.newInstance()
            callback.onSuccess(result)

        }
    }
}