package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class DetailProduct(var id: Int = 1,
                    @SerializedName("tenmau") var name: String? = "",
                    @SerializedName("mota") var description: String? = "",
                    @SerializedName("baohanh") var warranty: String? = "",
                    @SerializedName("mausac") var listColor: List<String> = listOf(),
                    @SerializedName("dungluong") var listStorage: List<String> = arrayListOf(),
                    @SerializedName("nhacungcap") var supplier: Supplier,
                    @SerializedName("id_youtube") var trailer: String? =""
)