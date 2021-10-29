package com.example.phonestore.services.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemChooseProductBinding
import com.example.phonestore.databinding.ItemChooseStorageBinding
import com.example.phonestore.extendsion.*
import com.example.phonestore.model.cart.Color

class ChooseProductAdapter(var listImage: List<Color>?, val clickItem: ((String?)->Unit)? = null):
    RecyclerView.Adapter<ChooseProductAdapter.ItemChooseProductViewHolder>() {
    class ItemChooseProductViewHolder(val binding: ItemChooseProductBinding): RecyclerView.ViewHolder(binding.root)
    private var itemBindingPrevious: ItemChooseProductBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChooseProductViewHolder {
        return ItemChooseProductViewHolder(ItemChooseProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemChooseProductViewHolder, position: Int) {
        var item = listImage?.get(position)
        Glide.with(holder.itemView.context)
            .load(item?.name)
            .error(R.drawable.noimage)
            .into(holder.binding.ivProduct)
        holder.binding.cbProduct.setOnClickListener {
            if(holder.binding.lnUnavailabe.visibility == View.GONE){
                if(itemBindingPrevious!=null && itemBindingPrevious != holder.binding){
                    itemBindingPrevious?.cbProduct?.isChecked = !itemBindingPrevious?.cbProduct?.isChecked!!
                }
                if(itemBindingPrevious!=holder.binding) itemBindingPrevious = holder.binding
                if(holder.binding.cbProduct.isChecked){
                    clickItem?.invoke(item?.name)
                }else holder.binding.cbProduct.isChecked = true
            }

        }
        holder.binding.ivProduct.setOnClickListener {
            if(holder.binding.lnUnavailabe.visibility == View.GONE) {
                if(!holder.binding.cbProduct.isChecked){
                    if (itemBindingPrevious != null && itemBindingPrevious != holder.binding) {
                        itemBindingPrevious?.cbProduct?.isChecked =
                            !itemBindingPrevious?.cbProduct?.isChecked!!
                    }
                    if (itemBindingPrevious != holder.binding) itemBindingPrevious = holder.binding
                    clickItem?.invoke(item?.name)
                    holder.binding.cbProduct.isChecked = true
                }
            }
        }
        if(item?.disable == true){
            holder.binding.lnUnavailabe.visible()
            holder.binding.cbProduct.invisible()
        }else{
            holder.binding.lnUnavailabe.gone()
            holder.binding.cbProduct.visible()
        }
    }

    override fun getItemCount(): Int {
        return  listImage?.size?:0
    }
}