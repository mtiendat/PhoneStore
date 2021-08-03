package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class CommentResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var listComment: ArrayList<Comment>? = arrayListOf())