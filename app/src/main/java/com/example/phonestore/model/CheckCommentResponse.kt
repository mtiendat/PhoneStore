package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class CheckCommentResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var listId: ArrayList<Int>?){
}