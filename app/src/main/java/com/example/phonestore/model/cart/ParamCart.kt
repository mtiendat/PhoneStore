package com.example.phonestore.model.cart

import android.os.Parcel
import android.os.Parcelable

class ParamCart(var storage: String? = "", var image: String? =""): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(storage)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParamCart> {
        override fun createFromParcel(parcel: Parcel): ParamCart {
            return ParamCart(parcel)
        }

        override fun newArray(size: Int): Array<ParamCart?> {
            return arrayOfNulls(size)
        }
    }
}