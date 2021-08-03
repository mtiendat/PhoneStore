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
import com.example.phonestore.extendsion.strikeThrough
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.services.Constant

class FeaturedProductAdapter(var listProduct: ArrayList<ProductInfo?>? = arrayListOf()): RecyclerView.Adapter<FeaturedProductAdapter.CateProductViewHolder>() {
    fun setItems(list: ArrayList<ProductInfo?>?) {
        val currentSize: Int? = listProduct?.size
        listProduct?.clear()
        if (list != null) {
            listProduct?.addAll(list)
        }
        if (currentSize != null) {
            notifyItemRangeRemoved(0, currentSize)
        }
        listProduct?.size?.let { notifyItemRangeInserted(0, it) }
    }
    class CateProductViewHolder(val bindingCate: ItemProductBinding): RecyclerView.ViewHolder(
        bindingCate.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedProductAdapter.CateProductViewHolder {
        return CateProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CateProductViewHolder, position: Int) {
        val item = listProduct?.get(position)
        holder.bindingCate.tvNameProductCate.text = item?.name
        holder.bindingCate.tvPriceProductCate.text = (item?.price?.minus((item.price * item.discount)))?.toInt().toVND()
        holder.bindingCate.tvOldPriceProductCate.text = item?.price.toVND()
        holder.bindingCate.tvOldPriceProductCate.paintFlags = holder.bindingCate.tvOldPriceProductCate.strikeThrough()
        holder.bindingCate.ratingBarProduct.rating = if(item?.totalJudge?:0f>0)(item?.totalVote?.div(item.totalJudge))?:0f else 0f
        holder.bindingCate.btnNumberDiscount.text = "-${item?.discount?.times(100)?.toInt()}%"
        holder.bindingCate.tvTotalJudge.text = "${item?.totalJudge?.toInt()} đánh giá"
        setImg(item?.img, holder.bindingCate.ivProductCate, holder.itemView.context)
        holder.bindingCate.ivProductCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("product" to item))
        }
        holder.bindingCate.tvNameProductCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("product" to item))
        }
    }

    override fun getItemCount(): Int {
       return listProduct?.size ?:0
    }
    private fun setImg(img: String?, v: ImageView, context: Context){
        Glide.with(context)
            .load(img)
            .error(R.drawable.noimage)
            .into(v)
    }
}