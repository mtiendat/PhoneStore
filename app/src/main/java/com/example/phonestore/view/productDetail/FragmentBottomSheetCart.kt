package com.example.phonestore.view.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.phonestore.databinding.FragmentBottomSheetCartBinding
import com.example.phonestore.model.cart.ParamCart
import com.example.phonestore.services.Constant
import com.example.phonestore.services.Constant.IMAGE
import com.example.phonestore.services.Constant.STORAGE
import com.example.phonestore.services.adapter.ChooseProductAdapter
import com.example.phonestore.services.adapter.ChooseStorageAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentBottomSheetCart: BottomSheetDialogFragment(){

    private var binding: FragmentBottomSheetCartBinding?  = null
    private var listStorage: ArrayList<String>? = null
    private var listColor: ArrayList<String>? = null
    private var image: String? = ""
    private var storage: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetCartBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listStorage = arguments?.getStringArrayList(STORAGE)
        listColor= arguments?.getStringArrayList(IMAGE)
        setView()
    }
    private fun setView(){
        var adapterStorage = ChooseStorageAdapter(listStorage){
            storage = it
        }
        binding?.rvChooseStorage?.layoutManager = StaggeredGridLayoutManager(
            4,
            LinearLayoutManager.VERTICAL
        )
        binding?.rvChooseStorage?.adapter = adapterStorage
        var adapterIColor = ChooseProductAdapter(listColor){
            image = it
        }
        binding?.rvChooseColor?.layoutManager = StaggeredGridLayoutManager(
            4,
            LinearLayoutManager.VERTICAL
        )
        binding?.rvChooseColor?.adapter = adapterIColor
        binding?.btnAddToCart?.setOnClickListener {
            if(checkSelectSpinner()){
                NavHostFragment.findNavController(this).apply {
                    previousBackStackEntry?.savedStateHandle?.set(
                        "paramCart",
                        ParamCart(storage, image)
                    )
                    popBackStack()
                }
            }

        }

    }
    private fun checkSelectSpinner(): Boolean{
        var string = ""
        var boolean = true

        if(image.isNullOrEmpty()){
            string = Constant.PLEASE_CHOOSE_COLOR
            boolean = false
        }
        if(storage.isNullOrEmpty()){
            string = Constant.PLEASE_CHOOSE_STORAGE
            boolean = false
        }
        if(image.isNullOrEmpty() && storage.isNullOrEmpty()){
            string ="${Constant.PLEASE_CHOOSE_COLOR} và dung lượng"
            boolean = false
        }
        if(!boolean) makeToast(string)
        return boolean
    }
    private fun makeToast(s: String?){
        context?.let{Toast.makeText(it, s, Toast.LENGTH_SHORT).show()}
    }


}