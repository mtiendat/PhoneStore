package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemRelatedProductBinding
import com.example.phonestore.extendsion.ratingBar
import com.example.phonestore.extendsion.strikeThrough
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.ProductInfo

class RelatedProductAdapter(val listRelatedProduct: ArrayList<ProductInfo?>?):
    RecyclerView.Adapter<RelatedProductAdapter.ItemRelatedProductViewHolder>() {
    fun setItems(listItem: ArrayList<ProductInfo?>) {
        val currentSize: Int? = listRelatedProduct?.size
        listRelatedProduct?.clear()
        listRelatedProduct?.addAll(listItem)

        if (currentSize != null) {
            notifyItemRangeRemoved(0, currentSize)
        }
        listRelatedProduct?.size?.let { notifyItemRangeInserted(0, it) }
    }
    class ItemRelatedProductViewHolder(val bindingRelated: ItemRelatedProductBinding): RecyclerView.ViewHolder(bindingRelated.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemRelatedProductViewHolder {
        return ItemRelatedProductViewHolder(ItemRelatedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemRelatedProductViewHolder, position: Int) {
        val item = listRelatedProduct?.get(position)
        holder.bindingRelated.tvNameRelatedProduct.text = "${item?.name} ${item?.storage}"
        holder.bindingRelated.tvPriceRelatedProduct.text =  (item?.price?.minus((item.price * item.discount)))?.toInt().toVND()
        holder.bindingRelated.tvPriceOldRelatedProduct.text = item?.price.toVND()
        holder.bindingRelated.tvPriceOldRelatedProduct.paintFlags = holder.bindingRelated.tvPriceOldRelatedProduct.strikeThrough()
        holder.bindingRelated.ratingBarRelatedProduct.rating = if(item?.totalJudge?:0f>0)(item?.totalVote?.div(item.totalJudge))?:0f else 0f
        holder.bindingRelated.btnDiscount.text = "-${item?.discount?.times(100)?.toInt()}%"
        holder.bindingRelated.ivRelatedProduct.setOnClickListener {
                it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("product" to item))
        }
        holder.bindingRelated.ivRelatedProduct.setOnClickListener {
                it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("product" to item))
        }
        Glide.with(holder.itemView.context)
                .load(item?.img)
                .error(R.drawable.noimage)
                .into(holder.bindingRelated.ivRelatedProduct)

    }

    override fun getItemCount(): Int {
        return listRelatedProduct?.size ?:0
    }
}