package com.learn.codingchallenge.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.learn.codingchallenge.R
import com.learn.codingchallenge.testutilandroid.RecyclerViewItemClickAction
import com.learn.codingchallenge.ui.gifsearch.adapter.GifAdapter
import com.learn.codingchallenge.utils.DataBindingIdlingResource
import com.learn.codingchallenge.utils.EspressoIdlingResource
import com.learn.codingchallenge.utils.FakeGifRepositoryImpl
import com.learn.codingchallenge.utils.monitorActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By Rafiqul Hasan
 */
@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class MainActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setUp() {
        hiltRule.inject()

        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun display_mainActivityOpen_opened() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.root)).check(matches(isDisplayed()))
    }

    @Test
    fun display_fragmentDetails_opened() {
        //open activity
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //triggering search
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(
            typeText(FakeGifRepositoryImpl.TRIGGERED_VALID_RESPONSE),
            pressImeActionButton()
        )
        Espresso.closeSoftKeyboard()

        Thread.sleep(500)

        //click to open fragment
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GifAdapter.GifViewHolder>(
                0,
                RecyclerViewItemClickAction.clickChildViewWithId(R.id.iv_image)
            )
        )

        //verifying ui is present on screen
        onView(withId(R.id.root_fragment_details)).check(matches(isDisplayed()))
    }

    @Test
    fun test_correctDataPassToDetailsGifFragment_dataPassSuccess() {
        //open activity
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //triggering search
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .perform(
                typeText(FakeGifRepositoryImpl.TRIGGERED_VALID_RESPONSE),
                pressImeActionButton()
            )
        Espresso.closeSoftKeyboard()

        Thread.sleep(500)

        //click to open details fragment
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GifAdapter.GifViewHolder>(
                FakeGifRepositoryImpl.TEST_INDEX_TO_TAP,
                RecyclerViewItemClickAction.clickChildViewWithId(R.id.iv_image)
            )
        )

        //verifying correct data set to ui
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(FakeGifRepositoryImpl.CLICK_TEST_DATA.title))))
    }
}