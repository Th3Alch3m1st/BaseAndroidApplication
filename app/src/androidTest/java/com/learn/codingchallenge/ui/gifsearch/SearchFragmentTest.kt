package com.learn.codingchallenge.ui.gifsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.learn.codingchallenge.R
import com.learn.codingchallenge.testutilandroid.ScrollToBottomAction
import com.learn.codingchallenge.utils.FakeGifRepositoryImpl
import com.learn.codingchallenge.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import javax.inject.Inject


/**
 * Created By Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class SearchFragmentTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val taskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fakeRepositoryImpl: FakeGifRepositoryImpl

    @Before
    fun setUp() {
        hiltRule.inject()
        val scheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setErrorHandler { }
    }

    @Test
    fun test_initialView_displayed() {
        //open fragment
        val mockNavController = mock(NavController::class.java)
        launchFragmentInHiltContainer<SearchFragment>(null, R.style.Theme_MobimeoCodingChallenge) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        //verifying cases
        onView(withId(R.id.search_view)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.view_empty)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_search)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTitle)).check(matches(withText("Search GIF")))
    }

    @Test
    fun test_emptyResponse_displayed() {
        //open fragment
        val mockNavController = mock(NavController::class.java)
        launchFragmentInHiltContainer<SearchFragment>(null, R.style.Theme_MobimeoCodingChallenge) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        //creating scenario for empty response
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .perform(
                typeText(FakeGifRepositoryImpl.TRIGGERED_EMPTY_RESPONSE),
                pressImeActionButton()
            )
        Espresso.closeSoftKeyboard()

        ////verifying cases
        onView(withId(R.id.tvTitle)).check(matches(withText(FakeGifRepositoryImpl.MSG_EMPTY)))
    }

    @Test
    fun test_errorResponse_displayed() {
        //open fragment
        val mockNavController = mock(NavController::class.java)
        launchFragmentInHiltContainer<SearchFragment>(null, R.style.Theme_MobimeoCodingChallenge) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        //creating scenario for error response
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .perform(
                typeText(FakeGifRepositoryImpl.TRIGGERED_ERROR_RESPONSE),
                pressImeActionButton()
            )
        Espresso.closeSoftKeyboard()

        //verifying cases
        onView(withId(R.id.tvTitle)).check(matches(withText(FakeGifRepositoryImpl.MSG_ERROR)))
    }

    @Test
    fun test_itemLoaded_rightItemCount() {
        //open fragment
        val mockNavController = mock(NavController::class.java)
        launchFragmentInHiltContainer<SearchFragment>(null, R.style.Theme_MobimeoCodingChallenge) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        //creating scenario for valid response
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(
            typeText(FakeGifRepositoryImpl.TRIGGERED_VALID_RESPONSE),
            pressImeActionButton()
        )
        Espresso.closeSoftKeyboard()

        //verifying itemCount is correct
        onView(withId(R.id.recycler_view)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val recyclerView = view as RecyclerView
            Assert.assertEquals(10, recyclerView.adapter?.itemCount)
        }
    }

    @Test
    fun test_afterInitialLoadingErrorHappen_ErrorItemDisplayed() {
        //open fragment
        val mockNavController = mock(NavController::class.java)
        launchFragmentInHiltContainer<SearchFragment>(null, R.style.Theme_MobimeoCodingChallenge) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }
        //creating scenario after load error
        fakeRepositoryImpl.isRetryTest = true
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .perform(
                typeText(FakeGifRepositoryImpl.TRIGGERED_VALID_RESPONSE),
                pressImeActionButton()
            )
        Espresso.closeSoftKeyboard()

        Thread.sleep(500)
        fakeRepositoryImpl.isError = true
        onView(withId(R.id.recycler_view)).perform(ScrollToBottomAction())

        //verifying cases
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withId(R.id.layout_state))))
        onView(withId(R.id.btn_retry)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_error_msg)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar_rv)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_afterInitialLoadingErrorHappen_retryToLoadList() {
        //open fragment
        val mockNavController = mock(NavController::class.java)
        launchFragmentInHiltContainer<SearchFragment>(null, R.style.Theme_MobimeoCodingChallenge) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }
        //creating scenario after load error
        fakeRepositoryImpl.isRetryTest = true
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .perform(
                typeText(FakeGifRepositoryImpl.TRIGGERED_VALID_RESPONSE),
                pressImeActionButton()
            ) //init api call
        Espresso.closeSoftKeyboard()

        Thread.sleep(500)
        fakeRepositoryImpl.isError = true //creating error scenario

        onView(withId(R.id.recycler_view)).perform((ScrollToBottomAction()))

        //to visible error item
        fakeRepositoryImpl.isError = false //preparing for retry - setting error to false
        onView(withId(R.id.btn_retry)).perform(click())
        onView(withId(R.id.recycler_view)).perform(ScrollToBottomAction())

        //validating cases
        onView(withId(R.id.recycler_view)).check(matches(not(hasDescendant(withId(R.id.btn_retry)))))
        onView(withId(R.id.recycler_view)).check(matches(not(hasDescendant(withId(R.id.tv_error_msg)))))
        onView(withId(R.id.recycler_view)).check(matches(not(hasDescendant(withId(R.id.progress_bar_rv)))))
    }
}