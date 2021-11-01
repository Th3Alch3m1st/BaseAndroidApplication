package com.learn.codingchallenge.ui.gifsearch.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.learn.codingchallenge.network.RequestException
import com.learn.codingchallenge.network.model.GIFResponse
import com.learn.codingchallenge.network.repository.GifRepository
import com.learn.codingchallenge.testutil.TestUtils.getTestData
import com.learn.codingchallenge.testutil.any
import com.learn.codingchallenge.testutil.argumentCaptor
import com.learn.codingchallenge.testutil.capture
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created By Rafiqul Hasan
 */
@RunWith(MockitoJUnitRunner::class)
class GifPagingSourceTest {
    companion object {
        const val SEARCH_QUERY = "funny cat"
        const val LOAD_SIZE = 10
        const val PAGE_LIMIT = 10
        const val OFFSET_FOR_PAGE_ONE = 0
        const val OFFSET_FOR_PAGE_TWO = 10
        const val OFFSET_FOR_END_PAGE = 270
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockGifRepository: GifRepository

    private lateinit var gifResponseOne: GIFResponse
    private lateinit var gifResponseTwo: GIFResponse
    private lateinit var gifResponseEnd: GIFResponse

    private lateinit var sutGifPagingSource: GifPagingSource

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setErrorHandler { }

        sutGifPagingSource = GifPagingSource(mockGifRepository, SEARCH_QUERY)

        gifResponseOne = getTestData("gifPage1.json")
        gifResponseTwo = getTestData("gifPage2.json")
        gifResponseEnd = getTestData("gifPageEnd.json")
    }

    @Test
    fun gifSearch_paramPass_paramPassCorrectly() {
        pageOneDataSuccess()

        val acString = argumentCaptor<String>()
        val acInt = argumentCaptor<Int>()

        val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )

        sutGifPagingSource.loadSingle(refreshRequest)

        Mockito.verify(mockGifRepository)
            .searchGIF(capture(acString), capture(acInt), capture(acInt))

        assertThat(acString.value).isEqualTo(SEARCH_QUERY)
        assertThat(acInt.allValues[0])
            .isEqualTo(OFFSET_FOR_PAGE_ONE)
        assertThat(acInt.allValues[1]).isEqualTo(PAGE_LIMIT)

    }

    @Test
    fun gifSearch_offsetKeyPassToNetworkRepository_correctOffsetKeyPassAsParam() {
        pageOneDataSuccess()

        val acString = argumentCaptor<String>()
        val acInt = argumentCaptor<Int>()

        val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
            PagingSource.LoadParams.Refresh(OFFSET_FOR_PAGE_TWO, LOAD_SIZE, false)

        sutGifPagingSource.loadSingle(refreshRequest)

        Mockito.verify(mockGifRepository)
            .searchGIF(capture(acString), capture(acInt), capture(acInt))

        assertThat(acString.value).isEqualTo(SEARCH_QUERY)
        assertThat(acInt.allValues[0])
            .isEqualTo(OFFSET_FOR_PAGE_TWO)
        assertThat(acInt.allValues[1]).isEqualTo(PAGE_LIMIT)
    }

    @Test
    fun gifSearch_firstPagingDataLoadingSuccess_SuccessReturned() {
        pageOneDataSuccess()

        val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
            PagingSource.LoadParams.Refresh(OFFSET_FOR_PAGE_ONE, LOAD_SIZE, false)

        sutGifPagingSource.loadSingle(refreshRequest)
            .test()
            .await()
            .assertValueCount(1)
            .assertValue(
                PagingSource.LoadResult.Page(
                    data = gifResponseOne.gifList ?: mutableListOf(),
                    prevKey = null,
                    nextKey = (gifResponseOne.pagination?.offset ?: 0) + LOAD_SIZE
                )
            )
    }

    @Test
    fun gifSearch_secondPagingDataLoading_successReturned() {
        pageTwoDataSuccess()

        val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
            PagingSource.LoadParams.Refresh(OFFSET_FOR_PAGE_TWO, LOAD_SIZE, false)

        sutGifPagingSource.loadSingle(refreshRequest).test()
            .await()
            .assertValueCount(1)
            .assertValue(
                PagingSource.LoadResult.Page(
                    data = gifResponseTwo.gifList ?: mutableListOf(),
                    prevKey = (gifResponseTwo.pagination?.offset ?: 0) - LOAD_SIZE,
                    nextKey = (gifResponseTwo.pagination?.offset ?: 0) + LOAD_SIZE
                )
            )
    }

    @Test
    fun gifSearch_endPagingDataLoadingSuccessReturned() {
        lastPageDataSuccess()

        val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
            PagingSource.LoadParams.Refresh(OFFSET_FOR_END_PAGE, 10, false)

        sutGifPagingSource.loadSingle(refreshRequest).test()
            .await()
            .assertValueCount(1)
            .assertValue(
                PagingSource.LoadResult.Page(
                    data = gifResponseEnd.gifList ?: mutableListOf(),
                    prevKey = (gifResponseEnd.pagination?.offset ?: 0) - LOAD_SIZE,
                    null
                )
            )
    }

    @Test
    fun gifSearch_EmptyList() {
        emptyResponse()

        val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
            PagingSource.LoadParams.Refresh(OFFSET_FOR_PAGE_ONE, 10, false)

        sutGifPagingSource.loadSingle(refreshRequest).test()
            .await()
            .assertValueCount(1)
            .assertValue(
                PagingSource.LoadResult.Page(
                    mutableListOf(),
                    null, null
                )
            )
    }

    @Test
    fun gifSearch_failure() {
        errorResponse()

        val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
            PagingSource.LoadParams.Refresh(null, 0, false)
        sutGifPagingSource.loadSingle(refreshRequest)
            .test()
            .await()
            .assertValue {
                assertThat(it).isInstanceOf(PagingSource.LoadResult.Error::class.java)
                return@assertValue true
            }


    }

    private fun pageOneDataSuccess() {
        val testData = getTestData("gifPage1.json")
        Mockito.`when`(mockGifRepository.searchGIF(any(), Mockito.anyInt(), Mockito.anyInt()))
            .thenReturn(
                Single.create { emitter ->
                    emitter.onSuccess(testData)
                })
    }

    private fun pageTwoDataSuccess() {
        val testData = getTestData("gifPage2.json")
        Mockito.`when`(mockGifRepository.searchGIF(any(), Mockito.anyInt(), Mockito.anyInt()))
            .thenReturn(
                Single.create { emitter ->
                    emitter.onSuccess(testData)
                })
    }

    private fun lastPageDataSuccess() {
        val testData = getTestData("gifPageEnd.json")
        Mockito.`when`(mockGifRepository.searchGIF(any(), Mockito.anyInt(), Mockito.anyInt()))
            .thenReturn(
                Single.create { emitter ->
                    emitter.onSuccess(testData)
                })
    }

    private fun emptyResponse() {
        Mockito.`when`(mockGifRepository.searchGIF(any(), Mockito.anyInt(), Mockito.anyInt()))
            .thenReturn(
                Single.create { emitter ->
                    emitter.onSuccess(GIFResponse(gifList = mutableListOf()))
                })
    }

    private fun errorResponse() {
        Mockito.`when`(mockGifRepository.searchGIF(any(), Mockito.anyInt(), Mockito.anyInt()))
            .thenReturn(Single.error(RequestException()))
    }
}