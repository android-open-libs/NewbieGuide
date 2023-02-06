package sing.top.newbie.guide.model

import android.graphics.RectF
import android.view.View
import sing.top.newbie.guide.util.ViewUtils.getLocationInView

/**
 * Created by hubert
 *
 *
 * Created on 2017/7/27.
 */
class HighlightView(
    private val mHole: View?,
    override val shape: HighLight.Shape,
    override val round: Int,
    private val padding: Int // 高亮相对view的padding
) : HighLight {

    override var options: HighlightOptions? = null
    private var rectF: RectF? = null
    override val radius: Float
        get() {
            requireNotNull(mHole) { "the highlight view is null!" }
            return (Math.max(mHole.width / 2, mHole.height / 2) + padding).toFloat()
        }

    override fun getRectF(target: View): RectF? {
        requireNotNull(mHole) { "the highlight view is null!" }
        if (rectF == null) {
            rectF = fetchLocation(target)
        } else if (options != null && options!!.fetchLocationEveryTime) {
            rectF = fetchLocation(target)
        }
        return rectF
    }

    private fun fetchLocation(target: View?): RectF {
        val location = RectF()
        val locationInView = getLocationInView(target, mHole)
        location.left = (locationInView.left - padding).toFloat()
        location.top = (locationInView.top - padding).toFloat()
        location.right = (locationInView.right + padding).toFloat()
        location.bottom = (locationInView.bottom + padding).toFloat()
        return location
    }
}