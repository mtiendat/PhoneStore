package com.example.phonestore.exception

import com.example.phonestore.model.BaseResponse
import com.example.phonestore.model.ErrorResponse
import com.example.phonestore.services.Constant.CONNECT_ERROR
import com.example.phonestore.services.Constant.NOT_FOUND_API
import com.example.phonestore.services.Constant.REQUEST_TIMEOUT
import com.example.phonestore.services.Constant.SERVER_ERROR
import com.google.gson.Gson

class NetworkErrorException(message: String?, val code: Int? = null) : Exception(message) {
    companion object {
        val DEFAULT_ERROR = NetworkErrorException(SERVER_ERROR)
        val NO_NETWORK = NetworkErrorException(REQUEST_TIMEOUT)
        val NO_CONNECT_TO_SERVER = NetworkErrorException(CONNECT_ERROR)
        val API_DOESNT_EXIST = NetworkErrorException(SERVER_ERROR, NOT_FOUND_API)
        fun newWith(response: BaseResponse?): NetworkErrorException =
            response?.let {
                NetworkErrorException(it.message, it.code)
            } ?: DEFAULT_ERROR

        fun newWithWorker(errorBody: String?): NetworkErrorException {
            return if (errorBody.isNullOrEmpty()) {
                DEFAULT_ERROR
            } else {
                var error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                NetworkErrorException(error.error?.message?.error, error.error?.code)
            }
        }

        fun newWithWorker(): NetworkErrorException {
            return NetworkErrorException("Internal Server Error", 500)
        }

        fun newWithWorker(error: String?, code: Int?): NetworkErrorException {
            return if (error.isNullOrEmpty()) {
                DEFAULT_ERROR
            } else {
                NetworkErrorException(error, code)
            }
        }
    }
}