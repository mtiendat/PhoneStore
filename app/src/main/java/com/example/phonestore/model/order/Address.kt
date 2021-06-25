package com.example.phonestore.model.order

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Address(
    var id: Int? = -1,
    @SerializedName("hoten")
    var name: String? ="",
    @SerializedName("id_tk")
    var idUser: Int? = 0,
    @SerializedName("sdt")
    var phone: String? ="",
    @SerializedName("diachi")
    var address: String? ="",
    @SerializedName("phuongxa")
    var ward: String? ="",
    @SerializedName("quanhuyen")
    var district: String? ="",
    @SerializedName("tinhthanh")
    var city: String? ="",
    @SerializedName("macdinh")
    var default: Int? = 0,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeValue(idUser)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(ward)
        parcel.writeString(district)
        parcel.writeString(city)
        parcel.writeValue(default)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }
}