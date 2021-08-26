package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.databinding.FragmentContactBinding
import com.example.phonestore.databinding.ItemSupplierBinding
import com.example.phonestore.model.Supplier

class SupplierAdapter(val listSupplier: List<Supplier>): RecyclerView.Adapter<SupplierAdapter.ItemSupplierViewHolder>() {
    var clickSupplier: ((Int)->Unit)? = null
    class ItemSupplierViewHolder(val binding: ItemSupplierBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSupplierViewHolder {
        return ItemSupplierViewHolder(ItemSupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemSupplierViewHolder, position: Int) {
        val item = listSupplier[position]
        Glide.with(holder.itemView.context)
            .load(item.logoSupplier)
            .into(holder.binding.ivSupplier)
        holder.binding.ivSupplier.setOnClickListener {
            clickSupplier?.invoke(position)
            holder.binding.ivMarkSupplier.visibility = if(holder.binding.ivMarkSupplier.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return listSupplier.size
    }
}