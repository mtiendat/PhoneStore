package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentMeBinding

class FragmentMe: BaseFragment() {
    private lateinit var bindingMe: FragmentMeBinding
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View{
        bindingMe = FragmentMeBinding.inflate(inflater, container, false)
        return  bindingMe.root
    }

    override fun setUI() {
        bindingMe.btnFollowOrder.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentFollowOrder)
        }
    }
}