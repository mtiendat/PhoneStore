package com.example.phonestore.repo

import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.model.ProductResponse
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices

class AllProductRepo {
    fun callAllProduct(page: Int?, perPage: Int?, onSuccess: (ArrayList<ProductInfo>?) -> Unit, onError: (String?)-> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getAllProduct(page,perPage),
            onSuccess = {results -> results?.let { onSuccess.invoke(it.listProduct) } },
            onError = {message -> onError.invoke(message)}
        )
    }
}