package com.example.phonestore.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentAllProductBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.services.EndlessRecyclerViewScrollListener
import com.example.phonestore.services.adapter.AllProductAdapter
import com.example.phonestore.viewmodel.AllProductViewModel

class FragmentAllProduct : BaseFragment() {
    private var binding: FragmentAllProductBinding? = null
    private var viewModel: AllProductViewModel? = null
    private var adapter : AllProductAdapter = AllProductAdapter()
    private var listProductCurrent: ArrayList<ProductInfo>? = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentAllProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.shimmerLayoutAllProduct?.startShimmer()
        viewModel?.getAllProduct(1, 8)
    }
    override fun setViewModel() {
        viewModel = ViewModelProvider(this@FragmentAllProduct).get(AllProductViewModel::class.java)
    }

    override fun setObserve() {
      viewModel?.listProduct?.observe(viewLifecycleOwner, {
          if (it != null) {
              binding?.shimmerLayoutAllProduct?.stopShimmer()
              binding?.shimmerLayoutAllProduct?.gone()
              listProductCurrent?.addAll(it)
              adapter.notifyDataSetChanged()
          }
      })
    }

    override fun setUI() {
        context?.let {
            binding?.swipe?.setColorSchemeColors(ContextCompat.getColor(it, R.color.blue))
        }
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2,
            LinearLayoutManager.VERTICAL
        )
        adapter.listProduct = listProductCurrent
        binding?.rvAllProduct?.adapter = adapter
        binding?.rvAllProduct?.layoutManager = staggeredGridLayoutManager
        binding?.rvAllProduct?.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(staggeredGridLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
               viewModel?.getAllProduct(page, 8)
            }

        })
    }

    override fun setEvents() {
        binding?.swipe?.setOnRefreshListener {
            binding?.shimmerLayoutAllProduct?.visible()
            binding?.shimmerLayoutAllProduct?.startShimmer()
            listProductCurrent?.clear()
            viewModel?.listProduct?.value?.clear()
            viewModel?.getAllProduct(1, 8)
            binding?.swipe?.isRefreshing = false
        }
    }

}
