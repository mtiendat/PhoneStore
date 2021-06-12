package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.databinding.ItemAddressStoreBinding
import com.example.phonestore.databinding.ItemDiscountBinding
import com.example.phonestore.model.cart.Discount
import com.example.phonestore.model.order.AddressStore

class SelectDiscountAdapter(var itemClick: (discount: Discount?)-> Unit):
    RecyclerView.Adapter<SelectDiscountAdapter.DiscountViewHolder>() {
    class DiscountViewHolder(val binding: ItemDiscountBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Discount?){
            binding.tvDiscountContent.text = item?.content
            binding.tvDiscountDate.text = item?.dateEnd
            binding.tvDiscountNumber.text = "${item?.discount}%"
        }
    }
    private var listDiscount: ArrayList<Discount>? = arrayListOf()
    fun submitList(discountItemList: ArrayList<Discount>) {
        if (listDiscount?.isEmpty() == true) {
            listDiscount = discountItemList
            notifyItemRangeInserted(0, discountItemList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return listDiscount?.size ?:0
                }

                override fun getNewListSize(): Int {
                    return listDiscount?.size ?:0
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    if (newItemPosition >= discountItemList.size && oldItemPosition >= listDiscount?.size ?:0)
                        return false
                    return listDiscount?.get(oldItemPosition)?.id == discountItemList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    if (newItemPosition >= discountItemList.size && oldItemPosition >= listDiscount?.size ?:0)
                        return false
                    val newProduct = discountItemList[newItemPosition]
                    val oldProduct = listDiscount?.get(oldItemPosition)
                    return (newProduct === oldProduct)
                }
            })
            listDiscount = discountItemList
            result.dispatchUpdatesTo(this)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder =
        DiscountViewHolder(ItemDiscountBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
        holder.bind(listDiscount?.get(position))
        holder.binding.btnDiscountSubmit.setOnClickListener {
            itemClick.invoke(listDiscount?.get(position))
        }
    }

    override fun getItemCount(): Int {
       return listDiscount?.size ?:0
    }
}