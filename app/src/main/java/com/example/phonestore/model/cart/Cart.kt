package com.example.phonestore.model.cart

import com.google.gson.annotations.SerializedName

class Cart(@SerializedName("CTGH") var listProduct: ArrayList<DetailCart>, @SerializedName("TongTien") var totalMoney: Int? = 0)