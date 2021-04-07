package com.example.phonestore.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentMeBinding
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentMe: BaseFragment() {
    private lateinit var bindingMe: FragmentMeBinding
    private var userViewModel: UserViewModel? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View{
        bindingMe = FragmentMeBinding.inflate(inflater, container, false)
        return  bindingMe.root
    }

    override fun setUI() {
        bindingMe.tvMyName.text = Constant.user?.name
        context?.let {
            Glide.with(it)
                    .load(Constant.user?.avatar)
                    .error(R.drawable.noimage)
                    .into(bindingMe.ivAvatar)
        }

        bindingMe.btnFollowOrder.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentFollowOrder)
        }
        bindingMe.btnLogout.setOnClickListener {
            userViewModel?.postSignOut()
        }
        bindingMe.btnSettingAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentChangeMyInfo)
        }

    }

    override fun setObserve() {
        val statusObserve = Observer<Boolean>{
            if(it){
                val ref = context?.getSharedPreferences("saveAccount", Context.MODE_PRIVATE)
                ref?.edit()?.clear()?.apply()
                view?.let { it1 -> Snackbar.make(it1, "Đăng xuất thành công",Snackbar.LENGTH_SHORT).show()
                it1.findNavController().navigate(FragmentMeDirections.actionFragmentMeToActivityLogin())
                activity?.finish() }
            }
        }
        userViewModel?.status?.observe(viewLifecycleOwner, statusObserve)
    }

    override fun setViewModel() {
        userViewModel = ViewModelProvider(this@FragmentMe).get(UserViewModel::class.java)
    }
}