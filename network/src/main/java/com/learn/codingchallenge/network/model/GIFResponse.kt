package com.learn.codingchallenge.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GIFResponse(

    @Json(name = "pagination")
    val pagination: Pagination? = null,

    @Json(name = "data")
    val gifList: List<GifInfo>? = null,
)

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "url")
    val url: String? = null
)

@JsonClass(generateAdapter = true)
data class Images(

    @Json(name = "original")
    val original: Image? = null,

    @Json(name = "fixed_width_downsampled")
    val downsized: Image? = null,
)

@JsonClass(generateAdapter = true)
data class Pagination(

    @Json(name = "offset")
    val offset: Int? = null,

    @Json(name = "total_count")
    val totalCount: Int? = null,

    @Json(name = "count")
    val count: Int? = null
)

@JsonClass(generateAdapter = true)
data class GifInfo(

    @Json(name = "id")
    val id: String? = null,

    @Json(name = "images")
    val images: Images? = null,

    @Json(name = "rating")
    val rating: String? = null,

    @Json(name = "source")
    val source: String? = null,

    @Json(name = "title")
    val title: String? = null
)
