package com.example.phonestore.services

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
                chain.request().newBuilder()
                .header("Authorization", "Bearer "+ Constant.TOKEN)
                .build()
        )
    }

}