package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Supplier(var id: Int = 0, @SerializedName("tenncc") var name: String?= "",@SerializedName("anhdaidien")var logoSupplier: String?="", @SerializedName("XacMinh") var auth: Int? =0, var check: Boolean? = false):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(logoSupplier)
        parcel.writeValue(auth)
        parcel.writeValue(check)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Supplier> {
        override fun createFromParcel(parcel: Parcel): Supplier {
            return Supplier(parcel)
        }

        override fun newArray(size: Int): Array<Supplier?> {
            return arrayOfNulls(size)
        }
    }
}