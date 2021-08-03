package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class ReplyResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var listReply: ArrayList<Reply?>? = arrayListOf())