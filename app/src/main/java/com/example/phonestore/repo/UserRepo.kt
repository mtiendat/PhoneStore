package com.example.phonestore.repo

import com.example.phonestore.model.FormLogin
import com.example.phonestore.model.LoginResponse
import com.example.phonestore.model.User
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant

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
    fun callCheckEmail(email: String? ="", onSuccess: (LoginResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.checkEmail(email),
                onSuccess = {results -> onSuccess.invoke(results)},
                onError = {results -> onError.invoke(results)}
        )
    }
    fun callChangePassword(email: String? ="",password: String? ="", onSuccess: (LoginResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.changePassword(email, password),
                onSuccess = {results -> onSuccess.invoke(results)},
                onError = {results -> onError.invoke(results)}
        )
    }
    fun callSignOut(onSuccess: (LoginResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.signOut(),
                onSuccess = {results -> onSuccess.invoke(results)},
                onError = {e -> onError.invoke(e)}
        )
    }
    fun callChangeInfoUser(name: String?, phone: String?, address: String?, onSuccess: (LoginResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
                call = APIServices.getInstance()?.changeInfoUser(Constant.idUser, name, phone, address),
                onSuccess = {results -> onSuccess.invoke(results)},
                onError = {e -> onError.invoke(e)}
        )
    }
}