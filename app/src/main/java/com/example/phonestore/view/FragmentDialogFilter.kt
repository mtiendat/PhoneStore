package com.example.phonestore.view

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.databinding.FragmentDialogFilterBinding
import com.example.phonestore.model.Filter
import com.example.phonestore.model.RamAndStorageResponse
import com.example.phonestore.viewmodel.AllProductViewModel

class FragmentDialogFilter: DialogFragment() {
    private lateinit var binding: FragmentDialogFilterBinding
    private var ramAndStorage: RamAndStorageResponse? = null
    private var filterOld: Filter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ramAndStorage = arguments?.getParcelable("ramAndStorage")
        filterOld = arguments?.getParcelable("filterOld")
        createCheckBox(ramAndStorage?.ramAndStorage?.ram, binding.groupRam)
        createCheckBox(ramAndStorage?.ramAndStorage?.storage, binding.groupStorage)

        if(filterOld!=null) {
            binding.groupRam.children.forEach {
                val rbRam: RadioButton = (requireView().findViewById(it.id) as RadioButton)
                if(rbRam.text == filterOld?.ram){
                    binding.groupRam.check(it.id)
                }
            }
            binding.groupStorage.children.forEach {
                val rbStorage: RadioButton = (requireView().findViewById(it.id) as RadioButton)
                if(rbStorage.text == filterOld?.storage){
                    binding.groupStorage.check(it.id)
                }
            }
            binding.edtPriceMax.setText(filterOld?.priceMax)
            binding.edtPriceMin.setText(filterOld?.priceMin)
        }

        setEvent()
    }
    private fun createCheckBox(list: List<String>?, rg: RadioGroup){
        list?.forEach {
            val radioButton = RadioButton(context)
            radioButton.text = it
            rg.addView(radioButton)
        }

    }

   private fun setEvent(){
       binding.btnFilterSave.setOnClickListener {
           val idRbRam = binding.groupRam.checkedRadioButtonId
           val idRbStorage = binding.groupStorage.checkedRadioButtonId
           if(idRbRam!=-1||idRbStorage!=-1){
               val result = getValue(idRbRam, idRbStorage)
               setFragmentResult("requestKey", bundleOf("filter" to result))
           }else if(binding.edtPriceMax.text?.isNotEmpty() == true && binding.edtPriceMin.text?.isNotEmpty() == true){
               val result =  Filter(priceMax = binding.edtPriceMax.text.toString(), priceMin = binding.edtPriceMin.text.toString())
               setFragmentResult("requestKey", bundleOf("filter" to result))
           }
           else{
               setFragmentResult("requestKey", bundleOf("filter" to null))
           }
           dismiss()
       }
       binding.tvClose.setOnClickListener {
           setFragmentResult("requestKey", bundleOf("filter" to null))
           dismiss()
       }
   }
    fun getValue(idRbRam: Int, idRbStorage: Int): Filter {
        return if(idRbRam!=-1&&idRbStorage!=-1){
            val rbRam: RadioButton = (requireView().findViewById(idRbRam) as RadioButton)
            val rbStorage: RadioButton = (requireView().findViewById(idRbStorage) as RadioButton)
            Filter(rbRam.text.toString(), rbStorage.text.toString(), binding.edtPriceMax.text.toString(), binding.edtPriceMin.text.toString())
        }else if(idRbRam!=-1){
            val rbRam: RadioButton = (requireView().findViewById(idRbRam) as RadioButton)
            Filter(rbRam.text.toString(), "", binding.edtPriceMax.text.toString(), binding.edtPriceMin.text.toString())
        }else {
            val rbStorage: RadioButton = (requireView().findViewById(idRbStorage) as RadioButton)
            Filter("", rbStorage.text.toString(), binding.edtPriceMax.text.toString(), binding.edtPriceMin.text.toString())
        }

    }
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        setFragmentResult("requestKey", bundleOf("filter" to null))
    }
}