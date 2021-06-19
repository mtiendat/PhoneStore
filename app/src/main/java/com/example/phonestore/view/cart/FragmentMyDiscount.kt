package com.example.phonestore.view.cart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils.getMonthString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.databinding.FragmentMyDiscountBinding
import com.example.phonestore.model.cart.Discount
import com.example.phonestore.services.adapter.SelectDiscountAdapter
import com.example.phonestore.viewmodel.CartViewModel


class FragmentMyDiscount: DialogFragment() {

    private var bindingMyDiscount: FragmentMyDiscountBinding? = null
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: SelectDiscountAdapter
    private var listMyDiscount: ArrayList<Discount> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingMyDiscount = FragmentMyDiscountBinding.inflate(inflater, container, false)
        return bindingMyDiscount?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    fun setUI() {
        adapter = SelectDiscountAdapter {
            cartViewModel.discount.value = it
            dismiss()
        }
        bindingMyDiscount?.rvMyDiscount?.layoutManager = LinearLayoutManager(context)
        bindingMyDiscount?.rvMyDiscount?.adapter = adapter

        for (i in 1..5){
            listMyDiscount.add(Discount(i, dateEnd = "1$i/0$i/2020", discount = i*10, content = "Áp dụng cho đơn hàng từ ${i}00.000đ"))
        }

        adapter.submitList(listMyDiscount)
    }
    override fun onStart() {
        super.onStart()
        //dialog full screen
        val window: Window? = dialog?.window
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        window?.setLayout(width, height)
    }
}