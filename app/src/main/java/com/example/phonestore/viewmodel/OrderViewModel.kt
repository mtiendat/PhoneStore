package com.example.phonestore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.*
import com.example.phonestore.model.order.*
import com.example.phonestore.model.payment.ZaloPayCreateOderResponse
import com.example.phonestore.model.payment.ZaloPayCreateOrderParam
import com.example.phonestore.repo.OrderRepo
import okhttp3.FormBody
import okhttp3.RequestBody

class OrderViewModel: ViewModel() {
    var listMyOrder: MutableLiveData<ArrayList<MyOrder>?> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()
    var detailOrder: MutableLiveData<DetailOrder> = MutableLiveData()
    var result: MutableLiveData<Boolean?> = MutableLiveData()
    var resultOrder: MutableLiveData<Boolean> = MutableLiveData()
    var listProvince: MutableLiveData<ArrayList<Province>?> = MutableLiveData()
    var listAddressStore: MutableLiveData<ArrayList<AddressStore>?> = MutableLiveData()
    var listCheckProductInStore: MutableLiveData<CheckProductInStoreResponse?> = MutableLiveData()
    var tokenZaloPayOrder: MutableLiveData<String> = MutableLiveData()
    var addressDefault: MutableLiveData<Address?> = MutableLiveData()
    private var orderRepo = OrderRepo()
    fun createOrder(order: Order){
        orderRepo.createOrder(order, this::onSuccessOrder, this::onError)
    }

    fun getMyOrder(state: String? = "all"){
        orderRepo.getMyOrder(state, this::onSuccessMyOrder, this::onError)
    }
    fun getListProductOrder(idOrder: Int? = 0){
        orderRepo.getDetailOrder(idOrder, this::onSuccessDetailOrder, this::onError)
    }
    fun checkProductInStore(idStore: Int?, list: ParamListID){
        orderRepo.callCheckProductInStore(idStore, list, this::onSuccessCheckProductInStore, this::onError)
    }
    fun cancelOrder(idOrder: Int? = 0){
        orderRepo.cancelOrder(idOrder, this::onSuccessCancelOrder, this::onError)
    }
    fun getToken(param: RequestBody){
        orderRepo.sendPost(param, this::onSuccessCreateOrderZaloPay, this::onError)
    }
    fun getProvinceOfStore(){
        orderRepo.getProvince(this::onSuccessProvinceOfStore, this::onError)
    }
    fun getAddressStore(idProvince: Int?){
        orderRepo.getAddressStore(idProvince, this::onSuccessAddressStore, this::onError)
    }
    fun getMyAddress(){
        orderRepo.callMyAddress(this::onSuccessMyAddressDefault, this::onError)
    }
    private fun onSuccessCreateOrderZaloPay(success: ZaloPayCreateOderResponse?){
        tokenZaloPayOrder.value = success?.zptranstoken
    }
    private fun onSuccessOrder(o: Boolean?){
        resultOrder.value = o
    }
    private fun onSuccessMyOrder(list: ArrayList<MyOrder>?){
        listMyOrder.value = list
    }
    private fun onSuccessDetailOrder(detail: DetailOrderResponse?){
        detailOrder.value = detail?.detail
    }
    private fun onSuccessCancelOrder(b: Boolean?){
        result.value = b
    }
    private fun onSuccessProvinceOfStore(p: ProvinceResponse?){
        listProvince.value = p?.list
    }
    private fun onSuccessAddressStore(a: AddressStoreResponse?){
        listAddressStore.value = a?.listAddressStore
    }
    private fun onSuccessMyAddressDefault(a: Address?){
        addressDefault.value = a
    }
    private fun onSuccessCheckProductInStore(c: CheckProductInStoreResponse?){
        listCheckProductInStore.value = c
    }
    fun onError(s: String?){
        message.value = s
    }
}