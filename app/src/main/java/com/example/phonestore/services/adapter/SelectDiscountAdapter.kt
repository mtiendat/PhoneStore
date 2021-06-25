package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemAddressStoreBinding
import com.example.phonestore.databinding.ItemDiscountBinding
import com.example.phonestore.extendsion.enabled
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.model.cart.Discount
import com.example.phonestore.model.cart.Voucher
import com.example.phonestore.model.order.AddressStore

class SelectDiscountAdapter():
    RecyclerView.Adapter<SelectDiscountAdapter.DiscountViewHolder>() {
    class DiscountViewHolder(val binding: ItemDiscountBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Voucher?){
            binding.tvDiscountContent.text = "Áp dụng cho đơn hàng từ ${item?.condition.toVND()}"
            binding.tvDiscountDate.text = item?.end_date
            binding.tvDiscountNumber.text = "${item?.discount}%"

            if(item?.active == false){
                binding.btnDiscountSubmit.enabled()
                binding.ctrlVoucher.setBackgroundResource(R.color.dray)
                binding.ctrlRootVoucher.setBackgroundResource(R.drawable.bg_custom_voucher_nonactive)
                binding.ctrlOvalTop.setBackgroundResource(R.drawable.oval_gray)
                binding.ctrlOvalBottom.setBackgroundResource(R.drawable.oval_gray)
                binding.btnDiscountSubmit.text = "Đã hết hạn"
            }
        }
    }
    private var listDiscount: ArrayList<Voucher>? = arrayListOf()
    var itemClick: ((voucher: Voucher?)-> Unit)? = null
    var deleteItem: ((id: Int?)->Unit)? = null
    fun submitList(discountItemList: ArrayList<Voucher>?) {
        if (listDiscount?.isEmpty() == true) {
            listDiscount = discountItemList
            discountItemList?.size?.let { notifyItemRangeInserted(0, it) }
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return listDiscount?.size ?:0
                }

                override fun getNewListSize(): Int {
                    return listDiscount?.size ?:0
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    if (newItemPosition >= discountItemList?.size?:0 && oldItemPosition >= listDiscount?.size ?:0)
                        return false
                    return listDiscount?.get(oldItemPosition)?.id == discountItemList?.get(newItemPosition)?.id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    if (newItemPosition >= discountItemList?.size?:0 && oldItemPosition >= listDiscount?.size ?:0)
                        return false
                    val newProduct = discountItemList?.get(newItemPosition)
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
        val item = listDiscount?.get(position)
        holder.bind(item)
        holder.binding.btnDiscountSubmit.setOnClickListener {
            itemClick?.invoke(item)
        }
        holder.binding.ivDelete.setOnClickListener {
            deleteItem?.invoke(item?.id)
        }
    }

    override fun getItemCount(): Int {
       return listDiscount?.size ?:0
    }
}