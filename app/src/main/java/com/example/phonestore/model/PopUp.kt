package com.example.phonestore.model

import android.app.Dialog
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.databinding.ViewDataBinding

data class PopUp(
        var popupId: Int = 0,
        var isBottomSheet: Boolean = false,
        var isCancelable: Boolean = true,
        var isMatchParent: Boolean = true,
        var popupBinding: ViewDataBinding? = null,
        var messageQueue: ArrayList<(popupBinding: ViewDataBinding?, view: View?, dialog: Dialog?) -> Unit>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(popupId)
        parcel.writeByte(if (isBottomSheet) 1 else 0)
        parcel.writeByte(if (isCancelable) 1 else 0)
        parcel.writeByte(if (isMatchParent) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PopUp> {
        override fun createFromParcel(parcel: Parcel): PopUp {
            return PopUp(parcel)
        }

        override fun newArray(size: Int): Array<PopUp?> {
            return arrayOfNulls(size)
        }
    }

}