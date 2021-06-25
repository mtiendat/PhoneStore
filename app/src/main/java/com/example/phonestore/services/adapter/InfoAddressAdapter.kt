package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.databinding.ItemCityBinding
import com.example.phonestore.model.order.Item

class InfoAddressAdapter(val list: ArrayList<Item>? = arrayListOf()): RecyclerView.Adapter<InfoAddressAdapter.ItemCityAddressViewModel>() {
    var itemClick: ((Item?)->Unit)?=null
    fun setItems(listItem: ArrayList<Item>?) {
        val currentSize: Int? = list?.size
        list?.clear()
        if (listItem != null) {
            list?.addAll(listItem)
        }

        if (currentSize != null) {
            notifyItemRangeRemoved(0, currentSize)
        }
        list?.size?.let { notifyItemRangeInserted(0, it) }
    }
    class ItemCityAddressViewModel(val binding: ItemCityBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCityAddressViewModel {
        return ItemCityAddressViewModel(ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemCityAddressViewModel, position: Int) {
        val item = list?.get(position)
            holder.binding.btnName.text = item?.name
            holder.binding.btnName.setOnClickListener {
                itemClick?.invoke(item)
            }

    }

    override fun getItemCount(): Int {
        return list?.size ?:0
    }

}