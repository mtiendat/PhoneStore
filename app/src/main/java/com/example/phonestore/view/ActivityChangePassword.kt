package com.example.phonestore.view

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityChangePasswordBinding
import com.example.phonestore.databinding.ActivityForgotPasswordBinding
import com.example.phonestore.viewmodel.UserViewModel

class ActivityChangePassword: BaseActivity() {
    companion object{
        fun intentFor(context: Context, e: String? =""): Intent =
                Intent(context, ActivityChangePassword::class.java).apply {
                    putExtra("email", e)
                }
    }
    private lateinit var bindingChangePassword: ActivityChangePasswordBinding
    private var changePasswordViewModel: UserViewModel? = null
    private var email: String? =""
    override fun setBinding() {
        bindingChangePassword = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(bindingChangePassword.root)
    }

    override fun setToolBar() {
        bindingChangePassword.toolbarChangePassword.toolbar.title = "Thay đổi mật khẩu"
        setSupportActionBar(bindingChangePassword.toolbarChangePassword.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun setViewModel() {
        changePasswordViewModel = ViewModelProvider(this@ActivityChangePassword).get(UserViewModel::class.java)
        val changePasswordObserve = Observer<Boolean>{
            if(it){
                Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        changePasswordViewModel?.status?.observe(this@ActivityChangePassword, changePasswordObserve)
    }

    override fun setUI() {
        email = intent.getStringExtra("email")
        bindingChangePassword.btnChangePassword.setOnClickListener {
            if(validate()){
                changePasswordViewModel?.changePassword(email, bindingChangePassword.edtNewPassword.text.toString())
            }
        }
    }
    private fun validate(): Boolean{
        return if(bindingChangePassword.edtNewPassword.text.toString()!=bindingChangePassword.edtConfirmNewPassword.text.toString()){
            bindingChangePassword.edtConfirmNewPassword.error = "Xác nhận password chưa đúng"
            false
        }else true
    }
}