package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.SlideItemContainerBinding
import com.example.phonestore.model.Slideshow



class SlideshowAdapter(private var sliderItem: ArrayList<Slideshow>?, private var viewPager2: ViewPager2?): RecyclerView.Adapter<SlideshowAdapter.SliderViewHolder>() {
    private var list: ArrayList<Slideshow>? = arrayListOf()
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

        holder.bindingImage.imageSlide.setOnClickListener {
            //nextChrome(holder, item)
        }
        Glide.with(holder.itemView.context)
            .load(item?.img)
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
    private fun nextChrome(holder: SliderViewHolder, slide : Slideshow?){
        val builder = CustomTabsIntent.Builder()
        val params = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
            .build()
        builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
        builder.setDefaultColorSchemeParams(params)
        builder.setStartAnimations(holder.itemView.context,R.anim.slide_in_right,R.anim.slide_in_left)
        builder.setExitAnimations(holder.itemView.context,R.anim.slide_in_right,R.anim.slide_in_left)
        val custom = builder.build()
        slide?.url?.toUri()?.let { custom.launchUrl(holder.itemView.context, it) }
    }
}