package com.learn.codingchallenge.network.datasource

import com.learn.codingchallenge.network.model.GIFResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created By Rafiqul Hasan
 */
interface GifService {
    @GET("v1/gifs/search")
    fun searchGIF(@QueryMap queryMap: Map<String, String>): Single<Response<GIFResponse>>
}