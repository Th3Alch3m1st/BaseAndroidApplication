package com.learn.codingchallenge.repository

import androidx.paging.PagingData
import com.learn.codingchallenge.network.model.GifInfo
import io.reactivex.rxjava3.core.Flowable

/**
 * Created By Rafiqul Hasan
 */
interface PagingRepository {
    fun searchGif(searchQuery: String): Flowable<PagingData<GifInfo>>
}