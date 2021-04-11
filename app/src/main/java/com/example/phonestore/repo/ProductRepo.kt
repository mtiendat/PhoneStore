package com.example.phonestore.repo

import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.model.Slideshow
import com.example.phonestore.model.Supplier
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices

class ProductRepo {
    fun callSlideShow( onSuccess: (ArrayList<Slideshow>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getSlideshow(),
            onSuccess = {results -> results?.listSlideshow?.let { onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }
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
    fun callCateProduct(page: Int? = 1,perPage:Int?=5, orderBy: Int? ,onSuccess: (ArrayList<CateProductInfo>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.getCateProduct(page,perPage, orderBy),
                onSuccess = {results -> results?.listCate?.let { onSuccess.invoke(it) } },
                onError = {message -> onError.invoke(message)}
        )
    }
    fun getCateProductBySupplier(page: Int? = 1, idSupplier: Int? = 0, onSuccess: (ArrayList<CateProductInfo>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?. getCateProductBySupplier(page,5, idSupplier),
                onSuccess = {results -> results?.listCate?.let { onSuccess.invoke(it) } },
                onError = {message -> onError.invoke(message)}
        )
    }
    fun getNewCateProductBySupplier( idSupplier: Int? = 0, onSuccess: (ArrayList<CateProductInfo>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?. getNewCateProductBySupplier( idSupplier),
                onSuccess = {results -> results?.listCate?.let { onSuccess.invoke(it) } },
                onError = {message -> onError.invoke(message)}
        )
    }
    fun searchName(q: String? ="", onSuccess: (ArrayList<CateProductInfo>?) -> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.searchName(q),
                onSuccess = {results ->results?.listSearch?.let {onSuccess.invoke(it) } },
                onError = {message -> onError.invoke(message)}
        )
    }
}