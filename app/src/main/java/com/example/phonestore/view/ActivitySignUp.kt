package com.example.phonestore.view

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivitySignUpBinding
import com.example.phonestore.model.User
import com.example.phonestore.viewmodel.UserViewModel

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
        val loginObserve = Observer<String>{
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            },3000)

        }
        signUpViewModel.message.observe(this,loginObserve)
    }

    override fun setUI() {
        bindingSignUp.btnSignUp.setOnClickListener {
            if(validate()) {
                signUpViewModel.postSignUp(User(name = bindingSignUp.edtSignUpFullName.text.toString(),
                        email = bindingSignUp.edtSignUpEmail.text.toString(),
                        password = bindingSignUp.edtSignUpPassword.text.toString()))
            }
        }
    }
    private fun validate(): Boolean{
        return if(bindingSignUp.edtSignUpFullName.text.isNullOrBlank()){
            bindingSignUp.edtSignUpFullName.error = "Fullname must be not empty"
            false
        }else if(bindingSignUp.edtSignUpEmail.text.isNullOrBlank()){
            bindingSignUp.edtSignUpEmail.error = "Email must be not empty"
            false
        }else if(bindingSignUp.edtSignUpPassword.text.isNullOrBlank()){
            bindingSignUp.edtSignUpPassword.error = "Password must be not empty"
            false
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(bindingSignUp.edtSignUpEmail.text).matches()) {
            bindingSignUp.edtSignUpEmail.error = "Invalid Email address";
            false
        }else if (!bindingSignUp.cbPrivacy.isChecked) {
            bindingSignUp.cbPrivacy.error = "Please accept our privacy";
            false
        }else true
    }
}