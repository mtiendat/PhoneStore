package com.example.phonestore.model.auth

import com.google.gson.annotations.SerializedName

class User(var id: Int = 1,
           @SerializedName("sdt")
           var phone: String? = null,
           var password: String? = null,
           @SerializedName("hoten")
           var name: String? = "",
           var email: String? = null ,
           @SerializedName("anhdaidien")
           var avatar: String? ="",
           @SerializedName("htdn") var formality: String? =""
)