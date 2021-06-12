package com.example.phonestore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.repo.AllProductRepo

class AllProductViewModel: ViewModel() {
    var listProduct: MutableLiveData<ArrayList<ProductInfo>?> = MutableLiveData()
    var message: MutableLiveData<String?> = MutableLiveData()
    private var allProductRepo = AllProductRepo()

    fun getAllProduct(page: Int?, per_page: Int?){
        allProductRepo.callAllProduct(page, per_page, this::onSuccessListProduct, this::onError)
    }

    private fun onSuccessListProduct(list: ArrayList<ProductInfo>?){
        listProduct.value = list

    }
    private fun onError(e: String?){
        message.value = e
    }

}