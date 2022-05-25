package xyz.asmallmuseum.android.network.Apis

interface ResponseHandler<T> {
    fun onSuccess(result: T)
    fun onFailure(code: Int, result: String)
}