package com.example.phonestore.viewmodel

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
    private fun onLoginSuccess(loginResponse: LoginResponse?){
        message.value = loginResponse?.message
        status.value = loginResponse?.status
        user.value = loginResponse?.user
        Constant.TOKEN = loginResponse?.token
        Constant.idUser = loginResponse?.user?.id ?: 0
        Constant.user = loginResponse?.user
    }
    private fun onSignUpSuccess(loginResponse: LoginResponse?){
        message.value = loginResponse?.message
    }
    private fun onError(err: String?){
      message.value = err
    }
}