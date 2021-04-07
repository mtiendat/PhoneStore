package com.example.phonestore.viewmodel

import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.FormLogin
import com.example.phonestore.model.LoginResponse
import com.example.phonestore.model.User
import com.example.phonestore.repo.UserRepo
import com.example.phonestore.services.Constant

class UserViewModel: ViewModel() {
    private var userRepo: UserRepo = UserRepo()
    var user: MutableLiveData<User> = MutableLiveData()
    var message:  MutableLiveData<String> = MutableLiveData()
    var status:  MutableLiveData<Boolean> = MutableLiveData()
    fun postLogin(user: FormLogin){
        userRepo.callLogin(user, this::onLoginSuccess, this::onError)
    }
    fun postSignUp(user: User){
        userRepo.callSignUp(user, this::onSignUpSuccess, this::onError)
    }
    fun postSignOut(){
        userRepo.callSignOut(this::onSignUpSuccess, this::onError)
    }
    fun checkEmail(email: String? =""){
        userRepo.callCheckEmail(email, this::onSignUpSuccess, this::onError)
    }
    fun changePassword(email: String? ="", password: String=""){
        userRepo.callChangePassword(email, password, this::onSignUpSuccess, this::onError)
    }
    fun changeInfoUser(name: String?, phone: String?, address: String?){
        userRepo.callChangeInfoUser(name, phone,address, this::onLoginSuccess, this::onError)
    }
    private fun onLoginSuccess(loginResponse: LoginResponse?){
        message.value = loginResponse?.message
        status.value = loginResponse?.status
        user.value = loginResponse?.user
        loginResponse?.token?.let { Constant.TOKEN = it }

        Constant.idUser = loginResponse?.user?.id ?: 0
        Constant.user = loginResponse?.user
    }
    private fun onSignUpSuccess(loginResponse: LoginResponse?){
        message.value = loginResponse?.message
        status.value = loginResponse?.status
    }
    private fun onError(err: String?){
      message.value = err
    }
}