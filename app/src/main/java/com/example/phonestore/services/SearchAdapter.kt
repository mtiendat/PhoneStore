package com.example.phonestore.services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemSearchNameBinding
import com.example.phonestore.model.CateProductInfo

class SearchAdapter<E>(var listProduct: ArrayList<E>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    fun setItems(list: ArrayList<E>) {
        val currentSize: Int? = listProduct?.size
        listProduct?.clear()
        listProduct?.addAll(list)
        if (currentSize != null) {
            notifyItemRangeRemoved(0, currentSize)
        }
        listProduct?.size?.let { notifyItemRangeInserted(0, it) }
    }
    class ItemSearchNameViewHolder(val bindingSearch: ItemSearchNameBinding): RecyclerView.ViewHolder(
            bindingSearch.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  ItemSearchNameViewHolder(ItemSearchNameBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listProduct?.get(position)
        if(holder is ItemSearchNameViewHolder && item is CateProductInfo){
            holder.bindingSearch.btnSearchName.text = item.name
            holder.bindingSearch.btnSearchName.setOnClickListener {
                it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("idCate" to item.id, "name" to item.name))
            }
        }
    }

    override fun getItemCount(): Int {
        return listProduct?.size ?:0
    }
}