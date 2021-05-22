package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable

data class ErrorResponse(
    var status: Int?,
    var error: Error?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        TODO("error")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ErrorResponse> {
        override fun createFromParcel(parcel: Parcel): ErrorResponse {
            return ErrorResponse(parcel)
        }

        override fun newArray(size: Int): Array<ErrorResponse?> {
            return arrayOfNulls(size)
        }
    }
}