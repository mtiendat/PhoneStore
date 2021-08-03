package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class CheckComment(
    @SerializedName("new")
    var listIdNew: ArrayList<Int>? = arrayListOf(),
    @SerializedName("edit")
    var listIdEdit: ArrayList<Int>? = arrayListOf()
)