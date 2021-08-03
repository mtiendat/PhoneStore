package com.example.phonestore.repo

import com.example.phonestore.model.*
import com.example.phonestore.model.order.*
import com.example.phonestore.model.payment.ZaloPayCreateOderResponse
import com.example.phonestore.model.payment.ZaloPayCreateOrderParam
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant
import com.example.phonestore.services.payment.PaymentService
import okhttp3.FormBody
import okhttp3.RequestBody

class OrderRepo {
    fun createOrder(order: Order, onSuccess: (Boolean?)-> Unit, onError: (String?) -> Unit ){
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
    fun getDetailOrder(idOrder: Int? = 0, onSuccess: (DetailOrderResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getDetailOrder(idOrder),
            onSuccess = {results ->results?.let {onSuccess.invoke(it) }},
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
    fun getProvince(onSuccess: (ProvinceResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getProvinceOfStore(),
            onSuccess = {results ->results?.let {onSuccess.invoke(it) }},
            onError = {e -> onError.invoke(e)}
        )
    }
    fun callCheckProductInStore(idStore: Int?, method: String?, list: ParamListHasQty, onSuccess: (CheckProductInStoreResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.checkProduct(idStore,method, list),
            onSuccess = {results ->results?.let {onSuccess.invoke(it) }},
            onError = {e -> onError.invoke(e)}
        )
    }
    fun getAddressStore(idProvince: Int?, onSuccess: (AddressStoreResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getAddressStore(idProvince),
            onSuccess = {results ->results?.let {onSuccess.invoke(it) }},
            onError = {e -> onError.invoke(e)}
        )
    }
    fun sendPost(param: RequestBody, onSuccess: (ZaloPayCreateOderResponse?) -> Unit, onError: (String?)->Unit) {
        APIRequest.callRequest(
            call = PaymentService.getInstance()?.createOrder(param),
            onSuccess = {
                onSuccess.invoke(it)
            },
            onError = {
                onError.invoke(it)
            }
        )
    }
    fun callMyAddress(onSuccess: (Address?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getMyAddress(Constant.idUser),
            onSuccess = {
                it?.let {
                    onSuccess.invoke(it)
                }
            },
            onError = {
                it?.let {
                    onError.invoke(it)
                }
            }
        )
    }
}