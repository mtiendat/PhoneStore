package com.example.phonestore.base

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.phonestore.R

abstract class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val background: Drawable? = ContextCompat.getDrawable(this, R.drawable.background_gradient)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            navigationBarColor = ContextCompat.getColor(context, android.R.color.transparent)
            statusBarColor =  ContextCompat.getColor(context, android.R.color.transparent)
            setBackgroundDrawable(background)
        }
        setViewModel()
        setBinding()
        setToolBar()
        setUI()
    }

    abstract fun setBinding()
    open fun setViewModel(){}
    open fun setUI(){}
    open fun setToolBar(){}
}