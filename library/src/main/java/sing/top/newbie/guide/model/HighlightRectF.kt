package sing.top.newbie.guide.model

import android.graphics.RectF
import android.view.View

class HighlightRectF(
    private val rectF: RectF,
    override val shape: HighLight.Shape,
    override val round: Int
) : HighLight {
    override var options: HighlightOptions? = null
    override fun getRectF(view: View): RectF {
        return rectF
    }

    override val radius: Float
        get() = Math.min(rectF.width() / 2, rectF.height() / 2)
}