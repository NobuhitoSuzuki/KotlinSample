package jp.co.sgnet.mediaPicker.internal.loader

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import android.provider.MediaStore
import android.support.v4.content.CursorLoader
import jp.co.sgnet.mediaPicker.internal.entity.Item
import jp.co.sgnet.mediaPicker.internal.entity.SelectionSpec
import jp.co.sgnet.mediaPicker.internal.entitydata.Album
import jp.co.sgnet.mediaPicker.internal.utils.MediaStoreCompat

class AlbumMediaLoader(context: Context,
                       selection: String?,
                       selectionArgs: Array<out String>?,
                       private val enableCapture: Boolean) : CursorLoader(context, QUERY_URI, PROJECTION, selection, selectionArgs, ORDER_BY) {

    override fun loadInBackground(): Cursor? {
        val result = super.loadInBackground()
        if (!enableCapture || !MediaStoreCompat.hasCameraFeature(context)) {
            return result
        }
        val dummy = MatrixCursor(PROJECTION)
        dummy.addRow(arrayOf(Item.ITEM_ID_CAPTURE, Item.ITEM_DISPLAY_NAME_CAPTURE, "", 0, 0))
        return MergeCursor(arrayOf(dummy, result))
    }

    override fun onContentChanged() {
    }

    companion object {
        private val QUERY_URI = MediaStore.Files.getContentUri("external")
        private val PROJECTION = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            "duration"
        )
        // === params for album ALL && showSingleMediaType: false ===
        private const val SELECTION_ALL = (
                "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + " OR "
                        + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                        + " AND " + MediaStore.MediaColumns.SIZE + ">0")
        private val SELECTION_ALL_ARGS = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        // ===========================================================

        // === params for album ALL && showSingleMediaType: true ===
        private const val SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE = (
                MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + " AND " + MediaStore.MediaColumns.SIZE + ">0")

        private fun getSelectionArgsForSingleMediaType(mediaType: Int): Array<String> {
            return arrayOf(mediaType.toString())
        }
        // =========================================================
        // === params for ordinary album && showSingleMediaType: false ===
        private const val SELECTION_ALBUM = (
                "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + " OR "
                        + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                        + " AND "
                        + " bucket_id=?"
                        + " AND " + MediaStore.MediaColumns.SIZE + ">0")

        private fun getSelectionAlbumArgs(albumId: String): Array<String> {
            return arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
                albumId
            )
        }

        // ===============================================================

        // === params for ordinary album && showSingleMediaType: true ===
        private const val SELECTION_ALBUM_FOR_SINGLE_MEDIA_TYPE = (
                MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + " AND "
                        + " bucket_id=?"
                        + " AND " + MediaStore.MediaColumns.SIZE + ">0")

        private fun getSelectionAlbumArgsForSingleMediaType(mediaType: Int, albumId: String): Array<String> {
            return arrayOf(mediaType.toString(), albumId)
        }

        private const val ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC"

        fun newInstance(context: Context, album: Album, capture: Boolean): CursorLoader {
            val selection: String
            val selectionArgs: Array<String>
            val enableCapture: Boolean
            if (album.isAll()) {
                when {
                    SelectionSpec.onlyShowImages()-> {
                        selection =
                            SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE
                        selectionArgs =
                            getSelectionArgsForSingleMediaType(
                                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                            )
                    }
                    SelectionSpec.onlyShowVideos()-> {
                        selection =
                            SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE
                        selectionArgs =
                            getSelectionArgsForSingleMediaType(
                                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                            )
                    } else -> {
                    selection = SELECTION_ALL
                    selectionArgs =
                        SELECTION_ALL_ARGS
                }}
                enableCapture = capture
            } else {
                when {
                    SelectionSpec.onlyShowImages()-> {
                        selection =
                            SELECTION_ALBUM_FOR_SINGLE_MEDIA_TYPE
                        selectionArgs =
                            getSelectionAlbumArgsForSingleMediaType(
                                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                                album.id
                            )
                    }
                    SelectionSpec.onlyShowVideos()-> {
                        selection =
                            SELECTION_ALBUM_FOR_SINGLE_MEDIA_TYPE
                        selectionArgs =
                            getSelectionAlbumArgsForSingleMediaType(
                                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                                album.id
                            )
                    } else -> {
                    selection = SELECTION_ALBUM
                    selectionArgs =
                        getSelectionAlbumArgs(album.id)
                }}
                enableCapture = false
            }
            return AlbumMediaLoader(
                context,
                selection,
                selectionArgs,
                enableCapture
            )
        }
    }
}