package com.example.phonestore.services.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.extendsion.ratingBar
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.R
import com.example.phonestore.databinding.*
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.strikeThrough
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.model.Supplier
import com.example.phonestore.services.Constant
import com.example.phonestore.view.productDetail.FragmentDetailProduct


class ProductAdapter<E>(var listProduct: ArrayList<E>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemSupplierClick:((Int)->Unit)? =null
    fun setItems(list: ArrayList<E>) {
            val currentSize: Int? = listProduct?.size
            listProduct?.clear()
            listProduct?.addAll(list)
            if (currentSize != null) {
                notifyItemRangeRemoved(0, currentSize)
            }
            listProduct?.size?.let { notifyItemRangeInserted(0, it) }
    }
    class HotSaleViewHolder(val bindingHotSale: ItemHotSaleBinding): RecyclerView.ViewHolder(
            bindingHotSale.root
    )
    class LogoSupplierViewHolder(val bindingSupplier: ItemLogoSupplierBinding): RecyclerView.ViewHolder(
            bindingSupplier.root
    )
    class CateProductViewHolder(val bindingCate: ItemProductBinding): RecyclerView.ViewHolder(
            bindingCate.root
    )

    class ShimmerRecommendViewHolder(val bindingShimmer: ItemShimmerRecomendProductBinding): RecyclerView.ViewHolder(bindingShimmer.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            Constant.VIEW_HOTSALE_PRODUCT -> HotSaleViewHolder(
                    ItemHotSaleBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                    )
            )
            Constant.VIEW_LOGO_SUPPLIER -> LogoSupplierViewHolder(
                    ItemLogoSupplierBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                    )
            )
            Constant.VIEW_SHIMMER -> ShimmerRecommendViewHolder(
                    ItemShimmerRecomendProductBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                    )
            )
            else -> HotSaleViewHolder(
                    ItemHotSaleBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                    )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listProduct?.get(position)
        if(holder is HotSaleViewHolder && item is ProductInfo) {
            holder.bindingHotSale.tvOldPriceProduct.text = item.price.toVND()
            holder.bindingHotSale.tvOldPriceProduct.paintFlags = holder.bindingHotSale.tvOldPriceProduct.strikeThrough()
            holder.bindingHotSale.tvPriceProduct.text = (item.price - (item.price * item.discount)).toInt().toVND()
            holder.bindingHotSale.tvTotalJudge.text = "${item.totalJudge} đánh giá"
            holder.bindingHotSale.ratingBarProduct.rating = item.totalVote?: 0f
            holder.bindingHotSale.tvNumberDiscount.text = "-${(item.discount*100).toInt()}%"
            setImg(item.img, holder.bindingHotSale.ivProduct, holder.itemView.context)
            holder.bindingHotSale.ivProduct.setOnClickListener {
//                onItemClick?.invoke(item.idCate)
                FragmentDetailProduct.actionToFragmentDetail(it, bundleOf("product" to item))
            }
        }
        if(holder is LogoSupplierViewHolder && item is Supplier) {

            setImg(item.logoSupplier, holder.bindingSupplier.ivLogo, holder.itemView.context)
            holder.bindingSupplier.ivLogo.setOnClickListener {
                onItemSupplierClick?.invoke(item.id)
            }
        }

        if(holder is ShimmerRecommendViewHolder){
            Handler(Looper.getMainLooper()).postDelayed({
                holder.bindingShimmer.shimmerItemRecommend.gone()
            }, 5000)

        }

    }

    override fun getItemCount(): Int {
        return listProduct?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
//        return if(position == listProduct?.size?.minus(1) ?: 0 &&listProduct?.get(position) is CateProductInfo) {
//            Constant.VIEW_SHIMMER
//        }else{
            return when (listProduct?.get(0)) {
                is ProductInfo -> Constant.VIEW_HOTSALE_PRODUCT
                is Supplier -> Constant.VIEW_LOGO_SUPPLIER
                else -> Constant.VIEW_HOTSALE_PRODUCT
            }
        //}
    }
    private fun setImg(img: String?, v: ImageView, context: Context){
        Glide.with(context)
                .load(img)
                .error(R.drawable.noimage)
                .into(v)
    }
}