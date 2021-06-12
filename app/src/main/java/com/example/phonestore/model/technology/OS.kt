package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class OS(
    @SerializedName("HDH")
    var name: String? ="",
    @SerializedName("CPU")
    var cpu: String? ="",
    @SerializedName("CPU_speed")
    var speed: String? ="",
    @SerializedName("GPU")
    var gpu: String? ="",
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(cpu)
        parcel.writeString(speed)
        parcel.writeString(gpu)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OS> {
        override fun createFromParcel(parcel: Parcel): OS {
            return OS(parcel)
        }

        override fun newArray(size: Int): Array<OS?> {
            return arrayOfNulls(size)
        }
    }
}