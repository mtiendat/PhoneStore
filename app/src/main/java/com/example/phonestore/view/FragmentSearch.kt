package com.example.phonestore.view

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentSearchBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.services.adapter.SearchAdapter
import com.example.phonestore.viewmodel.ProductViewModel
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable


class FragmentSearch: BaseFragment() {
    private var bindingSearch:  FragmentSearchBinding? = null
    private var searchViewModel: ProductViewModel? = null
    private var searchNameAdapter: SearchAdapter<ProductInfo>? = null
    private var listName: ArrayList<ProductInfo?> = arrayListOf()
    private var length: Int? = 0
    private var flag = 0
    private lateinit var runnable: Runnable
    private var waitingTime  = 300
    private var countdown: CountDownTimer?= null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingSearch = FragmentSearchBinding.inflate(inflater, container, false)
        return bindingSearch?.root
    }

    override fun setViewModel() {
        searchViewModel = ViewModelProvider(this@FragmentSearch).get(ProductViewModel::class.java)
    }

    override fun setObserve() {
        searchViewModel?.listResultSearch?.observe(viewLifecycleOwner, {
            if(it?.size != 0) {
                searchNameAdapter?.setItems(it)
                bindingSearch?.ivBird?.gone()
                bindingSearch?.progressBarSearch?.gone()
            }else{
                listName.clear()
                searchNameAdapter?.notifyDataSetChanged()
                bindingSearch?.ivBird?.visible()
                bindingSearch?.progressBarSearch?.gone()
            }
        })
    }


    override fun setUI() {
        if(searchViewModel?.keyword.isNullOrEmpty()) {
            MainActivity.itemSearch?.expandActionView() //open searchSearchView
            flag = 0
        }else{
            MainActivity.itemSearch?.expandActionView() //open searchSearchView
            MainActivity.searchView?.get()?.setQuery(searchViewModel?.keyword, false)
        }
        MainActivity.searchView?.get()?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
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
                countdown?.cancel()
                bindingSearch?.progressBarSearch?.visible()
                bindingSearch?.ivBird?.gone()
                countdown = object : CountDownTimer(waitingTime.toLong(), 500) {
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {
                        if(newText.isNullOrBlank()){
                            listName.clear()
                            searchNameAdapter?.notifyDataSetChanged()
                            bindingSearch?.progressBarSearch?.gone()
                            bindingSearch?.ivBird?.visible()
                        }else searchViewModel?.searchName(newText)
                    }
                }
                (countdown as CountDownTimer).start()
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
            searchViewModel?.keyword = MainActivity.searchView?.get()?.query.toString()
        }

    }
    private fun initRecyclerView(){
        searchNameAdapter = SearchAdapter(listName)
        bindingSearch?.rvSearchName?.adapter = searchNameAdapter
        bindingSearch?.rvSearchName?.layoutManager = LinearLayoutManager(context)
    }

}