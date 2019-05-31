package jp.co.sgnet.mediaPicker.ui.widget

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import jp.co.sgnet.mediaPicker.R
import jp.co.sgnet.mediaPicker.databinding.MediaGridContentBinding
import jp.co.sgnet.mediaPicker.internal.entity.Item
import jp.co.sgnet.mediaPicker.internal.entity.SelectionSpec

class MediaGrid: SquareFrameLayout, View.OnClickListener {
    private lateinit var media: Item
    private var binding: MediaGridContentBinding
    private lateinit var bindInfo: BindInfo

   var listener: OnMediaGridClickListener? = null

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.media_grid_content, this, true)
    }

    override fun onClick(v: View?) {
        when (v) {
        }
    }

    interface OnMediaGridClickListener {
        fun onCheckViewClicked(thumbnail: ImageView, item: Item, holder: RecyclerView.ViewHolder)
    }

    fun preBindMedia(bindInfo: BindInfo) {
        this.bindInfo = bindInfo
    }

    fun bindMedia(item: Item) {
        media = item
        initCheckView()
        setImage()
        setVideoDuration()
    }

    private fun initCheckView() {
    }

    fun setCheckEnabled(enabled: Boolean) {
    }

    fun setCheckedNum(checkedNum: Int) {
    }

    fun setChecked(checked: Boolean) {
    }

    private fun setVideoDuration() {
        if (media.isVideo()) {
            binding.videoDuration.visibility = VISIBLE
            binding.videoDuration.text = DateUtils.formatElapsedTime(media.duration/ 1000)
        } else {
            binding.videoDuration.visibility = GONE
        }
    }

    private fun setImage() {
        SelectionSpec.imageEngine?.loadThumbnail(context, bindInfo.resize, bindInfo.placeholder, binding.mediaThumbnail, media.uri!!)
    }

    data class BindInfo(val resize: Int,
                        val placeholder: Drawable,
                        val checkViewCountable: Boolean,
                        val viewHolder: RecyclerView.ViewHolder)
}