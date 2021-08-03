package com.example.phonestore.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

class UpdateImageModel(
    @SerializedName("listImageOld")
    var listID: ArrayList<Int>? = arrayListOf()
)