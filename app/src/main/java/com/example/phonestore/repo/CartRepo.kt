package com.example.phonestore.repo

import com.example.phonestore.model.Cart
import com.example.phonestore.model.DetailCart
import com.example.phonestore.model.Order
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
    fun order(order: Order, onSuccess: (Boolean?)-> Unit, onError: (String?) -> Unit ){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.order(Constant.idUser, order),
                onSuccess = {results -> onSuccess.invoke(results?.status)},
                onError = {err -> onError.invoke(err)}
        )
    }
}