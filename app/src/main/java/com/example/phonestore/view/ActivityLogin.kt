package com.example.phonestore.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.BuildConfig
import com.example.phonestore.extendsion.visible
import com.example.phonestore.R
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityLoginBinding
import com.example.phonestore.model.FormLogin
import com.example.phonestore.model.User
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.UserViewModel
import com.facebook.*
import com.facebook.login.LoginResult
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable

class ActivityLogin: BaseActivity() {
    companion object{
        fun intentFor(context: Context): Intent =
            Intent(context, ActivityLogin::class.java)
    }
    private var bindingLogin: ActivityLoginBinding? = null
    private var loginViewModel: UserViewModel? = null
    private var callbackManager: CallbackManager? = null
    private var name: String? =""
    private var email: String? =""
    private var avatar: String? =""
    private var idFB: String? = ""
    override fun setBinding() {
        bindingLogin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLogin?.root)
    }

    override fun setUI() {
        bindingLogin?.pBLogin?.setIndeterminateDrawableTiled(
            FoldingCirclesDrawable.Builder(this).colors(resources.getIntArray(
                R.array.google_colors)).build())
        bindingLogin?.btnLoginWithFacebook?.setPermissions(listOf("public_profile", "email"))
        setOnClickListener()
        loginWithFacebook()

    }
    fun setOnClickListener(){
        bindingLogin?.btnLogin?.setOnClickListener {
            if(validate()) {
                if(bindingLogin?.cbSaveAccount?.isChecked == true){
                    saveSharedPreferences(bindingLogin?.edtLoginEmail?.text.toString(), bindingLogin?.edtLoginPassword?.text.toString())
                }
                bindingLogin?.pBLogin?.visible()
                loginViewModel?.postLogin(FormLogin(bindingLogin?.edtLoginEmail?.text.toString(), bindingLogin?.edtLoginPassword?.text.toString()))
            }
        }
        bindingLogin?.tvSignUp?.setOnClickListener {
            startActivity(ActivitySignUp.intentFor(this))
        }
        bindingLogin?.tvLoginForgotPassword?.setOnClickListener {
            startActivity(ActivityForgotPassword.intentFor(this))
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun loginWithFacebook(){
        callbackManager = CallbackManager.Factory.create()
        bindingLogin?.btnLoginWithFacebook?.registerCallback(callbackManager, object :FacebookCallback<LoginResult?>{
            override fun onSuccess(result: LoginResult?) {
                getInfoUser(result?.accessToken, result?.accessToken?.userId)
            }

            override fun onCancel() {
                Toast.makeText(this@ActivityLogin, "Hủy", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@ActivityLogin, "Lỗi", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun getInfoUser(token: AccessToken?, userId: String?){
        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture.type(large), email"
        )
        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET
        ) { response ->
            val jsonObject = response.jsonObject

            if (BuildConfig.DEBUG) {
                FacebookSdk.setIsDebugEnabled(true)
                FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
            }
            // Facebook Id
            if (jsonObject.has("id")) {
                idFB = jsonObject.getString("id")
            } else {
                Log.i("Facebook Id: ", "Không tồn tại")
            }
            // Facebook Name
            if (jsonObject.has("name")) {
                name = jsonObject.getString("name")
            } else {
                Log.i("Facebook Name: ", "Không tồn tại")
            }
            // Facebook Profile Pic URL
            if (jsonObject.has("picture")) {
                val facebookPictureObject = jsonObject.getJSONObject("picture")
                if (facebookPictureObject.has("data")) {
                    val facebookDataObject = facebookPictureObject.getJSONObject("data")
                    if (facebookDataObject.has("url")) {
                        avatar = facebookDataObject.getString("url")
                    }
                }
            } else {
                Log.i("Facebook Profile Pic URL: ", "Không tồn tại")
            }

            // Facebook Email
            if (jsonObject.has("email")) {
                email = jsonObject.getString("email")
            } else {
                Log.i("Facebook Email: ", "Không tồn tại")
            }
            loginViewModel?.postSignUpSocialNetwork(User(0, name, avatar, email, idFB,"","","Facebook"))
        }.executeAsync()


    }

    override fun setViewModel() {
        loginViewModel = ViewModelProvider(this@ActivityLogin).get(UserViewModel::class.java)


    }

    override fun setObserve() {
        val statusObserve = Observer<Boolean?>{
            if(it!=null && it) {
                startActivity(MainActivity.intentFor(this))
                finish()
            }else Toast.makeText(this, Constant.LOGIN_FAILURE, Toast.LENGTH_SHORT).show()
        }
        loginViewModel?.status?.observe(this, statusObserve)
        val statusSocialNetworkObserve = Observer<Boolean?>{
            if(it!=null && it) {
                loginViewModel?.postLogin(FormLogin(email, idFB))
                saveSharedPreferences(email, idFB)
            }
        }
        loginViewModel?.statusSocialNetwork?.observe(this, statusSocialNetworkObserve)
    }
    private fun saveSharedPreferences(email: String?, password: String?){
        val pref: SharedPreferences = this.getSharedPreferences("saveAccount",Context.MODE_PRIVATE)
        pref.edit().apply {
            putString("email", email)
            putString("password",password)
        }.apply()
    }
    private fun validate(): Boolean {
        return if (bindingLogin?.edtLoginEmail?.text.isNullOrBlank()) {
            bindingLogin?.edtLoginEmail?.error = Constant.VALIDATE_EMAIL
            false
        } else if (bindingLogin?.edtLoginPassword?.text.isNullOrBlank()) {
            bindingLogin?.edtLoginPassword?.error = Constant.VALIDATE_PASSWORD
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(bindingLogin?.edtLoginEmail?.text!!).matches()) {
            bindingLogin?.edtLoginEmail?.error = Constant.EMAIL_INVALID
            false
        } else true
    }
}