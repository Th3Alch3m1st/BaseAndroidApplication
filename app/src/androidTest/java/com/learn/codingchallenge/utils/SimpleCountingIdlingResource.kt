package com.learn.codingchallenge.utils

import androidx.test.espresso.IdlingResource

import java.util.concurrent.atomic.AtomicInteger

/**
 * An simple counter implementation of [IdlingResource] that determines idleness by
 * maintaining an internal counter. When the counter is 0 - it is considered to be idle, when it is
 * non-zero it is not idle. This is very similar to the way a [java.util.concurrent.Semaphore]
 * behaves.
 *
 *
 * This class can then be used to wrap up operations that while in progress should block tests from
 * accessing the UI.
 */
class SimpleCountingIdlingResource(private val resourceName: String) : IdlingResource {

    private val counter = AtomicInteger(0)

    // written from main thread, read from any thread.
    @Volatile
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = resourceName

    override fun isIdleNow() = counter.get() == 0

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallback = resourceCallback
    }
}