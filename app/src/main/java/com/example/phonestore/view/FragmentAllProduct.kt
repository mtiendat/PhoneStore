package com.example.phonestore.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
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
import com.example.phonestore.services.widget.EndlessRecyclerViewScrollListener
import com.example.phonestore.services.adapter.AllProductAdapter
import com.example.phonestore.viewmodel.AllProductViewModel
import okhttp3.internal.notify

class FragmentAllProduct : BaseFragment() {
    private lateinit var dialog: FragmentDialogFilter
    private lateinit var binding: FragmentAllProductBinding
    private var viewModel: AllProductViewModel? = null
    private var adapter : AllProductAdapter = AllProductAdapter()
    private var listProductCurrent: ArrayList<ProductInfo?>? = arrayListOf()
    private var filterCurrent: Filter? = null
    private var isFilter: Boolean = false
    private var page = 0
    private var flag = 0
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentAllProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerLayoutAllProduct.startShimmer()
        if(flag == 0)
            viewModel?.getAllProduct(1, 8)
    }
    override fun setViewModel() {
        viewModel = ViewModelProvider(this@FragmentAllProduct).get(AllProductViewModel::class.java)
    }

    override fun setObserve() {
      viewModel?.listProduct?.observe(viewLifecycleOwner, {
          if (it != null) {
              binding.groupNotFound.gone()
              binding.shimmerLayoutAllProduct.stopShimmer()
              binding.shimmerLayoutAllProduct.gone()
              listProductCurrent?.addAll(it)
              adapter.notifyDataSetChanged()
              binding.swipe.isRefreshing = false
              if(it.size == 0 && page == 1){
                  binding.groupNotFound.visible()
              }
          }
      })
        viewModel?.ramAndStorage?.observe(viewLifecycleOwner, {
            binding.btnFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_filter_blue,0)
            binding.btnClear.visible()
            dialog.arguments = bundleOf("ramAndStorage" to it, "filterOld" to filterCurrent)
            activity?.supportFragmentManager?.let {
                dialog.show(it, "Filter")
            }
            AppEvent.notifyClosePopUp()
            isFilter = true
        })
        activity?.supportFragmentManager?.setFragmentResultListener("requestKey", viewLifecycleOwner){ key, bundle ->
            if(key == "requestKey"){
                val filter: Filter? = bundle.getParcelable<Filter?>("filter")
                if(filter!=null){
                    binding.groupNotFound.gone()
                    binding.shimmerLayoutAllProduct.visible()
                    binding.shimmerLayoutAllProduct.startShimmer()
                    listProductCurrent?.clear()
                    page = 1
                    viewModel?.filterProduct(1, 8, ram = filter.ram, storage = filter.storage, priceMax = filter.priceMax, priceMin = filter.priceMin, filter.listIDSupplier)
                    filterCurrent = filter
                    binding.rvAllProduct.clearOnScrollListeners()
                    setOnScrollRecyclerviewFilter()

                }else{
                    binding.btnFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_filter,0)
                    binding.btnClear.gone()
                    filterCurrent = null
                }

            }
        }
    }

    override fun setUI() {
        context?.let {
            binding.swipe.setColorSchemeColors(ContextCompat.getColor(it, R.color.blue))
        }
        adapter.click = {
            flag++
        }
        adapter.listProduct = listProductCurrent
        binding.rvAllProduct.adapter = adapter

        setOnScrollRecyclerview()
    }

    override fun setEvents() {
        binding.swipe.setOnRefreshListener {
            binding.btnFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_filter,0)
            binding.btnClear.gone()
            binding.shimmerLayoutAllProduct.visible()
            binding.shimmerLayoutAllProduct.startShimmer()
            flag = 0
            listProductCurrent?.clear()
            viewModel?.listProduct?.value?.clear()
            viewModel?.getAllProduct(1, 8)
            binding.rvAllProduct.clearOnScrollListeners()
            setOnScrollRecyclerview()

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
            AppEvent.notifyShowPopUp()
        }
        binding.btnClear.setOnClickListener {
            binding.btnFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_filter,0)
            binding.shimmerLayoutAllProduct.visible()
            binding.shimmerLayoutAllProduct.startShimmer()
            page = 1
            flag = 0
            viewModel?.getAllProduct(1, 8)
            binding.btnClear.gone()
            listProductCurrent?.clear()
            viewModel?.listProduct?.value?.clear()
            binding.rvAllProduct.clearOnScrollListeners()
            setOnScrollRecyclerview()

        }
    }
    private fun changeStateBtnSort(){
        if( binding.sort.visibility == View.GONE) {
            binding.btnSort.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_sort_blue,0)
            binding.sort.visible()
        } else  {
            binding.btnSort.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_sort,0)
            binding.sort.gone()
        }
    }
    private fun setOnScrollRecyclerview(){
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2,
            LinearLayoutManager.VERTICAL
        )
        binding.rvAllProduct.layoutManager = staggeredGridLayoutManager
        binding.rvAllProduct.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(staggeredGridLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                this@FragmentAllProduct.page = page
                if(flag == 0) viewModel?.getAllProduct(page, 8)
            }
        })
    }
    private fun setOnScrollRecyclerviewFilter(){
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2,
            LinearLayoutManager.VERTICAL
        )
        binding.rvAllProduct.layoutManager = staggeredGridLayoutManager
        binding.rvAllProduct.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(staggeredGridLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                this@FragmentAllProduct.page = page
                viewModel?.filterProduct(page, 8, ram = filterCurrent?.ram, storage = filterCurrent?.storage, priceMax = filterCurrent?.priceMax, priceMin = filterCurrent?.priceMin, filterCurrent?.listIDSupplier)
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = FragmentDialogFilter()

    }

    override fun onResume() {
        super.onResume()
        if(isFilter){
            dialog.dismiss()
            flag == 0
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

}
