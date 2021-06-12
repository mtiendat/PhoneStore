package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Storage(
    @SerializedName("RAM")
    var ram: String? ="",
    @SerializedName("bo_nho_trong")
    var internal: String? ="",
    @SerializedName("bo_nho_con_lai")
    var hasUse: String? ="",
    @SerializedName("the_nho")
    var sd: String? ="",
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ram)
        parcel.writeString(internal)
        parcel.writeString(hasUse)
        parcel.writeString(sd)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Storage> {
        override fun createFromParcel(parcel: Parcel): Storage {
            return Storage(parcel)
        }

        override fun newArray(size: Int): Array<Storage?> {
            return arrayOfNulls(size)
        }
    }
}