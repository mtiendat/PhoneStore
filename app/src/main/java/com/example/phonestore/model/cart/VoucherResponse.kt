package com.example.phonestore.model.cart

import com.google.gson.annotations.SerializedName

class VoucherResponse(var status: Boolean? = false, var message: String? ="", @SerializedName("data") var listMyVoucher: ArrayList<Voucher>? = arrayListOf())