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
import com.example.phonestore.databinding.FragmentFollowOrderDeliveredBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.order.MyOrder
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.viewmodel.OrderViewModel

class FragmentFollowOrderDelivered: BaseFragment() {
    private lateinit var bindingDeliveredOrder: FragmentFollowOrderDeliveredBinding
    private var orderViewModel: OrderViewModel? = null
    private var myOrderAdapter: DetailProductAdapter<MyOrder>? = null
    private var listMyOrder: ArrayList<MyOrder>? = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingDeliveredOrder = FragmentFollowOrderDeliveredBinding.inflate(inflater, container, false)
        return bindingDeliveredOrder.root
    }
    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
    }
    override fun setUI() {
        context?.let { ContextCompat.getColor(it, R.color.green) }?.let {
            bindingDeliveredOrder.swipe.setColorSchemeColors(
                it
            )
        }
        bindingDeliveredOrder.shimmerOrder.startShimmer()
        initRecyclerView()
        orderViewModel?.getMyOrder(Constant.DELIVERED)
    }
    override fun setEvents() {
        bindingDeliveredOrder.swipe.setOnRefreshListener {
            listMyOrder?.clear()
            myOrderAdapter?.notifyDataSetChanged()
            bindingDeliveredOrder.shimmerOrder.startShimmer()
            orderViewModel?.getMyOrder(Constant.DELIVERED)
            bindingDeliveredOrder.swipe.isRefreshing = false
        }
    }
    override fun setObserve() {
        orderViewModel?.listMyOrder?.observe(viewLifecycleOwner, {
            bindingDeliveredOrder.shimmerOrder.stopShimmer()
            bindingDeliveredOrder.shimmerOrder.gone()
            if(it?.size?:0 >0) {
                if (it != null) {
                    listMyOrder?.addAll(it)
                    myOrderAdapter?.notifyDataSetChanged()
                }
            }else bindingDeliveredOrder.ivBillDelivered.visible()
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
        bindingDeliveredOrder.rvDeliveredOrder.adapter = myOrderAdapter
        bindingDeliveredOrder.rvDeliveredOrder.layoutManager = LinearLayoutManager(context)
    }

}