package com.learn.codingchallenge.network

import com.squareup.moshi.JsonClass

/**
 * Created By Rafiqul Hasan
 */
@JsonClass(generateAdapter = true)
data class Error(
    val message: String? = null,
)