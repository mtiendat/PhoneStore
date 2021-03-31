package com.example.phonestore.Extension

import java.text.DecimalFormat

fun String.ratingBar(): Float{
    return (this.toFloat()?: 0.1f)/2
}
fun String.toVND(): String{
    val numFormat = DecimalFormat("#,###,###")
    return numFormat.format(this)+"đ"
}