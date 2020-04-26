package com.journal.common


import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.journal.R


class CollapsingViewBehavior : CoordinatorLayout.Behavior<View> {

    private var mTargetId: Int = 0
    private var maxTop: Float = 0f

    private var mView: IntArray? = null

    private var mTarget: IntArray? = null

    private lateinit var appBarLayout: AppBarLayout

    constructor()

    constructor(context: Context, attrs: AttributeSet?) {

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CollapsingViewBehavior)
            mTargetId = a.getResourceId(R.styleable.CollapsingViewBehavior_collapsedTarget, 0)
            a.recycle()
        }

        if (mTargetId == 0) {
            throw IllegalStateException("collapsedTarget attribute not specified on view for behavior")
        }
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        setup(parent, child)

        appBarLayout = dependency as AppBarLayout

        val range = appBarLayout.totalScrollRange
        val factor = -appBarLayout.y / range

        val left = mView!![X] + (factor * (mTarget!![X] - mView!![X])).toInt()
        val top = mView!![Y] + (factor * (mTarget!![Y] - mView!![Y])).toInt()
        val width = mView!![WIDTH] + (factor * (mTarget!![WIDTH] - mView!![WIDTH])).toInt()
        val height = mView!![HEIGHT] + (factor * (mTarget!![HEIGHT] - mView!![HEIGHT])).toInt()

        val lp = child.layoutParams as CoordinatorLayout.LayoutParams
        lp.width = width
        lp.height = height
        child.layoutParams = lp
        child.x = left.toFloat()
        child.y = if (top.toFloat() < maxTop) maxTop else top.toFloat()

        return true
    }

    private fun setup(parent: CoordinatorLayout, child: View) {

        if (mView != null) return

        mView = IntArray(4)
        mTarget = IntArray(4)

        mView!![X] = child.x.toInt()
        mView!![Y] = child.y.toInt()
        mView!![WIDTH] = child.width
        mView!![HEIGHT] = child.height

        val target = parent.findViewById<View>(mTargetId)
            ?: throw IllegalStateException("target view not found")

        mTarget!![WIDTH] += target.width
        mTarget!![HEIGHT] += target.height

        var view: View = target
        while (view !== parent) {
            mTarget!![X] += view.x.toInt()
            mTarget!![Y] += view.y.toInt()
            view = view.parent as View
            maxTop = if (maxTop == 0f) mTarget!![Y].toFloat() else maxTop
        }
    }

    override fun onSaveInstanceState(parent: CoordinatorLayout, child: View): Parcelable? {
        return Bundle().apply {
            putFloat(MAX_TOP, maxTop)
        }
    }

    override fun onRestoreInstanceState(parent: CoordinatorLayout, child: View, state: Parcelable) {
        maxTop = (state as Bundle).getFloat(MAX_TOP)
        super.onRestoreInstanceState(parent, child, state)
    }

    companion object {

        private const val X = 0
        private const val Y = 1
        private const val WIDTH = 2
        private const val HEIGHT = 3
        private const val MAX_TOP = "max_top"
    }
}