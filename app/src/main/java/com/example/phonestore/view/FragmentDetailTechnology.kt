package com.example.phonestore.view

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.phonestore.databinding.FragmentDetailTechnologyBinding


class FragmentDetailTechnology: DialogFragment() {
    private var bindingFragmentDetailTechnology: FragmentDetailTechnologyBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingFragmentDetailTechnology = FragmentDetailTechnologyBinding.inflate(inflater, container, false)
        return bindingFragmentDetailTechnology?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        //dialog full screen
        val window: Window? = dialog?.window
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        window?.setLayout(width, height)
    }
}