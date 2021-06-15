package com.example.phonestore.model.auth

import com.google.gson.annotations.SerializedName

class FormLogin(var phone: String? = "", var email: String? = null, var password: String? = "", @SerializedName("htdn") var formality: String? ="")