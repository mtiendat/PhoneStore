package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class VoteResponse(var status: Boolean = false, var message: Boolean = false, @SerializedName("data") var listVote: ArrayList<Vote>? = arrayListOf())