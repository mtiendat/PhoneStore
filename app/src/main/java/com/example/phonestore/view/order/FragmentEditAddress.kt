package com.example.phonestore.view.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentEditAddressBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.model.Location
import com.example.phonestore.model.order.Address
import com.example.phonestore.model.order.Item
import com.example.phonestore.services.Constant
import com.example.phonestore.view.cart.FragmentDialog
import com.example.phonestore.viewmodel.AddressViewModel


class FragmentEditAddress: BaseFragment() {
    private lateinit var binding: FragmentEditAddressBinding
    private lateinit var resultsInfoAddress: ActivityResultLauncher<Intent>
    private val viewModel: AddressViewModel by activityViewModels()
    private var address: Address? = null
    private var isEdit: Boolean? = false
    private var isName: Boolean = false
    private var isNew: Boolean? = false
    private var item: Location? = null
    private var idLocation: Int? = null
    private var isDistrict: Boolean = false
    private var wasValidate: Boolean= false
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentEditAddressBinding.inflate(inflater, container, false)
        address = arguments?.getParcelable("address")
        isEdit = arguments?.getBoolean("isCreateOrEdit", false)
        isNew = arguments?.getBoolean("caseNew", false)
        return binding.root
    }

    override fun setEvents() {
        findNavController().currentBackStackEntry?.savedStateHandle?.apply {
            getLiveData<String>("key").observe(viewLifecycleOwner){
                if(wasValidate) {
                    clearValidate()
                    wasValidate = false
                }
                if(isName)
                    binding.tvFullname.text = it
                else binding.tvPhone.text = it

            }
        }
        binding.btnProvince.setOnClickListener {
            resultsInfoAddress.launch(context?.let { it1 -> ActivityChooseInfoAddress.intentFor(it1, -1) })
        }
        binding.btnDistrict.setOnClickListener {
            if(isEdit==true){
                isDistrict = true
                address?.city?.let { viewModel.getSearchCity(it) }
            }else {
                if(item?.code == null){
                    activity?.let { it1 -> FragmentDialog.newInstance(it1, "Thông báo", Constant.WARNING_CITY,"Đóng") }
                }else resultsInfoAddress.launch(context?.let { it1 -> ActivityChooseInfoAddress.intentFor(it1,
                    item?.code!!, true) })
            }
        }
        binding.btnTown.setOnClickListener {
            if(isEdit==true) {
                isDistrict = false
                address?.district?.let { viewModel.getSearchDistrict(it) }
            }else {
                if(item?.code == null){
                    activity?.let { it1 -> FragmentDialog.newInstance(it1, "Thông báo", Constant.WARNING_CITY,"Đóng") }
                }else if(binding.tvDistrict.text.isNullOrEmpty()){
                    activity?.let { it1 -> FragmentDialog.newInstance(it1, "Thông báo", Constant.WARNING_DISTRICT,"Đóng") }
                } else resultsInfoAddress.launch(context?.let { it1 -> ActivityChooseInfoAddress.intentFor(it1, item?.code!!, ) })
            }
        }
        binding.btnFullName.setOnClickListener {
                isName = true
                it.findNavController().navigate(R.id.action_fragmentEditAddress_to_bottomSheet, bundleOf("data" to address?.name, "title" to binding.btnFullName.text))
        }
        binding.btnPhone.setOnClickListener {
            isName = false
            it.findNavController().navigate(R.id.action_fragmentEditAddress_to_bottomSheet, bundleOf("data" to address?.phone, "title" to binding.btnPhone.text))
        }
        binding.btnSubmit.setOnClickListener {
            if(validate()){
                val address = Address(
                    idUser = Constant.idUser,
                    name = binding.tvFullname.text.toString(),
                    phone = binding.tvPhone.text.toString(),
                    address = binding.edtAddress.text.toString(),
                    district = binding.tvDistrict.text.toString(),
                    ward = binding.tvWard.text.toString(),
                    city = binding.tvCity.text.toString(),
                    default = if(binding.swDefault.isChecked) 1 else 0
                )
                AppEvent.notifyShowPopUp()
                if(isEdit == true){
                    viewModel.updateMyAddress(this.address?.id, address)
                }else viewModel.createMyAddress(address)
            }

        }
        binding.edtAddress.addTextChangedListener{
          binding.textInputAddress.error = null
        }
    }

    override fun setUI() {
        if(isNew == true){
            binding.swDefault.isChecked = true
            binding.swDefault.setOnClickListener {
                binding.swDefault.isChecked = true
            }
        }
        resultsInfoAddress = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                if(wasValidate) {
                    clearValidate()
                    wasValidate = false
                }
                val data = result.data?.extras?.get("data") as Location
                when(result.data?.extras?.getString("is") as String){
                    "city"-> {
                        binding.tvCity.text = data.name
                        binding.tvDistrict.text =""
                        binding.tvWard.text = ""
                        isEdit = false
                    }
                    "district"-> binding.tvDistrict.text = data.name
                    "ward"-> binding.tvWard.text = data.name
               }
                item = data
            }
        }
        if(isEdit == true){
            binding.tvCity.text = address?.city
            binding.tvDistrict.text = address?.district
            binding.tvWard.text = address?.ward
            binding.tvFullname.text = address?.name
            binding.tvPhone.text = address?.phone
            binding.edtAddress.setText(address?.address)
            binding.swDefault.isChecked = address?.default==1

        }
    }

    override fun setObserve() {
        viewModel.status.observe(viewLifecycleOwner, {
            if(it == true){
                AppEvent.notifyClosePopUp()
                NavHostFragment.findNavController(this).popBackStack()
            }
        })
        viewModel.listLocation.observe(viewLifecycleOwner, {
            if(isDistrict) resultsInfoAddress.launch(context?.let { it1 -> ActivityChooseInfoAddress.intentFor(it1, it?.get(0)?.code!!, true) })
            else resultsInfoAddress.launch(context?.let { it1 -> ActivityChooseInfoAddress.intentFor(it1, it?.get(0)?.code!!) })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.viewModelStore?.clear()
    }
    fun validate(): Boolean{
        var b = true
        if(binding.edtAddress.text.isNullOrEmpty()){
            binding.textInputAddress.error = "Vui lòng nhập địa chỉ"
           b=  false
        }
        if(binding.tvFullname.text.isNullOrEmpty()){
            binding.tvFullname.text = "Bạn chưa nhập tên"
            binding.tvFullname.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
           b = false
        }
        if(binding.tvDistrict.text.isNullOrEmpty()){
            binding.tvDistrict.text = "Bạn chưa chọn quận/huyện"
            binding.tvDistrict.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
            b = false
        }
        if(binding.tvCity.text.isNullOrEmpty()){
            binding.tvCity.text = "Bạn chưa chọn tỉnh/thành phố"
            binding.tvCity.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
            b = false
        }
        if(binding.tvPhone.text.isNullOrEmpty()){
            binding.tvPhone.text = "Bạn chưa nhập sdt"
            binding.tvPhone.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
            b = false
        }
        if(binding.tvWard.text.isNullOrEmpty()){
            binding.tvWard.text = "Bạn chưa chọn phường/xã"
            binding.tvWard.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
            b = false

        }
        wasValidate = !b
        return b
    }
    private fun clearValidate(){
        ContextCompat.getColor(requireContext(),R.color.black)?.let {
            binding.tvFullname.setTextColor(it)
            binding.tvPhone.setTextColor(it)
            binding.tvCity.setTextColor(it)
            binding.tvWard.setTextColor(it)
            binding.tvDistrict.setTextColor(it)
            binding.textInputAddress.error = null
        }
        binding.tvFullname.text = null
        binding.tvPhone.text = null
        binding.tvCity.text = null
        binding.tvWard.text = null
        binding.tvDistrict.text = null
    }
}