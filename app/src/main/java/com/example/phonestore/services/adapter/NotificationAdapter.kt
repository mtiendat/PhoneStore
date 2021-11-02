package com.example.phonestore.services.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemNotificationBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.Notification
import com.example.phonestore.services.Constant

class NotificationAdapter(var list: ArrayList<Notification>?): RecyclerView.Adapter<NotificationAdapter.ItemNotificationViewHolder>() {
    class ItemNotificationViewHolder(val bindingItemNotification: ItemNotificationBinding): RecyclerView.ViewHolder(bindingItemNotification.root)
    var updateNotification: ((Int?)-> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNotificationViewHolder {
        return  ItemNotificationViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemNotificationViewHolder, position: Int) {

        val item = list?.get(position)
        holder.bindingItemNotification.tvNotiTitle.text = item?.title
        holder.bindingItemNotification.tvNotiContent.text = HtmlCompat.fromHtml(item?.content?:"", HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.bindingItemNotification.tvNotiTime.text = item?.time
        holder.bindingItemNotification.cvNotification.setOnClickListener {
            holder.bindingItemNotification.apply {
                tvNotiTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
                tvNotiContent.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
                tvNotiTime.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
            }
            holder.bindingItemNotification.cvNotification.isEnabled = true
            updateNotification?.invoke(item?.id)
        }
        if(item?.send == 1){
            holder.bindingItemNotification.apply {
                tvNotiTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
                tvNotiContent.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
                tvNotiTime.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
            }
            Log.d("ÁDSAdadasda", "ádadsadsa")
        }
        when(item?.title){
            Constant.BILL_RECEIVED -> {
                holder.bindingItemNotification.iconNotiBill.visible()
                holder.bindingItemNotification.iconNotiBillSuccess.gone()
                holder.bindingItemNotification.iconNotiBillConfirm.gone()
                holder.bindingItemNotification.iconNotiDiscount.gone()
                holder.bindingItemNotification.iconNotiReply.gone()
            }
            Constant.BILL_SUCCESS -> {
                holder.bindingItemNotification.iconNotiBillSuccess.visible()
                holder.bindingItemNotification.iconNotiBill.gone()
                holder.bindingItemNotification.iconNotiBillConfirm.gone()
                holder.bindingItemNotification.iconNotiDiscount.gone()
                holder.bindingItemNotification.iconNotiReply.gone()
            }
            Constant.BILL_CONFIRMED -> {
                holder.bindingItemNotification.iconNotiBillConfirm.visible()
                holder.bindingItemNotification.iconNotiBill.gone()
                holder.bindingItemNotification.iconNotiDiscount.gone()
                holder.bindingItemNotification.iconNotiReply.gone()
                holder.bindingItemNotification.iconNotiBillSuccess.gone()
            }
            Constant.DISCOUNTS -> {
                holder.bindingItemNotification.iconNotiDiscount.visible()
                holder.bindingItemNotification.iconNotiReply.gone()
                holder.bindingItemNotification.iconNotiBillSuccess.gone()
                holder.bindingItemNotification.iconNotiBill.gone()
                holder.bindingItemNotification.iconNotiBillConfirm.gone()
            }
            Constant.REPLY -> {
                holder.bindingItemNotification.iconNotiReply.visible()
                holder.bindingItemNotification.iconNotiBillSuccess.gone()
                holder.bindingItemNotification.iconNotiBill.gone()
                holder.bindingItemNotification.iconNotiBillConfirm.gone()
                holder.bindingItemNotification.iconNotiDiscount.gone()
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}