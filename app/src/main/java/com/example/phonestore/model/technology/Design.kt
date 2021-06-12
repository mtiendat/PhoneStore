package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Design(
    @SerializedName("thiet_ke")
    var design: String? ="",
    @SerializedName("chat_lieu")
    var material: String? ="",
    @SerializedName("kich_thuoc")
    var size: String? ="",
    @SerializedName("khoi_luong")
    var weight: String? ="",
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(design)
        parcel.writeString(material)
        parcel.writeString(size)
        parcel.writeString(weight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Design> {
        override fun createFromParcel(parcel: Parcel): Design {
            return Design(parcel)
        }

        override fun newArray(size: Int): Array<Design?> {
            return arrayOfNulls(size)
        }
    }
}