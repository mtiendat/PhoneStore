package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RawRes
import com.google.gson.annotations.SerializedName

class RearCamera(
    @SerializedName("do_phan_giai")
    var pixel: String? ="",
    @SerializedName("quay_phim")
    var video: List<Quality>? = listOf(),
    @SerializedName("den_flash")
    var flash: String? ="",
    @SerializedName("tinh_nang")
    var feature: List<Name>? = listOf(),
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(Quality),
        parcel.readString(),
        parcel.createTypedArrayList(Name)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pixel)
        parcel.writeTypedList(video)
        parcel.writeString(flash)
        parcel.writeTypedList(feature)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RearCamera> {
        override fun createFromParcel(parcel: Parcel): RearCamera {
            return RearCamera(parcel)
        }

        override fun newArray(size: Int): Array<RearCamera?> {
            return arrayOfNulls(size)
        }
    }
}