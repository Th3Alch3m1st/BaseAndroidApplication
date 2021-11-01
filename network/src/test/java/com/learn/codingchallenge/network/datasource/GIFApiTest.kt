package com.learn.codingchallenge.network.datasource

import android.app.SearchManager.QUERY
import com.learn.codingchallenge.network.datasource.GifServiceTest.Companion.END_PAGE_DATA_COUNT
import com.learn.codingchallenge.network.datasource.GifServiceTest.Companion.PAGE_LIMIT
import com.learn.codingchallenge.network.datasource.GifServiceTest.Companion.OFFSET_FOR_END_PAGE
import com.learn.codingchallenge.network.datasource.GifServiceTest.Companion.OFFSET_FOR_PAGE_ONE
import com.learn.codingchallenge.network.testutil.TestUtils
import com.squareup.moshi.Moshi
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created By Rafiqul Hasan
 */
@RunWith(MockitoJUnitRunner::class)
class GIFApiTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var mockGifService: GifService

    private lateinit var sutAPI: GIFApi
    private lateinit var queryMap: Map<String, String>

    @Before
    fun setup() {
        val moshi = Moshi.Builder()
            .build()

        mockGifService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(GifService::class.java)

        sutAPI = GIFApi(mockGifService)
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getGif_successPage1_successReturned() {
        queryMap = mapOf(
            "api_key" to "DfBVgBEsIuBkmypL5mddFsPOe0BYF2MH",
            "q" to GifServiceTest.QUERY,
            "offset" to OFFSET_FOR_PAGE_ONE.toString(),
            "limit" to PAGE_LIMIT.toString(),
        )
        mockWebServer.enqueue(TestUtils.mockResponse("gifPage1.json"))
        sutAPI.searchGIF(QUERY, OFFSET_FOR_PAGE_ONE, PAGE_LIMIT).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                Assertions.assertThat(it.gifList?.size).isEqualTo(PAGE_LIMIT)
                Assertions.assertThat(it.pagination?.count)
                    .isEqualTo(PAGE_LIMIT)
                Assertions.assertThat(it.pagination?.offset)
                    .isEqualTo(OFFSET_FOR_PAGE_ONE)
                return@assertValue true
            }
    }

    @Test
    fun getGif_successPage2_successReturned() {
        queryMap = mapOf(
            "api_key" to "DfBVgBEsIuBkmypL5mddFsPOe0BYF2MH",
            "q" to GifServiceTest.QUERY,
            "offset" to GifServiceTest.OFFSET_FOR_PAGE_TWO.toString(),
            "limit" to PAGE_LIMIT.toString(),
        )
        mockWebServer.enqueue(TestUtils.mockResponse("gifPage2.json"))
        sutAPI.searchGIF(QUERY, OFFSET_FOR_PAGE_ONE, PAGE_LIMIT).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                Assertions.assertThat(it.gifList?.size).isEqualTo(PAGE_LIMIT)
                Assertions.assertThat(it.pagination?.count)
                    .isEqualTo(PAGE_LIMIT)
                Assertions.assertThat(it.pagination?.offset)
                    .isEqualTo(GifServiceTest.OFFSET_FOR_PAGE_TWO)
                return@assertValue true
            }
    }

    @Test
    fun getGif_endPage_successReturned() {
        queryMap = mapOf(
            "api_key" to "DfBVgBEsIuBkmypL5mddFsPOe0BYF2MH",
            "q" to GifServiceTest.QUERY,
            "offset" to GifServiceTest.OFFSET_FOR_PAGE_TWO.toString(),
            "limit" to PAGE_LIMIT.toString(),
        )
        mockWebServer.enqueue(TestUtils.mockResponse("gifPageEnd.json"))
        sutAPI.searchGIF(QUERY, OFFSET_FOR_END_PAGE, PAGE_LIMIT).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                Assertions.assertThat(it.gifList?.size).isEqualTo(END_PAGE_DATA_COUNT)
                Assertions.assertThat(it.pagination?.count)
                    .isEqualTo(END_PAGE_DATA_COUNT)
                Assertions.assertThat(it.pagination?.offset)
                    .isEqualTo(OFFSET_FOR_END_PAGE)
                return@assertValue true
            }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .dispatcher(Dispatcher(TestUtils.immediateExecutorService()))
            .retryOnConnectionFailure(true).build()
    }
}