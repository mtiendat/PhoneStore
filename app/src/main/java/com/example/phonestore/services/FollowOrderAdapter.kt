package com.example.phonestore.services

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.phonestore.view.FragmentAllOrder
import com.example.phonestore.view.FragmentDeliveredOrder
import com.example.phonestore.view.FragmentTransportedOrder

class FollowOrderAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    var listFragment: ArrayList<Fragment> = arrayListOf(
            FragmentAllOrder(),
            FragmentTransportedOrder(),
            FragmentDeliveredOrder()
    )
    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }

}