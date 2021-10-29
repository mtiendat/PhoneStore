package com.example.phonestore.model.cart

import android.os.Parcel
import android.os.Parcelable

class ParamCart(var storage: String? = "", var image: String? ="", var qty: Int?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(storage)
        parcel.writeString(image)
        parcel.writeValue(qty)
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