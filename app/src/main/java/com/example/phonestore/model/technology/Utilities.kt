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
        TODO("security"),
        TODO("list"),
        parcel.readString(),
        TODO("movie"),
        TODO("music")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(record)
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