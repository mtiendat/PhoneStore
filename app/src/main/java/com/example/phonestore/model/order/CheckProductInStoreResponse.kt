package com.example.phonestore.model.order

import com.example.phonestore.model.CheckProductID
import com.google.gson.annotations.SerializedName

class CheckProductInStoreResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var check: CheckProductID? = null) {
}