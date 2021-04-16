package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentSupplierNewProductBinding
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.Supplier
import com.example.phonestore.services.adapter.ProductAdapter
import com.example.phonestore.viewmodel.ProductViewModel

class FragmentSupplierNewProduct(var supplier: Supplier?): BaseFragment() {
    private var bindingSupplierNewProduct: FragmentSupplierNewProductBinding? = null
    private var supplierViewModel: ProductViewModel? = null
    private var adapter: ProductAdapter<CateProductInfo>? = null
    private var listCateProductInfo: ArrayList<CateProductInfo> = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingSupplierNewProduct = FragmentSupplierNewProductBinding.inflate(inflater, container, false)
        return bindingSupplierNewProduct?.root
    }
    override fun setViewModel() {
        supplierViewModel = ViewModelProvider(this@FragmentSupplierNewProduct).get(ProductViewModel::class.java)
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
        supplierViewModel?.getNewCateProductBySupplier( idSupplier = supplier?.id)
    }
    private fun initRecyclerView(){
        adapter = ProductAdapter(listCateProductInfo)
        bindingSupplierNewProduct?.rvSupplierNewProduct?.adapter = adapter
        bindingSupplierNewProduct?.rvSupplierNewProduct?.layoutManager = StaggeredGridLayoutManager(
                2,
                LinearLayoutManager.VERTICAL
        )
    }
}