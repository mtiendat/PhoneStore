package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class ProvinceResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var list: ArrayList<Province>)