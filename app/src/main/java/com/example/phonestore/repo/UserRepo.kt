package com.example.phonestore.repo

import com.example.phonestore.model.FormLogin
import com.example.phonestore.model.LoginResponse
import com.example.phonestore.model.User
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices

class UserRepo {
    fun callLogin(user: FormLogin, onSuccess: (LoginResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.postLogin(user),
                onSuccess = {results -> onSuccess.invoke(results)},
                onError = {results -> onError.invoke(results)}
        )
    }
    fun callSignUp(user: User, onSuccess: (LoginResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.postSignUp(user),
                onSuccess = {results -> onSuccess.invoke(results)},
                onError = {results -> onError.invoke(results)}
        )
    }
}