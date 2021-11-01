package com.learn.codingchallenge.testsuite

import com.learn.codingchallenge.ui.MainActivityTest
import com.learn.codingchallenge.ui.gifdetails.GifDetailsFragmentTest
import com.learn.codingchallenge.ui.gifsearch.SearchFragmentTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Created By Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityTest::class,
    SearchFragmentTest::class,
    GifDetailsFragmentTest::class
)
class TestSuiteGifSearchApp