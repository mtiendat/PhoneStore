package com.example.phonestore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.CateProductReponse
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.model.Supplier
import com.example.phonestore.repo.ProductRepo

class ProductViewModel: ViewModel() {
    var listProduct: MutableLiveData<ArrayList<ProductInfo>?> = MutableLiveData()
    var listCateProduct: MutableLiveData<ArrayList<CateProductInfo>?> = MutableLiveData()

    var listSupplier: MutableLiveData<ArrayList<Supplier>?> = MutableLiveData()
    var messageError:  MutableLiveData<String> = MutableLiveData()
    private var productRepo: ProductRepo = ProductRepo()
    fun getListHotSaleProduct(){
        if(listProduct.value?.size  == null) { //Nếu đã có dữ liệu thì không call api trong trường hợp back fragment
            productRepo.callHotSaleProduct(1, this::onSuccessListHotSaleProduct, this::onError)
        }
    }
    fun getListSupplier(){
        if(listSupplier.value?.size  == null) {
            productRepo.callSupplier(this::onSuccessSupplier, this::onError)
        }
    }
    fun getListCateProduct(page: Int = 1){
        if(listCateProduct.value?.size  == null ){
            productRepo.callCateProduct(page, this::onSuccessListCateProduct, this::onError)
        }
    }
    fun getMoreListCateProduct(page: Int = 1){
        if(listCateProduct.value?.size  == null|| page > 1) {
            productRepo.callCateProduct(page, this::onSuccessListCateProduct, this::onError)

        }
    }

    private fun onSuccessListHotSaleProduct(list:ArrayList<ProductInfo>?){
        listProduct.value = list
    }
    private fun onSuccessListCateProduct(list: ArrayList<CateProductInfo>?){
        Log.d("jajaaj", "ÁDsada")
        this.listCateProduct.value = list
    }


    private fun onSuccessSupplier(list: ArrayList<Supplier>?){
        this.listSupplier.value = list
    }
    private fun onError(e: String?){
        messageError.value = e
    }
}