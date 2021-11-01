package com.learn.codingchallenge.utils

import com.learn.codingchallenge.network.RequestException
import com.learn.codingchallenge.network.model.*
import com.learn.codingchallenge.network.repository.GifRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Singleton
class FakeGifRepositoryImpl @Inject constructor() : GifRepository {
    companion object {
        const val TRIGGERED_EMPTY_RESPONSE = "asjdada"
        const val TRIGGERED_VALID_RESPONSE = "funny"
        const val TRIGGERED_ERROR_RESPONSE = "error!!"
        const val MSG_ERROR = "Invalid Token"
        const val MSG_EMPTY = "Sorry, no GIF found"

        const val TEST_INDEX_TO_TAP = 1
        val CLICK_TEST_DATA = GifInfo(
            "100",
            Images(
                Image("https://media2.giphy.com/media/1hqYk0leUMddBBkAM7/giphy.gif?cid=d5fd00167di4ffpy49iyaqkumt4dpzwytwwxku4auzoe2bnb&rid=giphy.gif&ct=g"),
                Image("https://media2.giphy.com/media/1hqYk0leUMddBBkAM7/200w_d.gif?cid=d5fd00167di4ffpy49iyaqkumt4dpzwytwwxku4auzoe2bnb&rid=200w_d.gif&ct=g")
            ),
            "g",
            "",
            "Cracking Up Reaction GIF by MOODMAN"
        )
    }

    var isError = false
    var isRetryTest = false

    override fun searchGIF(query: String, offset: Int, pageLimit: Int): Single<GIFResponse> {
        val pagination = if (isRetryTest) Pagination(
            offset = offset,
            totalCount = 500,
            count = pageLimit
        ) else Pagination(
            offset = offset,
            totalCount = pageLimit,
            count = pageLimit
        )
        val gifResponse = GIFResponse(
            pagination = pagination,
            gifList = getGifData(pageLimit),
        )
        return if (query.contains(TRIGGERED_EMPTY_RESPONSE)) {
            Single.create { emitter ->
                emitter.onSuccess(GIFResponse(pagination, listOf()))
            }
        } else if (query.contains(TRIGGERED_ERROR_RESPONSE) || isError) {
            Single.create { emitter ->
                emitter.onError(RequestException(MSG_ERROR))
            }
        } else {
            Single.create { emitter ->
                emitter.onSuccess(gifResponse)
            }
        }
    }

    private fun getGifData(pageLimit: Int): List<GifInfo> {
        val mockList = mutableListOf<GifInfo>()
        for (i in 0 until pageLimit) {
            when {
                i % 5 == 0 -> {
                    mockList.add(
                        GifInfo(
                            "100",
                            Images(
                                Image("https://media0.giphy.com/media/SggILpMXO7Xt6/giphy.gif?cid=d5fd00167di4ffpy49iyaqkumt4dpzwytwwxku4auzoe2bnb&rid=giphy.gif&ct=g"),
                                Image("https://media0.giphy.com/media/SggILpMXO7Xt6/giphy.gif?cid=d5fd00167di4ffpy49iyaqkumt4dpzwytwwxku4auzoe2bnb&rid=giphy.gif&ct=g")
                            ),
                            "1",
                            "https://www.youtube.com/watch?v=eVw6i4l6xTg",
                            "Funny Face Dog GIF"
                        )
                    )

                }
                i % 5 == 1 -> {
                    mockList.add(CLICK_TEST_DATA)
                }
                i % 5 == 2 -> {
                    mockList.add(
                        GifInfo(
                            "100",
                            Images(
                                Image("https://media3.giphy.com/media/S4H2ZREgH8c2EG6TmV/giphy.gif?cid=d5fd00167di4ffpy49iyaqkumt4dpzwytwwxku4auzoe2bnb&rid=giphy.gif&ct=g"),
                                Image("https://media3.giphy.com/media/S4H2ZREgH8c2EG6TmV/giphy.gif?cid=d5fd00167di4ffpy49iyaqkumt4dpzwytwwxku4auzoe2bnb&rid=giphy.gif&ct=g")
                            ),
                            "1",
                            "",
                            "Reaction Lol GIF by MOODMAN"
                        )
                    )
                }
                i % 5 == 3 -> {
                    mockList.add(
                        GifInfo(
                            "100",
                            Images(
                                Image("https://media2.giphy.com/media/wW95fEq09hOI8/giphy.gif?cid=d5fd00167di4ffpy49iyaqkumt4dpzwytwwxku4auzoe2bnb&rid=giphy.gif&ct=g"),
                                Image("https://media2.giphy.com/media/wW95fEq09hOI8/giphy.gif?cid=d5fd00167di4ffpy49iyaqkumt4dpzwytwwxku4auzoe2bnb&rid=giphy.gif&ct=g")
                            ),
                            "1",
                            "",
                            "Funny Or Die Dancing GIF"
                        )
                    )
                }
                else -> {
                    mockList.add(
                        GifInfo(
                            "100",
                            Images(
                                Image("https://media0.giphy.com/media/2A75RyXVzzSI2bx4Gj/giphy.gif?cid=d5fd00167di4ffpy49iyaqkumt4dpzwytwwxku4auzoe2bnb&rid=giphy.gif&ct=g"),
                                Image("https://media0.giphy.com/media/2A75RyXVzzSI2bx4Gj/200w_d.gif?cid=d5fd00167di4ffpy49iyaqkumt4dpzwytwwxku4auzoe2bnb&rid=200w_d.gif&ct=g")
                            ),
                            "1",
                            "",
                            "R And R Wow GI"
                        )
                    )
                }
            }
        }
        return mockList.toList()
    }
}