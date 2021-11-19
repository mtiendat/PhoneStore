package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemSearchNameBinding
import com.example.phonestore.model.ProductInfo

class SearchAdapter<E>(var listProduct: ArrayList<E?>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onSetFlag:(()->Unit)? = null
    fun setItems(list: ArrayList<E?>?) {
        val currentSize: Int? = listProduct?.size
        listProduct?.clear()
        if (list != null) {
            listProduct?.addAll(list)
        }
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
        if(holder is ItemSearchNameViewHolder && item is ProductInfo){
            holder.bindingSearch.btnSearchName.text = item.name
            holder.bindingSearch.btnSearchName.setOnClickListener {
                onSetFlag?.invoke()
                it.findNavController().navigate(R.id.action_fragmentSearch_to_fragmentDetailProduct, bundleOf("product" to item))
            }
        }
    }

    override fun getItemCount(): Int {
        return listProduct?.size ?:0
    }
}