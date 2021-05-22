package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentShippingOptionsBinding

class FragmentPaymentOption: BaseFragment() {
    private var bindingPaymentOption: FragmentShippingOptionsBinding? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingPaymentOption = FragmentShippingOptionsBinding.inflate(inflater, container, false)
        return bindingPaymentOption?.root
    }

}