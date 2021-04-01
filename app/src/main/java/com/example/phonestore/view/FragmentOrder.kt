package com.example.phonestore.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.Extension.toVND
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentOrderBinding
import com.example.phonestore.model.DetailCart
import com.example.phonestore.model.Order
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.Constant
import com.example.phonestore.services.DetailProductAdapter
import com.example.phonestore.viewmodel.CartViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentOrder: BaseFragment() {
    companion object {
        fun actionToFragmentOrder(v: View, item: Bundle){
            v.findNavController().navigate(
                    R.id.action_fragmentDetailCart_to_fragmentOrder,
                    item
            )
        }
    }
    private lateinit var bindingOrderBinding: FragmentOrderBinding
    private var listProductOrder: ArrayList<ProductOrder>? = arrayListOf()
    private var orderAdapter: DetailProductAdapter<ProductOrder>? = null
    private var totalMoney: Int = 0
    private lateinit var orderViewModel: CartViewModel
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingOrderBinding = FragmentOrderBinding.inflate(inflater, container, false)
        return bindingOrderBinding.root
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this@FragmentOrder).get(CartViewModel::class.java)
    }

    override fun setObserve() {
        val resultOrderObserve = Observer<Boolean>{
            if(it){
               view?.findNavController()?.navigate(R.id.action_fragmentOrder_to_fragmentSuccessOrder)
            }
        }
        orderViewModel.resultOrder.observe(viewLifecycleOwner, resultOrderObserve)
    }

    override fun setUI() {
        listProductOrder = arguments?.getParcelableArrayList("listProduct")
        totalMoney = listProductOrder!!.size - 1
        initRecyclerView()
        bindingOrderBinding.btnOrderFinish.setOnClickListener {
            val listProductFinish: ArrayList<DetailCart> = arrayListOf()
            for(i in listProductOrder!!){
                i.product?.let { it1 -> listProductFinish.add(it1) }
            }
            val order = Order(listProductOrder?.get(totalMoney)?.total, listProductFinish)
            orderViewModel.order(order)
        }
        bindingOrderBinding.rvOrderProduct.isNestedScrollingEnabled = false
        setInfoOrder()
    }
    private fun setInfoOrder(){
        bindingOrderBinding.tvOrderAddress.text = Constant.user?.address
        bindingOrderBinding.tvOrderNameUser.text = Constant.user?.name
        bindingOrderBinding.tvOrderPhoneUser.text = Constant.user?.phone
        bindingOrderBinding.tvOrderTotalMoney.text = listProductOrder?.get(totalMoney)?.total.toVND()
        bindingOrderBinding.tvOrderTotalMoneyFinish.text = listProductOrder?.get(totalMoney)?.total.toVND()

    }
    private fun initRecyclerView(){
        orderAdapter = DetailProductAdapter(listProductOrder)
        bindingOrderBinding.rvOrderProduct.adapter = orderAdapter
        bindingOrderBinding.rvOrderProduct.layoutManager = LinearLayoutManager(context)
    }
}