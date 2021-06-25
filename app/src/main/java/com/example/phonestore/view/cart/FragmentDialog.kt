package com.example.phonestore.view.cart

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentComingSoonBinding
import com.example.phonestore.databinding.LayoutDialogBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.services.Constant

class FragmentDialog :DialogFragment() {

    private lateinit var binding: LayoutDialogBinding
    private var title: String? = ""
    private var content: String? =""
    private var textButton: String? =""
    companion object{
        fun newInstance(activity: FragmentActivity, title: String?, content: String?, textButton: String?){
            val dialog = FragmentDialog()
            dialog.arguments = bundleOf("TITLE_DIALOG" to title, "CONTENT_DIALOG" to content, "BUTTON_DIALOG" to textButton)
            activity.supportFragmentManager.let { it1 -> dialog.show(it1, "VOUCHER") }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = arguments?.getString("TITLE_DIALOG")
        content = arguments?.getString("CONTENT_DIALOG")
        textButton = arguments?.getString("BUTTON_DIALOG")

        binding.tvTitleDialog.text = title
        binding.tvContentDialog.text = content
        binding.btnSubmit.setOnClickListener { dismiss() }
        if(textButton?.isNotEmpty() == true){
            binding.btnSubmit.text = textButton
            binding.btnSubmit.setOnClickListener { dismiss() }
        }else binding.btnSubmit.gone()
    }
    override fun onStart() {
        super.onStart()
        Log.d("asdasdas", "close")
        //dialog full screen
        val window: Window? = dialog?.window
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        window?.setLayout(width, height)
    }



}