package sing.top.newbie.guide.listener

import android.graphics.Canvas
import android.graphics.RectF

interface OnHighlightDrewListener {
    fun onHighlightDrew(canvas: Canvas, rectF: RectF)
}