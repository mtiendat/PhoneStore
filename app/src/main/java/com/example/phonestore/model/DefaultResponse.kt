package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class DefaultResponse(val status: Boolean? = null, val message: String? = null)
class QueueResponse(val status: Boolean? = null, val message: String? = null, @SerializedName("data") var listIDs: ArrayList<Int>? = arrayListOf())