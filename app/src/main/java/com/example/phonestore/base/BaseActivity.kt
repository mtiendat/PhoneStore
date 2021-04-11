package com.example.phonestore.base

import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.phonestore.R
import java.util.*


abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val background: Drawable? = ContextCompat.getDrawable(this, R.drawable.background_gradient)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            navigationBarColor = ContextCompat.getColor(context, android.R.color.transparent)
            statusBarColor = ContextCompat.getColor(context, android.R.color.transparent)
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
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val internet = manager.isActiveNetworkMetered
        if(internet) {
            Toast.makeText(this, "Không có kết nối internet", Toast.LENGTH_SHORT).show()
        }
    }


    abstract fun setBinding()
    open fun setViewModel(){}
    open fun setUI(){}
    open fun setToolBar(){}
}