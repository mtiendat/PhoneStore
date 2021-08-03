package com.example.phonestore.view.auth

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.widget.addTextChangedListener


import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityForgotPasswordBinding
import com.example.phonestore.services.Constant

import com.example.phonestore.viewmodel.UserViewModel
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.extendsion.visible

class ActivityForgotPassword: BaseActivity() {
    companion object{
        fun intentFor(context: Context, numberPhone: String?, isChangePassword: Boolean): Intent =
                Intent(context, ActivityForgotPassword::class.java).putExtra(Constant.PHONE, numberPhone).putExtra(Constant.CHANGE_PASSWORD, isChangePassword)
    }
    private lateinit var bindingForgotPassword: ActivityForgotPasswordBinding
    private var forgotPasswordViewModel: UserViewModel? = null
    private var numberPhone: String? = null
    private var isChangePassword = false
    override fun setBinding() {
        bindingForgotPassword = ActivityForgotPasswordBinding.inflate(layoutInflater)
       setContentView(bindingForgotPassword.root)
        isChangePassword = intent.getBooleanExtra(Constant.CHANGE_PASSWORD, false)
        numberPhone = intent.getStringExtra(Constant.PHONE)
    }

    override fun setViewModel() {
        forgotPasswordViewModel = ViewModelProvider(this@ActivityForgotPassword).get(UserViewModel::class.java)

    }

    override fun setObserve() {
        forgotPasswordViewModel?.loginResponse?.observe(this@ActivityForgotPassword, {
            if(isChangePassword){
                if(it?.status == true){
                    forgotPasswordViewModel?.changePassword(
                        phone = numberPhone,
                        password = bindingForgotPassword.edtFPPassword.text.toString()
                    )
                    isChangePassword = false
                }else bindingForgotPassword.textInputOldPassword.error = "Mật khẩu cũ chưa chính xác"
            }else {
                Toast.makeText(this, it?.messages.toString(), Toast.LENGTH_SHORT).show()
                if (it?.status == true) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(ActivityLogin.intentFor(this))
                        finish()
                    }, 2000)
                }
            }
        })
    }
    override fun setToolBar() {
        bindingForgotPassword.toolbarForgotPassword.toolbar.title = Constant.CHANGE_PASSWORD
        setSupportActionBar(bindingForgotPassword.toolbarForgotPassword.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    override fun setUI() {
        if(isChangePassword) bindingForgotPassword.textInputOldPassword.visible()
        bindingForgotPassword.btnChange.setOnClickListener {
            if(validate()) {
                if(isChangePassword) {
                    AppEvent.notifyShowPopUp()
                    forgotPasswordViewModel?.checkPassword(numberPhone, bindingForgotPassword.edtOldPassword.text.toString())
                }else {
                    AppEvent.notifyShowPopUp()
                    forgotPasswordViewModel?.changePassword(
                        phone = numberPhone,
                        password = bindingForgotPassword.edtFPPassword.text.toString()
                    )
                }
            }
        }
        bindingForgotPassword.edtFPConfirmPassword.addTextChangedListener {
            bindingForgotPassword.textInputConfirmPassword.error = null
        }
        bindingForgotPassword.edtFPPassword.addTextChangedListener {
            bindingForgotPassword.textInputPassword.error = null
        }
        bindingForgotPassword.edtOldPassword.addTextChangedListener {
            bindingForgotPassword.textInputOldPassword.error = null
        }
    }
    private fun validate(): Boolean {
        var valid = true
        if (isChangePassword&&bindingForgotPassword.edtOldPassword.text.isNullOrEmpty()){
            bindingForgotPassword.textInputOldPassword.error = Constant.VALIDATE_OLD_PASSWORD
            valid = false
        }
        if (bindingForgotPassword.edtFPPassword.text.isNullOrBlank()) {
            bindingForgotPassword.textInputPassword.error = Constant.VALIDATE_PASSWORD
            valid = false
        }else if(bindingForgotPassword.edtFPPassword.text?.length?:0 <=5 || bindingForgotPassword.edtFPPassword.text?.length?:0 >=16){
            bindingForgotPassword.textInputPassword.error = Constant.VALIDATE_LENGTH_PASSWORD
            valid = false
        }else if(bindingForgotPassword.edtFPPassword.text.toString() != bindingForgotPassword.edtFPConfirmPassword.text.toString()){
            bindingForgotPassword.textInputConfirmPassword.error = Constant.CONFIRM_PASSWORD_NOT_SAME
            valid = false
        }
        if (bindingForgotPassword.edtFPConfirmPassword.text.isNullOrBlank()) {
            bindingForgotPassword.textInputConfirmPassword.error = Constant.VALIDATE_CONFIRM_PASSWORD
            valid = false
        }

        return valid
    }


}