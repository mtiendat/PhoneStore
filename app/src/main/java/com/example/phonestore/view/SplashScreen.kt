package com.example.phonestore.view

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.model.FormLogin
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.UserViewModel

class SplashScreen :AppCompatActivity() {
    private var loginViewModel: UserViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val internet = manager.isActiveNetworkMetered
        if(internet) {
            Toast.makeText(this, "Không có kết nối internet", Toast.LENGTH_SHORT).show()
        }
            checkAndRequestPermissions()
    }
    private fun init(){
        loginViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val statusObserve = Observer<Boolean?> {
            if (it) {
                startActivity(MainActivity.intentFor(this))
                finish()
            }
        }
        loginViewModel?.status?.observe(this, statusObserve)
        Handler(Looper.getMainLooper()).postDelayed({
            val ref: SharedPreferences = this.getSharedPreferences("saveAccount", Context.MODE_PRIVATE)
            if (ref.getString("email", "") != "") {
                loginViewModel?.postLogin(FormLogin(ref.getString("email", ""), ref.getString("password", "")))
            } else {
                startActivity(ActivityLogin.intentFor(this))
                finish()
            }
        }, 3000)
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