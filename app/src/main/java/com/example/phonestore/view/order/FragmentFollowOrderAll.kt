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
import com.example.phonestore.databinding.FragmentFollowOrderAllBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.model.order.MyOrder
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.viewmodel.OrderViewModel

class FragmentFollowOrderAll: BaseFragment() {
    private var bindingAllFollow: FragmentFollowOrderAllBinding? = null
    private var orderViewModel: OrderViewModel? = null
    private var myOrderAdapter: DetailProductAdapter<MyOrder>? = null
    private var listMyOrder: ArrayList<MyOrder> = arrayListOf()
    private var idOrder : Int? = 0
    private var state : String? = ""
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingAllFollow = FragmentFollowOrderAllBinding.inflate(inflater, container, false)
        return bindingAllFollow?.root
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this@FragmentFollowOrderAll).get(OrderViewModel::class.java)
    }
    override fun setUI() {
        initRecyclerView()
        orderViewModel?.getMyOrder()
    }
    override fun setObserve() {
        val allOrderObserve = Observer<ArrayList<MyOrder>?>{
                bindingAllFollow?.ivBill?.gone()
                listMyOrder.addAll(listMyOrder)
                myOrderAdapter?.setItems(it)


        }
        orderViewModel?.listMyOrder?.observe(viewLifecycleOwner, allOrderObserve)
        val listProductOrderObserve = Observer<ArrayList<ProductOrder>>{
            val item = bundleOf("idOrder" to idOrder, "listProduct" to it,"state" to state, "key" to true)
            view?.findNavController()?.navigate(R.id.action_fragmentFollowOrder_to_fragmentOrder, item)
        }
        orderViewModel?.listProductOrder?.observe(viewLifecycleOwner, listProductOrderObserve)

    }
    private fun initRecyclerView(){
        myOrderAdapter = DetailProductAdapter(listMyOrder)
        myOrderAdapter?.nextInfoOrder = { idOrder, state ->
            this.idOrder = idOrder
            this.state = state
            orderViewModel?.getListProductOrder(idOrder)
        }
        bindingAllFollow?.rvAllOrder?.adapter = myOrderAdapter
        bindingAllFollow?.rvAllOrder?.layoutManager = LinearLayoutManager(context)
    }
}