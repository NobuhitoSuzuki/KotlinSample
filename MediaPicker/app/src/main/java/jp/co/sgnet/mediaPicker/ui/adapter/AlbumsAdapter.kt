package jp.co.sgnet.mediaPicker.ui.adapter

import android.content.Context
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import jp.co.sgnet.mediaPicker.R
import jp.co.sgnet.mediaPicker.databinding.AlbumListItemBinding
import jp.co.sgnet.mediaPicker.internal.entity.SelectionSpec
import jp.co.sgnet.mediaPicker.internal.entitydata.Album
import java.io.File

class AlbumsAdapter(context: Context?, cursor: Cursor?, autoRequery: Boolean): CursorAdapter(context, cursor, autoRequery) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val binding: AlbumListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.album_list_item, parent, false)
        return binding.root
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        if (view != null && cursor != null && context != null) {
            val album: Album = Album.valueOf(cursor)
            val binding = DataBindingUtil.getBinding<AlbumListItemBinding>(view)
            binding?.let {
                binding.albumName.text = album.name
                binding.albumMediaCount.text = album.count.toString()
                SelectionSpec.imageEngine?.loadThumbnail(context, context.resources.getDimensionPixelOffset(R.dimen.media_grid_size), null, binding.albumCover, Uri.fromFile(File(album.coverPath)))
            }
        }
    }
}