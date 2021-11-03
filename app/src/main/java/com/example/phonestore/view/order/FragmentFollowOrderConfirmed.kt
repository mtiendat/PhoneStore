package com.example.phonestore.view.order

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentFollowOrderConfirmedBinding
import com.example.phonestore.databinding.FragmentFollowOrderTransportedBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.model.order.MyOrder
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.viewmodel.OrderViewModel

class FragmentFollowOrderConfirmed: BaseFragment() {
    private lateinit var bindingOrderConfirmed: FragmentFollowOrderConfirmedBinding
    private var orderViewModel: OrderViewModel? = null
    private var myOrderAdapter: DetailProductAdapter<MyOrder>? = null
    private var listMyOrder: ArrayList<MyOrder> = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingOrderConfirmed = FragmentFollowOrderConfirmedBinding.inflate(inflater,container, false)
        return bindingOrderConfirmed.root
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
    }
    override fun setUI() {
        context?.let { ContextCompat.getColor(it, R.color.blue_navy) }?.let {
            bindingOrderConfirmed.swipe.setColorSchemeColors(
                it
            )
        }
        bindingOrderConfirmed.shimmerOrderConfirmed.startShimmer()
        initRecyclerView()
        orderViewModel?.getMyOrder(Constant.CONFIRMED)
    }
    override fun setObserve() {
        val allOrderObserve = Observer<ArrayList<MyOrder>?>{
            bindingOrderConfirmed.shimmerOrderConfirmed.stopShimmer()
            bindingOrderConfirmed.shimmerOrderConfirmed.gone()
            if(it.size >0) {
                listMyOrder.addAll(it)
                myOrderAdapter?.setItems(it)
            }else bindingOrderConfirmed.ivBillConfirmed.visible()
        }
        orderViewModel?.listMyOrder?.observe(viewLifecycleOwner, allOrderObserve)

        orderViewModel?.detailOrder?.observe(viewLifecycleOwner, {
            val item = bundleOf("listProduct" to it, "key" to true, "info" to it.order)
            view?.findNavController()?.navigate(R.id.action_fragmentFollowOrder_to_fragmentOrder, item)
        })
    }

    override fun setEvents() {
        bindingOrderConfirmed.swipe.setOnRefreshListener {
            listMyOrder.clear()
            myOrderAdapter?.notifyDataSetChanged()
            bindingOrderConfirmed.shimmerOrderConfirmed.startShimmer()
            orderViewModel?.getMyOrder(Constant.CONFIRMED)
            bindingOrderConfirmed.swipe.isRefreshing = false
        }
    }
    private fun initRecyclerView(){
        myOrderAdapter = DetailProductAdapter(listMyOrder)
        myOrderAdapter?.nextInfoOrder = {it, _ ->
            orderViewModel?.getListProductOrder(it)
        }
        bindingOrderConfirmed.rvConfirmedOrder.adapter = myOrderAdapter
        bindingOrderConfirmed.rvConfirmedOrder.layoutManager = LinearLayoutManager(context)
    }
}