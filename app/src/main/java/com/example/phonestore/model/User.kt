package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class User(var id: Int = 1,
           var name: String? = "",
           var avatar: String? ="",
           var email: String? ="" ,
           var password: String? ="",
           @SerializedName("SDT") var phone: String? = "",
           @SerializedName("DiaChi") var address: String? =""
)