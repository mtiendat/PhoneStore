package com.example.phonestore.view

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentWarrantyBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.Warranty
import com.example.phonestore.viewmodel.ProductViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
class FragmentWarranty: BaseFragment() {
    private lateinit var bindingWarranty: FragmentWarrantyBinding
    private lateinit var viewModel: ProductViewModel
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingWarranty = FragmentWarrantyBinding.inflate(inflater, container, false)
        return bindingWarranty.root
    }

    override fun setEvents() {
        bindingWarranty.btnSubmitWarranty.setOnClickListener {
            //call api
            if(validateImei()){
                hideSoftKeyboard(activity)
                viewModel.checkWarranty(bindingWarranty.edtImei.text.toString())
                AppEvent.notifyShowPopUp()
            }
        }
        bindingWarranty.tvWarrantyOther.setOnClickListener {
            bindingWarranty.groupWarranty.gone()
            bindingWarranty.groupFindWarranty.visible()
        }
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this@FragmentWarranty).get(ProductViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setObserve() {
        viewModel.warrantyResponse.observe(viewLifecycleOwner, {
            if(it?.status == true){
                setWarranty(it.warranty)
                bindingWarranty.groupFindWarranty.gone()
                bindingWarranty.groupWarranty.visible()
            }else bindingWarranty.textInputImei.error = it?.message.toString()
            AppEvent.notifyClosePopUp()
        })
    }

    override fun setUI() {
        bindingWarranty.edtImei.setText("766886096157001")
        bindingWarranty.edtImei.addTextChangedListener {
            bindingWarranty.textInputImei.error = null
        }
    }
    private fun validateImei(): Boolean{
        var check = true
        check = if(bindingWarranty.edtImei.text?.isNullOrEmpty() == true){
            bindingWarranty.textInputImei.error = "Bạn chưa nhập imei"
            false
        }else if(bindingWarranty.edtImei.text?.length!=15 || bindingWarranty?.edtImei?.text?.contains("7668")==false){
            bindingWarranty.textInputImei.error = "Imei không hợp lệ"
            false
        }else true
        return check
    }

    private fun setWarranty(warranty: Warranty?){
        Glide.with(requireContext())
            .load(warranty?.image)
            .error(R.drawable.noimage)
            .into(bindingWarranty.ivWarrantyPhone)
        bindingWarranty.tvWarrantyColorAndStorage.text = "Màu sắc: ${warranty?.color}   Dung lượng: ${warranty?.storage}"
        if(warranty?.dateStart.toString() < warranty?.dateEnd.toString()){
            bindingWarranty.tvWarrantyTime.visible()
            bindingWarranty.tvWarrantyTimeTitle.visible()
            bindingWarranty.tvWarrantyState.text = "Còn bảo hành"
            bindingWarranty.tvWarrantyState.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val currentDate = formatter.format(time)
            val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            val startDateValue: LocalDate = LocalDate.parse(currentDate, dateFormatter)
            val endDateValue: LocalDate = LocalDate.parse(warranty?.dateEnd, dateFormatter)
            bindingWarranty.tvWarrantyTime.text = "${ChronoUnit.DAYS.between(startDateValue, endDateValue).toString()} ngày"
        }else {
            bindingWarranty.tvWarrantyState.text = "Hết bảo hành"
            bindingWarranty.tvWarrantyTime.gone()
            bindingWarranty.tvWarrantyTimeTitle.gone()
            bindingWarranty.tvWarrantyState.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        }
        bindingWarranty.tvWarrantyName.text = warranty?.name
        bindingWarranty.tvWarrantyDateStart.text = warranty?.dateStart
        bindingWarranty.tvWarrantyDateEnd.text = warranty?.dateEnd
        bindingWarranty.tvWarrantyImei.text = warranty?.imei

    }
    private fun hideSoftKeyboard(activity: Activity?) {
        val inputMethodManager = activity?.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }

}