package com.example.phonestore.view.product

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.phonestore.R
import com.example.phonestore.databinding.FragmentBottomSheetCartBinding
import com.example.phonestore.extendsion.*
import com.example.phonestore.model.ParamBuyNow
import com.example.phonestore.model.PopUp
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.model.cart.Cart
import com.example.phonestore.model.cart.Color
import com.example.phonestore.model.cart.ParamCart
import com.example.phonestore.model.cart.Storage
import com.example.phonestore.services.Constant
import com.example.phonestore.services.Constant.IMAGE
import com.example.phonestore.services.Constant.STORAGE
import com.example.phonestore.services.adapter.ChooseProductAdapter
import com.example.phonestore.services.adapter.ChooseStorageAdapter
import com.example.phonestore.services.widget.PopupDialog
import com.example.phonestore.view.MainActivity
import com.example.phonestore.viewmodel.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentBottomSheetCart: BottomSheetDialogFragment(){

    private var binding: FragmentBottomSheetCartBinding?  = null
    private lateinit var viewModel: ProductViewModel
    private var lStorage: ArrayList<String>? = null
    private var lColor: ArrayList<String>? = null
    private var image: String? = ""
    private var storage: String? = ""
    private var idProduct: Int? = 0
    private val listColor: ArrayList<Color> = arrayListOf()
    private val listStorage: ArrayList<Storage> = arrayListOf()
    private lateinit var adapterIColor: ChooseProductAdapter
    private lateinit var adapterStorage: ChooseStorageAdapter
    private var isColor: Boolean = false
    private var tempStorage: String = ""
    private var tempColor: String = ""
    private var isPlus: Boolean = false
    private var listOfPopupDialogFragment: java.util.ArrayList<PopupDialog?>? = arrayListOf()
    private var action: Boolean? = null
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
        lColor= arguments?.getStringArrayList(IMAGE)
        lStorage = arguments?.getStringArrayList(STORAGE)
        idProduct = arguments?.getInt("idProduct")
        action = arguments?.getBoolean("ACTION_CART")
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        lColor?.forEach {
            listColor.add(Color(it))
        }
        lStorage?.forEach {
            listStorage.add(Storage(it))
        }
        setViewModel()
        setView()
    }
    private fun setViewModel(){
        viewModel.listProduct.observe(this, {
            if(tempStorage != storage){
                refreshListColor()
            }
            tempStorage = storage?:""
            if(tempColor != image){
                refreshListStorage()
            }
            tempColor = image?:""
            it?.forEach { product ->
                if(isColor){
                    for (i in 0 until listStorage.size) {
                        if(product?.storage == listStorage[i].name){
                            listStorage[i].disable = true
                            adapterStorage.notifyItemChanged(i)
                            if(storage == listStorage[i].name){
                                storage = null
                            }
                        }
                    }

                }else{
                    for (i in 0 until listColor.size) {
                        if(product?.img == listColor[i].name){
                            listColor[i].disable = true
                            adapterIColor.notifyItemChanged(i)
                            if(image == listColor[i].name){
                                image = null
                            }
                        }
                    }

                }
            }
            checkButton()
            closePopup()
        })
        viewModel.qtyResponse.observe(this, {
            closePopup()
            if(it?.message?.isNotEmpty() == true) binding?.tvErrorAddToCart?.text = it.message

            if(it?.qty != null){
                if(binding?.qty?.tvNumItem?.text.toString().toInt() > it?.qty!!){
                    binding?.qty?.tvNumItem?.text = it.qty?.toString()
                }else {
                    if(it.qty == 0){
                        binding?.btnAddToCart?.disable()
                        binding?.qty?.btnIncrease?.disableText()
                        binding?.qty?.btnDecrease?.disableText()
                        binding?.qty?.tvNumItem?.setTextColor(ContextCompat.getColor(requireContext(), R.color.dray))
                        binding?.tvErrorAddToCart?.text = "Tạm hết hàng"
                    }else checkMinMaxHasLimit(qtyLimit = it.qty)
                }
            }else checkMinMax()

        })
        viewModel.product.observe(this,{
            closePopup()
            var price = it?.price?.minus((it.price.times(it.discount )))?.toInt()
                ?.times(binding?.qty?.tvNumItem?.text.toString().toInt())
            val product = Cart( null, null,
                idProduct = it?.id,
                qty = binding?.qty?.tvNumItem?.text.toString().toInt(),
                price =it?.price?.minus((it.price.times(it.discount )))?.toInt()?:0,
                color = it?.color,
                storage = it?.storage,
                priceRoot = it?.price?:0,
                name = it?.name,
                avatar = it?.img,
            qtyInWH = 0)
            val productOrder = ProductOrder(product, binding?.qty?.tvNumItem?.text.toString().toInt())
            var productBuyNow: ArrayList<ProductOrder>? = arrayListOf()
            productBuyNow?.add(productOrder)
            val item = bundleOf("listProduct" to productBuyNow, "totalMoney" to price)
            NavHostFragment.findNavController(this).navigate(R.id.action_bottomSheetCart_to_fragmentOrder, item)

        })
        viewModel.price.observe(this, {
            if (it != null) {
                if(it>0)
                    binding?.tvPriceAddToCart?.text = it.toVND()
            }
        })
    }
    private fun checkMinMaxHasLimit(qtyLimit: Int?){
        var number = binding?.qty?.tvNumItem?.text.toString().toInt()
        if(isPlus){
            if(number < qtyLimit?:0){
                binding?.qty?.tvNumItem?.text = (number+1).toString()
            }
        }else if(number>1) binding?.qty?.tvNumItem?.text = (number-1).toString()
    }
    private fun checkMinMax(){
        var number = binding?.qty?.tvNumItem?.text.toString().toInt()
        if(isPlus){
            if(number < 5){
                binding?.qty?.tvNumItem?.text = (number+1).toString()
            }
        }else if(number>1) binding?.qty?.tvNumItem?.text = (number-1).toString()
    }
    private fun refreshListStorage(){
        for (i in 0 until listStorage.size) {
            if(listStorage[i].disable == true){
                listStorage[i].disable = false
                adapterStorage.notifyItemChanged(i)
            }
        }
    }
    private fun refreshListColor(){
        for (i in 0 until listColor.size) {
            if(listColor[i].disable == true){
                listColor[i].disable = false
                adapterIColor.notifyItemChanged(i)
            }
        }
    }
    private fun setView(){
        if(action == false){
            binding?.btnAddToCart?.text = "Mua ngay"
        }
        adapterStorage = ChooseStorageAdapter(listStorage){
            onShowPopup()
            storage = it
            isColor = false
            viewModel.checkQtyProductByColorStorage(idProduct!!, null, it)
            binding?.tvErrorAddToCart?.text = ""
            if(binding?.qty?.tvNumItem?.text.toString().toInt() == 0) binding?.qty?.tvNumItem?.text = "1"
        }
        binding?.rvChooseStorage?.layoutManager = StaggeredGridLayoutManager(
            4,
            LinearLayoutManager.VERTICAL
        )
        binding?.rvChooseStorage?.adapter = adapterStorage

        adapterIColor = ChooseProductAdapter(listColor){
            onShowPopup()
            image = it
            isColor = true
            viewModel.checkQtyProductByColorStorage(idProduct!!, it, null)
            binding?.tvErrorAddToCart?.text = ""
        }
        binding?.rvChooseColor?.layoutManager = StaggeredGridLayoutManager(
            4,
            LinearLayoutManager.VERTICAL
        )
        binding?.rvChooseColor?.adapter = adapterIColor
        binding?.btnAddToCart?.setOnClickListener {
                if(action == true){
                    NavHostFragment.findNavController(this).apply {
                        previousBackStackEntry?.savedStateHandle?.set(
                            "paramCart",
                            ParamCart(storage, image, binding?.qty?.tvNumItem?.text.toString().toInt())
                        )
                        popBackStack()
                    }
                }else {
                    onShowPopup()
                    viewModel.getInfoProduct(image, storage)
                }
        }
        binding?.qty?.btnDecrease?.setOnClickListener {
                onShowPopup()
                viewModel.checkQtyProductInWareHouse(storage = storage, color = image)
                isPlus = false
        }
        binding?.qty?.btnIncrease?.setOnClickListener {
                onShowPopup()
                viewModel.checkQtyProductInWareHouse(storage = storage, color = image)
                isPlus = true

        }
        binding?.ivClose?.setOnClickListener {
            dismiss()
        }
    }
//    private fun checkSelectSpinner(): Boolean{
//        var string = ""
//        var boolean = true
//
//        if(image.isNullOrEmpty()){
//            string = Constant.PLEASE_CHOOSE_COLOR
//            boolean = false
//        }
//        if(storage.isNullOrEmpty()){
//            string = Constant.PLEASE_CHOOSE_STORAGE
//            boolean = false
//        }
//        if(image.isNullOrEmpty() && storage.isNullOrEmpty()){
//            string ="${Constant.PLEASE_CHOOSE_COLOR} và dung lượng"
//            boolean = false
//        }
//        if(!boolean) makeToast(string)
//        return boolean
//    }
    private fun checkButton(){
        var boolean = true
        if(image.isNullOrEmpty()){
            boolean = false
        }
        if(storage.isNullOrEmpty()){
            boolean = false
        }
        if(image.isNullOrEmpty() && storage.isNullOrEmpty()){
            boolean = false
        }
        if(boolean) {
            binding?.btnAddToCart?.enable()
            binding?.qty?.btnIncrease?.enableText()
            binding?.qty?.btnDecrease?.enableText()
            binding?.qty?.tvNumItem?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        } else {
            binding?.btnAddToCart?.disable()
            binding?.qty?.btnIncrease?.disableText()
            binding?.qty?.btnDecrease?.disableText()
            binding?.qty?.tvNumItem?.setTextColor(ContextCompat.getColor(requireContext(), R.color.dray))
        }
    }
    private fun makeToast(s: String?){
        context?.let{Toast.makeText(it, s, Toast.LENGTH_SHORT).show()}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).supportActionBar?.title = arguments?.getString("nameProduct")
    }
    private fun closePopup() {
        binding?.popup?.gone()
    }
     private fun onShowPopup() {
         binding?.popup?.visible()
         binding?.rvChooseColor?.isEnabled = true

    }

}