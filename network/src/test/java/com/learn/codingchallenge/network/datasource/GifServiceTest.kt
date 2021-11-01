package com.learn.codingchallenge.network.datasource

import com.learn.codingchallenge.network.testutil.TestUtils.immediateExecutorService
import com.learn.codingchallenge.network.testutil.TestUtils.mockResponse
import com.squareup.moshi.Moshi
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created By Rafiqul Hasan
 */

@RunWith(JUnit4::class)
class GifServiceTest {
    companion object {
        const val QUERY = "funny cat"
        const val PAGE_LIMIT = 10
        const val OFFSET_FOR_PAGE_ONE = 0
        const val OFFSET_FOR_PAGE_TWO = 10
        const val OFFSET_FOR_END_PAGE = 270
        const val END_PAGE_DATA_COUNT = 2
    }

    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var sutGifService: GifService

    private lateinit var queryMap: Map<String, String>

    @Before
    fun setUp() {
        val moshi = Moshi.Builder()
            .build()

        sutGifService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(GifService::class.java)
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }


    @Test
    fun searchGif_pageOneData_pageOneDataReturned() {
        queryMap = mapOf(
            "api_key" to "DfBVgBEsIuBkmypL5mddFsPOe0BYF2MH",
            "q" to QUERY,
            "offset" to OFFSET_FOR_PAGE_ONE.toString(),
            "limit" to PAGE_LIMIT.toString(),
        )
        mockWebServer.enqueue(mockResponse("gifPage1.json"))
        sutGifService.searchGIF(queryMap).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                assertThat(it.body()?.gifList?.size).isEqualTo(PAGE_LIMIT)
                assertThat(it.body()?.pagination?.count).isEqualTo(PAGE_LIMIT)
                assertThat(it.body()?.pagination?.offset).isEqualTo(OFFSET_FOR_PAGE_ONE)
                return@assertValue true
            }
    }

    @Test
    fun searchGif_pageTwoData_pageTwoDataReturned() {
        queryMap = mapOf(
            "api_key" to "DfBVgBEsIuBkmypL5mddFsPOe0BYF2MH",
            "q" to QUERY,
            "offset" to OFFSET_FOR_PAGE_TWO.toString(),
            "limit" to PAGE_LIMIT.toString(),
        )
        mockWebServer.enqueue(mockResponse("gifPage2.json"))
        sutGifService.searchGIF(queryMap).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                assertThat(it.body()?.gifList?.size).isEqualTo(PAGE_LIMIT)
                assertThat(it.body()?.pagination?.count).isEqualTo(PAGE_LIMIT)
                assertThat(it.body()?.pagination?.offset).isEqualTo(OFFSET_FOR_PAGE_TWO)
                return@assertValue true
            }
    }

    @Test
    fun searchGif_EndPage_EndPageDataReturned() {
        queryMap = mapOf(
            "api_key" to "DfBVgBEsIuBkmypL5mddFsPOe0BYF2MH",
            "q" to QUERY,
            "offset" to OFFSET_FOR_PAGE_TWO.toString(),
            "limit" to PAGE_LIMIT.toString(),
        )
        mockWebServer.enqueue(mockResponse("gifPageEnd.json"))
        sutGifService.searchGIF(queryMap).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                assertThat(it.body()?.gifList?.size).isEqualTo(END_PAGE_DATA_COUNT)
                assertThat(it.body()?.pagination?.count).isEqualTo(END_PAGE_DATA_COUNT)
                assertThat(it.body()?.pagination?.offset).isEqualTo(OFFSET_FOR_END_PAGE)
                return@assertValue true
            }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .dispatcher(Dispatcher(immediateExecutorService()))
            .retryOnConnectionFailure(true).build()
    }
}