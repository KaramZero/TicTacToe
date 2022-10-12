package com.example.tictactoe.ui

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View

class MyAnimator {

    fun animateTranslationX(view: View, startPosition : Float) {
        val animator =
            ObjectAnimator.ofFloat(view, View.TRANSLATION_X, startPosition*2, 0f)
        animator.duration = 2000
        animator.start()
    }
    fun animateTranslationY(view: View, startPosition : Float) {
        val animator =
            ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startPosition, 0f)
        animator.duration = 1500
        animator.start()
    }
    fun animateFading(view: View) {
        val animator =
            ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f)
        animator.duration = 2000
        animator.start()
    }

    fun animateScale(view: View){
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, -1.5f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            view, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }
}