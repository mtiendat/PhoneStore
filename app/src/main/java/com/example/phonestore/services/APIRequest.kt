package com.example.phonestore.services

import retrofit2.Call
import retrofit2.Response

object APIRequest {
    inline fun <T> callRequest(
            call: Call<T>?,
            crossinline onSuccess: (T?) -> Unit,
            crossinline onError: (String?) -> Unit
    ){
        call?.enqueue(object : retrofit2.Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                onSuccess.invoke(response.body())
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                onError(t.message.toString())
            }

        })
    }
}