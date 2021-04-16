package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.extendsion.gone
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentAllVoteBinding
import com.example.phonestore.model.Vote
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.viewmodel.DetailProductViewModel
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable

class FragmentAllVote: BaseFragment() {
    private var bindingAllVote: FragmentAllVoteBinding? = null
    private var allVoteViewModel: DetailProductViewModel? = null
    private var adapter: DetailProductAdapter<Vote>? = null
    private var listAllVote: ArrayList<Vote>? = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingAllVote = FragmentAllVoteBinding.inflate(inflater, container, false)
        return bindingAllVote?.root
    }

    override fun setUI() {
        bindingAllVote?.pbAllVote?.setIndeterminateDrawableTiled(
                FoldingCirclesDrawable.Builder(context).colors(resources.getIntArray(
                        R.array.google_colors)).build())
        initRecyclerView()
        val idCate = arguments?.getInt("idCate")
        allVoteViewModel?.getListVote(idCate, true)

    }
    override fun setViewModel() {
        allVoteViewModel = ViewModelProvider(this@FragmentAllVote).get(DetailProductViewModel::class.java)
    }
    override fun setObserve() {
        val listAllVoteObserve = Observer<ArrayList<Vote>>{
            listAllVote?.addAll(it)
            adapter?.setItems(it)
            bindingAllVote?.pbAllVote?.gone()
        }
        allVoteViewModel?.listVote?.observe(viewLifecycleOwner, listAllVoteObserve)
    }
    private fun initRecyclerView(){
        adapter = DetailProductAdapter(listAllVote)
        bindingAllVote?.rvAllVote?.adapter = adapter
        bindingAllVote?.rvAllVote?.layoutManager = LinearLayoutManager(context)
    }
}