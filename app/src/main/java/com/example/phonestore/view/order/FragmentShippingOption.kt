package com.example.phonestore.view.order

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentShippingOptionsBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.*
import com.example.phonestore.model.order.AddressStore
import com.example.phonestore.services.Constant
import com.example.phonestore.services.CustomToast
import com.example.phonestore.services.GpsUtils
import com.example.phonestore.services.adapter.ListProductCheckAdapter
import com.example.phonestore.services.adapter.SelectAddressStoreAdapter
import com.example.phonestore.view.cart.FragmentDialog
import com.example.phonestore.viewmodel.OrderViewModel
import com.google.android.gms.location.*

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
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var currentLongitude: Double? = 0.0
    private var currentLatitude: Double? = 0.0
    private var isGPS = false
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {

            } else {

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GpsUtils(requireContext()).turnGPSOn(object : GpsUtils.onGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                // turn on GPS
                isGPS = isGPSEnable
            }
        })
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        locationRequest = LocationRequest.create()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = 2 * 1000
        locationRequest?.fastestInterval = 2 * 1000
        locationCallback = (object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                for (location: Location in locationResult.locations) {
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                    if (mFusedLocationClient != null) {
                        mFusedLocationClient?.removeLocationUpdates(locationCallback)
                    }
                }
            }
        })
        getLocation()
    }

    private fun getLocation() {
        context?.let {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestMultiplePermissions.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                    return
                }
                if(isGPS){
                    mFusedLocationClient?.lastLocation?.addOnSuccessListener(requireActivity()) { location ->
                        if (location != null) {
                            currentLatitude = location.latitude
                            currentLongitude = location.longitude

                        }
                    }
                }else {
                    CustomToast(requireContext(), "Vui l??ng b???t v??? tr??")
                    mFusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            }

    }
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
                activity?.let { it1 -> FragmentDialog.newInstance(it1, "Th??ng b??o", Constant.WARNING_ADDRESS_STORE,"????ng") }
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
                adapter?.setItems(it)
            }

        })
        viewModel?.listCheckProductInStore?.observe(viewLifecycleOwner, {
            val list: ArrayList<String> = arrayListOf()
            for(id in it?.check?.listIDExist!!){
                listProductOrder?.forEach { pro ->
                    if(pro.product?.idProduct == id.toInt()){
                        list.add("${pro.product?.name} ${pro.product?.storage} ${pro.product?.color}: C??n h??ng")
                    }
                }
            }
            for(id in it.check?.listIDNonExist!!){
                listProductOrder?.forEach { pro ->
                    if(pro.product?.idProduct == id.toInt()){
                        list.add("${pro.product?.name} ${pro.product?.storage} ${pro.product?.color}: H???t h??ng")
                    }
                }
            }
            for(id in it.check?.listIDDownOne!!){
                listProductOrder?.forEach { pro ->
                    if(pro.product?.idProduct == id.toInt()){
                        list.add("${pro.product?.name} ${pro.product?.storage} ${pro.product?.color}: Ch??? c??n 1 s???n ph???m")
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
                    val listID = arrayListOf<ParamTwoInt>()
                    listProductOrder?.forEach { product ->
                        product.product?.idProduct?.let {
                                it1 -> listID.add(ParamTwoInt(it1, product.qty?:0))
                        }
                    }
                    val param = ParamListHasQty(listID = listID)
                    viewModel?.checkProductInStore(address?.id,"store", param)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 555) {
                isGPS = true // flag maintain before get location

            }
        }
    }
}