package com.example.phonestore.view.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentAddressSelectionBinding
import com.example.phonestore.model.order.Address
import com.example.phonestore.services.Constant.ADDRESS
import com.example.phonestore.services.adapter.SelectAddressAdapter

class FragmentSelectAddress: BaseFragment() {
    private var bindingSelectAddress: FragmentAddressSelectionBinding? = null
    private lateinit var adapter: SelectAddressAdapter
    private var listAddress: ArrayList<Address> = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingSelectAddress = FragmentAddressSelectionBinding.inflate(inflater, container, false)
        return bindingSelectAddress?.root
    }

    override fun setUI() {
        adapter = SelectAddressAdapter {
            NavHostFragment.findNavController(this).apply {
                previousBackStackEntry?.savedStateHandle?.set(ADDRESS, it)
                popBackStack()
            }
        }
        bindingSelectAddress?.rvAddressSelection?.layoutManager = LinearLayoutManager(context)
        bindingSelectAddress?.rvAddressSelection?.adapter = adapter
        for(i in 1..3){
            listAddress.add(Address(id = i, name = "Mai Hoàng Tiến Đạt $i", phone = "038415150$i", address = "5$i, Khu C, Âu Dương Lân, Phường 3, Quạn 8, TPHCM"))
        }
        adapter.submitList(listAddress)
    }
}