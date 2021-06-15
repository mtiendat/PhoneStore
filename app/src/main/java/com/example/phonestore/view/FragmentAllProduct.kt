package com.example.phonestore.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentAllProductBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.Filter
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.services.EndlessRecyclerViewScrollListener
import com.example.phonestore.services.adapter.AllProductAdapter
import com.example.phonestore.view.productDetail.FragmentDetailTechnology
import com.example.phonestore.viewmodel.AllProductViewModel

class FragmentAllProduct : BaseFragment() {
    private lateinit var binding: FragmentAllProductBinding
    private var viewModel: AllProductViewModel? = null
    private var adapter : AllProductAdapter = AllProductAdapter()
    private var listProductCurrent: ArrayList<ProductInfo>? = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentAllProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerLayoutAllProduct?.startShimmer()
        viewModel?.getAllProduct(1, 8)
    }
    override fun setViewModel() {
        viewModel = ViewModelProvider(this@FragmentAllProduct).get(AllProductViewModel::class.java)
    }

    override fun setObserve() {
      viewModel?.listProduct?.observe(viewLifecycleOwner, {
          if (it != null) {
              binding.shimmerLayoutAllProduct.stopShimmer()
              binding.shimmerLayoutAllProduct.gone()
              listProductCurrent?.clear()
              listProductCurrent?.addAll(it)
              adapter.notifyDataSetChanged()
          }
      })
        viewModel?.ramAndStorage?.observe(viewLifecycleOwner, {
            val dialog = FragmentDialogFilter()
            dialog.arguments = bundleOf("ramAndStorage" to it)
            activity?.supportFragmentManager?.let {
                dialog.show(it, "Filter")
            }
        })
        activity?.supportFragmentManager?.setFragmentResultListener("requestKey", viewLifecycleOwner){ key, bundle ->
            if(key == "requestKey"){
                val filter = bundle.getParcelable<Filter>("filter")
                binding.shimmerLayoutAllProduct.visible()
                binding.shimmerLayoutAllProduct.startShimmer()
                viewModel?.filterProduct(1, 8, ram = filter?.ram, storage = filter?.storage, priceMax = filter?.priceMax, priceMin = filter?.priceMin)
            }
        }
    }

    override fun setUI() {
        context?.let {
            binding.swipe.setColorSchemeColors(ContextCompat.getColor(it, R.color.blue))
        }
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2,
            LinearLayoutManager.VERTICAL
        )
        adapter.listProduct = listProductCurrent
        binding.rvAllProduct.adapter = adapter
        binding.rvAllProduct.layoutManager = staggeredGridLayoutManager
        binding.rvAllProduct.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(staggeredGridLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
               viewModel?.getAllProduct(page, 8)
            }

        })
    }

    override fun setEvents() {
        binding.swipe.setOnRefreshListener {
            binding.shimmerLayoutAllProduct.visible()
            binding.shimmerLayoutAllProduct.startShimmer()
            listProductCurrent?.clear()
            viewModel?.listProduct?.value?.clear()
            viewModel?.getAllProduct(1, 8)
            binding.swipe.isRefreshing = false
        }
        binding.btnSort.setOnClickListener {
            changeStateBtnSort()
        }
        binding.btnPriceDown.setOnClickListener {
            AppEvent.notifyShowPopUp()
            listProductCurrent?.sortWith { o1, o2 ->
                (o1?.price?.minus((o1.price * o1.discount)))?.toInt()!! -  (o2?.price?.minus((o2.price * o2.discount)))!!.toInt() //Tăng dần
            }
            adapter.notifyDataSetChanged()
            changeStateBtnSort()
            AppEvent.notifyClosePopUp()
        }
        binding.btnPriceHigh.setOnClickListener {
            AppEvent.notifyShowPopUp()
            listProductCurrent?.sortWith { o1, o2 ->
                (o2?.price?.minus((o2.price * o2.discount)))!!.toInt() - (o1?.price?.minus((o1.price * o1.discount)))?.toInt()!!  //Tăng dần
            }
            adapter.notifyDataSetChanged()
            changeStateBtnSort()
            AppEvent.notifyClosePopUp()
        }
        binding.btnFilter.setOnClickListener {
            viewModel?.getRamAndStorage()
        }
    }
    private fun changeStateBtnSort(){
        if( binding.sort.visibility == View.GONE) binding.sort.visible() else  binding.sort.gone()
    }

}
