package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class SupplierReponse(var status: Boolean = false, var message: String? ="",@SerializedName("data") var listSupplier: ArrayList<Supplier>?)