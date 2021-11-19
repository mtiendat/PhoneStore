package com.example.phonestore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.District
import com.example.phonestore.model.DistrictParam
import com.example.phonestore.model.Location
import com.example.phonestore.model.order.Address
import com.example.phonestore.model.order.Item
import com.example.phonestore.model.order.ListMyAddressResponse
import com.example.phonestore.repo.AddressRepo
import com.google.gson.Gson

class AddressViewModel: ViewModel() {
    var listLocation: MutableLiveData<ArrayList<Location>?> = MutableLiveData()
    var listId: MutableLiveData<ArrayList<String>?> = MutableLiveData()
    var listMyAddress: MutableLiveData<ArrayList<Address>?> = MutableLiveData()
    var messageError: MutableLiveData<String?> = MutableLiveData()
    var status: MutableLiveData<Boolean?> = MutableLiveData()
    var message: MutableLiveData<String?> = MutableLiveData()
    var listDistrict: MutableLiveData<ArrayList<Location>?> = MutableLiveData()
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
    fun getDistrict(code: Int){
        addressRepo.callDistrict(code, this::onSuccess, this::onError)
    }
    fun getWard(code: Int){
        addressRepo.callWard(code, this::onSuccess, this::onError)
    }
    fun getSearchCity(city: String){
        addressRepo.callSearchCity(city, this::onSuccess, this::onError)
    }
    fun getSearchDistrict(district: String){
        addressRepo.callSearchDistrict(district, this::onSuccess, this::onError)
    }

    private fun onSuccess(c: ArrayList<Location>?){
        listLocation.value = c
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
    private fun onSuccessDistrict(a: ArrayList<Location>?){
        listLocation.value = a
    }
    private fun onError(e: String?){
        messageError.value = e
    }
}