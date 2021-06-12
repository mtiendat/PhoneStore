package com.example.phonestore.model.payment

class ZaloPayCreateOrderParam(
    var AppId: String,
    var AppUser: String,
    var AppTime: String,
    var Amount: String,
    var AppTransId: String,
    var EmbedData: String,
    var Items: String,
    var BankCode: String,
    var Description: String,
    var Mac: String = ""
)