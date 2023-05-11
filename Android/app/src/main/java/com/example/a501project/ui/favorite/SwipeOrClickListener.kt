package com.example.a501project.ui.favorite

import android.content.Context
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class SwipeOrClickListener(
    context: Context,
    private val swipeThresholdDistance: Int = 100,
    private val swipeThresholdVelocity: Int = 1000,
    private val onSwipe: (position: Int) -> Unit,
    private val onClick: (position: Int) -> Unit
) : RecyclerView.OnItemTouchListener {

    private var startX = 0f
    private var startY = 0f
    private var startTime = 0L

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = e.x
                startY = e.y
                startTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_UP -> {
                val endX = e.x
                val endY = e.y
                val endTime = System.currentTimeMillis()

                val deltaX = endX - startX
                val deltaY = endY - startY
                val duration = endTime - startTime
                val velocityX = Math.abs(deltaX) / duration
                val velocityY = Math.abs(deltaY) / duration

                val childView = rv.findChildViewUnder(e.x, e.y)
                val position = childView?.let { rv.getChildAdapterPosition(it) }

                if (position != null) {
                    if (Math.abs(deltaX) > swipeThresholdDistance && velocityX > swipeThresholdVelocity) {
                        // Swipe gesture detected
                        onSwipe(position)
                    } else if (Math.abs(deltaY) > swipeThresholdDistance && velocityY > swipeThresholdVelocity) {
                        // Swipe gesture detected
                        onSwipe(position)
                    } else {
                        // Click gesture detected
                        onClick(position)
                    }
                }
            }
        }

        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        // No implementation needed
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        // No implementation needed
    }
}
