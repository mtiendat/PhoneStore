package com.example.phonestore.repo

import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.model.ProductResponse
import com.example.phonestore.model.RamAndStorageResponse
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant

class AllProductRepo {
    fun callAllProduct(page: Int?, perPage: Int?, onSuccess: (ArrayList<ProductInfo?>?) -> Unit, onError: (String?)-> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getAllProduct(page, perPage),
            onSuccess = {results -> results?.let { onSuccess.invoke(it.listProduct) } },
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callRamAndStorage(onSuccess: (RamAndStorageResponse?) -> Unit, onError: (String?)-> Unit) {
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getRamAndStorage(),
            onSuccess = {results -> results?.let { onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }
    fun filterProduct(page: Int?, perPage: Int?,ram: String?, storage: String?, priceMax: String?, priceMin: String?, onSuccess: (ArrayList<ProductInfo?>?) -> Unit, onError: (String?)-> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.filterProduct( page = page, perPage = perPage, ram = ram, storage = storage, priceMax = priceMax, priceMin = priceMin),
            onSuccess = {results -> results?.let { onSuccess.invoke(it.listProduct) } },
            onError = {message -> onError.invoke(message)}
        )
    }
}