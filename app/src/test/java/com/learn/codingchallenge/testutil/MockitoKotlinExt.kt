package com.learn.codingchallenge.testutil

/**
 * Created By Rafiqul Hasan
 */

/**
 * a kotlin friendly mock that handles generics
 * Helper functions that are workarounds to kotlin Runtime Exceptions when using kotlin.
 */

import org.mockito.ArgumentCaptor
import org.mockito.Mockito


/**
 * Returns Mockito.any() as nullable type to avoid java.lang.IllegalStateException when
 * null is returned.
 */
fun <T> any(): T = Mockito.any()


/**
 * Returns ArgumentCaptor.capture() as nullable type to avoid java.lang.IllegalStateException
 * when null is returned.
 */
fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()


/**
 * Helper function for creating an argumentCaptor in kotlin.
 */
inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> =
    ArgumentCaptor.forClass(T::class.java)