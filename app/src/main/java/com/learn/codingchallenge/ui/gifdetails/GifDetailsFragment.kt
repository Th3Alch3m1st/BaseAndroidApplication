package com.learn.codingchallenge.ui.gifdetails

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.learn.codingchallenge.R
import com.learn.codingchallenge.core.fragment.BaseFragment
import com.learn.codingchallenge.core.glide.GlideApp
import com.learn.codingchallenge.databinding.FragmentGifDetailsBinding
import com.learn.codingchallenge.ui.gifsearch.SearchViewModel
import com.learn.codingchallenge.utils.gone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created By Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class GifDetailsFragment : BaseFragment<SearchViewModel, FragmentGifDetailsBinding>() {
    companion object {
        const val ARG_GIF_TITLE = "ARG_GIF_TITLE"
        const val ARG_GIF_URL = "ARG_GIF_URL"
    }

    private lateinit var gifTitle: String
    private lateinit var gifUrl: String

    private val viewModel: SearchViewModel by viewModels()
    override val layoutResourceId: Int
        get() = R.layout.fragment_gif_details

    override fun getVM(): SearchViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gifTitle = arguments?.getString(ARG_GIF_TITLE, "") ?: ""
        gifUrl = arguments?.getString(ARG_GIF_URL, "") ?: ""
    }

    override fun bindViewModel(binding: FragmentGifDetailsBinding, viewModel: SearchViewModel) {
        with(dataBinding) {
            toolbar.title = gifTitle
            fragmentCommunicator?.setActionBar(toolbar, true)

            GlideApp.with(requireContext())
                .asGif()
                .load(gifUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .listener(imageLoadListener)
                .into(ivGif)
        }
    }

    private val imageLoadListener = object : RequestListener<GifDrawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<GifDrawable>?,
            isFirstResource: Boolean
        ): Boolean {
            dataBinding.progressBar.gone()
            return false
        }

        override fun onResourceReady(
            resource: GifDrawable?,
            model: Any?,
            target: Target<GifDrawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            dataBinding.progressBar.gone()
            return false
        }
    }
}