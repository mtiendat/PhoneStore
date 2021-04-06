package com.example.phonestore.Extension

import java.text.DecimalFormat

fun String.ratingBar(): Float{
    return this.toFloat()
}
fun String.toVND(): String{
    val numFormat = DecimalFormat("#,###,###")
    return numFormat.format(this)+"đ"
}