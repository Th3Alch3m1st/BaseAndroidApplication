package com.learn.codingchallenge.network.repository

import com.learn.codingchallenge.network.RequestException
import com.learn.codingchallenge.network.datasource.GIFApi
import com.learn.codingchallenge.network.datasource.GifServiceTest.Companion.END_PAGE_DATA_COUNT
import com.learn.codingchallenge.network.datasource.GifServiceTest.Companion.OFFSET_FOR_END_PAGE
import com.learn.codingchallenge.network.datasource.GifServiceTest.Companion.PAGE_LIMIT
import com.learn.codingchallenge.network.datasource.GifServiceTest.Companion.OFFSET_FOR_PAGE_ONE
import com.learn.codingchallenge.network.datasource.GifServiceTest.Companion.OFFSET_FOR_PAGE_TWO
import com.learn.codingchallenge.network.datasource.GifServiceTest.Companion.QUERY
import com.learn.codingchallenge.network.testutil.TestUtils
import com.learn.codingchallenge.network.testutil.any
import com.learn.codingchallenge.network.testutil.argumentCaptor
import com.learn.codingchallenge.network.testutil.capture
import io.reactivex.rxjava3.core.Single
import org.assertj.core.api.Assertions.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created By Rafiqul Hasan
 */
@RunWith(MockitoJUnitRunner::class)
class GifRepositoryImplTest {
    @Mock
    lateinit var sutApi: GIFApi

    private lateinit var gifRepositoryImpl: GifRepositoryImpl

    @Before
    fun setUp() {
        gifRepositoryImpl = GifRepositoryImpl(sutApi)
    }

    @Test
    fun getGif_argumentPass_argumentPassCorrectly() {
        success()
        val acString = argumentCaptor<String>()
        val acInt = argumentCaptor<Int>()

        sutApi.searchGIF(QUERY, OFFSET_FOR_PAGE_ONE, PAGE_LIMIT)
        verify(sutApi).searchGIF(capture(acString), capture(acInt), capture(acInt))

        assertThat(acString.value).isEqualTo(QUERY)
        assertThat(acInt.allValues[0]).isEqualTo(OFFSET_FOR_PAGE_ONE)
        assertThat(acInt.allValues[1]).isEqualTo(PAGE_LIMIT)
    }

    @Test
    fun getGif_gifResponseSuccess_returnGifResponse() {
        success()

        sutApi.searchGIF(QUERY, OFFSET_FOR_PAGE_ONE, PAGE_LIMIT).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                assertThat(it.gifList?.size).isEqualTo(PAGE_LIMIT)
                assertThat(it.pagination?.count).isEqualTo(PAGE_LIMIT)
                assertThat(it.pagination?.offset).isEqualTo(OFFSET_FOR_PAGE_ONE)
                return@assertValue true
            }
    }

    @Test
    fun getGif_gifSecondPageResponseSuccess_returnGifResponse() {
        successPage2()

        sutApi.searchGIF(QUERY, OFFSET_FOR_PAGE_TWO, PAGE_LIMIT).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                assertThat(it.gifList?.size).isEqualTo(PAGE_LIMIT)
                assertThat(it.pagination?.count).isEqualTo(PAGE_LIMIT)
                assertThat(it.pagination?.offset).isEqualTo(OFFSET_FOR_PAGE_TWO)
                return@assertValue true
            }
    }

    @Test
    fun getGif_gifEndPageResponseSuccess_returnGifResponse() {
        successEndPage()

        sutApi.searchGIF(QUERY, OFFSET_FOR_END_PAGE, PAGE_LIMIT).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                assertThat(it.gifList?.size).isEqualTo(END_PAGE_DATA_COUNT)
                assertThat(it.pagination?.count).isEqualTo(END_PAGE_DATA_COUNT)
                assertThat(it.pagination?.offset).isEqualTo(OFFSET_FOR_END_PAGE)
                return@assertValue true
            }
    }

    @Test
    fun getGif_gifResponseFailure_returnFailure() {
        failure()

        sutApi.searchGIF(QUERY, OFFSET_FOR_PAGE_ONE, PAGE_LIMIT).test()
            .assertError {
                assertThat(it).isInstanceOf(RequestException::class.java)
                val error = it as RequestException
                assertThat(error.message).isEqualTo("Invalid authentication credentials")
                return@assertError true
            }

    }

    private fun success() {
        val testData = TestUtils.getTestData("gifPage1.json")
        `when`(sutApi.searchGIF(any(), anyInt(), anyInt())).thenReturn(Single.create { emitter ->
            emitter.onSuccess(testData)
        })
    }

    private fun successPage2() {
        val testData = TestUtils.getTestData("gifPage2.json")
        `when`(sutApi.searchGIF(any(), anyInt(), anyInt())).thenReturn(Single.create { emitter ->
            emitter.onSuccess(testData)
        })
    }

    private fun successEndPage() {
        val testData = TestUtils.getTestData("gifPageEnd.json")
        `when`(sutApi.searchGIF(any(), anyInt(), anyInt())).thenReturn(Single.create { emitter ->
            emitter.onSuccess(testData)
        })
    }

    private fun failure() {
        `when`(sutApi.searchGIF(any(), anyInt(), anyInt())).thenReturn(Single.create { emitter ->
            emitter.onError(RequestException("Invalid authentication credentials"))
        })
    }
}