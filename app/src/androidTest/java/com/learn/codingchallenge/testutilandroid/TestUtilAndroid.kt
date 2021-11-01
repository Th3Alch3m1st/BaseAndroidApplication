package com.learn.codingchallenge.testutilandroid

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

/**
 * Created By Rafiqul Hasan
 */
class ScrollToBottomAction : ViewAction {
    override fun getDescription(): String {
        return "scroll RecyclerView to bottom"
    }

    override fun getConstraints(): Matcher<View> {
        return CoreMatchers.allOf(
            ViewMatchers.isAssignableFrom(RecyclerView::class.java),
            ViewMatchers.isDisplayed()
        )
    }

    override fun perform(uiController: UiController?, view: View?) {
        if (view is RecyclerView) {
            val position = view.adapter?.itemCount?.minus(1) ?: 0
            view.scrollToPosition(position)
            uiController?.loopMainThreadUntilIdle()
            val lastView = view.findViewHolderForLayoutPosition(position)!!.itemView
            view.scrollBy(0, lastView.height)
            uiController?.loopMainThreadUntilIdle()
        }
    }
}

object RecyclerViewItemClickAction {
    fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v: View = view.findViewById(id)
                v.performClick()
            }
        }
    }
}