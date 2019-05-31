package jp.co.sgnet.mediaPicker.ui


import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import jp.co.sgnet.mediaPicker.R
import jp.co.sgnet.mediaPicker.databinding.FragmentMediaSelectionBinding
import jp.co.sgnet.mediaPicker.internal.entity.Item
import jp.co.sgnet.mediaPicker.internal.entitydata.Album
import jp.co.sgnet.mediaPicker.internal.model.SelectedItemCollection
import jp.co.sgnet.mediaPicker.ui.adapter.AlbumMediaAdapter
import kotlinx.android.synthetic.main.activity_base_preview.*

private const val ARG_ALBUM = "album"

class MediaSelectionFragment : Fragment(),
    AlbumMediaAdapter.CheckStateListener, AlbumMediaAdapter.OnMediaClickListener {

    lateinit var binding : FragmentMediaSelectionBinding
    lateinit var adapter: AlbumMediaAdapter
    private var selectionProvider: SelectionProvider? = null
    private var album: Album? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {bundle ->
            album = bundle.getParcelable(ARG_ALBUM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_media_selection, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        album ?: return
        selectionProvider?.let { provider ->
            adapter = AlbumMediaAdapter(requireContext(), provider.provideSelectedItemCollection(), binding.recyclerView)
            adapter.checkStateListener = this
            adapter.mediaClickListener = this

        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SelectionProvider) {
            selectionProvider = context as SelectionProvider
        } else {
            throw IllegalStateException("Context must implement SelectionProvider.")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onUpdate() {
    }

    override fun onMediaClick(album: Album?, item: Item, adapterPosition: Int) {
    }

    companion object {
        @JvmStatic
        fun newInstance(album: Album) =
            MediaSelectionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ALBUM, album)
                }
            }
    }

    interface SelectionProvider {
        fun provideSelectedItemCollection(): SelectedItemCollection
    }
}
