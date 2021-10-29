package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class Vote(var id: Int = 0,
           var name: String? ="",
           @SerializedName("id_sp") var idCate: Int = 0,
           @SerializedName("id_user")var idUser: Int = 0,
           @SerializedName("noidung")var content: String?= "",
           @SerializedName("danhgia")var vote: Int = 0,
           @SerializedName("ngaylap")var date: String?= "",
           @SerializedName("avatar_user")var avatarUser: String?= "")
