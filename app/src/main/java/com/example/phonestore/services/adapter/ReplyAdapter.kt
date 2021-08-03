package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemCommentReplyBinding
import com.example.phonestore.databinding.ItemViewAllBinding
import com.example.phonestore.databinding.ItemVoteCommentBinding
import com.example.phonestore.model.Comment
import com.example.phonestore.model.Reply

class ReplyAdapter(var listReply: ArrayList<Reply>? = arrayListOf()): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ItemReplyViewHolder(val binding: ItemCommentReplyBinding): RecyclerView.ViewHolder(binding.root)
    class ItemViewAllViewHolder(val binding: ItemViewAllBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType==0) {
            ItemReplyViewHolder(ItemCommentReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else ItemViewAllViewHolder(
            ItemViewAllBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listReply?.get(position)
        if(holder is ItemReplyViewHolder) {
            holder.binding.tvName.text = item?.name
            holder.binding.tvDate.text = item?.date
            holder.binding.tvContentReply.text = item?.content

            Glide.with(holder.itemView.context)
                .load(item?.avatar)
                .error(R.drawable.noimage)
                .into(holder.binding.ivAvatarReply)
        }
    }

    override fun getItemCount(): Int {
        return listReply?.size?:0
    }
    override fun getItemViewType(position: Int): Int {
        return if(position == listReply?.size?:0 ){
            1
        }else 0
    }
}