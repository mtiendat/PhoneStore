package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class SearchResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var listSearch: ArrayList<CateProductInfo>? )