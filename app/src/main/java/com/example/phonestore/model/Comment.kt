package com.example.phonestore.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Comment(
    var id: Int = 0,
    @SerializedName("id_sp")
    var idProduct: Int = 0,
    @SerializedName("listID")
    var listID: ArrayList<Int>? = arrayListOf(),
    @SerializedName("id_tk")
    var idUser: Int = 0,
    @SerializedName("hoten")
    var name: String? ="",
    @SerializedName("anhdaidien")
    var avatar: String? ="",
    @SerializedName("noidung")
    var content: String? = "",
    @SerializedName("thoigian")
    var date: String? ="",
    @SerializedName("soluotthich")
    var like: Int = 0,
    @SerializedName("soluottraloi")
    var totalReply: Int = 0,
    @SerializedName("danhgia")
    var vote: Int = 0,
    @SerializedName("mausac")
    var color: String? ="",
    @SerializedName("dungluong")
    var storage: String? = "",
    @SerializedName("like")
    var hasLike: Boolean = false,
    @SerializedName("dsHinhAnh")
    var listAttachment: ArrayList<Attachment>? = arrayListOf(),
    @SerializedName("dsPhanHoi")
    var listReply: ArrayList<Reply>? = arrayListOf()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        TODO("listID"),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        TODO("listAttachment"),
        TODO("listReply")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(idProduct)
        parcel.writeInt(idUser)
        parcel.writeString(name)
        parcel.writeString(avatar)
        parcel.writeString(content)
        parcel.writeString(date)
        parcel.writeInt(like)
        parcel.writeInt(totalReply)
        parcel.writeInt(vote)
        parcel.writeString(color)
        parcel.writeString(storage)
        parcel.writeByte(if (hasLike) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comment> {
        override fun createFromParcel(parcel: Parcel): Comment {
            return Comment(parcel)
        }

        override fun newArray(size: Int): Array<Comment?> {
            return arrayOfNulls(size)
        }
    }
}