package sing.top.newbie.guide.model

import android.graphics.RectF
import android.view.View
import android.view.animation.Animation
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import sing.top.newbie.guide.listener.OnHighlightDrewListener
import sing.top.newbie.guide.listener.OnLayoutInflatedListener


class GuidePage {
    private val highLights: MutableList<HighLight> = ArrayList()
    private var everywhereCancelable = true
    private var backgroundColor: Int = 0

    private var layoutResId = 0
    private var clickToDismissIds = intArrayOf()
    private var onLayoutInflatedListener: OnLayoutInflatedListener? = null
    private var onHighlightDrewListener: OnHighlightDrewListener? = null
    private var enterAnimation: Animation? = null
    private var exitAnimation:Animation? = null

    companion object {
        fun newInstance(): GuidePage {
            return GuidePage()
        }
    }

    fun addHighLight(view: View): GuidePage {
        return addHighLight(view, HighLight.Shape.RECTANGLE, 0, 0, null)
    }

    fun addHighLight(view: View, relativeGuide: RelativeGuide): GuidePage {
        return addHighLight(view, HighLight.Shape.RECTANGLE, 0, 0, relativeGuide)
    }

    fun addHighLight(view: View, shape: HighLight.Shape): GuidePage {
        return addHighLight(view, shape, 0, 0, null)
    }

    fun addHighLight(view: View, shape: HighLight.Shape, relativeGuide: RelativeGuide): GuidePage {
        return addHighLight(view, shape, 0, 0, relativeGuide)
    }

    fun addHighLight(view: View, shape: HighLight.Shape, padding: Int): GuidePage {
        return addHighLight(view, shape, 0, padding, null)
    }

    fun addHighLight(view: View, shape: HighLight.Shape, padding: Int, relativeGuide: RelativeGuide): GuidePage{
        return addHighLight(view, shape, 0, padding, relativeGuide)
    }

    /**
     * 添加需要高亮的view
     *
     * @param view          需要高亮的view
     * @param shape         高亮形状[sing.top.newbie.guide.model.HighLight.Shape]
     * @param round         圆角尺寸，单位dp，仅[sing.top.newbie.guide.model.HighLight.Shape.ROUND_RECTANGLE]有效
     * @param padding       高亮相对view的padding,单位px
     * @param relativeGuide 相对于高亮的引导布局
     */
    fun addHighLight(view: View, shape: HighLight.Shape, round: Int, padding: Int, @Nullable relativeGuide: RelativeGuide?): GuidePage {
        val highlight = HighlightView(view, shape, round, padding)
        if (relativeGuide != null) {
            relativeGuide.highLight = highlight
            highlight.options = HighlightOptions.Builder().setRelativeGuide(relativeGuide).build()
        }
        highLights.add(highlight)
        return this
    }


    fun addHighLight(rectF: RectF): GuidePage {
        return addHighLight(rectF, HighLight.Shape.RECTANGLE, 0, null)
    }

    fun addHighLight(rectF: RectF, relativeGuide: RelativeGuide): GuidePage {
        return addHighLight(rectF, HighLight.Shape.RECTANGLE, 0, relativeGuide)
    }

    fun addHighLight(rectF: RectF, shape: HighLight.Shape): GuidePage {
        return addHighLight(rectF, shape, 0, null)
    }

    fun addHighLight(rectF: RectF, shape: HighLight.Shape, relativeGuide: RelativeGuide): GuidePage {
        return addHighLight(rectF, shape, 0, relativeGuide)
    }

    fun addHighLight(rectF: RectF, shape: HighLight.Shape, round: Int): GuidePage {
        return addHighLight(rectF, shape, round, null)
    }

    /**
     * 添加高亮区域
     *
     * @param rectF         高亮区域，相对与anchor view（默认是decorView）
     * @param shape         高亮形状[sing.top.newbie.guide.model.HighLight.Shape]
     * @param round         圆角尺寸，单位dp，仅[sing.top.newbie.guide.model.HighLight.Shape.ROUND_RECTANGLE]有效
     * @param relativeGuide 相对于高亮的引导布局
     */
    fun addHighLight(rectF: RectF, shape: HighLight.Shape, round: Int, relativeGuide: RelativeGuide?): GuidePage {
        val highlight = HighlightRectF(rectF, shape, round)
        if (relativeGuide != null) {
            relativeGuide.highLight = highlight
            highlight.options = HighlightOptions.Builder().setRelativeGuide(relativeGuide).build()
        }
        highLights.add(highlight)
        return this
    }


    fun addHighLightWithOptions(view: View, options: HighlightOptions): GuidePage {
        return addHighLightWithOptions(view, HighLight.Shape.RECTANGLE, 0, 0, options)
    }

    fun addHighLightWithOptions(view: View, shape: HighLight.Shape, options: HighlightOptions): GuidePage {
        return addHighLightWithOptions(view, shape, 0, 0, options)
    }

    fun addHighLightWithOptions(view: View, shape: HighLight.Shape, round: Int, padding: Int, options: HighlightOptions?): GuidePage {
        val highlight = HighlightView(view, shape, round, padding)
        if (options != null) {
            if (options.relativeGuide != null) {
                options.relativeGuide!!.highLight = highlight
            }
        }
        highlight.options = options
        highLights.add(highlight)
        return this
    }

    fun addHighLightWithOptions(rectF: RectF, options: HighlightOptions): GuidePage {
        return addHighLightWithOptions(rectF, HighLight.Shape.RECTANGLE, 0, options)
    }

    fun addHighLightWithOptions(rectF: RectF, shape: HighLight.Shape, options: HighlightOptions): GuidePage {
        return addHighLightWithOptions(rectF, shape, 0, options)
    }

    fun addHighLightWithOptions(rectF: RectF, shape: HighLight.Shape, round: Int, options: HighlightOptions?): GuidePage {
        val highlight = HighlightRectF(rectF, shape, round)
        if (options != null) {
            if (options.relativeGuide != null) {
                options.relativeGuide!!.highLight = highlight
            }
        }
        highlight.options = options
        highLights.add(highlight)
        return this
    }


    /**
     * 添加引导层布局
     *
     * @param resId 布局id
     * @param id    布局中点击消失引导页的控件id
     */
    fun setLayoutRes(@LayoutRes resId: Int, vararg id: Int): GuidePage {
        layoutResId = resId
        clickToDismissIds = id
        return this
    }

    fun setEverywhereCancelable(everywhereCancelable: Boolean): GuidePage {
        this.everywhereCancelable = everywhereCancelable
        return this
    }

    /**
     * 设置背景色
     */
    fun setBackgroundColor(@ColorInt backgroundColor: Int): GuidePage {
        this.backgroundColor = backgroundColor
        return this
    }

    /**
     * 设置自定义layout填充监听，用于自定义layout初始化
     *
     * @param onLayoutInflatedListener listener
     */
    fun setOnLayoutInflatedListener(onLayoutInflatedListener: OnLayoutInflatedListener): GuidePage {
        this.onLayoutInflatedListener = onLayoutInflatedListener
        return this
    }

    /**
     * 设置进入动画
     */
    fun setEnterAnimation(enterAnimation: Animation): GuidePage {
        this.enterAnimation = enterAnimation
        return this
    }

    /**
     * 设置退出动画
     */
    fun setExitAnimation(exitAnimation: Animation): GuidePage {
        this.exitAnimation = exitAnimation
        return this
    }

    fun isEverywhereCancelable(): Boolean {
        return everywhereCancelable
    }

    fun isEmpty(): Boolean {
        return layoutResId == 0 && highLights.size == 0
    }

    fun getHighLights(): List<HighLight> {
        return highLights
    }

    fun getBackgroundColor(): Int {
        return backgroundColor
    }

    fun getLayoutResId(): Int {
        return layoutResId
    }

    fun getClickToDismissIds(): IntArray {
        return clickToDismissIds
    }

    fun getOnLayoutInflatedListener(): OnLayoutInflatedListener? {
        return onLayoutInflatedListener
    }

    fun getEnterAnimation(): Animation? {
        return enterAnimation
    }

    fun getExitAnimation(): Animation? {
        return exitAnimation
    }

    fun getRelativeGuides(): List<RelativeGuide> {
        val relativeGuides: MutableList<RelativeGuide> = ArrayList()
        for (highLight in highLights) {
            val options = highLight.options
            if (options != null) {
                if (options.relativeGuide != null) {
                    relativeGuides.add(options.relativeGuide!!)
                }
            }
        }
        return relativeGuides
    }
}