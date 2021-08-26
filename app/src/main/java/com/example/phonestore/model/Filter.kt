package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable

class Filter(var ram: String?= null, var storage: String? = null, var priceMax: String? = null, var priceMin: String?= null, var listIDSupplier: List<Int>? = listOf()): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("listIDSupplier")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ram)
        parcel.writeString(storage)
        parcel.writeString(priceMax)
        parcel.writeString(priceMin)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Filter> {
        override fun createFromParcel(parcel: Parcel): Filter {
            return Filter(parcel)
        }

        override fun newArray(size: Int): Array<Filter?> {
            return arrayOfNulls(size)
        }
    }
}