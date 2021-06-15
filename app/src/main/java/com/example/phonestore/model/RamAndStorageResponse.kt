package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class RamAndStorageResponse(var status: Boolean = false, var message: String? ="", @SerializedName("data") var ramAndStorage: RamAndStorage?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        TODO("ramAndStorage")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RamAndStorageResponse> {
        override fun createFromParcel(parcel: Parcel): RamAndStorageResponse {
            return RamAndStorageResponse(parcel)
        }

        override fun newArray(size: Int): Array<RamAndStorageResponse?> {
            return arrayOfNulls(size)
        }
    }
}