package com.example.phonestore.model.auth

import com.google.gson.annotations.SerializedName

class LoginResponse(var status: Boolean = false, var messages: String? ="", @SerializedName("data") var user: User?, var token: String? ="")