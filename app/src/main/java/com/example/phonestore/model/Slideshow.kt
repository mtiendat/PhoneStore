package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class Slideshow(var id: Int? = 0, @SerializedName("link") var url: String? ="", @SerializedName("hinhanh") var img: String? ="")