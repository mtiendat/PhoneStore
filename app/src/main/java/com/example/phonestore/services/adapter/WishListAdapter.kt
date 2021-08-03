package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemProductCompareBinding
import com.example.phonestore.databinding.ItemProductWishlIstBinding
import com.example.phonestore.extendsion.strikeThrough
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.model.ProductInfo

class WishListAdapter():
    RecyclerView.Adapter<WishListAdapter.ItemWishListHolder>() {
    val listCompareProduct: ArrayList<ProductInfo> = arrayListOf()
    var clickCompare: ((ProductInfo?)->Unit)? = null
    fun setItems(listItem: ArrayList<ProductInfo>) {
        val currentSize: Int = listCompareProduct?.size
        listCompareProduct.clear()
        listCompareProduct.addAll(listItem)


            notifyItemRangeRemoved(0, currentSize)

        listCompareProduct.size.let { notifyItemRangeInserted(0, it) }
    }
    class ItemWishListHolder(val binding: ItemProductWishlIstBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemWishListHolder {
        return ItemWishListHolder(ItemProductWishlIstBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemWishListHolder, position: Int) {
        val item = listCompareProduct.get(position)
        holder.binding.tvNameProductCate.text = item.name
        holder.binding.tvPriceProductCate.text =  (item.price.minus((item.price * item.discount)))?.toInt().toVND()
        holder.binding.tvOldPriceProductCate.text = item.price.toVND()
        holder.binding.tvOldPriceProductCate.paintFlags = holder.binding.tvOldPriceProductCate.strikeThrough()
        holder.binding.tvTotalJudge.text = "${item?.totalJudge?.toInt()} đánh giá"
        holder.binding.ratingBarProduct.rating =if(item.totalJudge?:0f>0f)(item?.totalVote?.div(item.totalJudge))?.toFloat()?:0f else 0f
        holder.binding.btnNumberDiscount.text = "-${item.discount?.times(100)?.toInt()}%"
        holder.binding.ivProductCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentWishList_to_fragmentDetailProduct, bundleOf("product" to item))
        }
        holder.binding.ivProductCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentWishList_to_fragmentDetailProduct, bundleOf("product" to item))
        }

        Glide.with(holder.itemView.context)
            .load(item.img)
            .error(R.drawable.noimage)
            .into(holder.binding.ivProductCate)

    }

    override fun getItemCount(): Int {
        return listCompareProduct.size ?:0
    }
}