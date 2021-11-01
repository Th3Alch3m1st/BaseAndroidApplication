package com.learn.codingchallenge.ui.gifdetails

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.learn.codingchallenge.R
import com.learn.codingchallenge.utils.FakeGifRepositoryImpl
import com.learn.codingchallenge.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

/**
 * Created By Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class GifDetailsFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun display_view_opened() {
        //open fragment
        val mockNavController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<GifDetailsFragment>(
            null,
            R.style.Theme_MobimeoCodingChallenge
        ) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        //verifying cases
        onView(withId(R.id.root_fragment_details)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_gif)).check(matches(isDisplayed()))
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun test_dataSetCorrectly_Success() {
        //open fragment
        val mockNavController = Mockito.mock(NavController::class.java)
        val bundle = Bundle().apply {
            putString(GifDetailsFragment.ARG_GIF_TITLE, FakeGifRepositoryImpl.CLICK_TEST_DATA.title)
            putString(
                GifDetailsFragment.ARG_GIF_URL,
                FakeGifRepositoryImpl.CLICK_TEST_DATA.images?.original?.url
            )
        }

        launchFragmentInHiltContainer<GifDetailsFragment>(
            bundle,
            R.style.Theme_MobimeoCodingChallenge
        ) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        //verifying cases
        onView(withId(R.id.toolbar)).check(
            matches(
                hasDescendant(
                    withText(FakeGifRepositoryImpl.CLICK_TEST_DATA.title)
                )
            )
        )
    }
}