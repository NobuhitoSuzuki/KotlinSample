package jp.co.sgnet.mediaPicker.internal.entity

import android.content.pm.ActivityInfo
import android.support.annotation.StyleRes
import android.util.ArraySet
import jp.co.sgnet.mediaPicker.MimeType
import jp.co.sgnet.mediaPicker.engine.GlideEngine
import jp.co.sgnet.mediaPicker.engine.ImageEngine

object SelectionSpec {
    var mimeTypeSet: Set<MimeType>? = null
    var mediaTypeExclusive = false
    var showSingleMediaType = false
    @StyleRes
    var orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    var countable = false
    var maxSelectable = 1
    var maxImageSelectable = 0
    var maxVideoSelectable = 0
    var capture: Boolean = false
    var captureStrategy: CaptureStrategy? = null
    var spanCount: Int = 3
    var gridExpectedSize: Int = 0
    var thumbnailScale: Float = 0.5F
    var imageEngine: ImageEngine? = null
    var hasInited: Boolean = false
    var originalable: Boolean = false
    var autoHideToobar: Boolean = false
    var originalMaxSize: Int = 0
    var selectedItem: List<Item>? = null

    private fun reset() {
        mimeTypeSet = null
        mediaTypeExclusive = true
        showSingleMediaType = false
        orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        countable = false
        maxSelectable = 1
        maxImageSelectable = 0
        maxVideoSelectable = 0
        capture = false
        captureStrategy = null
        spanCount = 3
        gridExpectedSize = 0
        thumbnailScale = 0.5f
        imageEngine = GlideEngine()
        hasInited = true
        originalable = false
        autoHideToobar = false
        originalMaxSize = Integer.MAX_VALUE
        selectedItem = null
    }

    fun singleSelectModeEnabled(): Boolean = !countable && (maxSelectable == 1 || (maxImageSelectable == 1 && maxVideoSelectable == 1))
    fun needOrientationRestriction(): Boolean = orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    fun onlyShowImages(): Boolean = showSingleMediaType && mimeTypeSet?.let { MimeType.ofImage().containsAll(it) } ?: false
    fun onlyShowVideos(): Boolean = showSingleMediaType && mimeTypeSet?.let { MimeType.ofVideo().containsAll(it) } ?: false
}