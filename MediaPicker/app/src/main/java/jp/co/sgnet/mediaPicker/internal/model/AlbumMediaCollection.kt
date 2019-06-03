package jp.co.sgnet.mediaPicker.internal.model

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import jp.co.sgnet.mediaPicker.internal.entitydata.Album
import jp.co.sgnet.mediaPicker.internal.loader.AlbumMediaLoader
import org.jetbrains.annotations.Nullable
import java.lang.ref.WeakReference

private const val ARGS_ALBUM = "args_album"
private const val ARGS_ENABLE_CAPTURE = "args_enable_capture"
private const val LOADER_ID = 2

class AlbumMediaCollection(private val context: FragmentActivity, private var callbacks: AlbumMediaCallbacks?): LoaderManager.LoaderCallbacks<Cursor> {

    private var loaderManager : LoaderManager = LoaderManager.getInstance(context)

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        var album: Album? = args?.getParcelable(ARGS_ALBUM)
        val enableCapture: Boolean? = args?.getBoolean(ARGS_ENABLE_CAPTURE, false)
        if (album == null) {
            album = Album("","","",0)
        }
        return AlbumMediaLoader.newInstance(context, album, enableCapture!!)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        cursor?.let {
            callbacks?.onAlbumMediaLoad(it)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        callbacks?.onAlbumMediaReset()
    }

    fun load(target: Album) {
        val args = Bundle().apply {
            putParcelable(ARGS_ALBUM, target)
        }
        loaderManager.initLoader(LOADER_ID, args, this)
    }

    fun onDestory() {
        loaderManager.destroyLoader(LOADER_ID)
        callbacks = null
    }

    interface AlbumMediaCallbacks {
        fun onAlbumMediaLoad(cursor: Cursor)
        fun onAlbumMediaReset()
    }
}