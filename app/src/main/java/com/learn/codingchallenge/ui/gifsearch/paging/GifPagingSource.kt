package com.learn.codingchallenge.ui.gifsearch.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.learn.codingchallenge.network.model.GIFResponse
import com.learn.codingchallenge.network.model.GifInfo
import com.learn.codingchallenge.network.repository.GifRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created By Rafiqul Hasan
 */
class GifPagingSource(private val repository: GifRepository, private val query: String) :
    RxPagingSource<Int, GifInfo>() {
    companion object {
        const val PAGE_LIMIT = 10
    }

    override fun getRefreshKey(state: PagingState<Int, GifInfo>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, GifInfo>> {
        val pageNumber = params.key ?: 0

        return repository.searchGIF(query, pageNumber, PAGE_LIMIT)
            .subscribeOn(Schedulers.io())
            .map { response ->
                toLoadResult(response, params.loadSize)
            }.onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(data: GIFResponse, loadSize: Int): LoadResult<Int, GifInfo> {
        val totalCount = data.pagination?.totalCount ?: 0
        val offset = data.pagination?.offset ?: 0

        return LoadResult.Page(
            data = data.gifList ?: mutableListOf(),
            prevKey = if (offset == 0) null else offset - loadSize,
            nextKey = if (offset.plus(loadSize) < totalCount) offset + loadSize else null
        )
    }
}