package com.learn.codingchallenge.network

import okhttp3.ResponseBody

/**
 * Created By Rafiqul Hasan
 */
class RequestException(
    override var message: String = "",
    var statusCode: Int = 0
) : Exception(message)

class ApiException(
    val statusCode: Int,
    val errorBody: ResponseBody?,
    override val message: String
) : Exception(message)
