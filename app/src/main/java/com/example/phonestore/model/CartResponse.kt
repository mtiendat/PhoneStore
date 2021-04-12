package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class CartResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var totalProduct: Int? = 0)