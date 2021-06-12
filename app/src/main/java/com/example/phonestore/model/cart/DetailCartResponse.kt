package com.example.phonestore.model.cart

import com.example.phonestore.model.cart.Cart
import com.google.gson.annotations.SerializedName

class DetailCartResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var cart: Cart? = null)