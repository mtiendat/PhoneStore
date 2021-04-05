package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class DetailCart(var id: Int? = 1,
                 @SerializedName("MaGH") var idCart: Int? = 1,
                 @SerializedName("MaSP") var idProduct: Int? = 1,
                 @SerializedName("SoLuong") var qty: Int? = 1,
                 @SerializedName("Gia") var price: Int? = 0,
                 @SerializedName("MauSac") var color: String? ="",
                 @SerializedName("DungLuong") var storage: String? ="",
                 @SerializedName("TenSP") var nameProduct: String? ="",
                 @SerializedName("HinhAnh1") var img: String? ="",
                 )