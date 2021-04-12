package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class NotificationResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var listNotification: ArrayList<Notification>? = arrayListOf())