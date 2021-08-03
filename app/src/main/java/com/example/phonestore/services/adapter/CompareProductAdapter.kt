package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemProductCompareBinding
import com.example.phonestore.databinding.ItemRelatedProductBinding
import com.example.phonestore.extendsion.strikeThrough
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.model.ProductInfo

class CompareProductAdapter(val listCompareProduct: ArrayList<ProductInfo?>?):
    RecyclerView.Adapter<CompareProductAdapter.ItemCompareViewHolder>() {
    var clickCompare: ((ProductInfo?)->Unit)? = null
    fun setItems(listItem: ArrayList<ProductInfo?>) {
        val currentSize: Int? = listCompareProduct?.size
        listCompareProduct?.clear()
        listCompareProduct?.addAll(listItem)

        if (currentSize != null) {
            notifyItemRangeRemoved(0, currentSize)
        }
        listCompareProduct?.size?.let { notifyItemRangeInserted(0, it) }
    }
    class ItemCompareViewHolder(val bindingCompare: ItemProductCompareBinding): RecyclerView.ViewHolder(bindingCompare.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemCompareViewHolder {
        return ItemCompareViewHolder(ItemProductCompareBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemCompareViewHolder, position: Int) {
        val item = listCompareProduct?.get(position)
        holder.bindingCompare.tvNameProductCate.text = item?.name
        holder.bindingCompare.tvPriceProductCate.text =  (item?.price?.minus((item.price * item.discount)))?.toInt().toVND()
        holder.bindingCompare.tvOldPriceProductCate.text = item?.price.toVND()
        holder.bindingCompare.tvOldPriceProductCate.paintFlags = holder.bindingCompare.tvOldPriceProductCate.strikeThrough()
        holder.bindingCompare.tvTotalJudge.text = "${item?.totalJudge?.toInt()} đánh giá"
        holder.bindingCompare.ratingBarProduct.rating =if(item?.totalJudge?:0f>0f)(item?.totalVote?.div(item.totalJudge))?.toFloat()?:0f else 0f
        holder.bindingCompare.btnNumberDiscount.text = "-${item?.discount?.times(100)?.toInt()}%"
        holder.bindingCompare.ivProductCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("product" to item))
        }
        holder.bindingCompare.ivProductCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("product" to item))
        }
        holder.bindingCompare.tvCompareWith.setOnClickListener {
            clickCompare?.invoke(item)
        }
        Glide.with(holder.itemView.context)
            .load(item?.img)
            .error(R.drawable.noimage)
            .into(holder.bindingCompare.ivProductCate)

    }

    override fun getItemCount(): Int {
        return listCompareProduct?.size ?:0
    }
}