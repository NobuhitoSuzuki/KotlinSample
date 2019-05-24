package com.example.mvvm.adapter

import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

object BindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView , imageUrl: String?) {
        Picasso.get().load(imageUrl).into(imageView)
    }

    @BindingAdapter("visibleGone")
    @JvmStatic
    fun showHide(view: View, visibility: Boolean) {
        if (visibility) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}