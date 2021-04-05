package com.example.phonestore.services

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.Extension.gone
import com.example.phonestore.Extension.ratingBar
import com.example.phonestore.Extension.strikeThrough
import com.example.phonestore.Extension.toVND
import com.example.phonestore.R
import com.example.phonestore.databinding.*
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.model.Supplier
import com.example.phonestore.view.FragmentDetailProduct


class ProductAdapter<E>(var listProduct: ArrayList<E>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick:((Int)->Unit)? =null
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
    class ItemSearchNameViewHolder(val bindingSearch: ItemSearchNameBinding): RecyclerView.ViewHolder(
            bindingSearch.root
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
            Constant.VIEW_CATEPRODUCT -> CateProductViewHolder(
                    ItemProductBinding.inflate(
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
            Constant.VIEW_SEARCH_NAME -> ItemSearchNameViewHolder(
                    ItemSearchNameBinding.inflate(
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
            holder.bindingHotSale.tvOldPriceProduct.text = item.priceOld.toVND()
            holder.bindingHotSale.tvOldPriceProduct.paintFlags = holder.bindingHotSale.tvOldPriceProduct.strikeThrough()
            holder.bindingHotSale.tvPriceProduct.text = item.priceNew.toVND()
            setImg(item.img, holder.bindingHotSale.ivProduct, holder.itemView.context)
            holder.bindingHotSale.ivProduct.setOnClickListener {
//                onItemClick?.invoke(item.idCate)
                FragmentDetailProduct.actionToFragmentDetail(it, bundleOf("idCate" to item.idCate))
            }
        }
        if(holder is LogoSupplierViewHolder && item is Supplier) {
            setImg(item.logoSupplier, holder.bindingSupplier.ivLogo, holder.itemView.context)
        }
        if(holder is CateProductViewHolder && item is CateProductInfo) {
            holder.bindingCate.tvNameProductCate.text = item.name
            holder.bindingCate.tvPriceProductCate.text = item.priceNew.toVND()
            holder.bindingCate.tvOldPriceProductCate.text = item.priceOld.toVND()
            holder.bindingCate.tvOldPriceProductCate.paintFlags = holder.bindingCate.tvOldPriceProductCate.strikeThrough()
            holder.bindingCate.ratingBarProduct.rating = item.vote?.ratingBar() ?: 0.1f
            setImg(item.img, holder.bindingCate.ivProductCate, holder.itemView.context)

            holder.bindingCate.ivProductCate.setOnClickListener {
                it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("idCate" to item.id))
            }
        }
        if(holder is ShimmerRecommendViewHolder){
            Handler(Looper.getMainLooper()).postDelayed({
                holder.bindingShimmer.shimmerItemRecomend.gone()
            }, 5000)

        }

    }

    override fun getItemCount(): Int {
        return listProduct?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(listProduct?.get(position) == listProduct?.size?.minus(1)?.let { listProduct?.get(it)} &&listProduct?.get(position) is CateProductInfo) {
            Constant.VIEW_SHIMMER
        }else{
            return when (listProduct?.get(0)) {
                is ProductInfo -> Constant.VIEW_HOTSALE_PRODUCT
                is Supplier -> Constant.VIEW_LOGO_SUPPLIER
                is CateProductInfo -> Constant.VIEW_CATEPRODUCT
                is String -> Constant.VIEW_SEARCH_NAME
                else -> Constant.VIEW_HOTSALE_PRODUCT
            }
        }
    }
    private fun setImg(img: String?, v: ImageView, context: Context){
        Glide.with(context)
                .load(img)
                .error(R.drawable.noimage)
                .into(v)
    }
}