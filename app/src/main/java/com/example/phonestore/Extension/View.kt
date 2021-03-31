package com.example.phonestore.Extension

import android.graphics.Paint
import android.opengl.Visibility
import android.view.View
import android.widget.TextView

fun View.visible(){
    this.visibility = View.VISIBLE
}
fun View.gone(){
    this.visibility = View.GONE
}
fun TextView.strikeThrough(): Int{
    return this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}