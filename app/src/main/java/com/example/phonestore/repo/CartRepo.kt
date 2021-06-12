package com.example.phonestore.repo

import com.example.phonestore.model.cart.Cart
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant

class CartRepo {
    fun getTotalProductInCart(onSuccess: (Int?)-> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getTotalProductInCart(Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results?.totalProduct)},
            onError = {err -> onError.invoke(err)}
        )
    }
    fun addToCart(idProduct: Int?, onSuccess: (Boolean?)-> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.addToCart(idProduct, Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {err -> onError.invoke(err)}
        )
    }
    fun getMyCart(onSuccess: (Cart?)-> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getMyCart(Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results?.cart)},
            onError = {err -> onError.invoke(err)}
        )
    }
    fun deleteItem(idProduct: Int? = 0 ,onSuccess: (Boolean?)-> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.deleteItem(Constant.idUser, idProduct),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {err -> onError.invoke(err)}
        )
    }
    fun getTotalNotification(onSuccess: (Int)-> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getTotalNotification(Constant.idUser),
            onSuccess = {results -> results?.listNotification?.size?.let { onSuccess.invoke(it) } },
            onError = {e -> onError.invoke(e)}
        )
    }

}