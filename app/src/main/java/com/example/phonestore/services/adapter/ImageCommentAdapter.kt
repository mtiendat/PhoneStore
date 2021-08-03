package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemImageCommentBinding
import com.example.phonestore.model.ProductInfo

class ImageCommentAdapter(var listPath: ArrayList<String>? = arrayListOf()): RecyclerView.Adapter<ImageCommentAdapter.ItemImageViewViewHolder>() {
    class ItemImageViewViewHolder(val binding: ItemImageCommentBinding): RecyclerView.ViewHolder(binding.root)
    fun setItems(listItem: ArrayList<String>) {
        val currentSize: Int? = listPath?.size
        listPath?.clear()
        listPath?.addAll(listItem)

        if (currentSize != null) {
            notifyItemRangeRemoved(0, currentSize)
        }
        listPath?.size?.let { notifyItemRangeInserted(0, it) }
    }
    var itemClick: ((Int)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageViewViewHolder {
        return ItemImageViewViewHolder(ItemImageCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemImageViewViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(listPath?.get(position))
            .error(R.drawable.noimage)
            .into(holder.binding.ivComment)
        holder.binding.ivComment.setOnClickListener {
            itemClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return listPath?.size?:0
    }
}