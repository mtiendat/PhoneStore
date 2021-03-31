package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class Supplier(var id: Int = 0, @SerializedName("TenNCC") var name: String?= "",@SerializedName("logo")var logoSupplier: String?="", @SerializedName("XacMinh") var auth: Int? =0)