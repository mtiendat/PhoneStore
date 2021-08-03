package com.example.phonestore.model.cart

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Cart(var id: Int?,
           @SerializedName("id_tk")
           var idUser: Int?,
           @SerializedName("id_sp")
           var idProduct: Int?,
           @SerializedName("sl")
           var qty: Int?,
           var name: String? ="",
           var avatar: String? ="",
           var color: String? ="",
           var storage: String? ="",
           var price: Int,
           var priceRoot: Int,
           var discount: Float = 0f,
           var isAvailable: Boolean? = true,
           var isQtyAvailable: Boolean? = true,
           var isChecked: Boolean = true
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(idUser)
        parcel.writeValue(idProduct)
        parcel.writeValue(qty)
        parcel.writeString(name)
        parcel.writeString(avatar)
        parcel.writeString(color)
        parcel.writeString(storage)
        parcel.writeInt(price)
        parcel.writeInt(priceRoot)
        parcel.writeFloat(discount)
        parcel.writeByte(if (isAvailable == true) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }
}