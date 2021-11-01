package com.learn.codingchallenge.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.learn.codingchallenge.network.model.GifInfo
import com.learn.codingchallenge.network.repository.GifRepository
import com.learn.codingchallenge.ui.gifsearch.paging.GifPagingSource
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@ViewModelScoped
class PagingRepositoryImpl @Inject constructor(private val repository: GifRepository) :
    PagingRepository {

    override fun searchGif(searchQuery: String): Flowable<PagingData<GifInfo>> {
        return Pager(
            config = PagingConfig(
                pageSize = GifPagingSource.PAGE_LIMIT,
                prefetchDistance = 10,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { GifPagingSource(repository, searchQuery) }
        ).flowable
    }
}