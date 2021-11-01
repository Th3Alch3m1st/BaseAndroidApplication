package com.learn.codingchallenge.network

import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created By Rafiqul Hasan
 */
const val SOCKET_TIME_OUT_EXCEPTION =
    "Request timed out while trying to connect. Please ensure you have a strong signal and try again."
const val UNKNOWN_NETWORK_EXCEPTION =
    "An unexpected error has occurred. Please check your network connection and try again."
const val CONNECT_EXCEPTION =
    "Could not connect to the server. Please check your internet connection and try again."
const val UNKNOWN_HOST_EXCEPTION =
    "Couldn't connect to the server at the moment. Please try again in a few minutes."

fun <T : Any> Single<Response<T>>.onResponse(): Single<T> {
    return map { response ->
        if (response.isSuccessful) {
            if (response.body() != null) {
                response.body()!!
            } else {
                throw ApiException(response.code(), null, response.message())
            }
        } else {
            throw ApiException(response.code(), response.errorBody(), response.message())
        }
    }
}

fun <T : Any> Single<T>.onException(): Single<T> {
    return this.onErrorResumeNext { exception ->
        Single.create { emitter ->
            if (exception is ApiException) {
                emitter.onError(
                    ErrorHandler.parseRequestException(
                        exception.statusCode,
                        exception.errorBody,
                        exception.message
                    )
                )
            } else {
                when (exception) {
                    is ConnectException -> {
                        emitter.onError(RequestException(message = CONNECT_EXCEPTION))
                    }
                    is UnknownHostException -> {
                        emitter.onError(RequestException(message = UNKNOWN_HOST_EXCEPTION))
                    }
                    is SocketTimeoutException -> {
                        emitter.onError(RequestException(message = SOCKET_TIME_OUT_EXCEPTION))
                    }
                    is HttpException -> {
                        emitter.onError(RequestException(message = UNKNOWN_NETWORK_EXCEPTION))
                    }
                    else -> {
                        emitter.onError(RequestException(message = UNKNOWN_NETWORK_EXCEPTION))
                    }
                }
            }

        }
    }
}