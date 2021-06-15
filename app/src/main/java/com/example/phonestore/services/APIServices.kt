package com.example.phonestore.services

import com.example.phonestore.model.*
import com.example.phonestore.model.auth.FormLogin
import com.example.phonestore.model.auth.LoginResponse
import com.example.phonestore.model.auth.User
import com.example.phonestore.model.cart.CartResponse
import com.example.phonestore.model.cart.DetailCartResponse
import com.example.phonestore.model.order.MyOrderResponse
import com.example.phonestore.model.order.Order
import com.example.phonestore.model.order.OrderResponse
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

    @POST("log-in")
    fun postLogin(@Body account: FormLogin): Call<LoginResponse>
    @POST("sign-up")
    fun postSignUp(@Body user: User): Call<LoginResponse>
    @DELETE("log-out")
    fun signOut():Call<LoginResponse>
    @PUT("change-info/{id}")
    fun changeInfoUser(@Path("id") idUser: Int?= 0, @Query("name") name: String?, @Query("phone") phone: String?, @Query("address") address: String?):Call<LoginResponse>
    @Multipart
    @POST("change-avatar/{id}")
    fun changeAvatar(@Path("id") idUser: Int? = 0, @Part avatar: MultipartBody.Part): Call<LoginResponse>
    @POST("check-number-phone")
    fun checkNumberPhone(@Query("sdt")phone: String? =""): Call<LoginResponse>
    @POST("change-password")
    fun changePassword(@Query("email")email: String? ="", @Query("password")password: String? =""): Call<LoginResponse>
    @GET("thong-bao/{id}")
    fun getNotification(@Path("id") idUser: Int?= 0): Call<NotificationResponse>
    @PUT("thong-bao/{id}")
    fun updateNotification(@Path("id") idUser: Int?= 0): Call<NotificationResponse>
    @DELETE("thong-bao/{id}")
    fun deleteNotification(@Path("id") idNotification: Int?= 0): Call<NotificationResponse>
    @GET("tong-thong-bao/{id}")
    fun getTotalNotification(@Path("id") idUser: Int?= 0): Call<NotificationResponse>
    @GET("slideshow")
    fun getSlideshow(): Call<SlideshowResponse>
    @GET("hotsale")
    fun getTopProduct(): Call<ProductResponse>
    @GET("supplier")
    fun getSupplier(): Call<SupplierResponse>
    @GET("featured-product")
    fun getFeaturedProduct(): Call<ProductResponse>
    @GET("all-product")
    fun getAllProduct(@Query("page") page: Int?= 0, @Query("per_page") perPage: Int? = 0): Call<ProductResponse>
    @GET("slideshow-product/{id}")
    fun getSlideshowProduct(@Path("id") id: Int?): Call<SlideSlideProductResponse>
    @GET("ram-storage")
    fun getRamAndStorage(): Call<RamAndStorageResponse>
    @GET("filter-product")
    fun filterProduct(@Query("page") page: Int?= 0, @Query("per_page") perPage: Int? = 0, @Query("ram") ram: String?, @Query("dungluong") storage: String?, @Query("priceMax") priceMax: String?,  @Query("priceMin") priceMin: String?): Call<ProductResponse>
    @GET("loai-sp-ncc")
    fun getCateProductBySupplier(@Query("page") page: Int?= 0, @Query("per_page") perPage: Int? = 0, @Query("ram") ram: String?, @Query("dungluong") storage: String?, @Query("priceMax") priceMax: String?,  @Query("priceMin") priceMin: String?): Call<CateProductResponse>
    @GET("new-loai-sp-ncc")
    fun getNewCateProductBySupplier( @Query("MaNCC") idSupplier: Int? = 0): Call<CateProductResponse>
    @GET("detail-product/{id}")
    fun getDetailProduct(@Path("id") id: Int?= 0): Call<DetailProductResponse>
    @GET("change-color-storage/{id}")
    fun getProductByColor(@Path("id") id: Int?= 0, @Query("mausac") color: String? ="", @Query("dungluong") storage: String? =""): Call<ProductResponse>
    @GET("related-product/{id}")
    fun getRelatedProduct(@Path("id") id: Int?= 0): Call<ProductResponse>
    @GET("compare-product/{id}")
    fun getCompareProduct(@Path("id") idCate: Int?, @Query("price") price: Int?= 0): Call<ProductResponse>

    @GET("total-product-in-cart/{id}")
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
    @GET("danh-gia/{id}")
    fun getListVote(@Path("id")idCate: Int? = 0, @Query("idUser") idUser: Int?, @Query("all") all: Boolean? = false): Call<VoteResponse>
    @POST("danh-gia/{id}")
    fun postVote(@Path("id")idCate: Int? = 0, @Body vote: Vote): Call<VoteResponse>


}