package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class DetailProduct(var id: Int = 1,
                    @SerializedName("tenmau") var name: String? = "",
                    @SerializedName("mota") var description: String? = "",
                    @SerializedName("baohanh") var warranty: String? = "",
                    @SerializedName("mausac") var listColor: ArrayList<String?>? = arrayListOf(),
                    @SerializedName("dungluong") var listStorage: ArrayList<String?>? = arrayListOf(),
                    @SerializedName("dsHinhAnh") var listImage: ArrayList<String>? = arrayListOf(),
                    @SerializedName("nhacungcap") var supplier: Supplier,
                    @SerializedName("like") var like: Boolean? = false,
                    @SerializedName("id_youtube") var trailer: String? ="",
                    @SerializedName("trangthai") var state: Int?
)