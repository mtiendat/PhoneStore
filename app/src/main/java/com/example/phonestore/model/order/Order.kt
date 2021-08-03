package com.example.phonestore.model.order

import android.os.Parcel
import android.os.Parcelable
import com.example.phonestore.model.cart.Cart
import com.google.gson.annotations.SerializedName


class Order(
        @SerializedName("id_vc")
        var idVoucher: Int?= null,
        @SerializedName("id_tk_dc")
        var idAccountAddress: Int? = null,
        @SerializedName("id_cn")
        var idStore: Int? = null,
        @SerializedName("pttt")
        var paymentMethod: String? ="",
        @SerializedName("hinhthuc")
        var shippingOption: String? ="",
        @SerializedName("diachigiaohang")
        var address: String? ="",
        @SerializedName("hoten")
        var name: String? ="",
        @SerializedName("sdt")
        var phone: String? ="",
        @SerializedName("tongtien")
        var totalMoney: Int? = 0,
        @SerializedName("infoUser")
        var infoUser: Address? = null,
        var listProduct: ArrayList<Cart> = arrayListOf()
): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readParcelable(Address::class.java.classLoader),
                TODO("listProduct")
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeValue(idVoucher)
                parcel.writeValue(idAccountAddress)
                parcel.writeValue(idStore)
                parcel.writeString(paymentMethod)
                parcel.writeString(shippingOption)
                parcel.writeString(address)
                parcel.writeString(name)
                parcel.writeString(phone)
                parcel.writeValue(totalMoney)
                parcel.writeParcelable(infoUser, flags)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Order> {
                override fun createFromParcel(parcel: Parcel): Order {
                        return Order(parcel)
                }

                override fun newArray(size: Int): Array<Order?> {
                        return arrayOfNulls(size)
                }
        }
}