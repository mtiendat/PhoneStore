package com.example.phonestore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.District
import com.example.phonestore.model.order.Address
import com.example.phonestore.model.order.Item
import com.example.phonestore.model.order.ListMyAddressResponse
import com.example.phonestore.repo.AddressRepo

class AddressViewModel: ViewModel() {
    var listCity: MutableLiveData<ArrayList<Item>?> = MutableLiveData()
    var listId: MutableLiveData<ArrayList<String>?> = MutableLiveData()
    var listMyAddress: MutableLiveData<ArrayList<Address>?> = MutableLiveData()
    var messageError: MutableLiveData<String?> = MutableLiveData()
    var status: MutableLiveData<Boolean?> = MutableLiveData()
    var message: MutableLiveData<String?> = MutableLiveData()
    private val addressRepo = AddressRepo()
    fun getListMyAddress(){
        addressRepo.callListMyAddress( this::onSuccessListMyAddress, this::onError)
    }
    fun createMyAddress(address: Address?){
        addressRepo.callCreateMyAddress(address, this::onSuccessMethod, this::onError)
    }
    fun updateMyAddress(id: Int?, address: Address?){
        addressRepo.callUpdateMyAddress(id, address, this::onSuccessMethod, this::onError)
    }
    fun deleteMyAddress(id: Int?){
        addressRepo.callDeleteMyAddress(id, this::onSuccessMethod, this::onError)
    }

    fun getCity(){
        addressRepo.callCity(this::onSuccess, this::onError)
    }
    fun getDistrict(id: String?){
        addressRepo.callDistrict(id, this::onSuccess, this::onError)
    }
    fun getWard(id: String?){
        addressRepo.callWard(id, this::onSuccess, this::onError)
    }
    fun getIDCityAndDistrict(city: String?, district: String?){
        addressRepo.callIdCityAndDistrict(city, district, this::onSuccessIdCityAndDistrict, this::onError)
    }

    private fun onSuccess(c: ArrayList<Item>?){
        listCity.value = c
    }
    private fun onSuccessMethod(b: ListMyAddressResponse?){
        status.value = b?.status
        message.value = b?.message
    }
    private fun onSuccessIdCityAndDistrict(i: ArrayList<String>?){
       listId.value = i
    }
    private fun onSuccessListMyAddress(a: ArrayList<Address>?){
        listMyAddress.value = a
    }
    private fun onError(e: String?){
        messageError.value = e
    }
}