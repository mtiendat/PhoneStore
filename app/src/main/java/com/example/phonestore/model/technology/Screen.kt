package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Screen(
    @SerializedName("cong_nghe_mh")
    var technology: String? ="",
    @SerializedName("do_phan_giai")
    var pixel: String? ="",
    @SerializedName("ty_le_mh")
    var size: String? ="",
    @SerializedName("kinh_cam_ung")
    var glass: String? ="",
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(technology)
        parcel.writeString(pixel)
        parcel.writeString(size)
        parcel.writeString(glass)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Screen> {
        override fun createFromParcel(parcel: Parcel): Screen {
            return Screen(parcel)
        }

        override fun newArray(size: Int): Array<Screen?> {
            return arrayOfNulls(size)
        }
    }
}