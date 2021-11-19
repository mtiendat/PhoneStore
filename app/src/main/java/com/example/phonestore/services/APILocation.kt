package com.example.phonestore.services

import com.example.phonestore.model.DistrictResponse
import com.example.phonestore.model.Location
import com.example.phonestore.model.WardResponse
import com.example.phonestore.model.order.InfoAddressResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface APILocation {
    companion object{
        private var instance: APILocation? = null
        private val tokenInterceptor = TokenInterceptor()
        private fun create(): APILocation{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
                .addNetworkInterceptor(tokenInterceptor)
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
            httpClient.addInterceptor(logging).addNetworkInterceptor(RateLimitInterceptor())
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.URL_API_ADDRESS)
                .client(httpClient.build())
                .build()
                .create(APILocation::class.java)

        }
        fun getInstance() : APILocation? {
            synchronized(APILocation::class.java){
                instance = create()
            }
            return instance
        }
    }
    @GET("p")
    fun getCity(): Call<ArrayList<Location>>
    @GET("p/search/")
    fun searchCity(@Query("q") name: String): Call<ArrayList<Location>>
    @GET("p/{code}?depth=2")
    fun getDistrict(@Path("code") code: Int): Call<DistrictResponse>

    @GET("d/{code}?depth=2")
    fun getWard(@Path("code") code: Int): Call<WardResponse>

    @GET("d/search/")
    fun searchDistrict(@Query("q") name: String): Call<ArrayList<Location>>
}