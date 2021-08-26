package com.example.phonestore.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentMeBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.UserViewModel
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import java.io.*


class FragmentMe: BaseFragment() {
    private lateinit var bindingMe: FragmentMeBinding
    private var userViewModel: UserViewModel? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View?{
        bindingMe = FragmentMeBinding.inflate(inflater, container, false)
        return  bindingMe.root
    }

    override fun setUI() {
            context?.let { setImg(Constant.user?.avatar, bindingMe.ivAvatar, it) }
            setData()
            setOnClickListener()
    }
    fun setOnClickListener(){

        bindingMe.btnFollowOrder.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentFollowOrder)
        }
        bindingMe.btnLogout.setOnClickListener {
            AppEvent.notifyShowPopUp()
            disconnectFromFacebook()
            disconnectFromGoogle()
            userViewModel?.postSignOut()
        }
        bindingMe.btnSettingAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentChangeMyInfo)
        }
        bindingMe.btnHelper.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentContact)
        }

        bindingMe.btnWarranty.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentWarranty)
        }
        bindingMe.btnWishList.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentWishList)
        }
        bindingMe.btnChat.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setPackage("com.facebook.orca")
            intent.data = Uri.parse("https://m.me/" + "157073173138532")
            startActivity(intent)
        }

    }
    private fun setData(){
        bindingMe.tvMyName.text = Constant.user?.name
    }
    override fun setObserve() {

        userViewModel?.status?.observe(viewLifecycleOwner, {
            if (it) {
                val ref = context?.getSharedPreferences("saveAccount", Context.MODE_PRIVATE)
                ref?.edit()?.clear()?.apply()
                view?.let { it1 ->
                    Snackbar.make(it1, Constant.SUCCESS_LOG_OUT, Snackbar.LENGTH_SHORT).show()
                    it1.findNavController().navigate(FragmentMeDirections.actionFragmentMeToActivityLogin())
                    activity?.finish()
                }
            }
        })

    }

    override fun setViewModel() {
        userViewModel = ViewModelProvider(this@FragmentMe).get(UserViewModel::class.java)
    }

    private fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE
        ) {
            AccessToken.refreshCurrentAccessTokenAsync()
            LoginManager.getInstance().logOut()
        }.executeAsync()
    }
    private fun disconnectFromGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mGoogleSignInClient.signOut()
        mGoogleSignInClient.revokeAccess()
    }
    private fun setImg(img: String?, v: ImageView?, context: Context){
        if (v != null) {
            Glide.with(context)
                .load(img)
                .error(R.drawable.noimage)
                .into(v)
        }
    }
}