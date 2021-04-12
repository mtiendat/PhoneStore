package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class DetailCartResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var cart: Cart? = null)