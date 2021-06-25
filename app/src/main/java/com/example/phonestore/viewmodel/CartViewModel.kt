package com.example.phonestore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.cart.*
import com.example.phonestore.repo.CartRepo

class CartViewModel: ViewModel() {
    var resultAddToCart: MutableLiveData<Boolean> = MutableLiveData()
    var resultDeleteItem: MutableLiveData<Boolean>? = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()
    var cartResponse: MutableLiveData<CartResponse> = MutableLiveData()
    var totalProduct: MutableLiveData<Int> = MutableLiveData()
    var totalNotification: MutableLiveData<Int> = MutableLiveData()
    var voucher: MutableLiveData<Voucher?> = MutableLiveData()
    var listProduct: MutableLiveData<ArrayList<Cart>?> = MutableLiveData()
    var flag: Int = 0
    var flagDelete: Int = 0
    var listMyVoucher: MutableLiveData<ArrayList<Voucher>?> = MutableLiveData()
    var totalMoney: MutableLiveData<Int> = MutableLiveData()
    var deleteVoucher: MutableLiveData<VoucherResponse?> = MutableLiveData()
    private var cartRepo: CartRepo = CartRepo()
    fun addToCart(idProduct: Int?){
        cartRepo.callAddToCart(idProduct, this::onSuccessAddToCart, this::onError)
    }
    fun getTotalProduct(){
        cartRepo.callTotalProductInCart(this::onSuccessTotalProduct, this::onError)
    }
    fun getTotalNotification(){
        cartRepo.callTotalNotification(this::onSuccessTotalNotification, this::onError)
    }
    fun getMyCart(){
        if(listProduct.value?.size == null){
            cartRepo.callMyCart(this::onSuccessMyCart, this::onError)
        }

    }
    fun deleteItem(idCart: Int? = 0){
        cartRepo.callDeleteItem(idCart, this::onSuccessDeleteItemCart, this::onError)
    }
    fun updateItem(idCart: Int?, method: String?){
        cartRepo.callUpdateItem(idCart, method, this::onSuccessUpdateItemCart, this::onError)
    }
    fun getMyVoucher(){
        cartRepo.callMyVoucher(this::onSuccessMyVoucher, this::onError)
    }
    fun deleteVoucher(idVoucher: Int?){
        cartRepo.callDeleteVoucher(idVoucher, this::onSuccessDeleteVoucher, this::onError)
    }
    private fun onSuccessAddToCart(c: CartResponse?){
        cartResponse.value = c
    }
    private fun onSuccessDeleteItemCart(s: Boolean?){
        resultDeleteItem?.value = s
    }
    private fun onSuccessTotalProduct(t: Int?){
        totalProduct.value = t
    }
    private fun onSuccessUpdateItemCart(u: CartResponse?){
        message.value = u?.message
    }
    private fun onSuccessTotalNotification(p: Int?){
        totalNotification.value = p
    }
    private fun onSuccessMyCart(cart: ArrayList<Cart>?){
        var total = 0
        cart?.forEach {
            total += if(it.qty == 2){
                it.price*2
            }else it.price
        }
        totalMoney.value = total
        listProduct.value = cart

    }
    private fun onSuccessMyVoucher(v: VoucherResponse?){
        listMyVoucher.value = v?.listMyVoucher
    }
    private fun onSuccessDeleteVoucher(v: VoucherResponse?){
        deleteVoucher.value = v
    }
    private fun onError(e: String?){
        message.value = e
    }
}