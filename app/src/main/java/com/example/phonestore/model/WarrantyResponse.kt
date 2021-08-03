package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class WarrantyResponse(var status: Boolean = false, var message: String? = "", @SerializedName("data") var warranty: Warranty?)