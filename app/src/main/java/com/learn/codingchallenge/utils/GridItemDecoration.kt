package com.learn.codingchallenge.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created By Rafiqul Hasan
 */
class GridItemDecoration constructor(private val spacing: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildViewHolder(view).absoluteAdapterPosition
        outRect.left = if (position % 2 == 0) spacing else spacing / 2
        outRect.right = if (position % 2 == 1) spacing else spacing / 2
        outRect.top = if (position == 0 || position == 1) 0 else spacing

        outRect.bottom = 0
    }
}