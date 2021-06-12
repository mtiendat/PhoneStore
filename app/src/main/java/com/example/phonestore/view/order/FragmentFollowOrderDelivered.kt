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
import com.example.phonestore.databinding.FragmentFollowOrderDeliveredBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.model.order.MyOrder
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.viewmodel.OrderViewModel

class FragmentFollowOrderDelivered: BaseFragment() {
    private var bindingDeliveredOrder: FragmentFollowOrderDeliveredBinding? = null
    private var orderViewModel: OrderViewModel? = null
    private var myOrderAdapter: DetailProductAdapter<MyOrder>? = null
    private var listMyOrder: ArrayList<MyOrder>? = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingDeliveredOrder = FragmentFollowOrderDeliveredBinding.inflate(inflater, container, false)
        return bindingDeliveredOrder?.root
    }
    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
    }
    override fun setUI() {
        initRecyclerView()

    }
    override fun setObserve() {
        val allOrderObserve = Observer<ArrayList<MyOrder>?>{
            if(it.size >0) {
                listMyOrder?.addAll(it)
                myOrderAdapter?.setItems(it)
                bindingDeliveredOrder?.ivBillDelivered?.gone()
            }
        }
        orderViewModel?.listMyOrder?.observe(viewLifecycleOwner, allOrderObserve)
        val listProductOrderObserve = Observer<ArrayList<ProductOrder>>{
            val item = bundleOf("listProduct" to it, "key" to true)
            view?.findNavController()?.navigate(R.id.action_fragmentFollowOrder_to_fragmentOrder, item)
        }
        orderViewModel?.listProductOrder?.observe(viewLifecycleOwner, listProductOrderObserve)
    }
    private fun initRecyclerView(){
        myOrderAdapter = DetailProductAdapter(listMyOrder)
        myOrderAdapter?.nextInfoOrder = {it, _ ->
            orderViewModel?.getListProductOrder(it)
        }
        orderViewModel?.getMyOrder(Constant.DELIVERED)
        bindingDeliveredOrder?.rvDeliveredOrder?.adapter = myOrderAdapter
        bindingDeliveredOrder?.rvDeliveredOrder?.layoutManager = LinearLayoutManager(context)
    }

}