package com.example.phonestore.view.auth

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper

import android.widget.Toast
import androidx.core.widget.addTextChangedListener

import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivitySignUpBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.model.auth.User
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.UserViewModel


class ActivitySignUp: BaseActivity() {
    companion object{
        fun intentFor(context: Context, numberPhone: String?): Intent =
                Intent(context, ActivitySignUp::class.java).putExtra("numberPhone", numberPhone)
    }
    private lateinit var bindingSignUp: ActivitySignUpBinding
    private var signUpViewModel: UserViewModel? = null
    private var numberPhone: String? = null
    override fun setBinding() {
        bindingSignUp = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(bindingSignUp.root)
        numberPhone = intent?.getStringExtra("numberPhone")
    }
    override fun setToolBar() {
        bindingSignUp.toolbarSignUp.toolbar.title = "Thông tin cá nhân"
        setSupportActionBar(bindingSignUp.toolbarSignUp.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun setViewModel() {
        signUpViewModel = ViewModelProvider(this@ActivitySignUp).get(UserViewModel::class.java)

    }

    override fun setObserve() {
        signUpViewModel?.loginResponse?.observe(this, {
            Toast.makeText(this, it?.messages, Toast.LENGTH_SHORT).show()
            if(it?.status == true) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(ActivityLogin.intentFor(this))
                    finish()
                }, 2000)
            }
        })
        signUpViewModel?.message?.observe(this, {
            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show()
        })
    }
    override fun setUI() {
        bindingSignUp.btnSignUp.setOnClickListener {
            if(validate()) {
                signUpViewModel?.postSignUp(
                    User(name = bindingSignUp.edtSignUpFullName.text.toString(),
                        password = bindingSignUp.edtSignUpPassword.text.toString(),
                        phone = numberPhone,
                        formality = "normal"
                ))
                AppEvent.notifyShowPopUp()
            }
        }

        bindingSignUp.edtSignUpFullName.addTextChangedListener {
            bindingSignUp.textInputName.error = null
        }
        bindingSignUp.edtSignUpPassword.addTextChangedListener {
            bindingSignUp.textInputPassword.error = null
        }
        bindingSignUp.edtSignUpConfirmPassword.addTextChangedListener {
            bindingSignUp.textInputConfirmPassword.error = null
        }
    }
    private fun validate(): Boolean {
        var valid = true
        if (bindingSignUp.edtSignUpFullName.text.isNullOrBlank()) {
            bindingSignUp.textInputName.error = Constant.VALIDATE_FULL_NAME
            valid = false
        }
        if (bindingSignUp.edtSignUpPassword.text.isNullOrBlank()) {
            bindingSignUp.textInputPassword.error = Constant.VALIDATE_PASSWORD
            valid = false
        }else if(bindingSignUp.edtSignUpPassword.text?.length?:0 <= 5 || bindingSignUp.edtSignUpPassword.text?.length?:0 >=16){
            bindingSignUp.textInputPassword.error = Constant.VALIDATE_LENGTH_PASSWORD
            valid = false
        }else if(bindingSignUp.edtSignUpPassword.text.toString() != bindingSignUp.edtSignUpConfirmPassword.text.toString()){
            bindingSignUp.textInputConfirmPassword.error = Constant.CONFIRM_PASSWORD_NOT_SAME
            valid = false
        }
        if (bindingSignUp.edtSignUpConfirmPassword.text.isNullOrBlank()) {
            bindingSignUp.textInputConfirmPassword.error = Constant.VALIDATE_CONFIRM_PASSWORD
            valid = false
        }

        return valid
    }
}