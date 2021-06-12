package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable
import com.example.phonestore.model.cart.DetailCart

class ProductOrder(var product: DetailCart? = null, var qty: Int? = 0, var total: Int?  = 0):Parcelable {
    constructor(parcel: Parcel) : this(
            TODO("product"),
            parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(qty)
        parcel.writeValue(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductOrder> {
        override fun createFromParcel(parcel: Parcel): ProductOrder {
            return ProductOrder(parcel)
        }

        override fun newArray(size: Int): Array<ProductOrder?> {
            return arrayOfNulls(size)
        }
    }
}