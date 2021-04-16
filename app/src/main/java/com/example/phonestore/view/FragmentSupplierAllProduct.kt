package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentSupplierAllProductBinding
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.Supplier
import com.example.phonestore.services.adapter.ProductAdapter
import com.example.phonestore.viewmodel.ProductViewModel

class FragmentSupplierAllProduct(var supplier: Supplier?): BaseFragment() {
    private var bindingSupplierAllProduct: FragmentSupplierAllProductBinding? = null
    private var supplierViewModel: ProductViewModel? = null
    private var adapter: ProductAdapter<CateProductInfo>? = null
    private var listCateProductInfo: ArrayList<CateProductInfo> = arrayListOf()
    private var orderBy: Int = 0
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingSupplierAllProduct = FragmentSupplierAllProductBinding.inflate(inflater, container, false)
        return bindingSupplierAllProduct?.root
    }

    override fun setViewModel() {
        supplierViewModel = ViewModelProvider(this@FragmentSupplierAllProduct).get(ProductViewModel::class.java)
    }

    override fun setObserve() {
        val listProductObserve = Observer<ArrayList<CateProductInfo>?>{
            listCateProductInfo.addAll(it)
            adapter?.notifyDataSetChanged()
        }
        supplierViewModel?.listCateProduct?.observe(viewLifecycleOwner, listProductObserve)
    }

    override fun setUI() {
        initRecyclerView()
        supplierViewModel?.getListCateProduct(1, idSupplier = supplier?.id)
        bindingSupplierAllProduct?.btnSortAllProduct?.setOnClickListener {
            if(orderBy ==0) {
                listCateProductInfo.sortWith { o1, o2 ->
                    o1.priceNew - o2.priceNew //Tăng dần
                }
                adapter?.notifyDataSetChanged()
                orderBy = 1
                bindingSupplierAllProduct?.btnSortAllProduct?.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_down,0)
                return@setOnClickListener
            }else if(orderBy==1)
                listCateProductInfo  .sortWith { o1, o2 ->
                    o2.priceNew - o1.priceNew //Giảm dần
                }
            adapter?.notifyDataSetChanged()
            orderBy = 0
            bindingSupplierAllProduct?.btnSortAllProduct?.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_up,0)
        }
    }

    private fun initRecyclerView(){
        adapter = ProductAdapter(listCateProductInfo)
        bindingSupplierAllProduct?.rvSupplierAllProduct?.adapter = adapter
        bindingSupplierAllProduct?.rvSupplierAllProduct?.layoutManager = StaggeredGridLayoutManager(
                2,
                LinearLayoutManager.VERTICAL
        )
    }
}