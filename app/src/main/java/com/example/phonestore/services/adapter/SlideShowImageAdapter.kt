package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.SlideImageProductItemBinding

class SlideShowImageAdapter(private var sliderItem: ArrayList<String>?): RecyclerView.Adapter<SlideShowImageAdapter.ItemImageVieHolder>() {
    class ItemImageVieHolder(val binding: SlideImageProductItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageVieHolder {
        return ItemImageVieHolder(SlideImageProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemImageVieHolder, position: Int) {
        val item = sliderItem?.get(position)
        Glide.with(holder.itemView.context)
            .load(item)
            .error(R.drawable.noimage)
            .into(holder.binding.ivProduct)
    }

    override fun getItemCount(): Int {
       return sliderItem?.size ?:0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}