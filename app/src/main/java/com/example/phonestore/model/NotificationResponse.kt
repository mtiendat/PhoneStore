package com.example.phonestore.model

import com.google.gson.annotations.SerializedName

class NotificationResponse(var status: Boolean? = false, var messages: String? ="", @SerializedName("data") var listNotification: ArrayList<Notification>? = arrayListOf())