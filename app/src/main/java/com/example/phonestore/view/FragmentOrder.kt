package com.example.phonestore.view

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.extendsion.enabled
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.extendsion.visible
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentOrderBinding
import com.example.phonestore.model.DetailCart
import com.example.phonestore.model.Order
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.viewmodel.OrderViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentOrder: BaseFragment() {

    private var bindingOrderBinding: FragmentOrderBinding? = null
    private var listProductOrder: ArrayList<ProductOrder>? = null
    private var orderAdapter: DetailProductAdapter<ProductOrder>? = null
    private var totalMoney: Int = 0
    private var idOrder:  Int? = 0
    private var orderViewModel: OrderViewModel? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingOrderBinding = FragmentOrderBinding.inflate(inflater, container, false)
        return bindingOrderBinding?.root
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
        orderViewModel?.resultOrder?.observe(viewLifecycleOwner, resultOrderObserve)
        val cancelObserve  = Observer<Boolean?> {
            if(it==true){
                view?.let { it1 -> Snackbar.make(it1, Constant.SUCCESS_CANCEL, Snackbar.LENGTH_SHORT).show() }
                view?.findNavController()?.apply {
                    popBackStack(R.id.fragmentFollowOrder, true)
                    navigate(R.id.fragmentFollowOrder)
                }
            }
        }
        orderViewModel?.result?.observe(viewLifecycleOwner, cancelObserve)
    }

    override fun setUI() {
        val isFragmentFollowOrder = arguments?.getBoolean("key")
        val state  = arguments?.getString("state")
        listProductOrder = arguments?.getParcelableArrayList("listProduct")
        idOrder = arguments?.getInt("idOrder")
        if(isFragmentFollowOrder==true){
            bindingOrderBinding?.tvChangeInfo?.gone()
            if(state==Constant.CANCEL){
                bindingOrderBinding?.btnCancelOrder?.visible()
                context?.let { ContextCompat.getColor(it, R.color.dray) }?.let { bindingOrderBinding?.btnCancelOrder?.setBackgroundColor(it) }
                bindingOrderBinding?.btnCancelOrder?.text = Constant.CANCEL
                bindingOrderBinding?.btnCancelOrder?.enabled()
            }else if(state==Constant.RECEIVED){
                bindingOrderBinding?.btnCancelOrder?.visible()
            }
            bindingOrderBinding?.ctrlOrder?.gone()
        }
        totalMoney = listProductOrder?.size?.minus(1) ?: 0 //lấy tổng tiền ở ptu vị trí cuối
        bindingOrderBinding?.rvOrderProduct?.isNestedScrollingEnabled = false
        initRecyclerView()
        setOnClickListener()
        setInfoOrder()

    }
    fun setOnClickListener(){
        bindingOrderBinding?.btnOrderFinish?.setOnClickListener {
            val listProductFinish: ArrayList<DetailCart> = arrayListOf()
            for(i in listProductOrder!!){
                i.product?.let { it1 -> listProductFinish.add(it1) }
            }
            val order = Order(listProductOrder?.get(totalMoney)?.total, listProductFinish)
            orderViewModel?.order(order)
        }
        bindingOrderBinding?.btnCancelOrder?.setOnClickListener {
            alertCancel()
        }
        bindingOrderBinding?.tvChangeInfo?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentChangeMyInfo)
        }
        bindingOrderBinding?.btnOrderShippingOption?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentShippingOption)
        }
        bindingOrderBinding?.btnPaymentOption?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentPaymentOption)
        }
    }
    private fun setInfoOrder(){
        bindingOrderBinding?.tvOrderAddress?.text = Constant.user?.address
        bindingOrderBinding?.tvOrderNameUser?.text = Constant.user?.name
        bindingOrderBinding?.tvOrderPhoneUser?.text = Constant.user?.phone
        bindingOrderBinding?.tvOrderTotalMoney?.text = listProductOrder?.get(totalMoney)?.total.toVND()
        bindingOrderBinding?.tvOrderTotalMoneyFinish?.text = listProductOrder?.get(totalMoney)?.total.toVND()

    }
    private fun initRecyclerView(){
        orderAdapter = DetailProductAdapter(listProductOrder)
        bindingOrderBinding?.rvOrderProduct?.adapter = orderAdapter
        bindingOrderBinding?.rvOrderProduct?.layoutManager = LinearLayoutManager(context)
    }
    private fun alertCancel(){
       val builder = AlertDialog.Builder(context)
        builder.setMessage(Constant.QUESTION_CANCEL)
        builder.setTitle(Constant.NOTIFICATION)
        builder.setCancelable(false)
        builder.setPositiveButton(Constant.YES) { dialog, _ ->
            orderViewModel?.cancelOrder(idOrder)
            dialog.cancel()
        }
        builder.setNegativeButton(Constant.NO) { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            context?.let {alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(it, R.color.blue))}
            context?.let {alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(it, R.color.blue))}
        }

        alertDialog.show()
    }
}