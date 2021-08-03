package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Reply(
    var id: Int = 0,
    @SerializedName("id_tk")
    var idUser: Int = 0,
    @SerializedName("hoten")
    var name: String = "",
    @SerializedName("anhdaidien")
    var avatar: String = "",
    @SerializedName("id_dg")
    var idComment: Int = 0,
    @SerializedName("noidung")
    var content: String?="",
    @SerializedName("thoigian")
    var date: String?=""): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(idUser)
        parcel.writeString(name)
        parcel.writeString(avatar)
        parcel.writeInt(idComment)
        parcel.writeString(content)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reply> {
        override fun createFromParcel(parcel: Parcel): Reply {
            return Reply(parcel)
        }

        override fun newArray(size: Int): Array<Reply?> {
            return arrayOfNulls(size)
        }
    }
}