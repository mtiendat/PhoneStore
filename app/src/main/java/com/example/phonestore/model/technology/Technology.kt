package com.example.phonestore.model.technology

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Technology(
    @SerializedName("man_hinh")
    var screen: Screen?,
    @SerializedName("camera_sau")
    var rearCamera: RearCamera?,
    @SerializedName("camera_truoc")
    var frontCamera: FrontCamera?,
    @SerializedName("HDH_CPU")
    var os_cpu: OS?,
    @SerializedName("luu_tru")
    var storage: Storage?,
    @SerializedName("ket_noi")
    var network: Network?,
    @SerializedName("thiet_ke_trong_luong")
    var design: Design?,
    @SerializedName("pin")
    var battery: Battery?,
    @SerializedName("tien_ich")
    var utilities: Utilities?,
    @SerializedName("thong_tin_khac")
    var date: DateRelease?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Screen::class.java.classLoader),
        parcel.readParcelable(RearCamera::class.java.classLoader),
        parcel.readParcelable(FrontCamera::class.java.classLoader),
        parcel.readParcelable(OS::class.java.classLoader),
        parcel.readParcelable(Storage::class.java.classLoader),
        parcel.readParcelable(Network::class.java.classLoader),
        parcel.readParcelable(Design::class.java.classLoader),
        parcel.readParcelable(Battery::class.java.classLoader),
        parcel.readParcelable(Utilities::class.java.classLoader),
        parcel.readParcelable(DateRelease::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(screen, flags)
        parcel.writeParcelable(rearCamera, flags)
        parcel.writeParcelable(frontCamera, flags)
        parcel.writeParcelable(os_cpu, flags)
        parcel.writeParcelable(storage, flags)
        parcel.writeParcelable(network, flags)
        parcel.writeParcelable(design, flags)
        parcel.writeParcelable(battery, flags)
        parcel.writeParcelable(utilities, flags)
        parcel.writeParcelable(date, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Technology> {
        override fun createFromParcel(parcel: Parcel): Technology {
            return Technology(parcel)
        }

        override fun newArray(size: Int): Array<Technology?> {
            return arrayOfNulls(size)
        }
    }
}