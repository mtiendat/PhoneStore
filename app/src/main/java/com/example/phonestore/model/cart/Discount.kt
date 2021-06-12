package com.example.phonestore.model.cart

import android.os.Parcel
import android.os.Parcelable

class Discount(
    var id: Int = 0,
    var discount: Int? = 0,
    var dateEnd: String? ="",
    var content: String? = "",
    var condition: String? =""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeValue(discount)
        parcel.writeString(dateEnd)
        parcel.writeString(content)
        parcel.writeString(condition)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Discount> {
        override fun createFromParcel(parcel: Parcel): Discount {
            return Discount(parcel)
        }

        override fun newArray(size: Int): Array<Discount?> {
            return arrayOfNulls(size)
        }
    }
}