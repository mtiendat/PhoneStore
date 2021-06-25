package com.example.phonestore.model.cart

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Voucher(
    var id: Int,
    var code: String? ="",
    @SerializedName("noidung")
    var content: String? = "",
    @SerializedName("chietkhau")
    var discount: Int = 0,
    @SerializedName("ngayketthuc")
    var end_date: String? ="",
    @SerializedName("dieukien")
    var condition: Int? = 0,
    var active: Boolean? = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(code)
        parcel.writeString(content)
        parcel.writeInt(discount)
        parcel.writeString(end_date)
        parcel.writeValue(condition)
        parcel.writeValue(active)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Voucher> {
        override fun createFromParcel(parcel: Parcel): Voucher {
            return Voucher(parcel)
        }

        override fun newArray(size: Int): Array<Voucher?> {
            return arrayOfNulls(size)
        }
    }
}