package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class DateRelease(
    @SerializedName("thoi_diem_ra_mat")
    var date: String? =""
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DateRelease> {
        override fun createFromParcel(parcel: Parcel): DateRelease {
            return DateRelease(parcel)
        }

        override fun newArray(size: Int): Array<DateRelease?> {
            return arrayOfNulls(size)
        }
    }
}