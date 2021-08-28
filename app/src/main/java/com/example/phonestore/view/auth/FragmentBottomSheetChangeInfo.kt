package com.example.phonestore.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.NavHostFragment
import com.example.phonestore.R
import com.example.phonestore.databinding.FragmentBottomsheetDialogChangeInfoBinding
import com.example.phonestore.services.Constant
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.regex.Pattern

class FragmentBottomSheetChangeInfo: BottomSheetDialogFragment() {
    private var bindingBottom: FragmentBottomsheetDialogChangeInfoBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingBottom = FragmentBottomsheetDialogChangeInfoBinding.inflate(inflater, container, false)
        return bindingBottom?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }
    private fun setUI(){
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
        bindingBottom?.edtChange?.addTextChangedListener {
            bindingBottom?.textInput?.error = null
        }
    }

    private fun validate(title: String?="") :Boolean{
        return if(title == "Họ và tên"){
            if(bindingBottom?.edtChange?.text.isNullOrBlank()) {
                bindingBottom?.textInput?.error = Constant.VALIDATE_FULL_NAME
                false
            }else true
        }else if(title == "Số điện thoại"){
            if(!Pattern.compile("^(\\+84|0)+([3|5|7|8|9])+([0-9]{8})$").matcher(bindingBottom?.edtChange?.text!!).matches()) {
                bindingBottom?.textInput?.error = Constant.PHONE_INVALID
                false
            }else true

        }else if(title == "Tên"){
            if(bindingBottom?.edtChange?.text.isNullOrBlank()){
                bindingBottom?.edtChange?.error = Constant.VALIDATE_FULL_NAME
                false
            }else true

        }else true
    }
}