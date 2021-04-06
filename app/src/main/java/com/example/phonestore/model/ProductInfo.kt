package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ProductInfo(var id: Int = 0,
                  @SerializedName("DungLuong") var storage: String? = "",
                  @SerializedName("GiaMoi") var priceNew: Int =0,
                  @SerializedName("Gia") var priceOld: Int= 0,
                  @SerializedName("HinhAnh1") var img: String?= "",
                  @SerializedName("MauSac") var color: String?= "",
                  @SerializedName("TenSP") var name: String? = "",
                  @SerializedName("MaLoai") var idCate: Int? = 0)