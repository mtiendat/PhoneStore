package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class DetailOrderResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var detail: DetailOrder? = null)