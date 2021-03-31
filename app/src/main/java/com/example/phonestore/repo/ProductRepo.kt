package com.example.phonestore.repo

import android.util.Log
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.model.ProductReponse
import com.example.phonestore.model.Supplier
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices

class ProductRepo {
    fun callHotSaleProduct(index: Int, onSuccess: (ArrayList<ProductInfo>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.getTopProduct(index),
                onSuccess = {results -> results?.listProduct?.let { onSuccess.invoke(it) } },
                onError = {message -> onError.invoke(message)}
        )
    }
    fun callSupplier(onSuccess: (ArrayList<Supplier>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getSupplier(),
            onSuccess = {results -> results?.listSupplier?.let { onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callCateProduct(page: Int? = 1, onSuccess: (ArrayList<CateProductInfo>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.getCateProduct(page,5),
                onSuccess = {results -> results?.listCate?.let { onSuccess.invoke(it) } },
                onError = {message -> onError.invoke(message)}
        )
    }

}