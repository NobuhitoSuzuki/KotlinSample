package jp.co.sgnet.mediaPicker.engine

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

class GlideEngine: ImageEngine {
    override fun loadThumbnail(context: Context, resize: Int, placeholder: Drawable?, imageView: ImageView, uri: Uri) {
        Glide.with(imageView).asBitmap().override(resize, resize).centerCrop().load(uri).into(imageView)
    }

    override fun loadGifThumbnail(context: Context, resize: Int, placeholder: Drawable, imageView: ImageView, uri: Uri) {
    }

    override fun loadImage(context: Context, resizeX: Int, resizeY: Int, imageView: ImageView, uri: Uri) {
    }

    override fun loadGifImage(context: Context, resizeX: Int, resizeY: Int, imageView: ImageView, uri: Uri) {
    }

    override fun supportAnimatedGif(): Boolean {
        return false
    }
}