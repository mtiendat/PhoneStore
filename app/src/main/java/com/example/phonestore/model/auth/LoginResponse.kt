package com.example.phonestore.model.auth

import com.google.gson.annotations.SerializedName

class LoginResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var user: User?, var token: String? ="")