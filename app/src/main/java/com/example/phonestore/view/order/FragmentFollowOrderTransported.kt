package com.example.phonestore.view.order

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
import com.example.phonestore.databinding.FragmentFollowOrderTransportedBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.order.MyOrder
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.viewmodel.OrderViewModel

class FragmentFollowOrderTransported: BaseFragment() {
    private lateinit var bindingTransportedOrder: FragmentFollowOrderTransportedBinding
    private var orderViewModel: OrderViewModel? = null
    private var myOrderAdapter: DetailProductAdapter<MyOrder>? = null
    private var listMyOrder: ArrayList<MyOrder> = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingTransportedOrder = FragmentFollowOrderTransportedBinding.inflate(inflater,container, false)
        return bindingTransportedOrder.root
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
    }
    override fun setUI() {
        context?.let { ContextCompat.getColor(it, R.color.red) }?.let {
            bindingTransportedOrder.swipe.setColorSchemeColors(
                it
            )
        }
        bindingTransportedOrder.shimmerOrder.startShimmer()
        initRecyclerView()
        orderViewModel?.getMyOrder(Constant.TRANSPORTED)
    }
    override fun setEvents() {
        bindingTransportedOrder.swipe.setOnRefreshListener {
            listMyOrder.clear()
            myOrderAdapter?.notifyDataSetChanged()
            bindingTransportedOrder.shimmerOrder.startShimmer()
            orderViewModel?.getMyOrder(Constant.TRANSPORTED)
            bindingTransportedOrder.swipe.isRefreshing = false
        }
    }
    override fun setObserve() {
        orderViewModel?.listMyOrder?.observe(viewLifecycleOwner, {
            bindingTransportedOrder.shimmerOrder.stopShimmer()
            bindingTransportedOrder.shimmerOrder.gone()
            if(it?.size?:0 > 0) {
                if (it != null) {
                    listMyOrder.addAll(it)
                    myOrderAdapter?.setItems(it)
                }
            }else bindingTransportedOrder.ivBillTransported.visible()
        })
        orderViewModel?.detailOrder?.observe(viewLifecycleOwner, {
            val item = bundleOf("listProduct" to it.listProduct, "key" to true, "info" to it.order)
            view?.findNavController()?.navigate(R.id.action_fragmentFollowOrder_to_fragmentOrder, item)
        })
    }
    private fun initRecyclerView(){
        myOrderAdapter = DetailProductAdapter(listMyOrder)
        myOrderAdapter?.nextInfoOrder = {it, _ ->
            orderViewModel?.getListProductOrder(it)
        }
        bindingTransportedOrder.rvTransportedOrder.adapter = myOrderAdapter
        bindingTransportedOrder.rvTransportedOrder.layoutManager = LinearLayoutManager(context)
    }
}