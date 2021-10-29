package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class ProductResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var listProduct: ArrayList<ProductInfo?>?)
class ProductNotListResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var product: ProductInfo?)
class ProductCartResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var product: ProductInfoCart?)