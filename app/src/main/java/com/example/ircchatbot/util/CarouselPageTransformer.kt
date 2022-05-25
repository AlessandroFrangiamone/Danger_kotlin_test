package com.example.ircchatbot.util


import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs
import kotlin.math.max

class CarouselPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        view.translationX = pageWidth * 1.9f / 5 * -position

        if (position < 1) {

            if (position == 0f) {
                //current page!
                view.scaleX = 1f
                view.scaleY = 1f
                view.alpha = 1f
            } else {
                val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                // Fade the page relative to its size.
                view.alpha =
                    MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            }
        } else {
            view.scaleX = MIN_SCALE
            view.scaleY = MIN_SCALE
            view.alpha = MIN_ALPHA
        }
    }

    companion object {
        const val MIN_SCALE = 0.7f
        const val MIN_ALPHA = 0.7f
    }
}