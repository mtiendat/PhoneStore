package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.databinding.ItemChooseStorageBinding

class ChooseStorageAdapter(val listStorage: List<String>?, var clickItem: ((String)->Unit)? = null):
    RecyclerView.Adapter<ChooseStorageAdapter.ItemChooseStorageViewHolder>() {
     class ItemChooseStorageViewHolder(val binding: ItemChooseStorageBinding): RecyclerView.ViewHolder(binding.root)

    private var itemBindingPrevious: ItemChooseStorageBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChooseStorageViewHolder {
        return ItemChooseStorageViewHolder(ItemChooseStorageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemChooseStorageViewHolder, position: Int) {
        holder.binding.btnStorage.text = listStorage?.get(position)
        holder.binding.btnStorage.setOnClickListener {
            if(itemBindingPrevious!=null && itemBindingPrevious != holder.binding){
                itemBindingPrevious?.ivCheck?.visibility = if(itemBindingPrevious?.ivCheck?.visibility == View.GONE) View.VISIBLE else View.GONE
            }
            if(itemBindingPrevious!=holder.binding) itemBindingPrevious = holder.binding
            holder.binding.ivCheck.visibility = if(holder.binding.ivCheck.visibility == View.GONE) View.VISIBLE else View.GONE
            clickItem?.invoke(holder.binding.btnStorage.text.toString())

        }
    }

    override fun getItemCount(): Int {
        return listStorage?.size ?: 0
    }
}