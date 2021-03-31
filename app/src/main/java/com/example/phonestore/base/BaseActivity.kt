package com.example.phonestore.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding()
        setToolBar()
        setViewModel()
        setUI()
    }

    abstract fun setBinding()
    open fun setViewModel(){}
    open fun setUI(){}
    open fun setToolBar(){}
}