package com.example.phonestore.view

import android.content.Intent
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentSearchBinding
import com.example.phonestore.services.ProductAdapter
import com.example.phonestore.viewmodel.ProductViewModel
import android.os.Handler
import android.widget.ImageView
import com.example.phonestore.R
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.services.SearchAdapter

class FragmentSearch: BaseFragment() {
    private lateinit var bindingSearch:  FragmentSearchBinding
    private lateinit var searchViewModel: ProductViewModel
    private var searchNameAdapter: SearchAdapter<CateProductInfo>? = null
    private var listName: ArrayList<CateProductInfo> = arrayListOf()
    private var length: Int? = 0
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingSearch = FragmentSearchBinding.inflate(inflater, container, false)
        return bindingSearch.root
    }

    override fun setViewModel() {
        searchViewModel = ViewModelProvider(this@FragmentSearch).get(ProductViewModel::class.java)
    }

    override fun setObserve() {
        val listNameObserve = Observer<ArrayList<CateProductInfo>?>{
            listName.addAll(it)
            searchNameAdapter?.setItems(it)
        }
        searchViewModel.listResultSearch.observe(viewLifecycleOwner, listNameObserve)
    }
    override fun setUI() {

        MainActivity.searchView?.get()?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.length!! < length!!){
                    if(newText.isEmpty()){
                        listName.clear()
                        searchNameAdapter?.notifyDataSetChanged()
                        length = 0
                    }
                }else{
                    length = newText.length
                    Handler(Looper.getMainLooper()).postDelayed({
                        listName.clear()
                        searchNameAdapter?.notifyDataSetChanged()
                        searchViewModel.searchName(newText)

                    },1000)
                }



                return true
            }
        })
        initRecyclerView()

    }
    private fun initRecyclerView(){
        searchNameAdapter = SearchAdapter(listName)
        bindingSearch.rvSearchName.adapter = searchNameAdapter
        bindingSearch.rvSearchName.layoutManager = LinearLayoutManager(context)
    }
    private fun colorClear(){

    }
}