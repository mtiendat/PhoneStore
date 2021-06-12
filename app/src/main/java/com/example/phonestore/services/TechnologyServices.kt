package com.example.phonestore.services

import com.example.phonestore.model.technology.TechnologyResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface TechnologyServices {
    companion object{
        private var instance: TechnologyServices? = null
        private val tokenInterceptor = TokenInterceptor()
        private fun create(): TechnologyServices{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
            httpClient.addInterceptor(logging)
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.URL_ROOT)
                .client(httpClient.build())
                .build()
                .create(TechnologyServices::class.java)
        }
        fun getInstance() : TechnologyServices? {
            synchronized(TechnologyServices::class.java){
                instance = create()
            }
            return instance
        }
    }
    @GET("json/{file}")
    fun getTechnology(@Path("file") s: String): Call<TechnologyResponse>

}