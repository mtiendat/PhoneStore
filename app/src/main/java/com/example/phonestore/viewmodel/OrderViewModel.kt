package com.example.phonestore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.MyOrder
import com.example.phonestore.model.Order
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.repo.OrderRepo

class OrderViewModel: ViewModel() {
    var listMyOrder: MutableLiveData<ArrayList<MyOrder>> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()
    var listProductOrder: MutableLiveData<ArrayList<ProductOrder>> = MutableLiveData()
    var result: MutableLiveData<Boolean?> = MutableLiveData()
    var resultOrder: MutableLiveData<Boolean> = MutableLiveData()
    private var orderRepo = OrderRepo()
    fun order(order: Order){
        orderRepo.order(order, this::onSuccessOrder, this::onError)
    }

    fun getMyOrder(state: String? = "all"){
        orderRepo.getMyOrder(state, this::onSuccessMyOrder, this::onError)
    }
    fun getListProductOrder(idOrder: Int? = 0){
        orderRepo.getDetailOrder(idOrder, this::onSuccessListProductOrder, this::onError)
    }
    fun cancelOrder(idOrder: Int? = 0){
        orderRepo.cancelOrder(idOrder, this::onSuccessCancelOrder, this::onError)
    }
    private fun onSuccessOrder(o: Boolean?){
        resultOrder.value = o
    }
    private fun onSuccessMyOrder(list: ArrayList<MyOrder>?){
        listMyOrder.value = list
    }
    private fun onSuccessListProductOrder(list: ArrayList<ProductOrder>?){
        listProductOrder.value = list
    }
    private fun onSuccessCancelOrder(b: Boolean?){
        result.value = b
    }
    fun onError(s: String?){
        message.value = s
    }
}