package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class FrontCamera(
    @SerializedName("do_phan_giai")
    var pixel: String? ="",
    @SerializedName("quay_phim")
    var quality: List<Quality>? = arrayListOf(),
    @SerializedName("den_flash")
    var flash: String? ="",
    @SerializedName("tinh_nang")
    var feature: List<Name>? = listOf()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        TODO("quality"),
        parcel.readString(),
        TODO("feature")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pixel)
        parcel.writeString(flash)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FrontCamera> {
        override fun createFromParcel(parcel: Parcel): FrontCamera {
            return FrontCamera(parcel)
        }

        override fun newArray(size: Int): Array<FrontCamera?> {
            return arrayOfNulls(size)
        }
    }
}