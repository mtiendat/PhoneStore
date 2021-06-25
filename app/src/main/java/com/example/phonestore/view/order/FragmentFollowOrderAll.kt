package com.example.phonestore.view.order

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentFollowOrderAllBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.order.MyOrder
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.viewmodel.OrderViewModel

class FragmentFollowOrderAll: BaseFragment() {
    private lateinit var bindingAllFollow: FragmentFollowOrderAllBinding
    private var orderViewModel: OrderViewModel? = null
    private var myOrderAdapter: DetailProductAdapter<MyOrder>? = null
    private var listMyOrder: ArrayList<MyOrder> = arrayListOf()
    private var idOrder : Int? = 0
    private var state : String? = ""
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingAllFollow = FragmentFollowOrderAllBinding.inflate(inflater, container, false)
        return bindingAllFollow.root
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this@FragmentFollowOrderAll).get(OrderViewModel::class.java)
    }
    override fun setUI() {
        bindingAllFollow.swipe.setColorSchemeColors(Color.BLUE)
        bindingAllFollow.shimmerOrderAll.startShimmer()
        initRecyclerView()
        orderViewModel?.getMyOrder()
    }
    override fun setObserve() {
        val allOrderObserve = Observer<ArrayList<MyOrder>?>{
            bindingAllFollow.shimmerOrderAll.stopShimmer()
            bindingAllFollow.shimmerOrderAll.gone()
            if(it.size > 0){
                listMyOrder.addAll(listMyOrder)
                myOrderAdapter?.setItems(it)
            }else bindingAllFollow.ivBill.visible()

        }
        orderViewModel?.listMyOrder?.observe(viewLifecycleOwner, allOrderObserve)
        orderViewModel?.detailOrder?.observe(viewLifecycleOwner, {
            val item = bundleOf("idOrder" to idOrder, "listProduct" to it.listProduct, "state" to state, "key" to true, "info" to it.order)
            view?.findNavController()?.navigate(R.id.action_fragmentFollowOrder_to_fragmentOrder, item)
        })

    }

    override fun setEvents() {
        bindingAllFollow.swipe.setOnRefreshListener {
            listMyOrder.clear()
            myOrderAdapter?.notifyDataSetChanged()
            bindingAllFollow.shimmerOrderAll.startShimmer()
            orderViewModel?.getMyOrder()
            bindingAllFollow.swipe.isRefreshing = false
        }
    }
    private fun initRecyclerView(){
        myOrderAdapter = DetailProductAdapter(listMyOrder)
        myOrderAdapter?.nextInfoOrder = { idOrder, state ->
            this.idOrder = idOrder
            this.state = state
            orderViewModel?.getListProductOrder(idOrder)
        }
        bindingAllFollow.rvAllOrder.adapter = myOrderAdapter
        bindingAllFollow.rvAllOrder.layoutManager = LinearLayoutManager(context)
    }
}