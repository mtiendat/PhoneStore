package com.example.phonestore.base

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.phonestore.R
import com.example.phonestore.extendsion.*
import com.example.phonestore.model.PopUp
import com.example.phonestore.services.Constant
import com.example.phonestore.services.widget.PopupDialog
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK
import java.util.*


abstract class BaseActivity: AppCompatActivity(), PopupEventListener, AccountSuspendedListener,
    AccountListener, NotifyCode {
    private var listOfPopupDialogFragment: ArrayList<PopupDialog?>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ZaloPay SDK Init
        ZaloPaySDK.init(Constant.APP_ID, Environment.SANDBOX)
        val background: Drawable? = ContextCompat.getDrawable(this, R.drawable.background_gradient)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            navigationBarColor = ContextCompat.getColor(context, android.R.color.transparent)
            statusBarColor = ContextCompat.getColor(context, android.R.color.transparent)
            setBackgroundDrawable(background)
        }
        AppEvent.addPopupEventListener(this)
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
        setObserve()
        setBinding()
        setToolBar()
        setUI()
    }
    private fun showPopup(
        popup: PopUp?
    ) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        closePopup()
        val popupDialogFragment = PopupDialog.newInstance(popup)
        popupDialogFragment.show(supportFragmentManager, PopupDialog().tag)
        listOfPopupDialogFragment?.add(popupDialogFragment)
    }
    fun closePopup() {
        for (item in listOfPopupDialogFragment ?: arrayListOf()) {
            item?.dismissAllowingStateLoss()
        }
        listOfPopupDialogFragment?.clear()
    }
    abstract fun setBinding()
    open fun setViewModel(){}
    open fun setObserve(){}
    open fun setUI(){}
    open fun setToolBar(){}
    override fun onShowPopup(popup: PopUp?) {
        if(popup == null){
            val popup = PopUp(popupId = R.layout.popup_loading, isMatchParent = true)
            popup.isCancelable = true
            showPopup(popup )
        }else {
            popup.isCancelable = true
            showPopup(popup )
        }


    }

    override fun onClosePopup() {
        closePopup()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppEvent.unRegisterPopupEventListener(this)
    }
    override fun onSuspendedAccount() {}
    override fun onAccountNotActivated() {}
    override fun notifyCode(code: Any) {
        if (code.toString() == "409") {


        }
        if (code.toString() == "403") {

        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}