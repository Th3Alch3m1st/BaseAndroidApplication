package com.learn.codingchallenge.ui.gifsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.learn.codingchallenge.network.model.GIFResponse
import com.learn.codingchallenge.network.model.GifInfo
import com.learn.codingchallenge.repository.PagingRepository
import com.learn.codingchallenge.testutil.TestUtils.getTestData
import com.learn.codingchallenge.testutil.any
import com.learn.codingchallenge.testutil.argumentCaptor
import com.learn.codingchallenge.testutil.capture
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created By Rafiqul Hasan
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {
    companion object {
        const val SEARCH_QUERY = "funny cat"
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockPagingRepository: PagingRepository

    private lateinit var sutViewModel: SearchViewModel

    //data set
    private lateinit var gifResponseOne: GIFResponse
    private lateinit var gifResponseTwo: GIFResponse

    private lateinit var pagingDataOne: PagingData<GifInfo>
    private lateinit var pagingDataTwo: PagingData<GifInfo>
    private lateinit var pagingDataEmpty: PagingData<GifInfo>

    //test observer
    @Mock
    private lateinit var mockObserver: Observer<PagingData<GifInfo>>

    @Before
    fun setup() {
        sutViewModel = SearchViewModel(mockPagingRepository)

        gifResponseOne = getTestData("gifPage1.json")
        gifResponseTwo = getTestData("gifPage2.json")

        pagingDataOne = PagingData.from(gifResponseOne.gifList ?: mutableListOf())
        pagingDataTwo = PagingData.from(gifResponseTwo.gifList ?: mutableListOf())
        pagingDataEmpty = PagingData.empty()

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setErrorHandler { }
    }

    @Test
    fun searchGif_passArgumentToPagingRepo_correctArgumentPass() {
        successResponsePageOne()

        sutViewModel.searchGifTest(SEARCH_QUERY)

        val argumentCaptor = argumentCaptor<String>()

        Thread.sleep(100)
        Mockito.verify(mockPagingRepository).searchGif(capture(argumentCaptor))
    }

    @Test
    fun searchGif_pagingDataReturned_returnedSuccess() {
        successResponsePageOne()
        sutViewModel.searchGifTest(SEARCH_QUERY)

        sutViewModel.searchPagingData.observeForever(mockObserver)

        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<PagingData<GifInfo>>()
        Mockito.verify(mockObserver).onChanged(capture(argumentCaptor))

        Assertions.assertThat(argumentCaptor.value).isEqualTo(pagingDataOne)
    }

    @Test
    fun searchGif_duringTwoSearchQueryLatestDataDisplayed_returnedLatestData() {
        successResponsePageOne()
        sutViewModel.searchGifTest(SEARCH_QUERY)

        sutViewModel.searchPagingData.observeForever(mockObserver)

        Thread.sleep(100)
        successResponsePageTwo()
        sutViewModel.searchGifTest(SEARCH_QUERY)

        val argumentCaptor = argumentCaptor<PagingData<GifInfo>>()
        Mockito.verify(mockObserver, times(2)).onChanged(capture(argumentCaptor))

        Assertions.assertThat(argumentCaptor.value).isEqualTo(pagingDataTwo)
    }

    @Test
    fun searchGif_emptyPagingData_emptyReturned() {
        emptyPagingData()

        sutViewModel.searchGifTest(SEARCH_QUERY)
        sutViewModel.searchPagingData.observeForever(mockObserver)

        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<PagingData<GifInfo>>()
        Mockito.verify(mockObserver).onChanged(capture(argumentCaptor))

        Assertions.assertThat(argumentCaptor.value).isEqualTo(pagingDataEmpty)
    }

    private fun successResponsePageOne() {
        Mockito.`when`(mockPagingRepository.searchGif(any()))
            .thenReturn(Flowable.just(pagingDataOne))
    }

    private fun successResponsePageTwo() {
        Mockito.`when`(mockPagingRepository.searchGif(any()))
            .thenReturn(Flowable.just(pagingDataTwo))
    }

    private fun emptyPagingData() {
        Mockito.`when`(mockPagingRepository.searchGif(any()))
            .thenReturn(Flowable.just(pagingDataEmpty))
    }
}