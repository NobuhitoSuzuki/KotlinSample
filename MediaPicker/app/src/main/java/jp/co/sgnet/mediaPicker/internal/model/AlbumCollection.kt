package jp.co.sgnet.mediaPicker.internal.model

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import jp.co.sgnet.mediaPicker.internal.loader.AlbumLoader
import java.lang.ref.WeakReference

private const val LOADER_ID = 1
private const val STATE_CURRENT_SELECTION = "state_current_selection"

class AlbumCollection(val context: Context): LoaderManager.LoaderCallbacks<Cursor> {
    private var loaderManager: LoaderManager? = null
    private var callbacks: AlbumCallbacks? = null
    var currentSelection: Int = 0

    private var loadFinished: Boolean = false

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {
        return AlbumLoader.newInstance(context)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (!loadFinished) {
            loadFinished = true
            data?.let { cursor ->
                callbacks?.onAlbumLoad(cursor)
            }
        }
    }

    override fun onLoaderReset(p0: Loader<Cursor>) {
        callbacks?.onAlbumReset()
    }

    fun loadAlbums() {
        loaderManager?.initLoader(LOADER_ID, null, this)
    }



    interface AlbumCallbacks {
        fun onAlbumLoad(cursor: Cursor)
        fun onAlbumReset()
    }
}