package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class Notification(var id: Int? = 1, @SerializedName("iduser") var idUser: Int? = 1, @SerializedName("tieude") var title: String? = "",@SerializedName("noidung") var content: String? ="", @SerializedName("ngaylap") var time: String? ="", @SerializedName("trangthai") var send: Int? = 0)