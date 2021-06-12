package com.example.phonestore.model.cart

import com.google.gson.annotations.SerializedName

class CartResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var totalProduct: Int? = 0)