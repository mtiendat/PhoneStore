package com.example.phonestore.base

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.phonestore.R
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

abstract class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val background: Drawable? = ContextCompat.getDrawable(this, R.drawable.background_gradient)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            navigationBarColor = ContextCompat.getColor(context, android.R.color.transparent)
            statusBarColor =  ContextCompat.getColor(context, android.R.color.transparent)
            setBackgroundDrawable(background)
        }
//        try {
//            val info = packageManager.getPackageInfo(
//                packageName,
//                PackageManager.GET_SIGNATURES
//            )
//            for (signature in info.signatures) {
//                val md: MessageDigest = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//        } catch (e: NoSuchAlgorithmException) {
//        }
        setViewModel()
        setBinding()
        setToolBar()
        setUI()
    }

    abstract fun setBinding()
    open fun setViewModel(){}
    open fun setUI(){}
    open fun setToolBar(){}
}