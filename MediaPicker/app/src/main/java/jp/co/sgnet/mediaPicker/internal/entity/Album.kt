package jp.co.sgnet.mediaPicker.internal.entitydata

import android.os.Parcel
import android.os.Parcelable

data class Album(val id: String,
                 val coverPath: String,
                 val name: String,
                 val count: Long) : Parcelable {
    fun isAll(): Boolean = ALBUM_ID_ALL == id
    fun isEmpty(): Boolean = count == 0L

    private constructor(p : Parcel) : this (
        p.readString(),
        p.readString(),
        p.readString(),
        p.readLong()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            dest.writeString(id)
            dest.writeString(coverPath)
            dest.writeString(name)
            dest.writeLong(count)
        }
    }

    override fun describeContents(): Int = 0

    companion object {
        @Suppress("unused")
        @JvmField
        var CREATOR: Parcelable.Creator<Album> = object : Parcelable.Creator<Album> {
            override fun createFromParcel(source: Parcel): Album {
                return Album(source)
            }

            override fun newArray(size: Int): Array<Album?> {
                return arrayOfNulls(size)
            }
        }
        const val ALBUM_ID_ALL = (-1).toString()
        const val ALBUM_NAME_ALL = "All"
    }


}