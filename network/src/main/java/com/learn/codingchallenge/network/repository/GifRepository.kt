package com.learn.codingchallenge.network.repository

import com.learn.codingchallenge.network.model.GIFResponse
import io.reactivex.rxjava3.core.Single

/**
 * Created By Rafiqul Hasan
 */
interface GifRepository {
    fun searchGIF(query: String, offset: Int, pageLimit: Int): Single<GIFResponse>
}