package com.example.phonestore.view.order


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
import com.example.phonestore.databinding.FragmentFollowOrderReceivedBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.model.order.MyOrder
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.viewmodel.OrderViewModel

class FragmentFollowOrderReceived: BaseFragment() {
    private var bindingOrderReceived: FragmentFollowOrderReceivedBinding? = null
    private var orderViewModel: OrderViewModel? = null
    private var myOrderAdapter: DetailProductAdapter<MyOrder>? = null
    private var listMyOrder: ArrayList<MyOrder> = arrayListOf()
    private var state : String? = ""
    private var idOrder : Int? = 0
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingOrderReceived = FragmentFollowOrderReceivedBinding.inflate(inflater, container, false)
        return bindingOrderReceived?.root
    }

    override fun setUI() {
        initRecyclerView()
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this@FragmentFollowOrderReceived).get(OrderViewModel::class.java)
    }
    override fun setObserve() {
        val allOrderObserve = Observer<ArrayList<MyOrder>?>{
            listMyOrder.addAll(listMyOrder)
            myOrderAdapter?.setItems(it)
            bindingOrderReceived?.ivBillReceived?.gone()
        }
        orderViewModel?.listMyOrder?.observe(viewLifecycleOwner, allOrderObserve)
        val listProductOrderObserve = Observer<ArrayList<ProductOrder>>{
            val item = bundleOf("idOrder" to idOrder, "listProduct" to it, "state" to state, "key" to true)
            view?.findNavController()?.navigate(R.id.action_fragmentFollowOrder_to_fragmentOrder, item)
        }
        orderViewModel?.listProductOrder?.observe(viewLifecycleOwner, listProductOrderObserve)
    }

    private fun initRecyclerView(){
        myOrderAdapter = DetailProductAdapter(listMyOrder)
        myOrderAdapter?.nextInfoOrder = {it, state ->
            this.idOrder = it
            this.state = state
            orderViewModel?.getListProductOrder(it)
        }
        orderViewModel?.getMyOrder(Constant.RECEIVED)
        bindingOrderReceived?.rvReceivedOrder?.adapter = myOrderAdapter
        bindingOrderReceived?.rvReceivedOrder?.layoutManager = LinearLayoutManager(context)
    }
}