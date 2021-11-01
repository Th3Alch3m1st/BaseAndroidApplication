package com.learn.codingchallenge.utils

import android.view.View

/**
 * Created By Rafiqul Hasan
 */


/**
 * Show the view  (visibility = View.VISIBLE)
 */
fun View.show() : View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * Remove the view (visibility = View.GONE)
 */
fun View.gone() : View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}
