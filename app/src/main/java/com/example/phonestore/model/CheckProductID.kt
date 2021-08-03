package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class CheckProductID(
    @SerializedName("exist")
    var listIDExist: ArrayList<String>? = arrayListOf(),
    @SerializedName("nonExist")
    var listIDNonExist: ArrayList<String>? = arrayListOf(),
    @SerializedName("downOne")
    var listIDDownOne: ArrayList<String>? = arrayListOf(),
): Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("listIDExist"),
        TODO("listIDNonExist"),
        TODO("listIDDownOne")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckProductID> {
        override fun createFromParcel(parcel: Parcel): CheckProductID {
            return CheckProductID(parcel)
        }

        override fun newArray(size: Int): Array<CheckProductID?> {
            return arrayOfNulls(size)
        }
    }
}