package com.example.phonestore.Extension

import android.graphics.Paint
import android.opengl.Visibility
import android.view.View
import android.widget.TextView
import com.example.phonestore.R

fun View.visible(){
    this.visibility = View.VISIBLE
}
fun View.invisible(){
    this.visibility = View.INVISIBLE
}
fun View.enabled(){
    this.setBackgroundResource(R.color.dray)
    this.isEnabled = false
}
fun View.unEnabled(){
    this.setBackgroundResource(R.color.blue)
    this.isEnabled = true
}
fun View.gone(){
    this.visibility = View.GONE
}
fun TextView.strikeThrough(): Int{
    return this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}