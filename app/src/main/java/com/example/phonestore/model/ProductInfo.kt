package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ProductInfo(var id: Int = 0,
                  @SerializedName("dungluong") var storage: String? = "",
                  @SerializedName("cauhinh") var technology: String? = "",
                  @SerializedName("giamgia") var discount: Float = 0f,
                  @SerializedName("gia") var price: Int = 0,
                  @SerializedName("hinhanh") var img: String?= "",
                  @SerializedName("mausac") var color: String?= "",
                  @SerializedName("tensp") var name: String? = "",
                  @SerializedName("like") var wish: Boolean? = false,
                  @SerializedName("tongluotvote") var totalVote: Float = 0f,
                  @SerializedName("tongdanhgia") var totalJudge: Float = 0f,
                  @SerializedName("id_msp") var idCate: Int? = 0): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(storage)
        parcel.writeString(technology)
        parcel.writeFloat(discount)
        parcel.writeInt(price)
        parcel.writeString(img)
        parcel.writeString(color)
        parcel.writeString(name)
        parcel.writeValue(wish)
        parcel.writeFloat(totalVote)
        parcel.writeFloat(totalJudge)
        parcel.writeValue(idCate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductInfo> {
        override fun createFromParcel(parcel: Parcel): ProductInfo {
            return ProductInfo(parcel)
        }

        override fun newArray(size: Int): Array<ProductInfo?> {
            return arrayOfNulls(size)
        }
    }
}