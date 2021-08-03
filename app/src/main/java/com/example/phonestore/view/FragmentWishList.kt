package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentWishListBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.services.adapter.WishListAdapter
import com.example.phonestore.viewmodel.DetailProductViewModel

class FragmentWishList: BaseFragment() {
    private lateinit var binding: FragmentWishListBinding
    private lateinit var viewModel: DetailProductViewModel
    private lateinit var listProduct: ArrayList<ProductInfo>
    private lateinit var adapter: WishListAdapter
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentWishListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this@FragmentWishList).get(DetailProductViewModel::class.java)
    }

    override fun setObserve() {
        viewModel.wishListResponse?.observe(viewLifecycleOwner, {
            if(it?.status == true){
                it.listProduct?.let { it1 -> adapter.setItems(it1) }
                binding.shimmerLayoutWishList.stopShimmer()
                binding.shimmerLayoutWishList.gone()
            }
        })
    }

    override fun setUI() {
        binding.shimmerLayoutWishList.startShimmer()
        context?.let {
            binding.swipe.setColorSchemeColors(ContextCompat.getColor(it, R.color.blue))
        }
        binding.swipe.setOnRefreshListener {
            binding.shimmerLayoutWishList.visible()
            binding.shimmerLayoutWishList.startShimmer()
            viewModel.wishList()
        }
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2,
            LinearLayoutManager.VERTICAL
        )
        adapter = WishListAdapter()
        binding.rvWishList.adapter = adapter
        binding.rvWishList.layoutManager = staggeredGridLayoutManager
        viewModel.wishList()
    }
}