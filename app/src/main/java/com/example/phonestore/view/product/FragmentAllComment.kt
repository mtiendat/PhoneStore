package com.example.phonestore.view.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentAllCommentBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.services.adapter.CommentAdapter
import com.example.phonestore.viewmodel.DetailProductViewModel


class FragmentAllComment: BaseFragment() {
    private var bindingAllVote: FragmentAllCommentBinding? = null
    private var viewModel: DetailProductViewModel? = null
    private var adapter: CommentAdapter? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingAllVote = FragmentAllCommentBinding.inflate(inflater, container, false)
        return bindingAllVote?.root
    }

    override fun setUI() {
        val idProduct = arguments?.getInt("idProduct")
        initRecyclerView()
        viewModel?.getListComment(idProduct)

    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this@FragmentAllComment).get(DetailProductViewModel::class.java)
    }
    override fun setObserve() {
        viewModel?.listComment?.observe(viewLifecycleOwner, {
            adapter?.setItems(it)
            bindingAllVote?.pbAllVote?.gone()
        })
    }
    private fun initRecyclerView(){
        adapter = CommentAdapter()
        adapter?.intoReply = {
            findNavController().navigate(R.id.action_fragmentAllCommment_to_fragmentReply, bundleOf("comment" to it))
        }
        adapter?.likeComment = { id, state ->
            if(state) viewModel?.postLike(id) else viewModel?.deleteLike(id)
        }
        bindingAllVote?.rvAllVote?.adapter = adapter
        bindingAllVote?.rvAllVote?.layoutManager = LinearLayoutManager(context)
    }
}