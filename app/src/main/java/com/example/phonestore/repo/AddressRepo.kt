package com.example.phonestore.repo

import com.example.phonestore.model.order.Address
import com.example.phonestore.model.order.Item
import com.example.phonestore.model.order.ListMyAddressResponse
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant

class AddressRepo {
    fun callListMyAddress(onSuccess: (ArrayList<Address>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getListMyAddress(Constant.user?.id),
            onSuccess = {
                it?.let {
                    onSuccess.invoke(it.list)
                }
            },
            onError = {
                it?.let {
                    onError.invoke(it)
                }
            }
        )
    }
    fun callCreateMyAddress(address: Address?, onSuccess: (ListMyAddressResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.createMyAddress(address),
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
    fun callUpdateMyAddress(id: Int?, address: Address?, onSuccess: (ListMyAddressResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.updateMyAddress(id, address),
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
    fun callDeleteMyAddress(id: Int?, onSuccess: (ListMyAddressResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.deleteMyAddress(id),
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
    fun callCity(onSuccess: (ArrayList<Item>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getCity(),
            onSuccess = {
                it?.let {
                    onSuccess.invoke(it.list)
                }
            },
            onError = {
                it?.let {
                    onError.invoke(it)
                }
            }
        )
    }
    fun callDistrict(id: String?, onSuccess: (ArrayList<Item>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getDistrict(id.toString()),
            onSuccess = {
                it?.let {
                    onSuccess.invoke(it.list)
                }
            },
            onError = {
                it?.let {
                    onError.invoke(it)
                }
            }
        )
    }
    fun callWard(id: String?, onSuccess: (ArrayList<Item>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getWard(id.toString()),
            onSuccess = {
                it?.let {
                    onSuccess.invoke(it.list)
                }
            },
            onError = {
                it?.let {
                    onError.invoke(it)
                }
            }
        )
    }
    fun callIdCityAndDistrict(city: String?, district: String?, onSuccess: (ArrayList<String>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getIdCityAndDistrict(city, district),
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