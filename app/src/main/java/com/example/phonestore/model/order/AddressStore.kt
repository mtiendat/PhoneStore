package com.example.phonestore.model.order

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class AddressStore(
    var id: Int = 0,
    @SerializedName("diachi")
    var address: String? ="",
    @SerializedName("sdt")
    var phone: String? ="",
    @SerializedName("id_tt")
    var idProvince: String? =""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(address)
        parcel.writeString(phone)
        parcel.writeString(idProvince)
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