package com.example.phonestore.model.order

import com.google.gson.annotations.SerializedName

class MyOrderResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var listMyOrder: ArrayList<MyOrder>? = arrayListOf())