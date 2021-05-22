package com.example.phonestore.repo

import com.example.phonestore.model.MyOrder
import com.example.phonestore.model.Order
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant

class OrderRepo {
    fun order(order: Order, onSuccess: (Boolean?)-> Unit, onError: (String?) -> Unit ){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.order(Constant.idUser, order),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {err -> onError.invoke(err)}
        )
    }
    fun getMyOrder(state: String? = "all", onSuccess: (ArrayList<MyOrder>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getMyOrder(Constant.idUser, state),
            onSuccess = {results ->results?.listMyOrder?.let{onSuccess.invoke(it)}},
            onError = {e -> onError.invoke(e)}
        )
    }
    fun getDetailOrder(idOrder: Int? = 0, onSuccess: (ArrayList<ProductOrder>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getDetailOrder(idOrder),
            onSuccess = {results ->results?.listProductOrder.let {onSuccess.invoke(it) }},
            onError = {e -> onError.invoke(e)}
        )
    }
    fun cancelOrder(idOrder: Int? = 0, onSuccess: (Boolean?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.cancelOrder(idOrder),
            onSuccess = {results ->onSuccess.invoke(results?.status)},
            onError = {e -> onError.invoke(e)}
        )
    }
}