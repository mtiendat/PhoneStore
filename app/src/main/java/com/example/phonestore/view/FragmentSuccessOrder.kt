package com.example.phonestore.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentSuccessOrderBinding
import com.example.phonestore.viewmodel.CartViewModel


class FragmentSuccessOrder: BaseFragment() {
    private lateinit var bindingSuccessOrder: FragmentSuccessOrderBinding
    private lateinit var cartViewModel: CartViewModel
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingSuccessOrder = FragmentSuccessOrderBinding.inflate(inflater, container, false)
        return bindingSuccessOrder.root
    }

    override fun setUI() {
        Glide.with(this)
                .asGif()
                .load(R.drawable.success)
                .into(bindingSuccessOrder.ivSuccess)
        cartViewModel.getTotalProduct()
    }

    override fun setViewModel() {
        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                view?.findNavController()?.navigate(R.id.action_fragmentSuccessOrder_to_fragmentHome)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

}