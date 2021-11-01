package com.learn.codingchallenge.network.repository

import com.learn.codingchallenge.network.datasource.GIFApi
import com.learn.codingchallenge.network.model.GIFResponse
import com.learn.codingchallenge.network.onException
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class GifRepositoryImpl @Inject constructor(private val api: GIFApi) : GifRepository {
    override fun searchGIF(query: String, offset: Int, pageLimit: Int): Single<GIFResponse> {
        return api.searchGIF(query, offset, pageLimit)
            .onException()
    }
}