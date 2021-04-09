package com.example.phonestore.Extension

import java.text.DecimalFormat

fun Int?.toVND(): String?{
    val numFormat = DecimalFormat("#,###,###")
    return numFormat.format(this)+" đ"
}