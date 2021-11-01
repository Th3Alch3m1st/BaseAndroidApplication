package com.learn.codingchallenge.ui.gifsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.learn.codingchallenge.databinding.SingleItemGifBinding
import com.learn.codingchallenge.network.model.GifInfo
import com.learn.codingchallenge.utils.setSafeOnClickListener

/**
 * Created By Rafiqul Hasan
 */
class GifAdapter(val onItemClicked: (GifInfo) -> Unit?) :
    PagingDataAdapter<GifInfo, GifAdapter.GifViewHolder>(GifComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        return GifViewHolder(
            SingleItemGifBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val gifImageItem = getItem(position)
        gifImageItem?.images?.downsized?.url?.let { gifUrl ->
            holder.bind(gifUrl)
        }
        holder.binding.ivImage.setSafeOnClickListener {
            gifImageItem?.let {
                onItemClicked(gifImageItem)
            }
        }
    }

    inner class GifViewHolder(val binding: SingleItemGifBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) = with(binding) {
            gifUrl = url
        }
    }

    private object GifComparator : DiffUtil.ItemCallback<GifInfo>() {
        override fun areItemsTheSame(oldItem: GifInfo, newItem: GifInfo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GifInfo, newItem: GifInfo) =
            oldItem == newItem
    }
}