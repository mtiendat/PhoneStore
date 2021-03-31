package com.example.phonestore.services

import android.icu.number.IntegerWidth
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.Extension.ratingBar
import com.example.phonestore.Extension.toVND
import com.example.phonestore.R
import com.example.phonestore.databinding.ActivityLoginBinding.inflate
import com.example.phonestore.databinding.ItemProductInCartBinding
import com.example.phonestore.databinding.ItemRelatedProductBinding
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.DetailCart

class DetailProductAdapter<T>(var list: ArrayList<T>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var clickCheckBox: ((Int, Boolean)->Unit)? = null
    var clickMaxMin: ((Int?, Boolean)->Unit)? = null
    var updateProductInList: ((Int?, Int)-> Unit)? = null
    fun setItems(listItem: ArrayList<T>) {
        val currentSize: Int? = list?.size
        list?.clear()
        list?.addAll(listItem)

        if (currentSize != null) {
            notifyItemRangeRemoved(0, currentSize)
        }
        list?.size?.let { notifyItemRangeInserted(0, it) }
    }
    class ItemRelatedProductViewHolder(val bindingRelated: ItemRelatedProductBinding): RecyclerView.ViewHolder(bindingRelated.root)
    class ItemProductInCartViewHolder(val bindingProductInCart: ItemProductInCartBinding): RecyclerView.ViewHolder(bindingProductInCart.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == Constant.VIEW_CATEPRODUCT){
            ItemRelatedProductViewHolder(ItemRelatedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else if(viewType == Constant.VIEW_MYCART){
            ItemProductInCartViewHolder(ItemProductInCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else ItemRelatedProductViewHolder(ItemRelatedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = list?.get(position)
        if(holder is ItemRelatedProductViewHolder && product is CateProductInfo){
            holder.bindingRelated.tvPriceRelatedProduct.text = product.priceNew.toVND()
            holder.bindingRelated.ratingBarRelatedProduct.rating = product.vote?.ratingBar() ?: 0.1f
            Glide.with(holder.itemView.context)
                    .load(product.img)
                    .error(R.drawable.noimage)
                    .into(holder.bindingRelated.ivRelatedProduct)
        }
        if(holder is ItemProductInCartViewHolder && product is DetailCart){
            var qty: Int?
            val price: Int?  = product.price
            var total: Int
            var check: Boolean = false
            holder.bindingProductInCart.tvProInCartName.text = product.nameProduct
            holder.bindingProductInCart.tvProInCartColor.text = product.color
            holder.bindingProductInCart.tvProInCartStorage.text = product.storage
            holder.bindingProductInCart.tvProInCartQty.text = product.qty.toString()
            qty =  holder.bindingProductInCart.tvProInCartQty.text.toString().toInt()
            holder.bindingProductInCart.cvMin.setOnClickListener {
                if(qty > 0) {
                    qty--
                    updateProductInList?.invoke(product.id, qty)
                    holder.bindingProductInCart.tvProInCartQty.text = qty.toString()
                    if(qty>=0&&check) {
                        clickMaxMin?.invoke(price, false)
                    }
                }
            }
            holder.bindingProductInCart.cvMax.setOnClickListener {
                if(qty < 2) {
                    qty++
                    updateProductInList?.invoke(product.id, qty)
                    holder.bindingProductInCart.tvProInCartQty.text = qty.toString()
                    if(check) {
                        clickMaxMin?.invoke(price, true)
                    }
                }else Toast.makeText(holder.itemView.context, "Bạn chỉ mua tối đa 2 sản phẩm", Toast.LENGTH_SHORT).show()
            }
            holder.bindingProductInCart.cbItemCart.setOnCheckedChangeListener { _, isChecked ->
                if (price != null) {
                    when(isChecked){
                        true -> {
                            total = price * qty
                            clickCheckBox?.invoke(total, true)
                            check = true
                        }
                        false ->{
                            total = price * qty
                            clickCheckBox?.invoke(total, false)
                            check = false
                        }
                    }


                }
            }
            Glide.with(holder.itemView.context)
                    .load(product.img)
                    .error(R.drawable.noimage)
                    .into(holder.bindingProductInCart.ivProInCart)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?:0
    }

    override fun getItemViewType(position: Int): Int {
        return when(list?.get(0)){
            is CateProductInfo -> Constant.VIEW_CATEPRODUCT
            is DetailCart -> Constant.VIEW_MYCART
            else -> 1
        }
    }

}