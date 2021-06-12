package com.example.phonestore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.cart.Cart
import com.example.phonestore.model.cart.DetailCart
import com.example.phonestore.repo.CartRepo

class CartViewModel: ViewModel() {
    var resultAddToCart: MutableLiveData<Boolean> = MutableLiveData()
    var resultDeleteItem: MutableLiveData<Boolean> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()
    
    var totalProduct: MutableLiveData<Int> = MutableLiveData()
    var totalNotification: MutableLiveData<Int> = MutableLiveData()
    
    var listProduct: MutableLiveData<ArrayList<DetailCart>?> = MutableLiveData()
    var totalMoney: MutableLiveData<Int> = MutableLiveData()
    private var cartRepo: CartRepo = CartRepo()
    fun addToCart(idProduct: Int?){
        cartRepo.addToCart(idProduct, this::onSuccessAddToCart, this::onError)
    }
    fun getTotalProduct(){
        cartRepo.getTotalProductInCart(this::onSuccessTotalProduct, this::onError)
    }
    fun getTotalNotification(){
        cartRepo.getTotalNotification(this::onSuccessTotalNotification, this::onError)
    }
    fun getMyCart(){
        cartRepo.getMyCart(this::onSuccessMyCart, this::onError)
    }
    fun deleteItem(idProduct: Int? = 0){
        cartRepo.deleteItem(idProduct, this::onSuccessDeleteItemCart, this::onError)
    }

    private fun onSuccessAddToCart(a: Boolean?){
        resultAddToCart.value = a
    }
    private fun onSuccessDeleteItemCart(s: Boolean?){
        resultDeleteItem.value = s
    }

    private fun onSuccessTotalProduct(t: Int?){
        totalProduct.value = t
    }
    private fun onSuccessTotalNotification(p: Int?){
        totalNotification.value = p
    }
    private fun onSuccessMyCart(c: Cart?){
        totalMoney.value = c?.totalMoney
        listProduct.value = c?.listProduct
    }
    private fun onError(e: String?){
        message.value = e
    }
}