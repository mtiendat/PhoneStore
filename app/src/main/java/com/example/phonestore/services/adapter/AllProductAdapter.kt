package com.example.phonestore.services.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemProductBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.strikeThrough
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.model.ProductInfo
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class AllProductAdapter(var listProduct: ArrayList<ProductInfo?>? = arrayListOf(), var click: (()-> Unit)?= null): RecyclerView.Adapter<AllProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(val binding: ItemProductBinding): RecyclerView.ViewHolder(
        binding.root
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = listProduct?.get(position)
        holder.binding.tvNameProductCate.text = item?.name
        holder.binding.tvPriceProductCate.text = (item?.price?.minus((item.price * item.discount)))?.toInt().toVND()
        holder.binding.tvOldPriceProductCate.text = item?.price.toVND()
        holder.binding.tvOldPriceProductCate.paintFlags = holder.binding.tvOldPriceProductCate.strikeThrough()
        holder.binding.ratingBarProduct.rating =if(item?.totalJudge?:0f > 0f)(item?.totalVote?.div(item.totalJudge))?:0f else 0f
        holder.binding.btnNumberDiscount.text = if(item?.discount?.times(100)?.toInt() == 0){
            "New"
        }else "-${item?.discount?.times(100)?.toInt()}%"
        holder.binding.tvTotalJudge.text = "${item?.totalJudge?.toInt()} đánh giá"
        if(holder.binding.tvPriceProductCate.text == holder.binding.tvOldPriceProductCate.text){
            holder.binding.tvOldPriceProductCate.text = ""
        }
        setImg(item?.img, holder.binding.ivProductCate, holder.itemView.context)
        holder.binding.ivProductCate.setOnClickListener {
            click?.invoke()
            it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("product" to item))
        }
        holder.binding.tvNameProductCate.setOnClickListener {
            click?.invoke()
            it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("product" to item))
        }
    }

    override fun getItemCount(): Int {
        return listProduct?.size ?:0
    }
    private fun setImg(img: String?, v: ImageView, context: Context){
        val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.7f) //the alpha of the underlying children
            .setHighlightAlpha(0.3f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

// This is the placeholder for the imageView
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        Glide.with(context)
            .load(img)
            .placeholder(shimmerDrawable)
            .error(R.drawable.noimage)
            .into(v)
    }
}