package com.learn.codingchallenge.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.learn.codingchallenge.R
import com.learn.codingchallenge.core.glide.GlideApp

/**
 * Created By Rafiqul Hasan
 */

@BindingAdapter("image_url")
fun ImageView.loadImage(url: String?) {
    GlideApp.with(this)
        .asGif()
        .placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder)
        .load(url)
        .into(this)
}