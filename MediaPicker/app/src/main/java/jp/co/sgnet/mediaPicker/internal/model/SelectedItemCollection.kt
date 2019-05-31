package jp.co.sgnet.mediaPicker.internal.model

import android.content.Context
import android.net.Uri
import android.os.Bundle
import jp.co.sgnet.mediaPicker.internal.entity.IncapableCause
import jp.co.sgnet.mediaPicker.internal.entity.Item
import jp.co.sgnet.mediaPicker.internal.entity.SelectionSpec
import jp.co.sgnet.mediaPicker.internal.utils.PathUtils
import jp.co.sgnet.mediaPicker.internal.utils.PhotoMetadataUtils
import jp.co.sgnet.mediaPicker.ui.widget.CheckView
import java.util.ArrayList
import java.util.LinkedHashSet

/**
 * Empty collection
 */
private const val COLLECTION_UNDEFINED = 0x0
/**
 * Collection only with images
 */
private const val COLLECTION_IMAGE = 0x01
/**
 * Collection only with videos
 */
private const val COLLECTION_VIDEO = 0x01 shl 1

/**
 * Collection with images and videos.
 */
private const val COLLECTION_MIXED = COLLECTION_IMAGE or COLLECTION_VIDEO
private const val STATE_SELECTION = "state_selection"
private const val STATE_COLLECTION_TYPE = "state_collection_type"

class SelectedItemCollection(val context: Context) {
    private var items: MutableSet<Item> = mutableSetOf()
    private var collectionType = COLLECTION_UNDEFINED

    fun onCreate(bundle: Bundle?) {
        if (bundle == null) {
            items = LinkedHashSet<Item>()
        } else {
            val saved = bundle.getParcelableArrayList<Item>(STATE_SELECTION)
            saved?.let {
                items = LinkedHashSet(it)
            }
            collectionType = bundle.getInt(STATE_COLLECTION_TYPE, COLLECTION_UNDEFINED)
        }
    }

    fun setDefaultSelection(uris: List<Item>) {
        items.addAll(uris)
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(STATE_SELECTION, ArrayList<Item>(items))
        outState.putInt(STATE_COLLECTION_TYPE, collectionType)
    }

    fun getDataWithBundle(): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList(STATE_SELECTION, ArrayList<Item>(items))
        bundle.putInt(STATE_COLLECTION_TYPE, collectionType)
        return bundle
    }

    fun add(item: Item): Boolean {
        if (typeConflict(item)) {
            throw IllegalArgumentException("Can't select images and videos at the same time.")
        }
        val added = items.add(item)
        if (added) {
            if (collectionType == COLLECTION_UNDEFINED) {
                if (item.isImage()) {
                    collectionType = COLLECTION_IMAGE
                } else if (item.isVideo()) {
                    collectionType = COLLECTION_VIDEO
                }
            } else if (collectionType == COLLECTION_IMAGE) {
                if (item.isVideo()) {
                    collectionType = COLLECTION_MIXED
                }
            } else if (collectionType == COLLECTION_VIDEO) {
                if (item.isImage()) {
                    collectionType = COLLECTION_MIXED
                }
            }
        }
        return added
    }

    fun remove(item: Item): Boolean {
        val removed = items.remove(item)
        if (removed) {
            if (items.size == 0) {
                collectionType = COLLECTION_UNDEFINED
            } else {
                if (collectionType == COLLECTION_MIXED) {
                    refineCollectionType()
                }
            }
        }
        return removed
    }

    fun removeAll() {
        items.clear()
        resetType()
    }

    private fun resetType() {
        if (items.size == 0) {
            collectionType = COLLECTION_UNDEFINED
        } else {
            if (collectionType == COLLECTION_MIXED) {
                refineCollectionType()
            }
        }
    }

    fun overwrite(items: ArrayList<Item>, collectionType: Int) {
        if (items.size == 0) {
            this.collectionType = COLLECTION_UNDEFINED
        } else {
            this.collectionType = collectionType
        }
        this.items.clear()
        this.items.addAll(items)
    }


    fun asList(): List<Item> {
        return ArrayList<Item>(items)
    }

    fun asListOfUri(): List<Uri> {
        val uris = ArrayList<Uri>()
        for (item in items) {
            item.uri?.let {
                uris.add(it)
            }
        }
        return uris
    }

    fun asListOfString(): List<String> {
        val paths = ArrayList<String>()
        for (item in items) {
            PathUtils.getPath(context, item.uri)?.let {
                paths.add(it)
            }
        }
        return paths
    }

    fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    fun isSelected(item: Item): Boolean {
        return items.contains(item)
    }

    // TODO: impl
    fun isAcceptable(item: Item): IncapableCause? {
//        if (maxSelectableReached()) {
//            val maxSelectable = currentMaxSelectable()
//            var cause: String = ""
//
//            try {
//                cause = context.resources.getQuantityString(
//                    R.string.abc_action_bar_home_description,
//                    maxSelectable,
//                    maxSelectable
//                )
//            } catch (e: Resources.NotFoundException) {
//                cause = mContext.getString(
//                    R.string.error_over_count,
//                    maxSelectable
//                )
//            } catch (e: NoClassDefFoundError) {
//                cause = mContext.getString(
//                    R.string.error_over_count,
//                    maxSelectable
//                )
//            }
//            return IncapableCause(cause)
//        } else if (typeConflict(item)) {
//            return IncapableCause(context.getString(R.string.error_type_conflict))
//        }
        return PhotoMetadataUtils.isAcceptable(context, item)
    }

    fun maxSelectableReached(): Boolean {
        return items.size == currentMaxSelectable()
    }

    // depends
    private fun currentMaxSelectable(): Int {
        val spec = SelectionSpec
        return when {
            spec.maxImageSelectable > 0 -> {
                spec.maxSelectable
            }
            collectionType == COLLECTION_IMAGE -> {
                spec.maxImageSelectable
            }
            collectionType == COLLECTION_VIDEO -> {
                spec.maxVideoSelectable
            } else -> {
                spec.maxSelectable
            }
        }
    }

    fun getCollectionType(): Int {
        return collectionType
    }

    private fun refineCollectionType() {
        var hasImage = false
        var hasVideo = false
        for (i in items) {
            if (i.isImage() && !hasImage) hasImage = true
            if (i.isVideo() && !hasVideo) hasVideo = true
        }
        if (hasImage && hasVideo) {
            collectionType = COLLECTION_MIXED
        } else if (hasImage) {
            collectionType = COLLECTION_IMAGE
        } else if (hasVideo) {
            collectionType = COLLECTION_VIDEO
        }
    }

    /**
     * Determine whether there will be conflict media types. A user can only select images and videos at the same time
     * while [SelectionSpec.mediaTypeExclusive] is set to false.
     */
    fun typeConflict(item: Item): Boolean {
        return SelectionSpec.mediaTypeExclusive && (item.isImage() && (collectionType == COLLECTION_VIDEO || collectionType == COLLECTION_MIXED) || item.isVideo() && (collectionType == COLLECTION_IMAGE || collectionType == COLLECTION_MIXED))
    }

    fun count(): Int {
        return items.size
    }

    fun checkedNumOf(item: Item): Int {
        val index = ArrayList<Item>(items).indexOf(item)
        return if (index == -1) CheckView.UNCHECKED else index + 1
    }
}