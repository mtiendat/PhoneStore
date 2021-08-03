package com.example.phonestore.services

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.example.phonestore.R

@SuppressLint("InflateParams")
class CustomToast(val context: Context, val content: String? = "", val color: Int = R.color.red) : Toast(context) {

    init {
        duration = Toast.LENGTH_SHORT
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val newView = inflater.inflate(R.layout.layout_toast, null)
        newView.findViewById<TextView>(R.id.tvContent).apply {
            text = content
            setBackgroundResource(color)
        }

        view = newView
    }
}