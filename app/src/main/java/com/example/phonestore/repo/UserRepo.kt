package com.example.phonestore.repo

import com.example.phonestore.model.auth.FormLogin
import com.example.phonestore.model.auth.LoginResponse
import com.example.phonestore.model.Notification
import com.example.phonestore.model.auth.User
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant
import okhttp3.MultipartBody

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
    fun callChangePassword(email: String? ="", password: String? ="", onSuccess: (LoginResponse?)->Unit, onError: (String?)->Unit){
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
    fun callChangeAvatar(filePart: MultipartBody.Part, onSuccess: (LoginResponse?)->Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.changeAvatar(Constant.idUser, filePart),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {e -> onError.invoke(e)}
        )
    }
    fun callGetNotification(onSuccess: (ArrayList<Notification>?)-> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getNotification(Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results?.listNotification)},
            onError = {e -> onError.invoke(e)}
        )
    }
    fun callUpdateNotification(idNotification: Int?, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.updateNotification(idNotification),
            onSuccess = {},
            onError = {e -> onError.invoke(e)}
        )
    }
    fun callDeleteNotification(idNotification: Int?, onSuccess: (Boolean?)-> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.deleteNotification(idNotification),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {e -> onError.invoke(e)}
        )
    }

}