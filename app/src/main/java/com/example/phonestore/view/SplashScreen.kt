package com.example.phonestore.view

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.R
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.model.FormLogin
import com.example.phonestore.viewmodel.UserViewModel

class SplashScreen :AppCompatActivity() {
    private var loginViewModel: UserViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val statusObserve = Observer<Boolean?>{
            if(it) {
                startActivity(MainActivity.intentFor(this))
                finish()
            }
        }
        loginViewModel?.status?.observe(this, statusObserve)
        Handler(Looper.getMainLooper()).postDelayed({
            val ref: SharedPreferences = this.getSharedPreferences("saveAccount", Context.MODE_PRIVATE)
            if(ref.getString("email", "")!=""){
                loginViewModel?.postLogin(FormLogin(ref.getString("email", ""), ref.getString("password", "")))
            }else {
                startActivity(ActivityLogin.intentFor(this))
                finish()
            }
        },3000)
    }
}