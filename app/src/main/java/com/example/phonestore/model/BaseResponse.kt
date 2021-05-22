package com.example.phonestore.model

import androidx.databinding.BaseObservable

abstract class BaseResponse(val status: Any? = null, val message: String? = null, val code: Int? = null) : BaseObservable() {
    fun checkStatus(): Boolean {
        return status == 200.0
    }
    fun isSuccess() = status == true
}