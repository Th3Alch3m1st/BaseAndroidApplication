package com.learn.codingchallenge.network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

/**
 * Created By Rafiqul Hasan
 */
object ErrorHandler {

    /**
     * parse [RequestException] from [okhttp3.OkHttpClient] response
     */
    fun parseRequestException(
        statusCode: Int,
        errorBody: ResponseBody? = null,
        message: String? = null
    ): RequestException {
        errorBody?.let { body ->
            val moshi = Moshi.Builder()
                .build()
            val jsonAdapter: JsonAdapter<Error> = moshi.adapter(Error::class.java)
            val apiError = jsonAdapter.fromJson(body.string())

            // if error response does not contain any specific message use a generic error message from resource
            return RequestException().apply {
                this.message =
                    apiError?.message ?: UNKNOWN_NETWORK_EXCEPTION
                this.statusCode = statusCode
            }
        }

        message?.let { msg ->
            return RequestException(message = msg)
        }
        return RequestException(message = UNKNOWN_NETWORK_EXCEPTION)
    }
}