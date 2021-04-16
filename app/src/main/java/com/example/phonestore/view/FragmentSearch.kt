package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentSearchBinding
import com.example.phonestore.viewmodel.ProductViewModel
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.phonestore.R
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.services.adapter.SearchAdapter
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable

class FragmentSearch: BaseFragment() {
    private var bindingSearch:  FragmentSearchBinding? = null
    private var searchViewModel: ProductViewModel? = null
    private var searchNameAdapter: SearchAdapter<CateProductInfo>? = null
    private var listName: ArrayList<CateProductInfo> = arrayListOf()
    private var length: Int? = 0
    private var flag = 0
    private var keyWord:String ?=""
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingSearch = FragmentSearchBinding.inflate(inflater, container, false)
        return bindingSearch?.root
    }

    override fun setViewModel() {
        searchViewModel = ViewModelProvider(this@FragmentSearch).get(ProductViewModel::class.java)
    }

    override fun setObserve() {
        val listNameObserve = Observer<ArrayList<CateProductInfo>?>{
            if(it.size != 0) {
                listName.addAll(it)
                searchNameAdapter?.setItems(it)
                bindingSearch?.ivBird?.gone()
                bindingSearch?.progressBarSearch?.gone()
            }else{
                listName.clear()
                searchNameAdapter?.notifyDataSetChanged()
                bindingSearch?.ivBird?.visible()
                bindingSearch?.progressBarSearch?.gone()
                Toast.makeText(context, "Từ khóa không khớp với bất kì sản phẩm nào", Toast.LENGTH_SHORT).show()
            }
        }
        searchViewModel?.listResultSearch?.observe(viewLifecycleOwner, listNameObserve)
    }


    override fun setUI() {
        bindingSearch?.progressBarSearch?.setIndeterminateDrawableTiled(
                FoldingCirclesDrawable.Builder(context).colors(resources.getIntArray(
                        R.array.google_colors)).build())
        if(flag == 1) {
            MainActivity.itemSearch?.expandActionView() //open searchSearchView
            MainActivity.searchView?.get()?.setQuery(keyWord, false)
            flag = 0
        }
        MainActivity.searchView?.get()?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                keyWord = query
                bindingSearch?.ivBird?.gone()
                bindingSearch?.progressBarSearch?.visible()
                searchViewModel?.searchName(query)
//                if(query?.length!! < length!!){
//                    if(query.isEmpty()){
//                        listName.clear()
//                        bindingSearch?.ivBird?.visible()
//                        searchNameAdapter?.notifyDataSetChanged()
//                        length = 0
//                    }
//                }else{
//                    length = query.length
//                    if(keyWord != query) {
//                        keyWord = query
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            listName.clear()
//                            searchNameAdapter?.notifyDataSetChanged()
//                            searchViewModel?.searchName(query)
//                            flag = 1
//
//                        }, 1000)
//                    }
//
//                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrBlank()){
                    listName.clear()
                    searchNameAdapter?.notifyDataSetChanged()
                    bindingSearch?.progressBarSearch?.gone()
                    bindingSearch?.ivBird?.visible()
                }
                return true
            }

        })
        MainActivity.itemSearch?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                view?.findNavController()?.popBackStack()
                activity?.invalidateOptionsMenu()
                return true
            }

        })
        initRecyclerView()
        searchNameAdapter?.onSetFlag = {
            flag = 1
        }

    }
    private fun initRecyclerView(){
        searchNameAdapter = SearchAdapter(listName)
        bindingSearch?.rvSearchName?.adapter = searchNameAdapter
        bindingSearch?.rvSearchName?.layoutManager = LinearLayoutManager(context)
    }

}