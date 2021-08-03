package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class PostCommentResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var id: Int?) {
}