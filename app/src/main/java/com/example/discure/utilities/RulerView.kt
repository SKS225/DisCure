package com.example.discure.utilities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class RulerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = 0xFF000000.toInt() // Black color
        strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas ?: return

        val width = width
        val height = height
        val spacing = 20

        // Draw the ruler lines
        for (i in 0..width step spacing) {
            val lineLength = if (i % 100 == 0) 100 else 50
            canvas.drawLine(i.toFloat(), height.toFloat(), i.toFloat(), (height - lineLength).toFloat(), paint)
        }
    }
}
