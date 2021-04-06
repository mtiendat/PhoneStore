package com.example.phonestore.services

import android.icu.number.IntegerWidth
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.Extension.ratingBar
import com.example.phonestore.Extension.toVND
import com.example.phonestore.R
import com.example.phonestore.databinding.*
import com.example.phonestore.databinding.ActivityLoginBinding.inflate
import com.example.phonestore.model.*

class DetailProductAdapter<T>(var list: ArrayList<T>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var clickCheckBox: ((Int, Boolean, DetailCart, Int)->Unit)? = null
    var clickMaxMin: ((Int?, Boolean)->Unit)? = null
    var check: Boolean? = null
    var updateProductInList: ((Int?, Int)-> Unit)? = null
    var nextInfoOrder: ((Int, String?)-> Unit)? = null
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
    class ItemProductOrderViewHolder(val bindingProductOrder: ItemProductOrderBinding): RecyclerView.ViewHolder(bindingProductOrder.root)
    class ItemMyOrderViewHolder(val bingdingMyOrder: ItemOrderBinding): RecyclerView.ViewHolder(bingdingMyOrder.root)
    class ItemVoteViewHolder(val bingdingItemVote: ItemVoteCommentBinding):RecyclerView.ViewHolder(bingdingItemVote.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == Constant.VIEW_CATEPRODUCT){
            ItemRelatedProductViewHolder(ItemRelatedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else if(viewType == Constant.VIEW_MYCART){
            ItemProductInCartViewHolder(ItemProductInCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else if(viewType == Constant.VIEW_PRODUCT_ORDER) {
            ItemProductOrderViewHolder(ItemProductOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else if(viewType == Constant.VIEW_MY_ORDER) {
            ItemMyOrderViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else if(viewType == Constant.VIEW_VOTE) {
            ItemVoteViewHolder(
                ItemVoteCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }else ItemRelatedProductViewHolder(ItemRelatedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list?.get(position)
        if(holder is ItemRelatedProductViewHolder && item is CateProductInfo){
            holder.bindingRelated.tvPriceRelatedProduct.text = item.priceNew.toVND()
            holder.bindingRelated.ratingBarRelatedProduct.rating = item.vote?.ratingBar() ?: 0.1f
            holder.bindingRelated.ivRelatedProduct.setOnClickListener {
                it.findNavController().navigate(R.id.action_global_fragmentDetailProduct, bundleOf("idCate" to item.id, "name" to item.name))
            }
            Glide.with(holder.itemView.context)
                    .load(item.img)
                    .error(R.drawable.noimage)
                    .into(holder.bindingRelated.ivRelatedProduct)
        }
        if(holder is ItemProductInCartViewHolder && item is DetailCart){
            var qty: Int?
            val price: Int?  = item.price
            var total: Int

            holder.bindingProductInCart.tvProInCartName.text = item.nameProduct
            holder.bindingProductInCart.tvProInCartColor.text = item.color
            holder.bindingProductInCart.tvProInCartStorage.text = item.storage
            holder.bindingProductInCart.tvProInCartQty.text = item.qty.toString()
            qty =  holder.bindingProductInCart.tvProInCartQty.text.toString().toInt()
            holder.bindingProductInCart.cvMin.setOnClickListener {
                if(qty > 0) {
                    qty--
                    updateProductInList?.invoke(item.id, qty)
                    holder.bindingProductInCart.tvProInCartQty.text = qty.toString()
                    if(qty>=0&& check == true) {
                        clickMaxMin?.invoke(price, false)
                    }
                }
            }
            holder.bindingProductInCart.cvMax.setOnClickListener {
                if(qty < 2) {
                    qty++
                    updateProductInList?.invoke(item.id, qty)
                    holder.bindingProductInCart.tvProInCartQty.text = qty.toString()
                    if(check == true) {
                        clickMaxMin?.invoke(price, true)
                    }
                }else Toast.makeText(holder.itemView.context, "Bạn chỉ mua tối đa 2 sản phẩm", Toast.LENGTH_SHORT).show()
            }
            holder.bindingProductInCart.cbItemCart.setOnCheckedChangeListener { _, isChecked ->
                if (price != null) {
                    when(isChecked){
                        true -> {
                            total = price * qty
                            clickCheckBox?.invoke(total, true, item, qty)
                            check = true
                        }
                        false ->{
                            total = price * qty
                            clickCheckBox?.invoke(total, false, item, qty)
                            check = false
                        }
                    }

                }
            }
            Glide.with(holder.itemView.context)
                    .load(item.img)
                    .error(R.drawable.noimage)
                    .into(holder.bindingProductInCart.ivProInCart)
        }
        if(holder is ItemProductOrderViewHolder && item is ProductOrder){
            holder.bindingProductOrder.tvOrderNameProduct.text = item.product?.nameProduct
            holder.bindingProductOrder.tvOrderQty.text = item.qty.toString()
            holder.bindingProductOrder.tvOderColorProduct.text = item.product?.color
            holder.bindingProductOrder.tvOrderStorageProduct.text = item.product?.storage
            holder.bindingProductOrder.tvOrderPriceProduct.text = item.product?.price.toVND()
            Glide.with(holder.itemView.context)
                    .load(item.product?.img)
                    .error(R.drawable.noimage)
                    .into(holder.bindingProductOrder.ivOrderProduct)
        }
        if(holder is ItemMyOrderViewHolder && item is MyOrder){
            holder.bingdingMyOrder.tvOderID.text = item.id.toString()
            holder.bingdingMyOrder.tvOrderDate.text = item.date
            holder.bingdingMyOrder.tvOrderQtyProduct.text = item.qty.toString()
            holder.bingdingMyOrder.tvOrderTotal.text = item.totalMoney.toVND()
            checkStateOrder(item.state.toString(), holder.bingdingMyOrder.btnStateOrder)
            holder.bingdingMyOrder.cvMyOrder.setOnClickListener {
                nextInfoOrder?.invoke(item.id, item.state)
            }
        }
        if(holder is ItemVoteViewHolder && item is Vote){
            holder.bingdingItemVote.tvNameVote.text = item.name
            holder.bingdingItemVote.tvContentVote.text = item.content
            holder.bingdingItemVote.rbDetailVote.rating = item.vote.toFloat()
            Glide.with(holder.itemView.context)
                .load(item.avatarUser)
                .into(holder.bingdingItemVote.ivAvatarComment)

        }
    }

    override fun getItemCount(): Int {
        return list?.size ?:0
    }

    override fun getItemViewType(position: Int): Int {
        return when(list?.get(0)){
            is CateProductInfo -> Constant.VIEW_CATEPRODUCT
            is DetailCart -> Constant.VIEW_MYCART
            is ProductOrder -> Constant.VIEW_PRODUCT_ORDER
            is MyOrder -> Constant.VIEW_MY_ORDER
            is Vote -> Constant.VIEW_VOTE
            else -> 1
        }
    }
    private fun checkStateOrder(s: String, btn: Button){
        when(s){
            Constant.RECEIVED ->{
                btn.setBackgroundResource(R.drawable.custom_state_order_received)
                btn.text = s
            }
            Constant.TRANSPORTED ->{
                btn.setBackgroundResource(R.drawable.custom_state_order_transported)
                btn.text = s
            }
            Constant.DELIVERED ->{
                btn.setBackgroundResource(R.drawable.custom_state_order_delivered)
                btn.text = s
            }
            Constant.CANCEL ->{
                btn.setBackgroundResource(R.drawable.custom_state_order_cancel)
                btn.text = s
            }
        }
    }
}