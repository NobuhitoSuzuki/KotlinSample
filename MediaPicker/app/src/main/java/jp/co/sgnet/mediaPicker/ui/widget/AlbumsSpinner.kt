package jp.co.sgnet.mediaPicker.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.widget.ListPopupWindow
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.CursorAdapter
import android.widget.TextView
import jp.co.sgnet.mediaPicker.R
import jp.co.sgnet.mediaPicker.internal.entitydata.Album

private const val MAX_SHOWN_COUNT = 6

class AlbumsSpinner(context: Context) {
    private var listPopupWindow: ListPopupWindow = ListPopupWindow(context, null, R.attr.listPopupWindowStyle)
    private var adapter: CursorAdapter? = null
    private var selectedTextView: TextView? = null
    private var itemSelectedListener: AdapterView.OnItemSelectedListener? = null

    fun setOnItemSelectedListener(listener: AdapterView.OnItemSelectedListener) {
        this.itemSelectedListener = listener
    }

    init {
        listPopupWindow.isModal = true
        val density = context.resources.displayMetrics.density
        listPopupWindow.setContentWidth((216 * density).toInt())
        listPopupWindow.horizontalOffset = (16 * density).toInt()
        listPopupWindow.verticalOffset = (-48 * density).toInt()
        listPopupWindow.setOnItemClickListener { parent, view, position, id ->
            this.onItemSelected(parent.context, position)
            itemSelectedListener?.onItemSelected(parent, view, position, id)
        }
    }

    private fun onItemSelected(context: Context, position: Int) {
        listPopupWindow.dismiss()
        val cursor = adapter?.cursor
        cursor?.let { c ->
            c.moveToPosition(position)
            val album = Album.valueOf(c)
            val name = album.name
            if (selectedTextView?.visibility == View.VISIBLE) {
                selectedTextView?.text = name
            } else {
                selectedTextView?.visibility = View.VISIBLE
                selectedTextView?.text = name
            }
        }
    }

    fun setAdapter(adapter: CursorAdapter) {
        listPopupWindow.setAdapter(adapter)
        this.adapter = adapter
    }

    fun setSelectedTextView(textView: TextView) {
        selectedTextView = textView
        textView.visibility = View.GONE
        textView.setOnClickListener {
            val itemHeight = it.resources.getDimensionPixelSize(R.dimen.album_item_height)
            adapter?.let { adapter->
                listPopupWindow.height = if (adapter.count > MAX_SHOWN_COUNT) itemHeight * MAX_SHOWN_COUNT else itemHeight * adapter.count
                listPopupWindow.show()
            }
        }
        textView.setOnTouchListener(listPopupWindow.createDragToOpenListener(textView))
    }

    fun setPopupAnchorView(view: View) {
        listPopupWindow.anchorView = view
    }

    fun setSelection(context: Context, position: Int) {
        listPopupWindow.setSelection(position)
        onItemSelected(context, position)
    }
}