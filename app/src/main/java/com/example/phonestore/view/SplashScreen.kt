package com.example.phonestore.view

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.R
import com.example.phonestore.databinding.SplashscreenBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.auth.FormLogin
import com.example.phonestore.services.Constant
import com.example.phonestore.services.CustomToast
import com.example.phonestore.view.auth.ActivityLogin
import com.example.phonestore.viewmodel.UserViewModel
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable

class SplashScreen :AppCompatActivity() {
    private var loginViewModel: UserViewModel? = null
    private var bindingSplashScreen: SplashscreenBinding? = null
    private var isDisconnected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSplashScreen = SplashscreenBinding.inflate(layoutInflater)
        setContentView(bindingSplashScreen?.root)
        hideSystemUI()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val internet = manager.isActiveNetworkMetered
            bindingSplashScreen?.progressBarSplashScreen?.setIndeterminateDrawableTiled(
                    FoldingCirclesDrawable.Builder(this).colors(resources.getIntArray(
                            R.array.google_colors)).build())
        init()
            //checkAndRequestPermissions()
        
    }
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
    private fun init(){
        loginViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        loginViewModel?.loginResponse?.observe(this, {
            if(isDisconnected) CustomToast(this, "Đã kết nối internet", R.color.green).show()
            if(it?.status == true) {
                startActivity(MainActivity.intentFor(this))
                finish()
            }else {
                startActivity(ActivityLogin.intentFor(this))
                finish()
            }
        })
        loginViewModel?.message?.observe(this, {
            if(it?.contains("Failed to connect to") == true || it?.contains("failed to connect to") == true){
                CustomToast(this, "Không có kết nối internet").show()
                bindingSplashScreen?.groupRetry?.visible()
                bindingSplashScreen?.progressBarSplashScreen?.gone()
                isDisconnected = true
            }else if(it?.contains("Connection reset") == true){
                CustomToast(this, "Có lỗi xảy ra").show()
                bindingSplashScreen?.groupRetry?.visible()
                bindingSplashScreen?.progressBarSplashScreen?.gone()
                isDisconnected = true
            }
        })
        Handler(Looper.getMainLooper()).postDelayed({
            bindingSplashScreen?.progressBarSplashScreen?.visible()
            val ref: SharedPreferences = this.getSharedPreferences("saveAccount", Context.MODE_PRIVATE)
            if (ref.getString("phone", "") != "") {
                loginViewModel?.postLogin(FormLogin(phone = ref.getString("phone", ""), password = ref.getString("password", ""), formality = "normal"))
            }else if(ref.getString("email", "") != "") {
                loginViewModel?.postLogin(FormLogin(email = ref.getString("email", ""), password = ref.getString("password", ""), formality = "socialNetwork"))
            }else{
                startActivity(ActivityLogin.intentFor(this))
                finish()
            }
        }, 2000)
        bindingSplashScreen?.retry?.setOnClickListener {
            bindingSplashScreen?.progressBarSplashScreen?.visible()
            bindingSplashScreen?.groupRetry?.gone()
            Handler(Looper.getMainLooper()).postDelayed({
                bindingSplashScreen?.progressBarSplashScreen?.visible()
                val ref: SharedPreferences = this.getSharedPreferences("saveAccount", Context.MODE_PRIVATE)
                if (ref.getString("phone", "") != "") {
                    loginViewModel?.postLogin(FormLogin(phone = ref.getString("phone", ""), password = ref.getString("password", ""), formality = "normal"))
                }else if(ref.getString("email", "") != "") {
                    loginViewModel?.postLogin(FormLogin(email = ref.getString("email", ""), password = ref.getString("password", ""), formality = "socialNetwork"))
                }else{
                    startActivity(ActivityLogin.intentFor(this))
                    finish()
                }
            }, 2000)
        }
    }
     private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
        val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), Constant.REQUEST_ID)
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constant.REQUEST_ID -> {
                val perms: MutableMap<String, Int> = HashMap()
                // Initialize the map with both permissions
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.isNotEmpty()) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }
                    // Check for both permissions
                    if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted")
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("in fragment on request", "Some permissions are not granted ask again ")
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK { _, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                    }
                                }
                            }
                        } else {

                            init()
                        }
                    }
                }
            }
        }
    }
    private  fun showDialogOK( okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setMessage(Constant.PLEASE_ACCEPT_PERMISSION)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Hủy", okListener)
                .create()
                .show()
    }
}