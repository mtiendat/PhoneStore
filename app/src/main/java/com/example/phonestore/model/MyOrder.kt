package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class MyOrder(  var id: Int, var iduser: Int,
                @SerializedName("SDT") var phone: String? ="",
                @SerializedName("SoLuongSP") var qty: Int = 0,
                @SerializedName("DiaChi") var address: String? ="",
                @SerializedName("ThanhTien") var totalMoney: Int? = 0,
                @SerializedName("TrangThaiDH") var state: String? = "",
                @SerializedName("NgayLap") var date: String? = "")