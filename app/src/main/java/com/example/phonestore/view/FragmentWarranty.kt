package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentWarrantyBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible

class FragmentWarranty: BaseFragment() {
    private var bindingWarranty: FragmentWarrantyBinding? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingWarranty = FragmentWarrantyBinding.inflate(inflater, container, false)
        return bindingWarranty?.root
    }

    override fun setEvents() {
        bindingWarranty?.btnSubmitWarranty?.setOnClickListener {
            //call api
            bindingWarranty?.groupFindWarranty?.gone()
            bindingWarranty?.groupWarranty?.visible()
        }
        bindingWarranty?.tvWarrantyOther?.setOnClickListener {
            bindingWarranty?.groupWarranty?.gone()
            bindingWarranty?.groupFindWarranty?.visible()
        }
    }

}