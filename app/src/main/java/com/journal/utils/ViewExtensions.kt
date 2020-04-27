package com.journal.utils

import android.animation.ValueAnimator
import android.view.View
import androidx.core.view.doOnDetach


fun View.rotate() {
    val animator = ValueAnimator.ofFloat(1f, 180f).apply {
        duration = 400
        addUpdateListener {
            rotation = it.animatedValue as Float
        }
    }
    animator.start()
    doOnDetach {
       animator.cancel()
    }
}