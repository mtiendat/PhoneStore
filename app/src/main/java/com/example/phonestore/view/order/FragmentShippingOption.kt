package com.example.phonestore.view.order

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentShippingOptionsBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.CheckProductID
import com.example.phonestore.model.ParamListID
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.model.Province
import com.example.phonestore.model.order.AddressStore
import com.example.phonestore.model.order.CheckProductInStoreResponse
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.ListProductCheckAdapter
import com.example.phonestore.services.adapter.SelectAddressStoreAdapter
import com.example.phonestore.view.cart.FragmentDialog
import com.example.phonestore.viewmodel.OrderViewModel

class FragmentShippingOption: BaseFragment() {
    private var bindingShippingOption: FragmentShippingOptionsBinding? = null
    private var isClick: Boolean = true
    private var payment: Int = 0
    private var adapter: SelectAddressStoreAdapter? = null
    private var childAdapter: ListProductCheckAdapter? = null
    private var listAddress: ArrayList<Province> = arrayListOf()
    private var addressStore : AddressStore? = null
    private var viewModel: OrderViewModel? = null
    private var listProductOrder: ArrayList<ProductOrder>? = arrayListOf()
    private var checkedList: CheckProductID? = null
    private var isStore: Boolean = false
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
            if(!isClick&&addressStore==null){
                activity?.let { it1 -> FragmentDialog.newInstance(it1, "Thông báo", Constant.WARNING_ADDRESS_STORE,"Đóng") }
            }else{
                if(isStore) {
                    NavHostFragment.findNavController(this).apply {
                        previousBackStackEntry?.savedStateHandle?.apply {
                            set(Constant.SHIPPING, addressStore)
                            set(Constant.LISTCHECK, checkedList)
                        }
                        popBackStack()
                    }
                }else{
                    NavHostFragment.findNavController(this).apply {
                        previousBackStackEntry?.savedStateHandle?.set(Constant.SHIPPING, addressStore)
                        popBackStack()
                    }
                }
            }
        }

    }

    override fun setObserve() {
        viewModel?.listProvince?.observe(viewLifecycleOwner, {
            if(it!=null){
                listAddress.addAll(it)
                setSpinner(it)
                viewModel?.getAddressStore(it[0].id)
            }
        })
        viewModel?.listAddressStore?.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter?.submitList(it)
            }

        })
        viewModel?.listCheckProductInStore?.observe(viewLifecycleOwner, {
            val list: ArrayList<String> = arrayListOf()
            for(id in it?.check?.listIDExist!!){
                listProductOrder?.forEach { pro ->
                    if(pro.product?.id == id.toInt()){
                        list.add("${pro.product?.name} ${pro.product?.storage} ${pro.product?.color}: Còn hàng")
                    }
                }
            }
            for(id in it.check?.listIDNonExist!!){
                listProductOrder?.forEach { pro ->
                    if(pro.product?.id == id.toInt()){
                        list.add("${pro.product?.name} ${pro.product?.storage} ${pro.product?.color}: Hết hàng")
                    }
                }
            }
            isStore = true
            adapter?.setData(list, childAdapter)
            checkedList = it.check

        })
    }
    override fun setUI() {
    listProductOrder = arguments?.getParcelableArrayList("listProductOrder")
    }
    private fun setSpinner(list: List<Province>){
        val listProvince = arrayListOf<String>()
        list.forEach {
            it.province?.let { it1 -> listProvince.add(it1) }
        }
        val adapterSpinner = context?.let { ArrayAdapter(
            it,
            android.R.layout.simple_spinner_item,
            listProvince.toList()
        ) }
        adapterSpinner?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        bindingShippingOption?.snStore?.adapter = adapterSpinner
        bindingShippingOption?.snStore?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                    val address = parent?.getItemAtPosition(position).toString()
                    viewModel?.getAddressStore(listAddress[position].id)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }
    override fun setViewModel() {
        viewModel = ViewModelProvider(this@FragmentShippingOption).get(OrderViewModel::class.java)
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
                bindingShippingOption?.groupChooseAdressStore?.visible()
                listAddress.clear()
                adapter = SelectAddressStoreAdapter ()
                adapter?.itemClick = {address, adapter ->
                    val listID = arrayListOf<Int>()
                    listProductOrder?.forEach { product ->
                        product.product?.id?.let { it1 -> listID.add(it1) }
                    }
                    val param = ParamListID(listID = listID)
                    viewModel?.checkProductInStore(address?.id, param)
                    addressStore = address
                    childAdapter = adapter
                }
                bindingShippingOption?.rvAddressStore?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                bindingShippingOption?.rvAddressStore?.layoutManager = LinearLayoutManager(context)
                bindingShippingOption?.rvAddressStore?.adapter = adapter
                viewModel?.getProvinceOfStore()

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
                bindingShippingOption?.groupChooseAdressStore?.gone()
                addressStore = null
                this.isClick = true
            }
        }
    }
}