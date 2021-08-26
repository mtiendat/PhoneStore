package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemChooseProductBinding
import com.example.phonestore.databinding.ItemChooseStorageBinding

class ChooseProductAdapter(var listImage: List<String>?, val clickItem: ((String?)->Unit)? = null):
    RecyclerView.Adapter<ChooseProductAdapter.ItemChooseProductViewHolder>() {
    class ItemChooseProductViewHolder(val binding: ItemChooseProductBinding): RecyclerView.ViewHolder(binding.root)
    private var itemBindingPrevious: ItemChooseProductBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChooseProductViewHolder {
        return ItemChooseProductViewHolder(ItemChooseProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemChooseProductViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(listImage?.get(position))
            .error(R.drawable.noimage)
            .into(holder.binding.ivProduct)
        holder.binding.cbProduct.setOnClickListener {
            if(itemBindingPrevious!=null && itemBindingPrevious != holder.binding){
                itemBindingPrevious?.cbProduct?.isChecked = !itemBindingPrevious?.cbProduct?.isChecked!!
            }
            if(itemBindingPrevious!=holder.binding) itemBindingPrevious = holder.binding
            clickItem?.invoke(listImage?.get(position))
        }
        holder.binding.ivProduct.setOnClickListener {
            if(itemBindingPrevious!=null && itemBindingPrevious != holder.binding){
                itemBindingPrevious?.cbProduct?.isChecked = !itemBindingPrevious?.cbProduct?.isChecked!!
            }
            if(itemBindingPrevious!=holder.binding) itemBindingPrevious = holder.binding
            clickItem?.invoke(listImage?.get(position))
            holder.binding.cbProduct.isChecked = !holder.binding.cbProduct.isChecked
        }
    }

    override fun getItemCount(): Int {
        return  listImage?.size?:0
    }
}