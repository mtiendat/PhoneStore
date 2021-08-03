package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.SlideItemContainerBinding

class SlideshowProductAdapter(private var sliderItem: ArrayList<String?>?, private var viewPager2: ViewPager2?): RecyclerView.Adapter<SlideshowProductAdapter.SliderViewHolder>() {
    private var list: ArrayList<String?>? = arrayListOf()
    private val runnable = Runnable {
        sliderItem?.let { list?.addAll(it) }
//        sliderItem.clear()
//        notifyItemRangeRemoved(0, list.size)
        list?.let { sliderItem?.addAll(it) }
        notifyDataSetChanged()

    }
    class SliderViewHolder(var bindingImage: SlideItemContainerBinding): RecyclerView.ViewHolder(
        bindingImage.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            SlideItemContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = sliderItem?.get(position)
        Glide.with(holder.itemView.context)
            .load(item)
            .error(R.drawable.noimage)
            .into(holder.bindingImage.imageSlide)
        if (position == sliderItem?.size?.minus(2)) {
            viewPager2?.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return sliderItem?.size ?:0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}