package com.example.phonestore.services

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.phonestore.databinding.SlideItemContainerBinding
import com.example.phonestore.model.SliderItem

class SlideAdapter(var sliderItem: ArrayList<SliderItem>, var viewPager2: ViewPager2): RecyclerView.Adapter<SlideAdapter.SliderViewHolder>() {
    private var list: ArrayList<SliderItem> = arrayListOf()
    private val runnable = Runnable {
        list.addAll(sliderItem)
//        sliderItem.clear()
//        notifyItemRangeRemoved(0, list.size)
        sliderItem.addAll(list)
        notifyDataSetChanged()

    }
    class SliderViewHolder(var bindingImage: SlideItemContainerBinding): RecyclerView.ViewHolder(bindingImage.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(SlideItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
            holder.bindingImage.imageSlide.setImageResource(sliderItem[position].image)
            if (position == sliderItem.size - 2) {
                viewPager2.post(runnable)
            }
    }

    override fun getItemCount(): Int {
        return sliderItem.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}