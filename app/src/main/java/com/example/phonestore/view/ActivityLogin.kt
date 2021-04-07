package com.example.phonestore.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.runtime.internal.composableLambda
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.Extension.visible
import com.example.phonestore.R
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityLoginBinding
import com.example.phonestore.model.FormLogin
import com.example.phonestore.viewmodel.UserViewModel
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable

class ActivityLogin: BaseActivity() {

    private lateinit var bindingLogin: ActivityLoginBinding
    private lateinit var loginViewModel: UserViewModel
    override fun setBinding() {
        bindingLogin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLogin.root)


    }

    override fun setUI() {
        bindingLogin.pBLogin.setIndeterminateDrawableTiled(
            FoldingCirclesDrawable.Builder(this).colors(resources.getIntArray(
                R.array.google_colors)).build())
        bindingLogin.btnLogin.setOnClickListener {
            if(validate()) {
                if(bindingLogin.cbSaveAccount.isChecked){
                    val pref: SharedPreferences = this.getSharedPreferences("saveAccount",Context.MODE_PRIVATE)
                    pref.edit().apply {
                        putString("email", bindingLogin.edtLoginEmail.text.toString())
                        putString("password",bindingLogin.edtLoginPassword.text.toString())
                    }.apply()
                }
                bindingLogin.pBLogin.visible()
                loginViewModel.postLogin(FormLogin(bindingLogin.edtLoginEmail.text.toString(), bindingLogin.edtLoginPassword.text.toString()))
            }
        }
        bindingLogin.tvSignUp.setOnClickListener {
            startActivity(ActivitySignUp.intentFor(this))
        }
        bindingLogin.tvLoginForgotPassword.setOnClickListener {
            startActivity(ActivityForgotPassword.intentFor(this))
        }

    }

    override fun setViewModel() {

        loginViewModel = ViewModelProvider(this@ActivityLogin).get(UserViewModel::class.java)
        val statusObserve = Observer<Boolean?>{
            if(it!=null && it) {
                startActivity(MainActivity.intentFor(this))
                finish()
            }else Toast.makeText(this, "Username or Passowrd not valid", Toast.LENGTH_SHORT).show()
        }
        loginViewModel.status.observe(this, statusObserve)
        val ref: SharedPreferences = this.getSharedPreferences("saveAccount", Context.MODE_PRIVATE)
        if(ref.getString("email", "")!=""){
            loginViewModel.postLogin(FormLogin(ref.getString("email", ""), ref.getString("password", "")))
        }
    }
    private fun validate(): Boolean {
        return if (bindingLogin.edtLoginEmail.text.isNullOrBlank()) {
            bindingLogin.edtLoginEmail.error = "Email must be not empty"
            false
        } else if (bindingLogin.edtLoginPassword.text.isNullOrBlank()) {
            bindingLogin.edtLoginPassword.error = "Password must be not empty"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(bindingLogin.edtLoginEmail.text).matches()) {
            bindingLogin.edtLoginEmail.error = "Invalid Email address";
            false
        } else true
    }
}