package jp.co.sgnet.mediaPicker.ui.adapter

import android.content.Context
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import jp.co.sgnet.mediaPicker.R
import jp.co.sgnet.mediaPicker.internal.entity.IncapableCause
import jp.co.sgnet.mediaPicker.internal.entity.Item
import jp.co.sgnet.mediaPicker.internal.entity.SelectionSpec
import jp.co.sgnet.mediaPicker.internal.entitydata.Album
import jp.co.sgnet.mediaPicker.internal.model.SelectedItemCollection
import jp.co.sgnet.mediaPicker.ui.widget.CheckView
import jp.co.sgnet.mediaPicker.ui.widget.MediaGrid

class AlbumMediaAdapter(context: Context, private val selectedCollection: SelectedItemCollection, private val recyclerView: RecyclerView) :
    RecyclerViewCursorAdapter<RecyclerView.ViewHolder>(null), MediaGrid.OnMediaGridClickListener {
    private var selectionSpec: SelectionSpec = SelectionSpec
    var checkStateListener: CheckStateListener? = null
    var mediaClickListener: OnMediaClickListener? = null
    private var mImageResize: Int = 0
    private var placeholder: Drawable? = null

    companion object {
        const val VIEW_TYPE_CAPTURE = 0X01
        const val VIEW_TYPE_MEDIA = 0X02
    }

    init {
        val ta = context.theme.obtainStyledAttributes(intArrayOf(android.R.color.holo_red_dark))
        placeholder = ta.getDrawable(0)
        ta.recycle()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_media_grid, parent, false)
        return MediaViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, cursor: Cursor) {
        if (holder is MediaViewHolder) {
            val item = Item.valueOf(cursor)
            holder.mMediaGrid.preBindMedia(
                MediaGrid.BindInfo(getImageResize(holder.mMediaGrid.context),
                placeholder!!, selectionSpec.countable, holder))
            holder.mMediaGrid.bindMedia(item)
            holder.mMediaGrid.listener = this
            setCheckStatus(item, holder.mMediaGrid)
        }
    }

    override fun getItemViewType(position: Int, cursor: Cursor) = if (Item.valueOf(cursor).isCapture()) VIEW_TYPE_CAPTURE else VIEW_TYPE_MEDIA

    private fun getImageResize(context: Context): Int {
        if (mImageResize != 0) {
            return mImageResize
        }

        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val spanCount = layoutManager.spanCount
        val screenWidth = context.resources.displayMetrics.widthPixels
        val availableWidth = screenWidth - context.resources.getDimensionPixelSize(
            R.dimen.media_grid_spacing) * (spanCount - 1)

        mImageResize = availableWidth / spanCount
        mImageResize = (mImageResize * selectionSpec.thumbnailScale).toInt()
        return mImageResize
    }

    private fun setCheckStatus(item: Item, mediaGrid: MediaGrid) {
        if (selectionSpec.countable) {
            val checkedNum = selectedCollection.checkedNumOf(item)

            if (checkedNum > 0) {
                mediaGrid.setCheckEnabled(true)
                mediaGrid.setCheckedNum(checkedNum)
            } else {
                if (selectedCollection.maxSelectableReached()) {
                    mediaGrid.setCheckEnabled(false)
                    mediaGrid.setCheckedNum(CheckView.UNCHECKED)
                } else {
                    mediaGrid.setCheckEnabled(true)
                    mediaGrid.setCheckedNum(checkedNum)
                }
            }
        } else {
            val selected = selectedCollection.isSelected(item)
            if (selected) {
                mediaGrid.setCheckEnabled(true)
                mediaGrid.setChecked(true)
            } else {
                // single check mode can be reCheck again
                if (selectedCollection.maxSelectableReached() && selectionSpec.maxSelectable != 1) {
                    mediaGrid.setCheckEnabled(false)
                } else {
                    mediaGrid.setCheckEnabled(true)
                }
                mediaGrid.setChecked(false)
            }
        }
    }

    override fun onCheckViewClicked(thumbnail: ImageView, item: Item, holder: RecyclerView.ViewHolder) {
        if (selectionSpec.countable) {
            val checkedNum = selectedCollection.checkedNumOf(item)
            if (checkedNum == CheckView.UNCHECKED) {
                if (assertAddSelection(holder.itemView.context, item)) {
                    selectedCollection.add(item)
                    notifyCheckStateChanged()
                }
            } else {
                selectedCollection.remove(item)
                notifyCheckStateChanged()
            }
        } else {
            if (selectedCollection.isSelected(item)) {
                selectedCollection.remove(item)
                notifyCheckStateChanged()
            } else {
                if (selectionSpec.maxSelectable <= 1) {
                    selectedCollection.removeAll()
                    if (!assertAddSelection(holder.itemView.context, item)) {
                        return
                    }
                    selectedCollection.add(item)
                    notifyCheckStateChanged()
                } else {
                    if (!assertAddSelection(holder.itemView.context, item)) {
                        return
                    }

                    selectedCollection.add(item)
                    notifyCheckStateChanged()
                }
            }
        }
    }

    private fun notifyCheckStateChanged() {
        notifyDataSetChanged()
        if (checkStateListener != null) {
            checkStateListener?.onUpdate()
        }
    }

    private fun assertAddSelection(context: Context, item: Item): Boolean {
        val cause = selectedCollection.isAcceptable(item)
        IncapableCause.handleCause(context, cause)
        return cause == null
    }

    interface CheckStateListener {
        fun onUpdate()
    }

    interface OnMediaClickListener {
        fun onMediaClick(album: Album?, item: Item, adapterPosition: Int)
    }

    interface OnPhotoCapture {
        fun capture()
    }

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mMediaGrid: MediaGrid = itemView as MediaGrid
    }
}