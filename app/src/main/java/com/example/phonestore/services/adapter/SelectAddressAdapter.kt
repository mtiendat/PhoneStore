package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.databinding.ItemAddressBinding
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.order.Address

class SelectAddressAdapter:
    RecyclerView.Adapter<SelectAddressAdapter.AddressViewHolder>() {
    class AddressViewHolder(val binding: ItemAddressBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Address?){
            binding.tvAddressDetail.text = "${item?.address}, ${item?.ward}, ${item?.district}, ${item?.city}"
            binding.tvAddressName.text = item?.name
            binding.tvAddressPhone.text = item?.phone
            if(item?.default == 1) binding.btnDefault.visible()
        }
    }
    private var listAddress: ArrayList<Address>? = arrayListOf()
    var itemClick: ((address: Address?)->Unit)? = null
    var editClick: ((address: Address?)->Unit)? = null
    fun submitList(addressItemList: ArrayList<Address>?) {
        if (listAddress?.isEmpty() == true) {
            listAddress = addressItemList
            addressItemList?.size?.let { notifyItemRangeInserted(0, it) }
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return listAddress?.size ?:0
                }

                override fun getNewListSize(): Int {
                    return listAddress?.size ?:0
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    if (newItemPosition >= addressItemList?.size?:0 && oldItemPosition >= listAddress?.size ?:0)
                        return false
                    return listAddress?.get(oldItemPosition)?.id == addressItemList?.get(newItemPosition)?.id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    if (newItemPosition >= addressItemList?.size?:0 && oldItemPosition >= listAddress?.size ?:0)
                        return false
                    val newProduct = addressItemList?.get(newItemPosition)
                    val oldProduct = listAddress?.get(oldItemPosition)
                    return (newProduct === oldProduct)
                }
            })
            listAddress = addressItemList
            result.dispatchUpdatesTo(this)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder = AddressViewHolder(
        ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(listAddress?.get(position))
        holder.binding.ctrlAddress.setOnClickListener {
            itemClick?.invoke(listAddress?.get(position))
        }
        holder.binding.tvAddressEdit.setOnClickListener {
            editClick?.invoke(listAddress?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return listAddress?.size ?:0
    }

}