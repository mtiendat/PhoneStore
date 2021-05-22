package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentShippingOptionsBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible

class FragmentShippingOption: BaseFragment() {
    private var bindingShippingOption: FragmentShippingOptionsBinding? = null
    private var isClick: Boolean = false
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingShippingOption = FragmentShippingOptionsBinding.inflate(inflater, container, false)
        return bindingShippingOption?.root
    }

    override fun setUI() {
        bindingShippingOption?.cbExpand?.setOnClickListener {
            isClick = !isClick
            if(isClick) bindingShippingOption?.ctrlChooseAddressStore?.visible() else bindingShippingOption?.ctrlChooseAddressStore?.gone()
        }
        bindingShippingOption?.ctrlShippingAtStore?.setOnClickListener {
            bindingShippingOption?.cbExpand?.performClick()
        }
    }
}