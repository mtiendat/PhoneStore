package com.example.phonestore.model.order

import com.google.gson.annotations.SerializedName

class MyOrder(  var id: Int,
                @SerializedName("id_tk") var iduser: Int,
                @SerializedName("soluong_sp") var qty: Int = 0,
                @SerializedName("diachigiaohang") var address: String? ="",
                @SerializedName("tongtien") var totalMoney: Int? = 0,
                @SerializedName("trangthaidonhang") var state: String? = "",
                @SerializedName("thoigian") var date: String? = "")