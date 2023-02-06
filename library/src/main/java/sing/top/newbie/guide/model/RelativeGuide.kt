package sing.top.newbie.guide.model

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import sing.top.newbie.guide.core.Controller
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

open class RelativeGuide {
    @IntDef(Gravity.LEFT, Gravity.TOP, Gravity.RIGHT, Gravity.BOTTOM)
    @Retention(RetentionPolicy.SOURCE)
    internal annotation class LimitGravity
    class MarginInfo {
        var leftMargin = 0
        var topMargin = 0
        var rightMargin = 0
        var bottomMargin = 0
        var gravity = 0
        override fun toString(): String {
            return "MarginInfo{" +
                    "leftMargin=" + leftMargin +
                    ", topMargin=" + topMargin +
                    ", rightMargin=" + rightMargin +
                    ", bottomMargin=" + bottomMargin +
                    ", gravity=" + gravity +
                    '}'
        }
    }

    var highLight: HighLight? = null

    @LayoutRes
    var layout: Int
    var padding = 0
    var gravity: Int

    constructor(@LayoutRes layout: Int, @LimitGravity gravity: Int) {
        this.layout = layout
        this.gravity = gravity
    }

    /**
     * @param layout  相对位置引导布局
     * @param gravity 仅限left top right bottom
     * @param padding 与高亮view的padding，单位px
     */
    constructor(@LayoutRes layout: Int, @LimitGravity gravity: Int, padding: Int) {
        this.layout = layout
        this.gravity = gravity
        this.padding = padding
    }

    fun getGuideLayout(viewGroup: ViewGroup, controller: Controller?): View {
        val view = LayoutInflater.from(viewGroup.context).inflate(layout, viewGroup, false)
        onLayoutInflated(view)
        onLayoutInflated(view, controller)
        val layoutParams = view.layoutParams as FrameLayout.LayoutParams
        val marginInfo = getMarginInfo(gravity, viewGroup, view)
        offsetMargin(marginInfo, viewGroup, view)
        layoutParams.gravity = marginInfo.gravity
        layoutParams.leftMargin += marginInfo.leftMargin
        layoutParams.topMargin += marginInfo.topMargin
        layoutParams.rightMargin += marginInfo.rightMargin
        layoutParams.bottomMargin += marginInfo.bottomMargin
        view.layoutParams = layoutParams
        return view
    }

    private fun getMarginInfo(@LimitGravity gravity: Int, viewGroup: ViewGroup, view: View): MarginInfo {
        val marginInfo = MarginInfo()
        val rectF = highLight!!.getRectF(viewGroup)
        when (gravity) {
            Gravity.LEFT -> {
                marginInfo.gravity = Gravity.RIGHT
                marginInfo.rightMargin = (viewGroup.width - rectF!!.left + padding).toInt()
                marginInfo.topMargin = rectF.top.toInt()
            }
            Gravity.TOP -> {
                marginInfo.gravity = Gravity.BOTTOM
                marginInfo.bottomMargin = (viewGroup.height - rectF!!.top + padding).toInt()
                marginInfo.leftMargin = rectF.left.toInt()
            }
            Gravity.RIGHT -> {
                marginInfo.leftMargin = (rectF!!.right + padding).toInt()
                marginInfo.topMargin = rectF.top.toInt()
            }
            Gravity.BOTTOM -> {
                marginInfo.topMargin = (rectF!!.bottom + padding).toInt()
                marginInfo.leftMargin = rectF.left.toInt()
            }
        }
        return marginInfo
    }

    protected open fun offsetMargin(marginInfo: MarginInfo?, viewGroup: ViewGroup?, view: View?) {
        //do nothing
    }

    /**
     * 复写初始化布局
     *
     * @param view inflated from layout
     * @see RelativeGuide.onLayoutInflated
     */
    @Deprecated("")
    protected fun onLayoutInflated(view: View?) {
        //do nothing
    }

    /**
     * 复写初始化布局
     *
     * @param view       inflated from layout
     * @param controller controller
     */
    protected open fun onLayoutInflated(view: View?, controller: Controller?) {
        //do nothing
    }
}