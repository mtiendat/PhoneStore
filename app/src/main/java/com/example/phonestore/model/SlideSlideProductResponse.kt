package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class SlideSlideProductResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var listImageSlideshow: ArrayList<String>? = arrayListOf()) {
}