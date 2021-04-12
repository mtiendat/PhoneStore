package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentComingSoonBinding

class FragmentHelper :BaseFragment() {
    private lateinit var bindingComingSoonBinding: FragmentComingSoonBinding
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingComingSoonBinding = FragmentComingSoonBinding.inflate(inflater, container, false)
        return bindingComingSoonBinding.root
    }
}