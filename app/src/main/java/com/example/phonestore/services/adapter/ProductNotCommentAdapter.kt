package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemReviewProductBinding
import com.example.phonestore.model.ProductInfo

class ProductNotCommentAdapter():
    RecyclerView.Adapter<ProductNotCommentAdapter.ItemProductReViewViewHolder>() {
    var list: ArrayList<ProductInfo?>? = arrayListOf()
    var itemClick: ((Int?)->Unit)? = null
    fun setItems(listItem: ArrayList<ProductInfo?>?) {
        val currentSize: Int = list?.size?:0
        list?.clear()
        if (listItem != null) {
            list?.addAll(listItem)
        }

        notifyItemRangeRemoved(0, currentSize)
        list?.size?.let { notifyItemRangeInserted(0, it) }
    }
    class ItemProductReViewViewHolder(val binding: ItemReviewProductBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductReViewViewHolder {
        return ItemProductReViewViewHolder(ItemReviewProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemProductReViewViewHolder, position: Int) {
        val item = list?.get(position)
        holder.binding.tvName.text = "${item?.name} ${item?.storage}"
        holder.binding.tvColorAndStorage.text = "Khả năng lưu trữ: ${item?.storage}, nhóm màu: ${item?.color}"
        holder.binding.cbReview.setOnClickListener {
            itemClick?.invoke(item?.id)
        }
        Glide.with(holder.itemView.context)
            .load(item?.img)
            .error(R.drawable.noimage)
            .into(holder.binding.ivProduct)
    }

    override fun getItemCount(): Int {
        return list?.size ?:0
    }
}