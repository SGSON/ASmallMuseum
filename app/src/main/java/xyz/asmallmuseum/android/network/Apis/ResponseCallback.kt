package xyz.asmallmuseum.android.network.Apis

interface ResponseCallback<T> {
    fun onSuccess(entity: T)
    fun onFailure(code: Int)
}