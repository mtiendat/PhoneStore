package com.example.phonestore.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.BuildConfig
import com.example.phonestore.Extension.visible
import com.example.phonestore.R
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityLoginBinding
import com.example.phonestore.model.FormLogin
import com.example.phonestore.model.User
import com.example.phonestore.viewmodel.UserViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable
import org.json.JSONObject

class ActivityLogin: BaseActivity() {
    companion object{
        fun intentFor(context: Context): Intent =
            Intent(context, ActivityLogin::class.java)
    }
    private lateinit var bindingLogin: ActivityLoginBinding
    private lateinit var loginViewModel: UserViewModel
    lateinit var callbackManager: CallbackManager
    private var name: String? =""
    private var email: String? =""
    private var avatar: String? =""
    private var idFB: String? = ""
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
                    saveSharedPreferences(bindingLogin.edtLoginEmail.text.toString(), bindingLogin.edtLoginPassword.text.toString())
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
        bindingLogin.btnLoginWithFacebook.setPermissions(listOf("public_profile", "email"))
        loginWithFacebook()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun loginWithFacebook(){
        callbackManager = CallbackManager.Factory.create()
        bindingLogin.btnLoginWithFacebook.registerCallback(callbackManager, object :FacebookCallback<LoginResult?>{
            override fun onSuccess(result: LoginResult?) {
                getInfoUser(result?.accessToken, result?.accessToken?.userId)
            }

            override fun onCancel() {
                Snackbar.make(View(this@ActivityLogin), "Cancel", Snackbar.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                Snackbar.make(View(this@ActivityLogin), "Cancel", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
    fun getInfoUser(token: AccessToken?, userId: String?){
        val parameters = Bundle()
//        val request = GraphRequest.newMeRequest(token
//        ) { jsonObject, _ ->
//            if (BuildConfig.DEBUG) {
//                FacebookSdk.setIsDebugEnabled(true)
//                FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
//            }
//            // Facebook Id
//            if (jsonObject?.has("id") == true) {
//                idFB = jsonObject.getString("id")
//            } else {
//                Log.i("Facebook Id: ", "Not exists")
//            }
//            // Facebook Name
//            if (jsonObject?.has("name") == true) {
//                name = jsonObject.getString("name")
//            } else {
//                Log.i("Facebook Name: ", "Not exists")
//            }
//            // Facebook Profile Pic URL
//            if (jsonObject?.has("picture") == true) {
//                val facebookPictureObject = jsonObject?.getJSONObject("picture")
//                if (facebookPictureObject.has("data")) {
//                    val facebookDataObject = facebookPictureObject.getJSONObject("data")
//                    if (facebookDataObject.has("url")) {
//                        avatar = facebookDataObject.getString("url")
//                    }
//                }
//            } else {
//                Log.i("Facebook Profile Pic URL: ", "Not exists")
//            }
//
//            // Facebook Email
//            if (jsonObject?.has("email") == true) {
//                email = jsonObject.getString("email")
//            } else {
//                Log.i("Facebook Email: ", "Not exists")
//            }
//        }
//        val parameter = Bundle()
//        parameter.putString("fields","id, first_name, last_name, email, gender, picture.type(large)")
//        request.parameters = parameters
//        request.executeAsync()
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
                Log.i("Facebook Id: ", "Not exists")
            }
            // Facebook Name
            if (jsonObject.has("name")) {
                name = jsonObject.getString("name")
            } else {
                Log.i("Facebook Name: ", "Not exists")
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
                Log.i("Facebook Profile Pic URL: ", "Not exists")
            }

            // Facebook Email
            if (jsonObject.has("email")) {
                email = jsonObject.getString("email")
            } else {
                Log.i("Facebook Email: ", "Not exists")
            }
            loginViewModel.postSignUpSocialNetwork(User(0, name, avatar, email, idFB,"","","Facebook"))
        }.executeAsync()


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
        val statusSocialNetworkObserve = Observer<Boolean?>{
            if(it!=null && it) {
                loginViewModel.postLogin(FormLogin(email, idFB))
                saveSharedPreferences(email, idFB)
            }
        }
        loginViewModel.statusSocialNetwork.observe(this, statusSocialNetworkObserve)

    }

    private fun saveSharedPreferences(email: String?, password: String?){
        val pref: SharedPreferences = this.getSharedPreferences("saveAccount",Context.MODE_PRIVATE)
        pref.edit().apply {
            putString("email", email)
            putString("password",password)
        }.apply()
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