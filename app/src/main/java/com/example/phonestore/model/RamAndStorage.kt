package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class RamAndStorage(
    var ram: List<String>? = listOf(),
    @SerializedName("dungluong")
    var storage: List<String>? = listOf(),
)