package com.example.phonestore.services

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout


class FixScrollingFooterBehavior(context: Context?, attrs: AttributeSet?): AppBarLayout.ScrollingViewBehavior() {
    private var appBarLayout: AppBarLayout? = null

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (appBarLayout == null) {
            appBarLayout = dependency as AppBarLayout
        }
        val result: Boolean =  super.onDependentViewChanged(parent, child, dependency)
        val bottomPadding: Int = calculateBottomPadding(appBarLayout)
        val paddingChanged: Boolean  = bottomPadding != child.paddingBottom
        if (paddingChanged) {
            child.setPadding(
                child.paddingLeft,
                child.paddingTop,
                child.paddingRight,
                bottomPadding)
            child.requestLayout()
        }
        return paddingChanged || result
    }



    // Calculate the padding needed to keep the bottom of the view pager's content at the same location on the screen.
    private fun calculateBottomPadding(dependency: AppBarLayout?): Int {
        val totalScrollRange = dependency!!.totalScrollRange
        return totalScrollRange + dependency.top
    }
}