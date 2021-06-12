package com.example.phonestore.services.payment

import com.example.phonestore.model.payment.ZaloPayCreateOderResponse
import com.example.phonestore.model.payment.ZaloPayCreateOrderParam
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant
import okhttp3.*
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface PaymentService {
    companion object{
        private var instance: PaymentService? = null
        private fun create(): PaymentService? {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                    TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()
            val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectionSpecs(listOf(spec))
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
            httpClient.addInterceptor(logging)

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.URL_CREATE_ORDER)
                .client(httpClient.build())
                .build()
                .create(PaymentService::class.java)
        }
        fun getInstance() : PaymentService? {
            synchronized(PaymentService::class.java){
                instance = create()
            }
            return instance
        }
    }
    @POST("createorder")
    fun createOrder(@Body param: RequestBody): Call<ZaloPayCreateOderResponse>
}