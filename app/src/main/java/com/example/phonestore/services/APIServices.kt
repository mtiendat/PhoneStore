package com.example.phonestore.services

import com.example.phonestore.model.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface APIServices {
    companion object{
        private var instance: APIServices? = null
        private val tokenInterceptor = TokenInterceptor()
        private fun create(): APIServices{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
                    .addInterceptor(tokenInterceptor)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20,TimeUnit.SECONDS)
            httpClient.addInterceptor(logging)
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constant.URL)
                    .client(httpClient.build())
                    .build()
                    .create(APIServices::class.java)
        }
        fun getInstance() : APIServices? {
            synchronized(APIServices::class.java){
                instance = create()
            }
            return instance
        }
    }
    @POST("sign-in")
    fun postLogin(@Body account: FormLogin): Call<LoginResponse>
    @POST("sign-up")
    fun postSignUp(@Body user: User): Call<LoginResponse>
    @DELETE("sign-out")
    fun signOut():Call<LoginResponse>
    @PUT("change-info/{id}")
    fun changeInfoUser(@Path("id") idUser: Int?= 0, @Query("name") name: String?, @Query("phone") phone: String?, @Query("address") address: String?):Call<LoginResponse>
    @Multipart
    @POST("change-avatar/{id}")
    fun changeAvatar(@Path("id") idUser: Int? = 0, @Part avatar: MultipartBody.Part): Call<LoginResponse>
    @POST("check-email")
    fun checkEmail(@Query("email")email: String? =""): Call<LoginResponse>
    @POST("change-password")
    fun changePassword(@Query("email")email: String? ="", @Query("password")password: String? =""): Call<LoginResponse>
    @GET("danh-muc")
    fun getTopProduct(@Query("danhmuc") q: Int): Call<ProductResponse>
    @GET("nha-cung-cap")
    fun getSupplier(): Call<SupplierResponse>
    @GET("loai-san-pham")
    fun getCateProduct(@Query("page") page: Int?= 0, @Query("per_page") perPage: Int? = 0, @Query("order") orderBy: Int?): Call<CateProductReponse>
    @GET("loai-sp-ncc")
    fun getCateProductBySupplier(@Query("page") page: Int?= 0, @Query("per_page") perPage: Int? = 0, @Query("MaNCC") idSupplier: Int? = 0): Call<CateProductReponse>
    @GET("loai-san-pham/{id}")
    fun getCateProductByID(@Path("id") id: Int?= 0): Call<CateProductResponseByID>
    @GET("san-pham/{id}")
    fun getProductByColor(@Path("id") id: Int?= 0, @Query("MauSac") color: String? ="", @Query("DungLuong") storage: String? =""): Call<ProductResponse>
    @GET("san-pham-lien-quan/{id}")
    fun getRelatedProduct(@Path("id") id: Int?= 0): Call<CateProductReponse>
    @GET("tong-san-pham/{id}")
    fun getTotalProductInCart(@Path("id") idUser: Int?= 0): Call<CartResponse>
    @POST("them-vao-gio/{id}")
    fun addToCart(@Path("id") idProduct: Int?= 0, @Query("id") idUser: Int?=0): Call<CartResponse>
    @GET("gio-hang/{id}")
    fun getMyCart(@Path("id") idUser: Int?= 0): Call<DetailCartResponse>
    @DELETE("xoa-item/{id}")
    fun deleteItem(@Path("id")idUser: Int?= 0, @Query("MaSP") idProduct: Int?= 0): Call<DetailCartResponse>
    @POST("dat-hang/{id}")
    fun order(@Path("id") idUser: Int?= 0, @Body order: Order): Call<OrderResponse>
    @GET("don-hang/{id}")
    fun getMyOrder(@Path("id") idUser: Int?= 0, @Query("state") s: String? ="all"): Call<MyOrderResponse>
    @GET("chi-tiet-don-hang/{id}")
    fun getDetailOrder(@Path("id") idOrder: Int?= 0): Call<DetailOrderResponse>
    @POST("huy-don-hang/{id}")
    fun cancelOrder(@Path("id") idOrder: Int?= 0): Call<MyOrderResponse>
    @GET("tim-kiem-ten")
    fun searchName(@Query("q") q: String? =""): Call<SearchResponse>
    @GET("tim-kiem-ds")
    fun search(@Query("q") q: String? =""): Call<SearchResponse>
    @GET("danh-gia/{id}")
    fun getListVote(@Path("id")idCate: Int? = 0, @Query("idUser") idUser: Int?, @Query("all") all: Boolean? = false): Call<VoteResponse>
    @POST("danh-gia/{id}")
    fun postVote(@Path("id")idCate: Int? = 0, @Body vote: Vote): Call<VoteResponse>


}