package com.example.phonestore.view.auth

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivitySignUpBinding
import com.example.phonestore.model.auth.User
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.UserViewModel

import java.util.regex.Pattern

class ActivitySignUp: BaseActivity() {
    companion object{
        fun intentFor(context: Context): Intent =
                Intent(context, ActivitySignUp::class.java)
    }
    private var bindingSignUp: ActivitySignUpBinding? = null
    private var signUpViewModel: UserViewModel? = null
    override fun setBinding() {
        bindingSignUp = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(bindingSignUp?.root)
    }
    override fun setToolBar() {
        bindingSignUp?.toolbarSignUp?.toolbar?.title = "Sign Up"
        setSupportActionBar(bindingSignUp?.toolbarSignUp?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun setViewModel() {
        signUpViewModel = ViewModelProvider(this@ActivitySignUp).get(UserViewModel::class.java)

    }

    override fun setObserve() {
        val signUpObserve = Observer<Boolean>{
            if(it) {
                Toast.makeText(this, "Đăng kí thành công! ", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 3000)
            }

        }
        signUpViewModel?.status?.observe(this,signUpObserve)
    }
    override fun setUI() {
        bindingSignUp?.btnSignUp?.setOnClickListener {
//            if(validate()) {
//                signUpViewModel?.postSignUp(
//                    User(name = bindingSignUp?.edtSignUpFullName?.text.toString(),
//                        email = bindingSignUp?.edtSignUpEmail?.text.toString(),
//                        password = bindingSignUp?.edtSignUpPassword?.text.toString(),
//                        phone = bindingSignUp?.edtSignUpPhone?.text.toString(),
//                        address = bindingSignUp?.edtSignUpLocation?.text.toString())
//                )
//            }
        }
    }
//    private fun validate(): Boolean{
//        return if(bindingSignUp?.edtSignUpFullName?.text.isNullOrBlank()){
//            bindingSignUp?.edtSignUpFullName?.error = Constant.VALIDATE_FULL_NAME
//            false
//        }else if(bindingSignUp?.edtSignUpPhone?.text.isNullOrBlank()) {
//            bindingSignUp?.edtSignUpPhone?.error = Constant.VALIDATE_PHONE
//            false
//        }else if(!Pattern.compile("^(0)+([0-9]{9})$").matcher(bindingSignUp?.edtSignUpPhone?.text!!).matches() ){
//            bindingSignUp?.edtSignUpPhone?.error = Constant.PHONE_INVALID
//            false
//        }else if(bindingSignUp?.edtSignUpLocation?.text.isNullOrBlank()){
//            bindingSignUp?.edtSignUpLocation?.error = Constant.VALIDATE_ADDRESS
//            false
//        }else if(bindingSignUp?.edtSignUpEmail?.text.isNullOrBlank()) {
//            bindingSignUp?.edtSignUpEmail?.error = Constant.VALIDATE_EMAIL
//            false
//        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(bindingSignUp?.edtSignUpEmail?.text!!).matches()) {
//            bindingSignUp?.edtSignUpEmail?.error = Constant.EMAIL_INVALID
//            false
//        }else if(bindingSignUp?.edtSignUpPassword?.text.isNullOrBlank()){
//            bindingSignUp?.edtSignUpPassword?.error = Constant.VALIDATE_PASSWORD
//            false
//        }else if(bindingSignUp?.edtSignUpConfirmPassword?.text.isNullOrBlank()){
//            bindingSignUp?.edtSignUpConfirmPassword?.error = Constant.VALIDATE_CONFIRM_PASSWORD
//            false
//
//        }else if (bindingSignUp?.edtSignUpPassword?.text.toString() != bindingSignUp?.edtSignUpConfirmPassword?.text.toString()) {
//            bindingSignUp?.edtSignUpConfirmPassword?.error = Constant.CONFIRM_PASSWORD_NOT_SAME
//            false
//        }else if (bindingSignUp?.cbPrivacy?.isChecked == false) {
//            bindingSignUp?.cbPrivacy?.error = Constant.VALIDATE_CHECKBOX_PRIVACY
//            false
//        }else true
//    }
}