package com.example.phonestore.viewmodel


import android.accounts.NetworkErrorException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.FormLogin
import com.example.phonestore.model.LoginResponse
import com.example.phonestore.model.Notification
import com.example.phonestore.model.User
import com.example.phonestore.repo.NetworkState
import com.example.phonestore.repo.UserRepo
import com.example.phonestore.services.Constant
import okhttp3.MultipartBody

class UserViewModel: ViewModel() {
    private var userRepo: UserRepo = UserRepo()
    var user: MutableLiveData<User> = MutableLiveData()
    var message:  MutableLiveData<String> = MutableLiveData()
    var status:  MutableLiveData<Boolean> = MutableLiveData()
    var statusSocialNetwork:  MutableLiveData<Boolean> = MutableLiveData()
    var statusChangeAvatar: MutableLiveData<Boolean> = MutableLiveData()
    var listNotification: MutableLiveData<ArrayList<Notification>?> = MutableLiveData()
    val errorLiveData: MutableLiveData<NetworkErrorException> = MutableLiveData()
    val errorLiveDataChangeStatus: MutableLiveData<NetworkErrorException> = MutableLiveData()
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    fun postLogin(user: FormLogin){
        userRepo.callLogin(user, this::onLoginSuccess, this::onError)
    }
    fun postSignUp(user: User){
        userRepo.callSignUp(user, this::onSignUpSuccess, this::onError)
    }
    fun postSignUpSocialNetwork(user: User){
        userRepo.callSignUp(user, this::onSignUpSocialNetworkSuccess, this::onError)
    }
    fun postSignOut(){
        userRepo.callSignOut(this::onSignUpSuccess, this::onError)
    }
    fun getNotification(){
        userRepo.callGetNotification(this::onNotificationSuccess, this::onError)
    }
    fun updateNotification(idNotification: Int?){
        userRepo.callUpdateNotification(idNotification,  this::onError)
    }
    fun deleteNotification(idNotification: Int?){
        userRepo.callDeleteNotification(idNotification, this::onDeleteNotificationSuccess, this::onError)
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
    fun changeAvatar(filePart: MultipartBody.Part){
        userRepo.callChangeAvatar(filePart, this::onChangeAvatarSuccess, this::onError)
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
    private fun onSignUpSocialNetworkSuccess(loginResponse: LoginResponse?){
        statusSocialNetwork.value = loginResponse?.status
    }
    private fun onChangeAvatarSuccess(loginResponse: LoginResponse?){
        message.value = loginResponse?.message
        statusChangeAvatar.value = loginResponse?.status
        Constant.user = loginResponse?.user
    }
    private fun onDeleteNotificationSuccess(b: Boolean?){
        status.value = b
    }
    private fun onNotificationSuccess(list: ArrayList<Notification>?){
        listNotification.value = list
    }
    private fun onError(err: String?){
      message.value = err
    }
}