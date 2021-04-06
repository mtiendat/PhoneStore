package com.example.phonestore.view

import android.util.Log
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
import com.example.phonestore.databinding.FragmentTransportedOrderBinding
import com.example.phonestore.model.MyOrder
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.Constant
import com.example.phonestore.services.DetailProductAdapter
import com.example.phonestore.viewmodel.OrderViewModel

class FragmentTransportedOrder: BaseFragment() {
    private lateinit var bindingTransportedOrder: FragmentTransportedOrderBinding
    private lateinit var orderViewModel: OrderViewModel
    private var myOrderAdapter: DetailProductAdapter<MyOrder>? = null
    private var listMyOrder: ArrayList<MyOrder> = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingTransportedOrder = FragmentTransportedOrderBinding.inflate(inflater,container, false)
        return bindingTransportedOrder.root
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
    }
    override fun setUI() {
        initRecyclerView()
        myOrderAdapter?.nextInfoOrder = { it, _ ->
            orderViewModel.getListProductOrder(it)
        }
        orderViewModel.getMyOrder(Constant.TRANSPORTED)
    }
    override fun setObserve() {
        val allOrderObserve = Observer<ArrayList<MyOrder>>{
            listMyOrder.addAll(listMyOrder)
            myOrderAdapter?.setItems(it)
        }
        orderViewModel.listMyOrder.observe(viewLifecycleOwner, allOrderObserve)
        val listProductOrderObserve = Observer<ArrayList<ProductOrder>>{
            Log.d("SIZELIST", it.size.toString())
            val item = bundleOf("listProduct" to it, "key" to true)
            view?.findNavController()?.navigate(R.id.action_fragmentFollowOrder_to_fragmentOrder, item)
        }
        orderViewModel.listProductOrder.observe(viewLifecycleOwner, listProductOrderObserve)
    }
    private fun initRecyclerView(){
        myOrderAdapter = DetailProductAdapter(listMyOrder)
        myOrderAdapter?.nextInfoOrder = {it, _ ->
            orderViewModel.getListProductOrder(it)
        }
        orderViewModel.getMyOrder(Constant.DELIVERED)
        bindingTransportedOrder.rvTransportedOrder.adapter = myOrderAdapter
        bindingTransportedOrder.rvTransportedOrder.layoutManager = LinearLayoutManager(context)
    }
}