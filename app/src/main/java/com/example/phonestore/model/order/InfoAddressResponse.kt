package com.example.phonestore.model.order

import com.google.gson.annotations.SerializedName

class InfoAddressResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var list: ArrayList<Item>? = arrayListOf())  {
}