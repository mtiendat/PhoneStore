package com.example.phonestore.view.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentPaymentOptionsBinding
import com.example.phonestore.services.Constant

class FragmentPaymentOption: BaseFragment() {
    private var bindingPaymentOption: FragmentPaymentOptionsBinding? = null
    private var payment: Int = 0
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingPaymentOption = FragmentPaymentOptionsBinding.inflate(inflater, container, false)
        return bindingPaymentOption?.root
    }

    override fun setEvents() {
        bindingPaymentOption?.btnPaymentDelivered?.setOnClickListener {
            payment = 1
        }
        bindingPaymentOption?.btnPaymentByZaloPay?.setOnClickListener {
            payment = 2
        }
        bindingPaymentOption?.btnPaymentConfirm?.setOnClickListener {
            NavHostFragment.findNavController(this).apply {
                previousBackStackEntry?.savedStateHandle?.set(Constant.PAYMENT, payment)
                popBackStack()
            }
        }
    }
}