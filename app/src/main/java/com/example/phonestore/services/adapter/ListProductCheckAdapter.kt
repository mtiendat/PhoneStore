package com.example.phonestore.services.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemStringBinding

class ListProductCheckAdapter():
    RecyclerView.Adapter<ListProductCheckAdapter.ListProductCheckViewHolder>() {
    var list: ArrayList<String>? = arrayListOf()
    class ListProductCheckViewHolder(val binding: ItemStringBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListProductCheckViewHolder {
        return ListProductCheckViewHolder(ItemStringBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListProductCheckViewHolder, position: Int) {
        val item = list?.get(position)
        if(item?.contains("Còn hàng") == true){
            holder.binding.tvCheckProduct.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        }else holder.binding.tvCheckProduct.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        holder.binding.tvCheckProduct.text = item
    }

    override fun getItemCount(): Int {
        return list?.size ?:0
    }
}