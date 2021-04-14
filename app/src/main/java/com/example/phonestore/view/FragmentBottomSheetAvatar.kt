package com.example.phonestore.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.phonestore.databinding.FragmentBottomsheetDialogAvatarBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentBottomSheetAvatar: BottomSheetDialogFragment() {
    private var bindingBottom: FragmentBottomsheetDialogAvatarBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingBottom = FragmentBottomsheetDialogAvatarBinding.inflate(inflater, container, false)
        return bindingBottom?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }
    private fun setUI(){
        bindingBottom?.btnChosseLibrary?.setOnClickListener {
            NavHostFragment.findNavController(this).apply {
                previousBackStackEntry?.savedStateHandle?.set("key", 1)
                popBackStack()
            }
        }
        bindingBottom?.btnTakeAPicture?.setOnClickListener {
            NavHostFragment.findNavController(this).apply {
                previousBackStackEntry?.savedStateHandle?.set("key", 2)
                popBackStack()
            }
        }
    }
}