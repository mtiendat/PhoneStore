package com.example.phonestore.view.auth

import android.content.Context
import android.content.Intent


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityForgotPasswordBinding
import com.example.phonestore.services.Constant

import com.example.phonestore.viewmodel.UserViewModel

class ActivityForgotPassword: BaseActivity() {
    companion object{
        fun intentFor(context: Context): Intent =
                Intent(context, ActivityForgotPassword::class.java)
    }
    private var bindingForgotPassword: ActivityForgotPasswordBinding? = null
    private var forgotPasswordViewModel: UserViewModel? = null
    override fun setBinding() {
        bindingForgotPassword = ActivityForgotPasswordBinding.inflate(layoutInflater)
       setContentView(bindingForgotPassword?.root)
    }

    override fun setViewModel() {
        forgotPasswordViewModel = ViewModelProvider(this@ActivityForgotPassword).get(UserViewModel::class.java)

    }

    override fun setObserve() {
        val checkEmailObserve = Observer<Boolean>{
            if(it){
                startActivity(ActivityChangePassword.intentFor(this, bindingForgotPassword?.edtFPEmail?.text.toString()))
            }
        }
        forgotPasswordViewModel?.status?.observe(this@ActivityForgotPassword, checkEmailObserve)
    }
    override fun setToolBar() {
        bindingForgotPassword?.toolbarForgotPassword?.toolbar?.title = Constant.FIND_ACCOUNT
        setSupportActionBar(bindingForgotPassword?.toolbarForgotPassword?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    override fun setUI() {

    }


}