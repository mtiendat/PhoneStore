package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.phonestore.extendsion.gone
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentSupplierBinding
import com.example.phonestore.model.Supplier
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.TabLayoutAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FragmentSupplier: BaseFragment() {
    private var bindingSupplier: FragmentSupplierBinding? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingSupplier = FragmentSupplierBinding.inflate(inflater, container, false)
        return bindingSupplier?.root
    }

    override fun setUI() {
        val supplier = arguments?.getParcelable<Supplier>("supplier")
        setInfoSupplier(supplier)
        val listFragment: ArrayList<Fragment> = arrayListOf(
                FragmentSupplierAllProduct(supplier),
                FragmentSupplierNewProduct(supplier)
        )
        val adapter = activity?.let {
            TabLayoutAdapter(listFragment, it.supportFragmentManager, lifecycle) }
        bindingSupplier?.vp2Supplier?.adapter = adapter

        val names: ArrayList<String> = arrayListOf(Constant.ALL, Constant.NEW_PRODUCT)
        bindingSupplier?.tabLayoutSupplier?.let {
            bindingSupplier?.vp2Supplier?.let { it1 ->
                TabLayoutMediator(it, it1){ tab, position ->
                tab.text = names[position]
            }.attach()
            }
        }
    }
    private fun setInfoSupplier(supplier: Supplier?){
        bindingSupplier?.tvNameSupplier?.text = supplier?.name
        context?.let {
            bindingSupplier?.ivSupplier?.let { imgV ->
                Glide.with(it)
                    .load(supplier?.logoSupplier)
                        .error(R.drawable.noimage)
                    .into(imgV)
            }
        }
        if(supplier?.auth == 0){
            bindingSupplier?.ivSupplierAuth?.gone()
        }
    }
}