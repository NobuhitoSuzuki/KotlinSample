package jp.co.sgnet.mediaPicker

import android.content.ContentResolver
import android.net.Uri
import android.util.ArraySet
import android.webkit.MimeTypeMap
import java.util.*

enum class MimeType(private val mimeTypeName: String?,
                    private val extensions: Set<String>) {
    // images
    JPEG("image/jpeg", setOf("jpg", "jpeg")),
    PNG("image/png", setOf("png")),
    GIF("image/gif", setOf("gif")),
    BMP("image/x-ms-bmp", setOf("bmp")),
    WEBP("image/webp", setOf("webp")),

    // videos
    MPEG("video/mpeg", setOf("mpeg", "mpg")),
    MP4("video/mp4", setOf("mp4", "m4v")),
    QUICKTIME("video/quicktime", setOf("mov")),
    THREEGPP("video/3gpp", setOf("3gp", "3gpp")),
    THREEGPP2("video/3gpp2", setOf("3g2", "3gpp2")),
    MKV("video/x-matroska", setOf("mkv")),
    WEBM("video/webm", setOf("webm")),
    TS("video/mp2ts", setOf("ts")),
    AVI("video/avi", setOf("avi"));

    fun checkType(contentResolver: ContentResolver, uri: Uri?): Boolean {
        val map = MimeTypeMap.getSingleton()
        return if (uri == null) {
            false
        } else {
            val type = map.getExtensionFromMimeType(contentResolver.getType(uri))
            val path: String? = null
            var pathParse = false

            for (extension in this.extensions) {
                if (extension == type) {
                    return true
                }
                if (!pathParse) {
                }


            }

            return false
        }
    }

    companion object {
        @JvmStatic
        fun all(): Set<MimeType> = EnumSet.allOf(MimeType::class.java)
        fun of(type: MimeType, vararg rest: MimeType): Set<MimeType> = EnumSet.of(type, *rest)
        fun ofImage(): Set<MimeType> = EnumSet.of(JPEG, PNG, GIF, BMP, WEBP)
        fun ofVideo(): Set<MimeType> = EnumSet.of(MPEG, MP4, QUICKTIME, THREEGPP, THREEGPP2, MKV, WEBM, TS, AVI)
        fun isImage(mimeType: String?): Boolean = mimeType?.startsWith("image") ?: false
        fun isVideo(mimeType: String?): Boolean = mimeType?.startsWith("video") ?: false
        fun isGif(mimeType: String?): Boolean = mimeType?.equals(MimeType.GIF.toString()) ?: false

    }

    override fun toString(): String {
        return mimeTypeName ?: ""
    }
}