package com.example.phonestore.extendsion

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.phonestore.R

fun View.visible(){
    this.visibility = View.VISIBLE
}
fun View.invisible(){
    this.visibility = View.INVISIBLE
}
fun View.disable(){
    this.setBackgroundResource(R.color.dray)
    this.isEnabled = false
}
fun View.enable(){
    this.setBackgroundResource(R.color.blue)
    this.isEnabled = true
}
fun TextView.disableText(){
    this.setTextColor(ContextCompat.getColor(context, R.color.dray))
    this.isEnabled = false
}
fun TextView.enableText(){
    this.setTextColor(ContextCompat.getColor(context, R.color.black))
    this.isEnabled = true
}
fun View.gone(){
    this.visibility = View.GONE
}
fun TextView.strikeThrough(): Int{
    return this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}