package jp.co.sgnet.mediaPicker

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.AdapterView
import jp.co.sgnet.mediaPicker.databinding.ActivityMainBinding
import jp.co.sgnet.mediaPicker.engine.GlideEngine
import jp.co.sgnet.mediaPicker.internal.entity.SelectionSpec
import jp.co.sgnet.mediaPicker.internal.entitydata.Album
import jp.co.sgnet.mediaPicker.internal.model.AlbumCollection
import jp.co.sgnet.mediaPicker.internal.model.SelectedItemCollection
import jp.co.sgnet.mediaPicker.ui.MediaSelectionFragment
import jp.co.sgnet.mediaPicker.ui.adapter.AlbumsAdapter
import jp.co.sgnet.mediaPicker.ui.widget.AlbumsSpinner
import java.security.Permission

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, MediaSelectionFragment.SelectionProvider {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: AlbumsAdapter
    private lateinit var spinner: AlbumsSpinner
    private lateinit var albumCollection: AlbumCollection
    private var selectedItemCollection: SelectedItemCollection = SelectedItemCollection(this)

    private val callbacks: AlbumCollection.AlbumCallbacks = object : AlbumCollection.AlbumCallbacks {
        override fun onAlbumLoad(cursor: Cursor) {
            adapter.swapCursor(cursor)
            Handler(Looper.getMainLooper()).post {
                cursor.moveToPosition(albumCollection.currentSelection)
                spinner.setSelection(this@MainActivity, albumCollection.currentSelection)
                val album = Album.valueOf(cursor)
                onAlbumSelected(album)
            }
        }

        override fun onAlbumReset() {
            adapter.swapCursor(null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SelectionSpec.hasInited) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return
        }

        SelectionSpec.mimeTypeSet = setOf(MimeType.JPEG)
        SelectionSpec.imageEngine = GlideEngine()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (SelectionSpec.needOrientationRestriction()) {
            requestedOrientation = SelectionSpec.orientation
        }

        adapter = AlbumsAdapter(this, null, false)
        spinner = AlbumsSpinner(this)
        spinner.apply {
            setOnItemSelectedListener(this@MainActivity)
            setSelectedTextView(binding.selectedAlbum)
            setPopupAnchorView(binding.toolbar)
            setAdapter(adapter)
        }

        albumCollection = AlbumCollection(this, callbacks)
        albumCollection.onRestoreInstanceState(savedInstanceState)
        albumCollection.loadAlbums()
    }

    private fun onAlbumSelected(album: Album) {
        if (album.isAll() && album.isEmpty()) {
            binding.container.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.container.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
            val fragment = MediaSelectionFragment.newInstance(album)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, MediaSelectionFragment::class.java.simpleName)
                .commitAllowingStateLoss()
        }
    }

    override fun provideSelectedItemCollection(): SelectedItemCollection {
        return selectedItemCollection
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        albumCollection.currentSelection = position
        adapter.cursor.moveToPosition(position)
        val album = Album.valueOf(adapter.cursor)
        onAlbumSelected(album)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
