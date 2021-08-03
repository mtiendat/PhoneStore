package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.databinding.FragmentPhotoBinding
import com.example.phonestore.model.Attachment

class PhotoPagerAdapter() : RecyclerView.Adapter<PhotoPagerAdapter.PhotoViewHolder>() {
    private var attachments: ArrayList<Attachment> = arrayListOf()
    class PhotoViewHolder(val binding: FragmentPhotoBinding): RecyclerView.ViewHolder(binding.root)
    fun setAttachments(attachments: ArrayList<Attachment>?) {
        if (attachments != null) {
            this.attachments = attachments
        }
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(FragmentPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = attachments[position]
        Glide.with(holder.itemView.context).load(item.attachment).into(holder.binding.ivPhoto)
    }

    override fun getItemCount(): Int {
        return attachments.size
    }

}