package com.example.phonestore.services
import androidx.constraintlayout.solver.state.State
import com.google.common.util.concurrent.RateLimiter

import okhttp3.Interceptor
import okhttp3.Response

class RateLimitInterceptor : Interceptor {
    private val limiter: RateLimiter = RateLimiter.create(3.0)

    override fun intercept(chain: Interceptor.Chain): Response {
        limiter.acquire(1)
        return chain.proceed(chain.request())
    }
}