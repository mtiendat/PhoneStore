package com.example.phonestore.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.phonestore.databinding.FragmentBottomsheetDialogChangeInfoBinding
import com.example.phonestore.services.Constant
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.regex.Pattern

class FragmentBottomSheetChangeInfo: BottomSheetDialogFragment() {
    private var bindingBottom: FragmentBottomsheetDialogChangeInfoBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingBottom = FragmentBottomsheetDialogChangeInfoBinding.inflate(inflater, container, false)
        return bindingBottom?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }
    fun setUI(){
        val data = arguments?.getString("data")
        val title = arguments?.getString("title")
        bindingBottom?.tvChange?.text = title
        bindingBottom?.edtChange?.setText(data)
        bindingBottom?.btnSave?.setOnClickListener {
            NavHostFragment.findNavController(this).apply {
                if(validate(title)){
                    previousBackStackEntry?.savedStateHandle?.set("key", bindingBottom?.edtChange?.text.toString())
                    popBackStack()
                }
            }
        }
    }
    private fun validate(title: String?="") :Boolean{
        return if(title =="Địa chỉ"){
            if(bindingBottom?.edtChange?.text.isNullOrBlank()) {
                bindingBottom?.edtChange?.error = Constant.VALIDATE_ADDRESS
                false
            }else true
        }else if(title =="SDT"){
            if(!Pattern.compile("^(0)+([0-9]{9})$").matcher(bindingBottom?.edtChange?.text!!).matches()) {
                bindingBottom?.edtChange?.error = Constant.PHONE_INVALID
                false
            }else true

        }else if(title =="Tên"){
            if(bindingBottom?.edtChange?.text.isNullOrBlank()){
                bindingBottom?.edtChange?.error = Constant.VALIDATE_FULL_NAME
                false
            }else true

        }else true
    }
}