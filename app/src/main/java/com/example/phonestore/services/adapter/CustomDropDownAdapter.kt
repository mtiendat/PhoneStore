package com.example.phonestore.services.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import com.example.phonestore.R

class CustomDropDownAdapter(context: Context, @LayoutRes private val layoutResourceId: Int,
                            private val possessTypeList: ArrayList<String?>?)
    : ArrayAdapter<String?>(context, layoutResourceId, possessTypeList?: arrayListOf()) {
    var selectedItem = -1
    private fun getCustomViewDropDown(position: Int, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(layoutResourceId, parent, false)

        val tvPossessType = layout.findViewById(R.id.tvSpinner) as TextView

        tvPossessType.text = possessTypeList?.get(position)

        if (position == selectedItem) {
            tvPossessType.setTextColor(ResourcesCompat.getColor(context.resources, R.color.colorPrimary, null))
        }

        return layout
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(layoutResourceId, parent, false)
        val tvPossessType = layout.findViewById(R.id.tvSpinner) as TextView
        tvPossessType.text = possessTypeList?.get(position)
        return layout
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomViewDropDown(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }
}