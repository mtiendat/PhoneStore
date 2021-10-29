package com.example.phonestore.services.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.R
import com.example.phonestore.databinding.ItemChooseStorageBinding
import com.example.phonestore.extendsion.disable
import com.example.phonestore.extendsion.enable
import com.example.phonestore.model.cart.Storage
import okhttp3.internal.notify

class ChooseStorageAdapter(val listStorage: List<Storage>?, var clickItem: ((String)->Unit)? = null):
    RecyclerView.Adapter<ChooseStorageAdapter.ItemChooseStorageViewHolder>() {
     class ItemChooseStorageViewHolder(val binding: ItemChooseStorageBinding): RecyclerView.ViewHolder(binding.root)

    private var itemBindingPrevious: ItemChooseStorageBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChooseStorageViewHolder {
        return ItemChooseStorageViewHolder(ItemChooseStorageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemChooseStorageViewHolder, position: Int) {
        var item = listStorage?.get(position)
        holder.binding.btnStorage.text = item?.name
        holder.binding.btnStorage.setOnClickListener {
            if(holder.binding.ivCheck.visibility == View.GONE){
                if(itemBindingPrevious!=null && itemBindingPrevious != holder.binding){
                    itemBindingPrevious?.ivCheck?.visibility = if(itemBindingPrevious?.ivCheck?.visibility == View.GONE) View.VISIBLE else View.GONE
                }
                if(itemBindingPrevious!=holder.binding) itemBindingPrevious = holder.binding
                holder.binding.ivCheck.visibility = View.VISIBLE
                clickItem?.invoke(holder.binding.btnStorage.text.toString())
            }
        }
        if(item?.disable == true){
            holder.binding.btnStorage.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dray))
            holder.binding.btnStorage.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.custom_stroke_button_gray)
            holder.binding.btnStorage.isEnabled = false
        }else {
            holder.binding.btnStorage.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.binding.btnStorage.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.custom_stroke_button)
            holder.binding.btnStorage.isEnabled = true
        }
    }

    override fun getItemCount(): Int {
        return listStorage?.size ?: 0
    }
}