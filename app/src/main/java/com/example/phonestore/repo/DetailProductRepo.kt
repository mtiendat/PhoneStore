package com.example.phonestore.repo

import com.example.phonestore.model.*
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant

class DetailProductRepo {
    fun callSlideshowProduct(id: Int?, onSuccess: (ArrayList<String>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getSlideshowProduct(id),
            onSuccess = {onSuccess.invoke(it?.listImageSlideshow)},
            onError = {onError.invoke(it)}
        )
    }

    fun callDetailProduct(id: Int? = 0, onSuccess: (DetailProduct?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getDetailProduct(id),
            onSuccess = {results -> onSuccess.invoke(results?.detailProduct)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callProductByColor(idCate: Int? = 0, color: String? = "", storage: String? = "", onSuccess: (ArrayList<ProductInfo>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getProductByColor(idCate, color, storage),
            onSuccess = {results -> onSuccess.invoke(results?.listProduct)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callRelatedProduct(idCate: Int? = 0, onSuccess: (ArrayList<ProductInfo>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getRelatedProduct(idCate),
            onSuccess = {results -> onSuccess.invoke(results?.listProduct)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callCompareProduct(price: Int?, onSuccess: (ArrayList<ProductInfo>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getCompareProduct(price),
            onSuccess = {results -> onSuccess.invoke(results?.listProduct)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callListVote(idCate: Int? = 0,all: Boolean?= false, onSuccess: (VoteResponse?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getListVote(idCate, Constant.idUser, all),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun postVote(idCate: Int? = 0, vote: Vote, onSuccess: (Boolean?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.postVote(idCate, vote),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {message -> onError.invoke(message)}
        )
    }
}