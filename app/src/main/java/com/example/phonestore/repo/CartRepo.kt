package com.example.phonestore.repo

import com.example.phonestore.model.QueueResponse
import com.example.phonestore.model.cart.Cart
import com.example.phonestore.model.cart.CartResponse
import com.example.phonestore.model.cart.VoucherResponse
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant

class CartRepo {
    fun callTotalProductInCart(onSuccess: (Int?)-> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getTotalProductInCart(Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results?.totalProduct)},
            onError = {err -> onError.invoke(err)}
        )
    }
    fun callAddToCart(storage: String?,image: String?, qty: Int?, onSuccess: (CartResponse?)-> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.addToCart(Constant.idUser, storage, image, qty),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {err -> onError.invoke(err)}
        )
    }
    fun callMyCart(onSuccess: (ArrayList<Cart>?)-> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getMyCart(Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results?.cart)},
            onError = {err -> onError.invoke(err)}
        )
    }
    fun callDeleteItem(idCart: Int?= 0 ,onSuccess: (Boolean?)-> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.deleteItem(idCart),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {err -> onError.invoke(err)}
        )
    }
    fun callUpdateItem(idCart: Int?= 0, method: String?, onSuccess: (CartResponse?)-> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.updateProductInCart(idCart, method),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {err -> onError.invoke(err)}
        )
    }
    fun callTotalNotification(onSuccess: (Int)-> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getTotalNotification(Constant.idUser),
            onSuccess = {results -> results?.listNotification?.size?.let { onSuccess.invoke(it) } },
            onError = {e -> onError.invoke(e)}
        )
    }
    fun callMyVoucher(onSuccess: (VoucherResponse?)-> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getMyVoucher(Constant.idUser),
            onSuccess = {onSuccess.invoke(it)},
            onError = {e -> onError.invoke(e)}
        )
    }
    fun callDeleteVoucher(idVoucher: Int?, onSuccess: (VoucherResponse?)-> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getDeleteVoucher(idVoucher),
            onSuccess = {onSuccess.invoke(it)},
            onError = {e -> onError.invoke(e)}
        )
    }
    fun callPostView(onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.postView(),
            onSuccess = {},
            onError = {onError.invoke(it)}
        )
    }
}