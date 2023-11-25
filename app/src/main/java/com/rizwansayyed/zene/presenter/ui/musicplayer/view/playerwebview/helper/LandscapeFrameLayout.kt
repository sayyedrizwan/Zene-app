package com.rizwansayyed.zene.presenter.ui.musicplayer.view.playerwebview.helper

import android.R.attr
import android.content.Context
import android.widget.FrameLayout


class LandscapeFrameLayout(context: Context) : FrameLayout(context) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child != null) {
                child.rotation = 90f
                val width = attr.right - attr.left
                val height = attr.bottom - attr.top
                child.translationX = ((width - height) / 2).toFloat()
                child.translationY = ((height - width) / 2).toFloat()
            }
        }
        super.onLayout(changed, 0, 0, attr.bottom, attr.right)
    }
}