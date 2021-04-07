package com.example.phonestore.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.Extension.enabled
import com.example.phonestore.Extension.gone
import com.example.phonestore.Extension.toVND
import com.example.phonestore.Extension.visible
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentOrderBinding
import com.example.phonestore.model.DetailCart
import com.example.phonestore.model.Order
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.Constant
import com.example.phonestore.services.DetailProductAdapter
import com.example.phonestore.viewmodel.CartViewModel
import com.example.phonestore.viewmodel.OrderViewModel
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
    private var idOrder:  Int? = 0
    private lateinit var orderViewModel: OrderViewModel
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingOrderBinding = FragmentOrderBinding.inflate(inflater, container, false)
        return bindingOrderBinding.root
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this@FragmentOrder).get(OrderViewModel::class.java)
    }

    override fun setObserve() {
        val resultOrderObserve = Observer<Boolean?>{
            if(it==true){
               view?.findNavController()?.navigate(R.id.action_fragmentOrder_to_fragmentSuccessOrder)
            }
        }
        orderViewModel.resultOrder.observe(viewLifecycleOwner, resultOrderObserve)
        val cancelObserve  = Observer<Boolean?> {
            if(it==true){
                view?.let { it1 -> Snackbar.make(it1, "Hủy thành công", Snackbar.LENGTH_SHORT).show() }
                view?.findNavController()?.apply {
                    popBackStack(R.id.fragmentFollowOrder, true)
                    navigate(R.id.fragmentFollowOrder)
                }
            }
        }
        orderViewModel.result.observe(viewLifecycleOwner, cancelObserve)
    }

    override fun setUI() {
        val isFragmentFollowOrder = arguments?.getBoolean("key")
        val state  = arguments?.getString("state")
        listProductOrder = arguments?.getParcelableArrayList("listProduct")
        idOrder = arguments?.getInt("idOrder")
        if(isFragmentFollowOrder==true){
            if(state==Constant.CANCEL){
                bindingOrderBinding.btnCancelOrder.visible()
                bindingOrderBinding.btnCancelOrder.text = "Đã hủy"
                bindingOrderBinding.btnCancelOrder.enabled()
            }else if(state==Constant.RECEIVED){
                bindingOrderBinding.btnCancelOrder.visible()
            }
            bindingOrderBinding.ctrlOrder.gone()
        }
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
        bindingOrderBinding.btnCancelOrder.setOnClickListener {
            alertCancel()
        }
        bindingOrderBinding.tvChangeInfo.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentChangeMyInfo)
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
    private fun alertCancel(){
       val builder = AlertDialog.Builder(context)
        builder.setMessage("Bạn có chắc chắn muốn hủy không? ")
        builder.setTitle("Thông báo")
        builder.setCancelable(false)
        builder.setPositiveButton("Có") { dialog, _ ->
            orderViewModel.cancelOrder(idOrder)
            dialog.cancel()
        }
        builder.setNegativeButton("Không") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}