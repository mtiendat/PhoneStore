package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class SupplierResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var listSupplier: ArrayList<Supplier?>?)