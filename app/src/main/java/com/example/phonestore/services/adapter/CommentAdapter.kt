package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemVoteCommentBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.Comment
import com.example.phonestore.services.Constant
import com.example.phonestore.view.product.ActivityPreviewPhoto


class CommentAdapter: RecyclerView.Adapter<CommentAdapter.ItemCommentViewHolder>() {
    class ItemCommentViewHolder(val bindingItemVote: ItemVoteCommentBinding): RecyclerView.ViewHolder(bindingItemVote.root)
    val listComment: ArrayList<Comment> = arrayListOf()
    var intoReply: ((Comment)->Unit)? = null
    var likeComment: ((Int?, Boolean) -> Unit)? = null
    var deleteComment: ((Int?, Int?)->Unit)? = null
    fun setItems(listItem: ArrayList<Comment>?) {
        val currentSize: Int = listComment.size
        listComment.clear()
        if (listItem != null) {
            listComment.addAll(listItem)
        }
        notifyItemRangeRemoved(0, currentSize)
        listComment.size.let { notifyItemRangeInserted(0, it) }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCommentViewHolder {
        return ItemCommentViewHolder(
                ItemVoteCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

    }

    override fun onBindViewHolder(holder: ItemCommentViewHolder, position: Int) {
        val item = listComment[position]
        holder.bindingItemVote.shimmerLayoutComment.startShimmer()
        holder.bindingItemVote.tvNameVote.text = item.name
        holder.bindingItemVote.tvContent.text = item.content
        holder.bindingItemVote.tvVoteDate.text = item.date
        holder.bindingItemVote.rbComment.rating = item.vote.toFloat()?: 0f
        holder.bindingItemVote.control.btnLike.text = "Thích (${item.like})"
        holder.bindingItemVote.tvCategory.text ="Khả năng lưu trữ: ${item.storage}, nhóm màu: ${item.color}"
        holder.bindingItemVote.btnTotalReply.setOnClickListener {
            if(holder.bindingItemVote.lnReply.visibility == View.GONE) {
                holder.bindingItemVote.btnTotalReply.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
                holder.bindingItemVote.lnReply.visible()
            } else {
                holder.bindingItemVote.btnTotalReply.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
                holder.bindingItemVote.lnReply.gone()
            }

        }
        holder.bindingItemVote.tvEdited.visibility = if(item.edited == 1) View.VISIBLE else View.GONE
        holder.bindingItemVote.control.btnReply.setOnClickListener {
            intoReply?.invoke(item)
        }
        holder.bindingItemVote.btnViewAllReply.setOnClickListener {
            intoReply?.invoke(item)
        }
        Glide.with(holder.itemView.context)
            .load(item.avatar)
            .error(R.drawable.noimage)
            .into(holder.bindingItemVote.ivAvatarComment)
        if(item.listReply?.size == 0){
            holder.bindingItemVote.btnTotalReply.gone()
        }else holder.bindingItemVote.btnTotalReply.text = "${item?.totalReply} trả lời"
        val adapter = ReplyAdapter(item.listReply)
        //holder.bindingItemVote.rvChildReply.addItemDecoration(DividerItemDecoration(holder.itemView.context, DividerItemDecoration.VERTICAL))
        holder.bindingItemVote.rvChildReply.adapter = adapter
        holder.bindingItemVote.rvChildReply.layoutManager = LinearLayoutManager(holder.itemView.context)
        if(item.hasLike){
            holder.bindingItemVote.control.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bg_ic_like_active, 0,0,0)
            holder.bindingItemVote.control.btnLike.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue_like_fb))
        }
        holder.bindingItemVote.control.btnLike.setOnClickListener {
            item.hasLike = !item.hasLike
            if(!item.hasLike){
                item.like--
                likeComment?.invoke(item.id, item.hasLike)
                holder.bindingItemVote.control.btnLike.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
                holder.bindingItemVote.control.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bg_ic_like, 0,0,0)

            }else{
                item.like++
                likeComment?.invoke(item.id, item.hasLike)
                holder.bindingItemVote.control.btnLike.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue_like_fb))
                holder.bindingItemVote.control.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bg_ic_like_active, 0,0,0)
            }
            holder.bindingItemVote.control.btnLike.text = "Thích (${item.like})"

        }
        if(item.listAttachment?.size?:-1>0){
            val listPath = arrayListOf<String>()
            item.listAttachment?.forEach {
                listPath.add(it.attachment?:"")
            }
            val adapterImage = ImageCommentAdapter(listPath)
            adapterImage.itemClick = {
                holder.itemView.context.startActivity(ActivityPreviewPhoto.intentFor(holder.itemView.context, item.listAttachment, false, it ))
            }
            holder.bindingItemVote.rvChildImage.adapter = adapterImage
            holder.bindingItemVote.rvChildImage.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.bindingItemVote.shimmerLayoutComment.stopShimmer()
            holder.bindingItemVote.shimmerLayoutComment.gone()
        }else {
            holder.bindingItemVote.shimmerLayoutComment.stopShimmer()
            holder.bindingItemVote.shimmerLayoutComment.gone()
            holder.bindingItemVote.rvChildImage.gone()
        }
        //spinner
        holder.bindingItemVote.spOption.setOnClickListener {
            if(holder.bindingItemVote.optionComment.visibility == View.GONE)
                holder.bindingItemVote.optionComment.visible()
            else holder.bindingItemVote.optionComment.gone()
        }
        holder.bindingItemVote.spOption.visibility = if(item.idUser == Constant.user?.id) View.VISIBLE else View.GONE
        holder.bindingItemVote.btnEditComment.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentComment, bundleOf("isEdit" to  true, "comment" to item))
        }
        holder.bindingItemVote.btnDeleteComment.setOnClickListener {
            deleteComment?.invoke(item.id, position)
        }
        


    }

    override fun getItemCount(): Int {
        return  listComment.size
    }
}