package com.example.phonestore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.cart.Cart
import com.example.phonestore.model.cart.CartResponse
import com.example.phonestore.model.cart.DetailCart
import com.example.phonestore.model.cart.Discount
import com.example.phonestore.repo.CartRepo

class CartViewModel: ViewModel() {
    var resultAddToCart: MutableLiveData<Boolean> = MutableLiveData()
    var resultDeleteItem: MutableLiveData<Boolean> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()
    var cartResponse: MutableLiveData<CartResponse> = MutableLiveData()
    var totalProduct: MutableLiveData<Int> = MutableLiveData()
    var totalNotification: MutableLiveData<Int> = MutableLiveData()
    var discount: MutableLiveData<Discount> = MutableLiveData()
    var listProduct: MutableLiveData<ArrayList<Cart>?> = MutableLiveData()
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
    fun deleteItem(idCart: Int? = 0){
        cartRepo.deleteItem(idCart, this::onSuccessDeleteItemCart, this::onError)
    }
    fun updateItem(idCart: Int?, method: String?){
        cartRepo.updateItem(idCart, method, this::onSuccessUpdateItemCart, this::onError)
    }
    private fun onSuccessAddToCart(c: CartResponse?){
        cartResponse.value = c
    }
    private fun onSuccessDeleteItemCart(s: Boolean?){
        resultDeleteItem.value = s
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
    private fun onError(e: String?){
        message.value = e
    }
}