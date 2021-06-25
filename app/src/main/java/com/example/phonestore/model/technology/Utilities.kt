package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Utilities(
    @SerializedName("bao_mat")
    var security: List<Name>? = listOf(),
    @SerializedName("tinh_nang_khac")
    var featureOther: List<Name>? = listOf(),
    @SerializedName("ghi_am")
    var record: String? = "",
    @SerializedName("xem_phim")
    var movie: List<Name>? = listOf(),
    @SerializedName("nghe_nhac")
    var music: List<Name>? = listOf(),

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Name),
        parcel.createTypedArrayList(Name),
        parcel.readString(),
        parcel.createTypedArrayList(Name),
        parcel.createTypedArrayList(Name)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(security)
        parcel.writeTypedList(featureOther)
        parcel.writeString(record)
        parcel.writeTypedList(movie)
        parcel.writeTypedList(music)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Utilities> {
        override fun createFromParcel(parcel: Parcel): Utilities {
            return Utilities(parcel)
        }

        override fun newArray(size: Int): Array<Utilities?> {
            return arrayOfNulls(size)
        }
    }
}