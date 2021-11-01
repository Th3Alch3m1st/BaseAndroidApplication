package com.learn.codingchallenge.ui.gifsearch

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.learn.codingchallenge.core.viewmodel.BaseViewModel
import com.learn.codingchallenge.network.model.GifInfo
import com.learn.codingchallenge.repository.PagingRepository
import com.learn.codingchallenge.test.OpenForTesting
import com.learn.codingchallenge.utils.withScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@HiltViewModel
@OpenForTesting
class SearchViewModel @Inject constructor(private val pagingRepo: PagingRepository) :
    BaseViewModel() {
    private var _searchPagingData = MutableLiveData<PagingData<GifInfo>>()

    val searchPagingData: MutableLiveData<PagingData<GifInfo>>
        get() = _searchPagingData

    fun searchGif(query: String) {
        if (compositeDisposable.size() > 1) {
            compositeDisposable.dispose()
            compositeDisposable = CompositeDisposable()
        }
        val disposable = pagingRepo.searchGif(query)
            .withScheduler()
            .cachedIn(viewModelScope)
            .subscribe({
                _searchPagingData.value = it
            }, {
            })
        compositeDisposable.add(disposable)
    }

    //cachedIn throw exception; cachedIn only for save paging state to survive orientation
    @VisibleForTesting
    fun searchGifTest(query: String) {
        if (compositeDisposable.size() > 1) {
            compositeDisposable.dispose()
            compositeDisposable = CompositeDisposable()
        }
        val disposable = pagingRepo.searchGif(query)
            .withScheduler()
            .subscribe({
                _searchPagingData.value = it
            }, {
                it.printStackTrace()
            })
        compositeDisposable.add(disposable)
    }
}