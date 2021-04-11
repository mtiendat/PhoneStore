package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentFollowOrderBinding
import com.example.phonestore.services.TabLayoutAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FragmentFollowOrder: BaseFragment() {
    private lateinit var bindingFollowOrder: FragmentFollowOrderBinding
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingFollowOrder = FragmentFollowOrderBinding.inflate(inflater, container, false)
        return bindingFollowOrder.root
    }

    override fun setUI() {
        initViewPager2()
    }
    private fun initViewPager2(){
        val listFragment: ArrayList<Fragment> = arrayListOf(
                FragmentFollowOrderAll(),
                FragmentFollowOrderReceived(),
                FragmentFollowOrderTransported(),
                FragmentFollowOrderDelivered()
        )
        val adapter = activity?.let { TabLayoutAdapter(listFragment, it.supportFragmentManager, lifecycle) }
        bindingFollowOrder.vp2Order.adapter = adapter

        val names: ArrayList<String> = arrayListOf("Tất cả","Đã tiếp nhận", "Đang giao hàng", "Thành công")
        TabLayoutMediator(bindingFollowOrder.tabLayoutOrder, bindingFollowOrder.vp2Order){ tab, position ->
            tab.text = names[position]
        }.attach()
    }
}