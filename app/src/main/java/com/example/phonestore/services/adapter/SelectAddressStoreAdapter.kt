package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.databinding.ItemAddressBinding
import com.example.phonestore.databinding.ItemAddressStoreBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.order.Address
import com.example.phonestore.model.order.AddressStore

class SelectAddressStoreAdapter():
    RecyclerView.Adapter<SelectAddressStoreAdapter.AddressStoreViewHolder>() {
    fun setData(list: ArrayList<String>, adapter: ListProductCheckAdapter?){
        adapter?.list = list
        adapter?.notifyDataSetChanged()
    }
    class AddressStoreViewHolder(val binding: ItemAddressStoreBinding): RecyclerView.ViewHolder(binding.root){

    }
    var itemClick: ((address: AddressStore?, adapter: ListProductCheckAdapter)-> Unit)? = null
    private var itemPrevious: ItemAddressStoreBinding? = null
    private var listAddress: ArrayList<AddressStore>? = arrayListOf()
    fun submitList(addressItemList: ArrayList<AddressStore>?) {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressStoreViewHolder =
        AddressStoreViewHolder(
            ItemAddressStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    override fun onBindViewHolder(holder: AddressStoreViewHolder, position: Int) {
        val item = listAddress?.get(position)
        holder.binding.rbAddressStore.text = item?.address
        val childAdapter = ListProductCheckAdapter()
        holder.binding.rvChild.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.binding.rvChild.adapter = childAdapter
        holder.binding.rbAddressStore.setOnClickListener {
            if(itemPrevious!=null) {
                itemPrevious?.rbAddressStore?.isChecked = false
                itemPrevious?.rvChild?.gone()
                itemPrevious = null
            }
            itemClick?.invoke(listAddress?.get(position), childAdapter)
            holder.binding.rvChild.visibility =  if(holder.binding.rvChild.visibility == View.GONE) View.VISIBLE else View.GONE
            itemPrevious = holder.binding
        }
    }

    override fun getItemCount(): Int {
        return listAddress?.size ?:0
    }

}