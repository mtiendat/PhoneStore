package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Network(
    @SerializedName("mang_mobile")
    var mobileNetwork : String ?="",
    @SerializedName("SIM")
    var sim : String? ="",
    var wifi : List<Name>? = listOf(),
    @SerializedName("GPS")
    var gps : List<Name>? = listOf(),
    var bluetooth: List<Name>? = listOf(),
    @SerializedName("cong_sac")
    var portCharge : String ?="",
    @SerializedName("jack_tai_nghe")
    var jackPhone : String ?="",
    @SerializedName("ket_noi_khac")
    var ortherNetwork : List<Name>? = listOf(),
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        TODO("wifi"),
        TODO("gps"),
        TODO("bluetooth"),
        parcel.readString(),
        parcel.readString(),
        TODO("ortherNetwork")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mobileNetwork)
        parcel.writeString(sim)
        parcel.writeString(portCharge)
        parcel.writeString(jackPhone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Network> {
        override fun createFromParcel(parcel: Parcel): Network {
            return Network(parcel)
        }

        override fun newArray(size: Int): Array<Network?> {
            return arrayOfNulls(size)
        }
    }
}