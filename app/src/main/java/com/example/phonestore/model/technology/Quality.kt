package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Quality(
    @SerializedName("chat_luong")
var quality: String? ="" ): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(quality)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Quality> {
        override fun createFromParcel(parcel: Parcel): Quality {
            return Quality(parcel)
        }

        override fun newArray(size: Int): Array<Quality?> {
            return arrayOfNulls(size)
        }
    }
}