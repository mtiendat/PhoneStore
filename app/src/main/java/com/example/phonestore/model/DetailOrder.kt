package com.example.phonestore.model

import com.example.phonestore.model.order.Order
import com.google.gson.annotations.SerializedName

class DetailOrder(
    @SerializedName("infoOrder")
    var order: Order? = null,
    @SerializedName("listProduct")
    var listProduct: ArrayList<ProductOrder>? = arrayListOf()
)