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
            binding.voucher = item
            binding.tvDiscountContent.text = "Áp dụng cho đơn hàng từ ${item?.condition.toVND()}"
            binding.tvDiscountDate.text = "HSD: ${item?.end_date}"
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
        val currentSize: Int? = listDiscount?.size
        listDiscount?.clear()
        if (discountItemList != null) {
            listDiscount?.addAll(discountItemList)
        }
        if (currentSize != null) {
            notifyItemRangeRemoved(0, currentSize)
        }
        listDiscount?.size.let {
            if (it != null) {
                notifyItemRangeInserted(0, it)
            }
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