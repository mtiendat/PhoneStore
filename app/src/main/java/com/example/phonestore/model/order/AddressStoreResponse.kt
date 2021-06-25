package com.example.phonestore.model.order

import com.example.phonestore.model.Notification
import com.google.gson.annotations.SerializedName

class AddressStoreResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var listAddressStore: ArrayList<AddressStore>? = arrayListOf()) {
}