package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class SlideshowResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var listSlideshow: ArrayList<Slideshow>? = arrayListOf())