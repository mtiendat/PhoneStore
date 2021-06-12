package com.example.phonestore.repo

import com.example.phonestore.model.technology.TechnologyResponse
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.TechnologyServices

class TakeTechnologyRepo {
    fun getTechnology(path: String, onSuccess: (TechnologyResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = TechnologyServices.getInstance()?.getTechnology(path),
            onSuccess = {onSuccess.invoke(it)},
            onError = {onError.invoke(it)}
        )
    }
}