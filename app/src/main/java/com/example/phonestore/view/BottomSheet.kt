package com.example.phonestore.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.BottomsheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.regex.Pattern

class BottomSheet: BottomSheetDialogFragment() {
    private lateinit var bindingBottom: BottomsheetDialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingBottom = BottomsheetDialogBinding.inflate(inflater, container, false)

        return bindingBottom.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }
    fun setUI(){
        val data = arguments?.getString("data")
        val title = arguments?.getString("title")
        bindingBottom.tvChange.text = title
        bindingBottom.edtChange.setText(data)
        bindingBottom.btnSave.setOnClickListener {
            NavHostFragment.findNavController(this).apply {
                if(title =="SDT"){
                    if(!Pattern.compile("^(0)+([0-9]{9})$").matcher(bindingBottom.edtChange.text).matches()){
                        bindingBottom.edtChange.error = "Sdt không hợp lệ"
                    }else {
                        previousBackStackEntry?.savedStateHandle?.set("key", bindingBottom.edtChange.text.toString())
                        popBackStack()
                    }
                }else {
                    previousBackStackEntry?.savedStateHandle?.set("key", bindingBottom.edtChange.text.toString())
                    popBackStack()
                }
            }
        }
    }
}