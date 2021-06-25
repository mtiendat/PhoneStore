package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable
import com.example.phonestore.model.cart.Cart
import com.example.phonestore.model.cart.DetailCart

class ProductOrder(var product: Cart? = null, var qty: Int? = 0):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Cart::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(product, flags)
        parcel.writeValue(qty)

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