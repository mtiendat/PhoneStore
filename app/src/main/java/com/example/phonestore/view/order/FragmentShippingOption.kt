package com.example.phonestore.view.order

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentShippingOptionsBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.order.AddressStore
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.SelectAddressStoreAdapter

class FragmentShippingOption: BaseFragment() {
    private var bindingShippingOption: FragmentShippingOptionsBinding? = null
    private var isClick: Boolean = true
    private var payment: Int = 0
    private var adapter: SelectAddressStoreAdapter? = null
    private var listAddress: ArrayList<AddressStore> = arrayListOf()
    private var addressStore : AddressStore? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingShippingOption = FragmentShippingOptionsBinding.inflate(inflater, container, false)
        return bindingShippingOption?.root
    }

    override fun setEvents() {
        bindingShippingOption?.btnAtStore?.setOnClickListener {
            if(isClick) visibilityAddressStore(isClick)

        }
        bindingShippingOption?.btnShippingReceive?.setOnClickListener {
            if(!isClick) visibilityAddressStore(isClick)

        }
        bindingShippingOption?.btnShipConfirm?.setOnClickListener {
            NavHostFragment.findNavController(this).apply {
                previousBackStackEntry?.savedStateHandle?.set(Constant.SHIPPING, addressStore)
                popBackStack()
            }
        }

    }
    private fun changeDrawableEndRadioButton(drawable: Drawable?){
        bindingShippingOption?.btnAtStore?.setCompoundDrawablesWithIntrinsicBounds(null,null, drawable, null)
    }
    private fun visibilityAddressStore(isClick: Boolean){
        when(isClick){
            true -> {
                context?.let {
                    changeDrawableEndRadioButton(
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.ic_arrow_up
                        )
                    )

                }
                bindingShippingOption?.ctrlChooseAddressStore?.visible()
                listAddress.clear()
                adapter = SelectAddressStoreAdapter {
                    addressStore = it
                }
                bindingShippingOption?.rvAddressStore?.layoutManager = LinearLayoutManager(context)
                bindingShippingOption?.rvAddressStore?.adapter = adapter
                for(i in 1..3){
                    listAddress.add(AddressStore(i, address = "số 9$i, Phạm Ngũ Lão, Quận 1, TPHCM"))
                }
                adapter?.submitList(listAddress)
                this.isClick = false
            }
            false -> {
                context?.let {
                    changeDrawableEndRadioButton(
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.ic_arrow_down
                        )
                    )
                }
                bindingShippingOption?.ctrlChooseAddressStore?.gone()
                addressStore = null
                this.isClick = true
            }
        }
    }
}