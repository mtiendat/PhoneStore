package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class CateProductInfo(var id: Int = 0,
                      @SerializedName("GiaMoi") var priceNew: Int =0,
                      @SerializedName("Gia") var priceOld: Int= 0,
                      @SerializedName("AnhDaiDien") var img: String?= "",
                      @SerializedName("Vote") var vote: String?= "",
                      @SerializedName("TenLoai") var name: String? = "",
                      @SerializedName("MoTa") var description: String? = "",
                      @SerializedName("BaoHanh") var warranty: Int = 0,
                      @SerializedName("MauSac") var listColor: List<String> = listOf(),
                      @SerializedName("DungLuong") var listStorage: List<String> = arrayListOf(),
                      @SerializedName("NhaCungCap") var supplier: Supplier,
                      @SerializedName("Trailer") var trailer: String? =""
)