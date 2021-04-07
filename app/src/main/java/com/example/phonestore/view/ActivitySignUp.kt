package com.example.phonestore.view

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivitySignUpBinding
import com.example.phonestore.model.User
import com.example.phonestore.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

class ActivitySignUp: BaseActivity() {
    companion object{
        fun intentFor(context: Context): Intent =
                Intent(context,ActivitySignUp::class.java)
    }
    private lateinit var bindingSignUp: ActivitySignUpBinding
    private lateinit var signUpViewModel: UserViewModel
    override fun setBinding() {
        bindingSignUp = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(bindingSignUp.root)
    }
    override fun setToolBar() {
        bindingSignUp.toolbarSignUp.toolbar.title = "Sign Up"
        setSupportActionBar(bindingSignUp.toolbarSignUp.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun setViewModel() {
        signUpViewModel = ViewModelProvider(this@ActivitySignUp).get(UserViewModel::class.java)
        val signUpObserve = Observer<Boolean>{
            if(it) {
                Toast.makeText(this, "Đăng kí thành công! ", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 3000)
            }

        }
        signUpViewModel.status.observe(this,signUpObserve)
    }

    override fun setUI() {
        bindingSignUp.btnSignUp.setOnClickListener {
            if(validate()) {
                signUpViewModel.postSignUp(User(name = bindingSignUp.edtSignUpFullName.text.toString(),
                        email = bindingSignUp.edtSignUpEmail.text.toString(),
                        password = bindingSignUp.edtSignUpPassword.text.toString(),
                        phone = bindingSignUp.edtSignUpPhone.text.toString(),
                        address = bindingSignUp.edtSignUpLocation.text.toString()))
            }
        }
    }
    private fun validate(): Boolean{
        return if(bindingSignUp.edtSignUpFullName.text.isNullOrBlank()){
            bindingSignUp.edtSignUpFullName.error = "Họ tên không được để trống"
            false
        }else if(bindingSignUp.edtSignUpPhone.text.isNullOrBlank()) {
            bindingSignUp.edtSignUpPhone.error = "Sdt không được để trống"
            false
        }else if(!Pattern.compile("^(0)+([0-9]{9})$").matcher(bindingSignUp.edtSignUpPhone.text).matches() ){
            bindingSignUp.edtSignUpPhone.error = "Sdt không hợp lệ"
            false
        }else if(bindingSignUp.edtSignUpLocation.text.isNullOrBlank()){
            bindingSignUp.edtSignUpLocation.error = "Địa chỉ không được để trống"
            false
        }else if(bindingSignUp.edtSignUpEmail.text.isNullOrBlank()) {
            bindingSignUp.edtSignUpEmail.error = "Email không được để trống"
            false
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(bindingSignUp.edtSignUpEmail.text).matches()) {
            bindingSignUp.edtSignUpEmail.error = "Email không hợp lệ";
            false
        }else if(bindingSignUp.edtSignUpPassword.text.isNullOrBlank()){
            bindingSignUp.edtSignUpPassword.error = "Password không được để trống"
            false
        }else if(bindingSignUp.edtSignUpConfirmPassword.text.isNullOrBlank()){
            bindingSignUp.edtSignUpConfirmPassword.error = "Xác nhận Password không được để trống"
            false

        }else if (bindingSignUp.edtSignUpPassword.text.toString() != bindingSignUp.edtSignUpConfirmPassword.text.toString()) {
            bindingSignUp.edtSignUpConfirmPassword.error = "Xác nhận password đúng";
            false
        }else if (!bindingSignUp.cbPrivacy.isChecked) {
            bindingSignUp.cbPrivacy.error = "Vui lòng đồng ý với chính sách của chúng tôi";
            false
        }else true
    }
}