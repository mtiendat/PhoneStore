package com.example.phonestore.services.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.*
import com.example.phonestore.extendsion.ratingBar
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.*
import com.example.phonestore.model.cart.Cart
import com.example.phonestore.model.cart.DetailCart
import com.example.phonestore.model.order.MyOrder
import com.example.phonestore.services.Constant


class DetailProductAdapter<T>(var list: ArrayList<T>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var clickCheckBox: ((Int, Boolean, Cart, Int)->Unit)? = null
    var clickMaxMin: ((Int?, Boolean)->Unit)? = null
    var updateProductInList: ((Cart?, Int)-> Unit)? = null
    var nextInfoOrder: ((Int, String?)-> Unit)? = null
    var updateNotification: ((Int?)-> Unit)? = null
    var updateItemCart: ((Int?, Boolean?)-> Unit)? = null
    var deleteItemCart: ((Int, Int)-> Unit)? = null
    fun setItems(listItem: ArrayList<T>) {
        val currentSize: Int? = list?.size
        list?.clear()
        list?.addAll(listItem)

        if (currentSize != null) {
            notifyItemRangeRemoved(0, currentSize)
        }
        list?.size?.let { notifyItemRangeInserted(0, it) }
    }

    class ItemProductInCartViewHolder(val bindingProductInCart: ItemProductInCartBinding): RecyclerView.ViewHolder(bindingProductInCart.root)
    class ItemProductOrderViewHolder(val bindingProductOrder: ItemProductOrderBinding): RecyclerView.ViewHolder(bindingProductOrder.root)
    class ItemMyOrderViewHolder(val bindingMyOrder: ItemOrderBinding): RecyclerView.ViewHolder(bindingMyOrder.root)

    class ItemNotificationViewHolder(val bindingItemNotification: ItemNotificationBinding): RecyclerView.ViewHolder(bindingItemNotification.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constant.VIEW_MYCART -> {
                ItemProductInCartViewHolder(ItemProductInCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            Constant.VIEW_PRODUCT_ORDER -> {
                ItemProductOrderViewHolder(ItemProductOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            Constant.VIEW_MY_ORDER -> {
                ItemMyOrderViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            Constant.NOTIFICATION_ID -> {
                ItemNotificationViewHolder(
                        ItemNotificationBinding.inflate(
                                LayoutInflater.from(parent.context),
                                parent,
                                false
                        )
                )
            }

            else -> {ItemProductInCartViewHolder(ItemProductInCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list?.get(position)
        var check: Boolean? = true

        if(holder is ItemProductInCartViewHolder && item is Cart){
            var qty: Int?
            val price: Int?  = item.price
            var total: Int
            holder.bindingProductInCart.tvProInCartName.text = item.name
            holder.bindingProductInCart.tvProInCartColor.text = item.color
            holder.bindingProductInCart.tvProInCartStorage.text = item.storage
            holder.bindingProductInCart.tvProInCartPrice.text = item.price.toVND()
            holder.bindingProductInCart.tvProInCartQty.text = item.qty.toString()
            qty =  holder.bindingProductInCart.tvProInCartQty.text.toString().toInt()
            holder.bindingProductInCart.cvMin.setOnClickListener {
                if(qty > 0) {
                    if(qty == 1){
                        deleteItemCart?.invoke(item.id!!, position)
                    }else {
                        updateItemCart?.invoke(item.id, false)
                        qty--
                        item.qty = qty
                        updateProductInList?.invoke(item, qty)
                        holder.bindingProductInCart.tvProInCartQty.text = qty.toString()
                        if(qty>=0&& check == true) {
                            clickMaxMin?.invoke(price, false)
                        }
                    }

                }
            }
            holder.bindingProductInCart.cvMax.setOnClickListener {
                if(qty < 2) {
                    updateItemCart?.invoke(item.id, true)
                    qty++
                    item.qty = qty
                    updateProductInList?.invoke(item, qty)
                    holder.bindingProductInCart.tvProInCartQty.text = qty.toString()
                    if(check == true) {
                        clickMaxMin?.invoke(price, true)
                    }
                }
            }

            holder.bindingProductInCart.cbItemCart.setOnCheckedChangeListener { _, isChecked ->
                if (price != null) {
                    when(isChecked){
                        true -> {
                            total = price * qty
                            clickCheckBox?.invoke(total, true, item, qty)
                            check = true
                        }
                        false -> {
                            total = price * qty
                            clickCheckBox?.invoke(total, false, item, qty)
                            check = false
                        }
                    }

                }
            }
            Glide.with(holder.itemView.context)
                    .load(item.avatar)
                    .error(R.drawable.noimage)
                    .into(holder.bindingProductInCart.ivProInCart)
        }
        if(holder is ItemProductOrderViewHolder && item is ProductOrder){
            holder.bindingProductOrder.tvOrderNameProduct.text = item.product?.name
            holder.bindingProductOrder.tvOrderQty.text = item.qty.toString()
            holder.bindingProductOrder.tvOderColorProduct.text = item.product?.color
            holder.bindingProductOrder.tvOrderStorageProduct.text = item.product?.storage
            holder.bindingProductOrder.tvOrderPriceProduct.text = item.product?.price.toVND()
            holder.bindingProductOrder.lnUnavailable.visibility = if(item.product?.isAvailable==true) View.GONE else  View.VISIBLE
            holder.bindingProductOrder.ivDelete.visibility = if(item.product?.isAvailable==true) View.GONE else  View.VISIBLE
            holder.bindingProductOrder.ivWarning.visibility = if(item.product?.isQtyAvailable==true) View.GONE else  View.VISIBLE
            holder.bindingProductOrder.ivDelete.setOnClickListener {
                deleteItemCart?.invoke(item.product?.id!!, position)
            }
            Glide.with(holder.itemView.context)
                    .load(item.product?.avatar)
                    .error(R.drawable.noimage)
                    .into(holder.bindingProductOrder.ivOrderProduct)
        }
        if(holder is ItemMyOrderViewHolder && item is MyOrder){
            holder.bindingMyOrder.tvOderID.text = item.id.toString()
            holder.bindingMyOrder.tvOrderDate.text = item.date
            holder.bindingMyOrder.tvOrderQtyProduct.text = item.qty.toString()
            holder.bindingMyOrder.tvOrderTotal.text = item.totalMoney.toVND()
            checkStateOrder(item.state.toString(), holder.bindingMyOrder.btnStateOrder, holder.bindingMyOrder.ivMark)
            holder.bindingMyOrder.cvMyOrder.setOnClickListener {
                nextInfoOrder?.invoke(item.id, item.state)
            }
        }

        if(holder is ItemNotificationViewHolder && item is Notification){
            holder.bindingItemNotification.tvNotiTitle.text = item.title
            holder.bindingItemNotification.tvNotiContent.text = item.content
            holder.bindingItemNotification.tvNotiTime.text = item.time
            holder.bindingItemNotification.cvNotification.setOnClickListener {
                holder.bindingItemNotification.apply {
                    tvNotiTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
                    tvNotiContent.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
                    tvNotiTime.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
                }
                holder.bindingItemNotification.cvNotification.isEnabled = true
                updateNotification?.invoke(item.id)
            }
            if(item.send == 1){
                holder.bindingItemNotification.apply {
                    tvNotiTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
                    tvNotiContent.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
                    tvNotiTime.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list?.size ?:0
    }

    override fun getItemViewType(position: Int): Int {
        return when(list?.get(0)){
            is Cart -> Constant.VIEW_MYCART
            is ProductOrder -> Constant.VIEW_PRODUCT_ORDER
            is MyOrder -> Constant.VIEW_MY_ORDER
            is Notification -> Constant.NOTIFICATION_ID
            else -> 1
        }
    }
    private fun checkStateOrder(s: String, btn: Button, iv: ImageView){
        when(s){
            Constant.RECEIVED -> {
                iv.setImageResource(R.drawable.vectore_triangle_blue)
                btn.setBackgroundResource(R.drawable.custom_state_order_received)
                btn.text = s
            }
            Constant.CONFIRMED -> {
                iv.setImageResource(R.drawable.vector_triangle_blue_navy)
                btn.setBackgroundResource(R.drawable.custom_state_order_confirmed)
                btn.text = s
            }
            Constant.TRANSPORTED -> {
                iv.setImageResource(R.drawable.vector_triangle_red)
                btn.setBackgroundResource(R.drawable.custom_state_order_transported)
                btn.text = s
            }
            Constant.DELIVERED -> {
                iv.setImageResource(R.drawable.vector_triangle_green)
                btn.setBackgroundResource(R.drawable.custom_state_order_delivered)
                btn.text = s
            }
            Constant.CANCEL -> {
                iv.setImageResource(R.drawable.vector_triangle_gray)
                btn.setBackgroundResource(R.drawable.custom_state_order_cancel)
                btn.text = s
            }
        }
    }

}