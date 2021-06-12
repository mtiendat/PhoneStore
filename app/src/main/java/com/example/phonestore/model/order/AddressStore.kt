package com.example.phonestore.model.order

import android.os.Parcel
import android.os.Parcelable

class AddressStore(
    var id: Int = 0,
    var address: String? =""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddressStore> {
        override fun createFromParcel(parcel: Parcel): AddressStore {
            return AddressStore(parcel)
        }

        override fun newArray(size: Int): Array<AddressStore?> {
            return arrayOfNulls(size)
        }
    }
}