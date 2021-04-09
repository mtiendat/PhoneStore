package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class Slideshow(var id: Int? = 0, @SerializedName("Link") var url: String? ="", @SerializedName("HinhAnh") var img: String? ="")