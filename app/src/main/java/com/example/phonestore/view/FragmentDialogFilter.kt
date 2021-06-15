package com.example.phonestore.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.os.bundleOf
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
        createCheckBox(ramAndStorage?.ramAndStorage?.ram, binding.groupRam)
        createCheckBox(ramAndStorage?.ramAndStorage?.storage, binding.groupStorage)
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
           val result = getValue()
           setFragmentResult("requestKey", bundleOf("filter" to result))
           dismiss()
       }
   }
    fun getValue(): Filter {
        val idRbRam = binding.groupRam.checkedRadioButtonId
        val idRbStorage = binding.groupStorage.checkedRadioButtonId

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
}