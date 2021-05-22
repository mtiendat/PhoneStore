package com.example.phonestore.repo

import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.model.Vote
import com.example.phonestore.model.VoteResponse
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant

class DetailProductRepo {
    fun callCateProductByID(id: Int? = 0, onSuccess: (CateProductInfo?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getCateProductByID(id),
            onSuccess = {results -> onSuccess.invoke(results?.cateProduct)},
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
    fun callRelatedProduct(idCate: Int? = 0, onSuccess: (ArrayList<CateProductInfo>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getRelatedProduct(idCate),
            onSuccess = {results -> onSuccess.invoke(results?.listCate)},
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