package jp.co.sgnet.mediaPicker.internal.entity

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore
import jp.co.sgnet.mediaPicker.MimeType

class Item : Parcelable {

    val id: Long
    val mimeType: String
    val size: Long
    val uri: Uri?
    val duration: Long

    constructor(id: Long, mimeType: String, size: Long, duration: Long) {
        this.id = id
        this.mimeType = mimeType
        this.size = size
        val uri: Uri = when {
            isImage()-> { MediaStore.Images.Media.EXTERNAL_CONTENT_URI }
            isVideo()-> { MediaStore.Video.Media.EXTERNAL_CONTENT_URI }
            else -> { MediaStore.Files.getContentUri("external") }
        }
        this.uri = ContentUris.withAppendedId(uri, id)
        this.duration = duration
    }

    constructor(p: Parcel) {
        id = p.readLong()
        mimeType = p.readString()
        size = p.readLong()
        uri = p.readParcelable<Uri>(Uri::class.java.classLoader)
        duration = p.readLong()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            it.writeLong(id)
            it.writeString(mimeType)
            it.writeLong(size)
            it.writeParcelable(uri, 0)
            it.writeLong(duration)
        }
    }
    override fun describeContents(): Int = 0

    fun isCapture(): Boolean = id == -1L
    fun isImage(): Boolean = MimeType.isImage(mimeType)
    fun isGif(): Boolean = MimeType.isGif(mimeType)
    fun isVideo(): Boolean =MimeType.isVideo(mimeType)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false
        return id == other.id
                && (mimeType == other.mimeType)
                && (uri != null && uri == other.uri) || (uri == null && other.uri == null)
                && size == other.size
                && duration == other.duration
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + mimeType.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + (uri?.hashCode() ?: 0)
        result = 31 * result + duration.hashCode()
        return result
    }

    companion object {
        @Suppress("unused")
        @JvmField
        var CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
            override fun createFromParcel(source: Parcel): Item {
                return Item(source)
            }

            override fun newArray(size: Int): Array<Item?> {
                return arrayOfNulls(size)
            }
        }

        @JvmStatic
        fun valueOf(cursor: Cursor): Item {
            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
            val mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE))
            val size = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE))
            val duration = cursor.getLong(cursor.getColumnIndex("duration"))
            return Item(id, mimeType, size, duration)
        }

        const val ITEM_ID_CAPTURE: Long = -1
        const val ITEM_DISPLAY_NAME_CAPTURE = "Capture"
    }

}