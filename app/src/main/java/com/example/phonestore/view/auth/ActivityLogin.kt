package com.example.phonestore.view.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.BuildConfig
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityLoginBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.auth.FormLogin
import com.example.phonestore.model.auth.User
import com.example.phonestore.services.Constant
import com.example.phonestore.view.MainActivity
import com.example.phonestore.viewmodel.UserViewModel
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Pattern


class ActivityLogin: BaseActivity() {
    companion object{
        fun intentFor(context: Context): Intent =
            Intent(context, ActivityLogin::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
    private lateinit var bindingLogin: ActivityLoginBinding
    private var loginViewModel: UserViewModel? = null
    private var callbackManager: CallbackManager? = null
    private var name: String? =""
    private var email: String? =""
    private var phone: String? =""
    private var avatar: String? =""
    private var idFB: String? = ""
    private val RC_SIGN_IN = 999
    private val isForgotPassword = true
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun setBinding() {
        bindingLogin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLogin.root)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    override fun setUI() {
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val internet = manager.isActiveNetworkMetered
        if(!internet) {
            Toast.makeText(this, "Không có kết nối internet", Toast.LENGTH_SHORT).show()
        }else {
            bindingLogin.btnLoginWithFacebook.setPermissions(listOf("public_profile", "email"))
            setOnClickListener()
            loginWithFacebook()
        }
        bindingLogin.edtLoginPassword.addTextChangedListener {
            bindingLogin.tvLoginFail.gone()
            bindingLogin.textInputPassword.error = null
        }
        bindingLogin.edtLoginPhone.addTextChangedListener{
            bindingLogin.tvLoginFail.gone()
            bindingLogin.textInputPhone.error = null
        }

    }
    fun setOnClickListener(){
        bindingLogin.btnLogin.setOnClickListener {
            if(validate()) {
                AppEvent.notifyShowPopUp()
                loginViewModel?.postLogin(FormLogin(phone = bindingLogin.edtLoginPhone.text.toString(), password = bindingLogin.edtLoginPassword.text.toString(), formality = "normal"))
            }
        }
        bindingLogin.tvSignUp.setOnClickListener {
            startActivity(ActivitySignUpInputPhone.intentFor(this, false))
        }
        bindingLogin.btnLoginGoogle.setOnClickListener {
            loginWithGoogle()
        }
        bindingLogin.tvLoginForgotPassword.setOnClickListener {
            startActivity(ActivitySignUpInputPhone.intentFor(this, isForgotPassword))
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun loginWithFacebook(){
        callbackManager = CallbackManager.Factory.create()
        bindingLogin.btnLoginWithFacebook.registerCallback(callbackManager, object :FacebookCallback<LoginResult?>{
            override fun onSuccess(result: LoginResult?) {
                AppEvent.notifyShowPopUp()
                getInfoUser(result?.accessToken, result?.accessToken?.userId)
            }

            override fun onCancel() {
                Toast.makeText(this@ActivityLogin, "Hủy", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@ActivityLogin, "Lỗi không xác định", Toast.LENGTH_SHORT).show()
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
            loginViewModel?.postSignUpSocialNetwork(User(0, name = name, avatar = avatar, email = email, password = null, formality = "facebook"))
        }.executeAsync()


    }

    override fun setViewModel() {
        loginViewModel = ViewModelProvider(this@ActivityLogin).get(UserViewModel::class.java)

    }

    override fun setObserve() {
        loginViewModel?.loginResponse?.observe(this, {
            if(it?.status == true) {
                if(it?.messages.equals("accountSocialCreated")){
                    loginViewModel?.postLogin(FormLogin(email = email, password = "", formality = "socialNetwork"))
                    saveSharedPreferences("", password = "", email = email)
                }else{
                    if(bindingLogin.cbSaveAccount.isChecked){
                        saveSharedPreferences(bindingLogin.edtLoginPhone.text.toString(), password = bindingLogin.edtLoginPassword.text.toString(), email = null)
                    }
                startActivity(MainActivity.intentFor(this))
                finish()
                }
            }else {
                bindingLogin.tvLoginFail.visible()
                bindingLogin.tvLoginFail.text = it?.messages
                AppEvent.notifyClosePopUp()
            }
        })
    }
    private fun saveSharedPreferences(phone:String, email: String?, password: String?){
        if(email!=null){
            val pref: SharedPreferences = this.getSharedPreferences("saveAccount",Context.MODE_PRIVATE)
            pref.edit().apply {
                putString("email", email)
                putString("password",password)
            }.apply()
        }else{
            val pref: SharedPreferences = this.getSharedPreferences("saveAccount",Context.MODE_PRIVATE)
            pref.edit().apply {
                putString("phone", phone)
                putString("password",password)
            }.apply()
        }

    }
    private fun validate(): Boolean {
        var valid = true
        if (bindingLogin.edtLoginPhone.text.isNullOrBlank()) {
            bindingLogin.textInputPhone.error = Constant.VALIDATE_PHONE
            valid = false
        }else if (!Pattern.compile("^(\\+84|0)+([3|5|7|8|9])+([0-9]{8})$").matcher(bindingLogin.edtLoginPhone.text!!).matches()){
            bindingLogin.textInputPhone.error = Constant.PHONE_INVALID
            valid =false
        }
        if (bindingLogin.edtLoginPassword.text.isNullOrBlank()) {
            bindingLogin.textInputPassword.error = Constant.VALIDATE_PASSWORD
            valid = false
        }

        return valid
    }
    override fun onSuspendedAccount() {
        runOnUiThread {
            closePopup()
            //XToast(this, "Your account has been suspended").show()
        }
    }
    private fun loginWithGoogle(){
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val pic = URL(account?.photoUrl.toString())
            // Signed in successfully, show authenticated UI.
            email = account?.email
            AppEvent.notifyShowPopUp()
            loginViewModel?.postSignUpSocialNetwork(User(0, name = account?.displayName, avatar = pic.toString(), email = account?.email, password = null, formality = "google"))

        } catch (e: ApiException) {
        } catch (e: MalformedURLException) {
        }
    }
}