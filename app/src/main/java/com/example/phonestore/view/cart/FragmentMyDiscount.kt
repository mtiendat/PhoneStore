package com.example.phonestore.view.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentComingSoonBinding
import com.example.phonestore.databinding.FragmentMyDiscountBinding
import com.example.phonestore.model.cart.Discount
import com.example.phonestore.services.Constant
import com.example.phonestore.services.Constant.DISCOUNT
import com.example.phonestore.services.adapter.SelectDiscountAdapter

class FragmentMyDiscount: BaseFragment() {

    private var bindingMyDiscount: FragmentMyDiscountBinding? = null
    private lateinit var adapter: SelectDiscountAdapter
    private var listMyDiscount: ArrayList<Discount> = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingMyDiscount = FragmentMyDiscountBinding.inflate(inflater, container, false)
        return bindingMyDiscount?.root
    }

    override fun setUI() {
        adapter = SelectDiscountAdapter {
            NavHostFragment.findNavController(this).apply {
                previousBackStackEntry?.savedStateHandle?.set(DISCOUNT, it)
                popBackStack()
            }
        }
        bindingMyDiscount?.rvMyDiscount?.layoutManager = LinearLayoutManager(context)
        bindingMyDiscount?.rvMyDiscount?.adapter = adapter

        for (i in 1..5){
            listMyDiscount.add(Discount(i, dateEnd = "1$i/0$i/2020", discount = i*10, content = "Áp dụng cho đơn hàng từ ${i}00.000đ"))
        }

        adapter.submitList(listMyDiscount)
    }
}