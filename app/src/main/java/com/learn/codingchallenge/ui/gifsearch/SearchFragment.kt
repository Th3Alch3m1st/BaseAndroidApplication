package com.learn.codingchallenge.ui.gifsearch

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.learn.codingchallenge.R
import com.learn.codingchallenge.core.fragment.BaseFragment
import com.learn.codingchallenge.databinding.FragmentGifSearchBinding
import com.learn.codingchallenge.network.RequestException
import com.learn.codingchallenge.ui.gifdetails.GifDetailsFragment
import com.learn.codingchallenge.ui.gifsearch.adapter.GifAdapter
import com.learn.codingchallenge.ui.gifsearch.adapter.PagingLoadStateAdapter
import com.learn.codingchallenge.utils.GridItemDecoration
import com.learn.codingchallenge.utils.gone
import com.learn.codingchallenge.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * Created By Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchViewModel, FragmentGifSearchBinding>() {
    private val viewModel: SearchViewModel by viewModels()
    private val itemDecoration by lazy {
        GridItemDecoration(
            requireContext().resources.getDimension(
                R.dimen._4sdp
            ).toInt()
        )
    }

    private lateinit var gifAdapter: GifAdapter
    private var isInitialLoadComplete = false

    override val layoutResourceId: Int
        get() = R.layout.fragment_gif_search

    override fun getVM(): SearchViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //adapter init
        gifAdapter = GifAdapter { gifInfo ->
            val bundle = Bundle().apply {
                putString(GifDetailsFragment.ARG_GIF_TITLE, gifInfo.title ?: "")
                putString(GifDetailsFragment.ARG_GIF_URL, gifInfo.images?.original?.url ?: "")
            }

            navigateFragment(R.id.action_fragment_gif_search_to_gif_details, bundle)
        }
    }

    /**
     * also work as onViewCreated
     */
    override fun bindViewModel(binding: FragmentGifSearchBinding, viewModel: SearchViewModel) {
        with(binding) {
            setViewModel(viewModel)
            fragmentCommunicator?.setActionBar(toolbar, false)
            searchView.setOnQueryTextListener(searchListener)
            initAdapterAndRecyclerView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gifAdapter.removeLoadStateListener(listener)
    }

    private fun initAdapterAndRecyclerView() {
        with(dataBinding) {

            recyclerView.apply {
                layoutManager = GridLayoutManager(context, 2)
                addItemDecoration(itemDecoration)
            }

            with(gifAdapter) {
                recyclerView.adapter = withLoadStateFooter(
                    footer = PagingLoadStateAdapter(gifAdapter)
                )
                addLoadStateListener(listener)
                if (itemCount > 0) {
                    viewEmpty.root.gone()
                }
            }
        }

        initSearchResultObserver()
    }

    private fun initSearchResultObserver() {
        viewModel.searchPagingData.observe(viewLifecycleOwner, { pagingData ->
            pagingData?.let {
                gifAdapter.submitData(lifecycle, pagingData)
            }
        })
    }

    private val listener = { loadStates: CombinedLoadStates ->
        if (loadStates.refresh is LoadState.Loading) {
            dataBinding.progressBar.show()
        } else {
            //when listener is attached LoadState.NotLoading is called one time, after api call if data not found LoadState.NotLoading is called again for empty state
            // to handle initial state isInitialLoadComplete flag is used to show initial page
            if (isInitialLoadComplete) {
                if (gifAdapter.itemCount == 0) {
                    dataBinding.viewEmpty.root.show()
                    dataBinding.viewEmpty.tvTitle.text = getString(R.string.nothing_found)
                } else {
                    dataBinding.viewEmpty.root.gone()
                }
                dataBinding.progressBar.gone()
            } else {
                isInitialLoadComplete = true
            }
        }

        if (loadStates.refresh is LoadState.Error) {
            val error = (loadStates.refresh as LoadState.Error).error
            if (error is RequestException) {
                dataBinding.viewEmpty.root.show()
                dataBinding.viewEmpty.tvTitle.text = error.message
            } else {
                dataBinding.viewEmpty.tvTitle.text = error.message
            }
        }
    }

    private val searchListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (!query.isNullOrEmpty()) {
                viewModel.searchGif(query)
                dataBinding.searchView.clearFocus()
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    }
}