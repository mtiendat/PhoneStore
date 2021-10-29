package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable

class ParamBuyNow(var productInfo: ProductInfo?, var qty: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(ProductInfo::class.java.classLoader),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(productInfo, flags)
        parcel.writeInt(qty)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParamBuyNow> {
        override fun createFromParcel(parcel: Parcel): ParamBuyNow {
            return ParamBuyNow(parcel)
        }

        override fun newArray(size: Int): Array<ParamBuyNow?> {
            return arrayOfNulls(size)
        }
    }
}