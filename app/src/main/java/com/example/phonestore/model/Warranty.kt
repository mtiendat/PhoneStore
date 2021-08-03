package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class Warranty(
    @SerializedName("id_imei")
    var idImei: Int? = 0,
    @SerializedName("ngaymua")
    var dateStart: String? = "",
    @SerializedName("ngayketthuc")
    var dateEnd: String? = "",
    var imei: String? = "",
    var image: String? = "",
    var name: String? = "",
    var color: String? ="",
    var storage: String? =""
)