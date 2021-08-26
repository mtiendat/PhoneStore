package com.example.phonestore.services

import com.example.phonestore.model.*
import com.example.phonestore.model.auth.FormLogin
import com.example.phonestore.model.auth.LoginResponse
import com.example.phonestore.model.auth.User
import com.example.phonestore.model.cart.CartResponse
import com.example.phonestore.model.cart.DetailCartResponse
import com.example.phonestore.model.cart.VoucherResponse
import com.example.phonestore.model.order.*
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
                .addNetworkInterceptor(tokenInterceptor)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20,TimeUnit.SECONDS)
            httpClient.addInterceptor(logging).addNetworkInterceptor(RateLimitInterceptor())
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

    @POST("increase-view")
    fun postView(): Call<LoginResponse>
    @POST("log-in")
    fun postLogin(@Body account: FormLogin): Call<LoginResponse>
    @POST("sign-up")
    fun postSignUp(@Body user: User): Call<LoginResponse>
    @DELETE("log-out")
    fun signOut():Call<LoginResponse>
    @PUT("change-info/{id}")
    fun changeInfoUser(@Path("id") idUser: Int?= 0, @Query("name") name: String?):Call<LoginResponse>
    @Multipart
    @POST("change-avatar/{id}")
    fun changeAvatar(@Path("id") idUser: Int? = 0, @Part avatar: MultipartBody.Part): Call<LoginResponse>
    @POST("check-number-phone")
    fun checkNumberPhone(@Query("sdt")phone: String? =""): Call<LoginResponse>
    @PUT("change-password")
    fun changePassword(@Query("phone") phone: String? ="", @Query("password")password: String? =""): Call<LoginResponse>
    @POST("check-password")
    fun checkPassword(@Query("phone") phone: String? ="", @Query("password") password: String? =""): Call<LoginResponse>

    @GET("notification/{id}")
    fun getNotification(@Path("id") idUser: Int?= 0): Call<NotificationResponse>
    @PUT("notification/{id}")
    fun updateNotification(@Path("id") idNotification: Int?= 0): Call<NotificationResponse>
    @DELETE("notification/{id}")
    fun deleteNotification(@Path("id") idNotification: Int?= 0): Call<NotificationResponse>
    @GET("total-notification/{id}")
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
    fun filterProduct(@Query("page") page: Int?= 0, @Query("per_page") perPage: Int? = 0, @Query("ram") ram: String?, @Query("dungluong") storage: String?, @Query("priceMax") priceMax: String?,  @Query("priceMin") priceMin: String?, @Query("suppliers", encoded = true) listSupplierID: String? = "[]"): Call<ProductResponse>
    @GET("loai-sp-ncc")
    fun getCateProductBySupplier(@Query("page") page: Int?= 0, @Query("per_page") perPage: Int? = 0, @Query("ram") ram: String?, @Query("dungluong") storage: String?, @Query("priceMax") priceMax: String?,  @Query("priceMin") priceMin: String?): Call<CateProductResponse>
    @GET("new-loai-sp-ncc")
    fun getNewCateProductBySupplier( @Query("MaNCC") idSupplier: Int? = 0): Call<CateProductResponse>
    @GET("detail-product/{id}")
    fun getDetailProduct(@Path("id") id: Int?=0, @Query("id_tk") idUser: Int?= 0): Call<DetailProductResponse>
    @GET("change-color-storage/{id}")
    fun getProductByColor(@Path("id") id: Int?= 0, @Query("mausac") color: String? ="", @Query("dungluong") storage: String? ="", @Query("id_tk") idUser: Int?= 0): Call<ProductResponse>
    @GET("related-product/{id}")
    fun getRelatedProduct(@Path("id") id: Int?= 0): Call<ProductResponse>
    @GET("compare-product/{id}")
    fun getCompareProduct(@Path("id") idCate: Int?, @Query("price") price: Int?= 0): Call<ProductResponse>
    @GET("check-comment/{id}")
    fun checkComment(@Path("id") idUser: Int?, @Query("idProduct") idProduct: Int?= 0): Call<CheckCommentResponse>

    @POST("list-product-comment")
    fun getListProductNotComment(@Body listID: ParamListID?): Call<ProductResponse>

    @GET("total-product-in-cart/{id}")
    fun getTotalProductInCart(@Path("id") idUser: Int?= 0): Call<CartResponse>
    @POST("add-to-cart")
    fun addToCart(@Query("id_user") idUser: Int?=0, @Query("dungluong") storage: String?, @Query("hinhanh") image: String?): Call<CartResponse>
    @GET("my-cart/{id}")
    fun getMyCart(@Path("id") idUser: Int?= 0): Call<DetailCartResponse>
    @DELETE("delete-product-in-cart/{id}")
    fun deleteItem(@Path("id")idCart: Int?= 0): Call<DetailCartResponse>
    @PUT("update-cart/{id}")
    fun updateProductInCart(@Path("id") idProduct: Int?= 0, @Query("plusOrMin") method: String?): Call<CartResponse>
    @GET("my-voucher/{id}")
    fun getMyVoucher(@Path("id") idUser: Int?= 0): Call<VoucherResponse>
    @DELETE("delete-voucher/{id}")
    fun getDeleteVoucher(@Path("id") idVoucher: Int?= 0): Call<VoucherResponse>

    @POST("create-order/{id}")
    fun order(@Path("id") idUser: Int?= 0, @Body order: Order): Call<OrderResponse>
    @GET("province-store")
    fun getProvinceOfStore(): Call<ProvinceResponse>
    @GET("address-store/{id}")
    fun getAddressStore(@Path("id") idProvince: Int?= 0): Call<AddressStoreResponse>
    @GET("my-order/{id}")
    fun getMyOrder(@Path("id") idUser: Int?= 0, @Query("state") s: String? = "all"): Call<MyOrderResponse>
    @GET("detail-order/{id}")
    fun getDetailOrder(@Path("id") idOrder: Int?= 0): Call<DetailOrderResponse>
    @POST("cancel-order/{id}")
    fun cancelOrder(@Path("id") idOrder: Int?= 0): Call<MyOrderResponse>
    @GET("search")
    fun searchName(@Query("q") q: String? =""): Call<SearchResponse>
    @GET("list-comment/{id}")
    fun getListComment(@Path("id")idProduct: Int? = 0, @Query("id_tk") idUser: Int?): Call<CommentResponse>
    @GET("list-reply/{id}")
    fun getListReply(@Path("id")idComment: Int? = 0): Call<ReplyResponse>
    @DELETE("delete-comment/{id}")
    fun deleteComment(@Path("id")idComment: Int? = 0): Call<ReplyResponse>
    @POST("comment")
    fun postComment(@Body comment: Comment): Call<PostCommentResponse>
    @Multipart
    @POST("upload-image-comment/{id}")
    fun postImageComment(@Path("id")idComment: Int? = 0, @Part image: ArrayList<MultipartBody.Part>): Call<CommentResponse>
    @Multipart
    @POST("update-image-new-comment/{id}")
    fun updateImageNewComment(@Path("id")idComment: Int? = 0, @Part image: ArrayList<MultipartBody.Part>? = arrayListOf()): Call<CommentResponse>
    @POST("update-image-old-comment/{id}")
    fun updateImageOldComment(@Path("id")idComment: Int? = 0,  @Body listID: UpdateImageModel): Call<CommentResponse>
    @PUT("update-comment/{id}")
    fun updateComment(@Path("id")idComment: Int? = 0, @Body comment: Comment): Call<PostCommentResponse>
    @POST("reply")
    fun postReply(@Body reply: Reply): Call<ReplyResponse>
    @POST("like/{id}")
    fun postLike(@Path("id")idComment: Int? = 0, @Query("id_tk") idUser: Int?): Call<ReplyResponse>
    @DELETE("like/{id}")
    fun deleteLike(@Path("id")idComment: Int? = 0, @Query("id_tk") idUser: Int?): Call<ReplyResponse>
    @POST("check-product/{id}")
    fun checkProduct(@Path("id") idStore: Int? = 0,@Query("method") method: String?, @Body list: ParamListHasQty): Call<CheckProductInStoreResponse>
    @GET("check-warranty")
    fun checkWarranty(@Query("imei") imei: String?): Call<WarrantyResponse>
    @GET("wishlist/{id}")
    fun wishList(@Path("id") idUser: Int? = 0): Call<WishListResponse>
    @GET("add-to-wishlist")
    fun addToWishList(@Query("id_tk") idUser: Int? = 0, @Query("id_sp") idProduct: Int? = 0): Call<WishListResponse>
    @DELETE("delete-wishlist")
    fun deleteWishList(@Query("id_tk") idUser: Int? = 0, @Query("id_sp") idProduct: Int? = 0): Call<WishListResponse>
    @POST("my-address")
    fun createMyAddress(@Body address: Address?): Call<ListMyAddressResponse>
    @GET("my-address-default/{id}")
    fun getMyAddress(@Path("id") id: Int? = 0): Call<Address>
    @PUT("my-address/{id}")
    fun updateMyAddress(@Path("id") idAddress: Int? = 0, @Body address: Address?): Call<ListMyAddressResponse>
    @DELETE("my-address/{id}")
    fun deleteMyAddress(@Path("id") idAddress: Int? = 0): Call<ListMyAddressResponse>
    @GET("my-address/{id}")
    fun getListMyAddress(@Path("id") id: Int? = 0): Call<ListMyAddressResponse>
    @GET("city")
    fun getCity(): Call<InfoAddressResponse>
    @GET("district")
    fun getDistrict(@Query("id") id: String?): Call<InfoAddressResponse>
    @GET("ward")
    fun getWard(@Query("id") id: String?): Call<InfoAddressResponse>
    @GET("id-address")
    fun getIdCityAndDistrict(@Query("city") city: String?, @Query("district") district: String?): Call<ArrayList<String>?>
}