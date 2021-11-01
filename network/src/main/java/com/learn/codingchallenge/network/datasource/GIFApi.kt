package com.learn.codingchallenge.network.datasource

import com.learn.codingchallenge.network.BuildConfig
import com.learn.codingchallenge.network.model.GIFResponse
import com.learn.codingchallenge.network.onResponse
import com.learn.codingchallenge.network.testing.OpenForTesting
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


/**
 * Created By Rafiqul Hasan
 */

@OpenForTesting
class GIFApi @Inject constructor(private val service: GifService) {
    fun searchGIF(query: String, offset: Int, pageLimit: Int): Single<GIFResponse> {
        val queryMap = mapOf(
            "api_key" to BuildConfig.AUTH_TOKEN,
            "q" to query,
            "offset" to offset.toString(),
            "limit" to pageLimit.toString(),
        )
        return service.searchGIF(queryMap)
            .onResponse()
    }
}