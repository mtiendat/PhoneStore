package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.databinding.ItemAddressBinding
import com.example.phonestore.databinding.ItemAddressStoreBinding
import com.example.phonestore.model.order.Address
import com.example.phonestore.model.order.AddressStore

class SelectAddressStoreAdapter(var itemClick: (address: AddressStore?)-> Unit):
    RecyclerView.Adapter<SelectAddressStoreAdapter.AddressStoreViewHolder>() {

    class AddressStoreViewHolder(val binding: ItemAddressStoreBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: AddressStore?){
            binding.rbAddressStore.text = item?.address
        }
    }
    private var listAddress: ArrayList<AddressStore>? = arrayListOf()
    fun submitList(addressItemList: ArrayList<AddressStore>) {
        if (listAddress?.isEmpty() == true) {
            listAddress = addressItemList
            notifyItemRangeInserted(0, addressItemList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return listAddress?.size ?:0
                }

                override fun getNewListSize(): Int {
                    return listAddress?.size ?:0
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    if (newItemPosition >= addressItemList.size && oldItemPosition >= listAddress?.size ?:0)
                        return false
                    return listAddress?.get(oldItemPosition)?.id == addressItemList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    if (newItemPosition >= addressItemList.size && oldItemPosition >= listAddress?.size ?:0)
                        return false
                    val newProduct = addressItemList[newItemPosition]
                    val oldProduct = listAddress?.get(oldItemPosition)
                    return (newProduct === oldProduct)
                }
            })
            listAddress = addressItemList
            result.dispatchUpdatesTo(this)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressStoreViewHolder =
        AddressStoreViewHolder(
            ItemAddressStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    override fun onBindViewHolder(holder: AddressStoreViewHolder, position: Int) {
        holder.bind(listAddress?.get(position))
        holder.binding.rbAddressStore.setOnClickListener {
            itemClick.invoke(listAddress?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return listAddress?.size ?:0
    }

}