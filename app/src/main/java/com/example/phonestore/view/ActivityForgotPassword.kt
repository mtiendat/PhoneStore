package com.example.phonestore.view

import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityForgotPasswordBinding
import com.example.phonestore.databinding.ActivitySignUpBinding
import com.example.phonestore.model.User
import com.example.phonestore.viewmodel.UserViewModel

class ActivityForgotPassword: BaseActivity() {
    companion object{
        fun intentFor(context: Context): Intent =
                Intent(context, ActivityForgotPassword::class.java)
    }
    private lateinit var bindingForgotPassword: ActivityForgotPasswordBinding
    private var forgotPasswordViewModel: UserViewModel? = null
    override fun setBinding() {
        bindingForgotPassword = ActivityForgotPasswordBinding.inflate(layoutInflater)
       setContentView(bindingForgotPassword.root)
    }

    override fun setViewModel() {
        forgotPasswordViewModel = ViewModelProvider(this@ActivityForgotPassword).get(UserViewModel::class.java)
        val checkEmailObserve = Observer<Boolean>{
            if(it){
                startActivity(ActivityChangePassword.intentFor(this, bindingForgotPassword.edtFPEmail.text.toString()))
            }
        }
        forgotPasswordViewModel?.status?.observe(this@ActivityForgotPassword, checkEmailObserve)
    }
    override fun setToolBar() {
        bindingForgotPassword.toolbarForgotPassword.toolbar.title = "Tìm tài khoản"
        setSupportActionBar(bindingForgotPassword.toolbarForgotPassword.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    override fun setUI() {
        bindingForgotPassword.btnFindEmail.setOnClickListener {
            forgotPasswordViewModel?.checkEmail(bindingForgotPassword.edtFPEmail.text.toString())
        }
    }

    override fun setActionBar(toolbar: Toolbar?) {
        super.setActionBar(toolbar)
    }
}