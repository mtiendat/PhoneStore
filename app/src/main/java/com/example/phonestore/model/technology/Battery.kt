package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Battery(
    @SerializedName("loai")
    var type: String? ="",
    @SerializedName("dung_luong")
    var capacity: String? ="",
    @SerializedName("cong_nghe")
    var technology: List<Name>? = listOf(),
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Name)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(capacity)
        parcel.writeTypedList(technology)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Battery> {
        override fun createFromParcel(parcel: Parcel): Battery {
            return Battery(parcel)
        }

        override fun newArray(size: Int): Array<Battery?> {
            return arrayOfNulls(size)
        }
    }
}