package com.example.phonestore.repo

import com.example.phonestore.model.District
import com.example.phonestore.model.DistrictParam
import com.example.phonestore.model.Location
import com.example.phonestore.model.order.Address
import com.example.phonestore.model.order.Item
import com.example.phonestore.model.order.ListMyAddressResponse
import com.example.phonestore.services.APILocation
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
    fun callCity(onSuccess: (ArrayList<Location>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APILocation.getInstance()?.getCity(),
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

    fun callDistrict(code: Int, onSuccess: (ArrayList<Location>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APILocation.getInstance()?.getDistrict(code),
            onSuccess = {
                it?.let {
                    onSuccess.invoke(it.districts)
                }
            },
            onError = {
                it?.let {
                    onError.invoke(it)
                }
            }
        )
    }
    fun callWard(code: Int, onSuccess: (ArrayList<Location>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APILocation.getInstance()?.getWard(code),
            onSuccess = {
                it?.let {
                    onSuccess.invoke(it.wards)
                }
            },
            onError = {
                it?.let {
                    onError.invoke(it)
                }
            }
        )
    }
    fun callSearchCity(name: String, onSuccess: (ArrayList<Location>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APILocation.getInstance()?.searchCity(name),
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
    fun callSearchDistrict(name: String, onSuccess: (ArrayList<Location>?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APILocation.getInstance()?.searchDistrict(name),
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