package com.example.phonestore.model

import android.os.Message
import android.os.Parcel
import android.os.Parcelable

data class Error(
    var code: Int?,
    var message: com.example.phonestore.model.Message?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(Message::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(code)
        parcel.writeParcelable(message, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Error> {
        override fun createFromParcel(parcel: Parcel): Error {
            return Error(parcel)
        }

        override fun newArray(size: Int): Array<Error?> {
            return arrayOfNulls(size)
        }
    }
}