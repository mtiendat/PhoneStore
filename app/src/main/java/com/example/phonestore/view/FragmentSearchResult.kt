package com.example.phonestore.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.phonestore.Extension.gone
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentSearchResultsBinding
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.services.ProductAdapter
import com.example.phonestore.viewmodel.ProductViewModel
import okhttp3.internal.addHeaderLenient

class FragmentSearchResult: BaseFragment() {

    private lateinit var bindingSearchResult: FragmentSearchResultsBinding
    private var searchResultsAdapter: ProductAdapter<CateProductInfo>? = null
    private var listSearchResults: ArrayList<CateProductInfo>? = arrayListOf()
    private lateinit var searchResultViewModel: ProductViewModel
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingSearchResult = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return  bindingSearchResult.root
    }

    override fun setViewModel() {
        searchResultViewModel = ViewModelProvider(this@FragmentSearchResult).get(ProductViewModel::class.java)
    }

    override fun setObserve() {
        val cateListProduct = Observer<ArrayList<CateProductInfo>?> {
            listSearchResults?.addAll(it)
            searchResultsAdapter?.notifyDataSetChanged()
//            bindingHome.shimmerLayoutRecommend.stopShimmer()
//            bindingHome.shimmerLayoutRecommend.gone()

        }
        searchResultViewModel.listResultSearch.observe(viewLifecycleOwner, cateListProduct)
    }
    override fun setUI() {
        initRecyclerView()
        val q = arguments?.getString("qSearch")


    }
    private fun initRecyclerView(){
        searchResultsAdapter = ProductAdapter(listSearchResults)
        bindingSearchResult.rvSearchResult.adapter = searchResultsAdapter
        bindingSearchResult.rvSearchResult.layoutManager = StaggeredGridLayoutManager(
                2,
                LinearLayoutManager.VERTICAL
        )
    }
}