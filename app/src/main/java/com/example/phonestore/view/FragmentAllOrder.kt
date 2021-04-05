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
import com.example.phonestore.databinding.FragmentAllFollowOrderBinding
import com.example.phonestore.model.MyOrder
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.DetailProductAdapter
import com.example.phonestore.viewmodel.OrderViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentAllOrder: BaseFragment() {
    private lateinit var bindingAllFollow: FragmentAllFollowOrderBinding
    private lateinit var orderViewModel: OrderViewModel
    private var myOrderAdapter: DetailProductAdapter<MyOrder>? = null
    private var listMyOrder: ArrayList<MyOrder> = arrayListOf()
    private var idOrder : Int? = 0
    private var state : String? = ""
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingAllFollow = FragmentAllFollowOrderBinding.inflate(inflater, container, false)
        return bindingAllFollow.root
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this@FragmentAllOrder).get(OrderViewModel::class.java)
    }
    override fun setUI() {
        initRecyclerView()
        myOrderAdapter?.nextInfoOrder = { idOrder, state ->
            this.idOrder = idOrder
            this.state = state
            orderViewModel.getListProductOrder(idOrder)
        }
        orderViewModel.getMyOrder()
    }
    override fun setObserve() {
        val allOrderObserve = Observer<ArrayList<MyOrder>>{
            listMyOrder.addAll(listMyOrder)
            myOrderAdapter?.setItems(it)
        }
        orderViewModel.listMyOrder.observe(viewLifecycleOwner, allOrderObserve)
        val listProductOrderObserve = Observer<ArrayList<ProductOrder>>{
            val item = bundleOf("idOrder" to idOrder,"listProduct" to it,"state" to state, "key" to true)
            view?.findNavController()?.navigate(R.id.action_fragmentFollowOrder_to_fragmentOrder, item)
        }
        orderViewModel.listProductOrder.observe(viewLifecycleOwner, listProductOrderObserve)

    }
    private fun initRecyclerView(){
        myOrderAdapter = DetailProductAdapter(listMyOrder)
        bindingAllFollow.rvAllOrder.adapter = myOrderAdapter
        bindingAllFollow.rvAllOrder.layoutManager = LinearLayoutManager(context)
    }
}