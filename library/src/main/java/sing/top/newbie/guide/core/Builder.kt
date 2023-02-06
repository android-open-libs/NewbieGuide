package sing.top.newbie.guide.core

import android.app.Activity
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import sing.top.newbie.guide.listener.OnGuideChangedListener
import sing.top.newbie.guide.listener.OnPageChangedListener
import sing.top.newbie.guide.model.GuidePage


class Builder {

    var activity: Activity? = null
    var fragment: Fragment? = null
    private var label: String = ""
    private var alwaysShow = false // 总是显示 default false
    private var showShadow = false // 是否显示阴影
    private var anchor : View? = null //锚点view
    private var showCounts = 1 // 显示次数 default once

    private var onGuideChangedListener: OnGuideChangedListener? = null
    private var onPageChangedListener: OnPageChangedListener? = null
    private var guidePages: MutableList<GuidePage> = arrayListOf()

    constructor(activity: Activity?) {
        this.activity = activity
    }

    constructor(fragment: Fragment) {
        this.fragment = fragment
        activity = fragment.activity
    }

    /**
     * 引导层显示的锚点，即根布局，不设置的话默认是decorView
     *
     * @param anchor root
     */
    fun anchor(anchor: View): Builder {
        this.anchor = anchor
        return this
    }

    fun getAnchor(): View? {
        return anchor
    }
    /**
     * 引导层的显示次数，默认是1次。<br></br>
     * 这里的次数是通过sp控制的，是指同一个label在不清除缓存的情况下可以显示的总次数。
     *
     * @param count 次数
     */
    fun setShowCounts(count: Int): Builder {
        showCounts = count
        return this
    }
    fun getShowCounts(): Int {
        return showCounts
    }

    /**
     * 是否总是显示引导层，即是否无限次的显示。<br></br>
     * 默认为false，如果设置了true，[Builder.setShowCounts] 将无效。
     *
     * @param b
     */
    fun alwaysShow(b: Boolean): Builder {
        alwaysShow = b
        return this
    }
    fun getAlwaysShow(): Boolean {
        return alwaysShow
    }

    /**
     * 是否现在阴影，默认不开启
     */
    fun showShadow(b: Boolean): Builder {
        showShadow = b
        return this
    }
    fun isShadow(): Boolean {
        return showShadow
    }

    /**
     * 添加引导页
     */
    fun addGuidePage(page: GuidePage): Builder {
        guidePages.add(page)
        return this
    }
    fun getGuidePage(): MutableList<GuidePage> {
        return guidePages
    }

    /**
     * 设置引导层隐藏，显示监听
     */
    fun setOnGuideChangedListener(listener: OnGuideChangedListener): Builder {
        onGuideChangedListener = listener
        return this
    }
    fun getGuideChangedListener() : OnGuideChangedListener?{
        return onGuideChangedListener
    }

    /**
     * 设置引导页切换监听
     */
    fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener): Builder {
        this.onPageChangedListener = onPageChangedListener
        return this
    }
    fun getPageChangedListener(): OnPageChangedListener? {
        return onPageChangedListener
    }

    /**
     * 设置引导层的辨识名，必须设置项，否则报错
     */
    fun setLabel(label: String): Builder {
        this.label = label
        return this
    }
    fun getLabel(): String {
        return label
    }

    /**
     * 构建引导层controller
     *
     * @return controller
     */
    fun build(): Controller {
        check()
        return Controller(this)
    }

    /**
     * 构建引导层controller并直接显示引导层
     *
     * @return controller
     */
    fun show(): Controller {
        check()
        val controller = Controller(this)
        controller.show()
        return controller
    }

    private fun check() {
        require(!TextUtils.isEmpty(label)) { "the param 'label' is missing, please call setLabel()" }
        check(!(activity == null && fragment != null)) { "activity is null, please make sure that fragment is showing when call NewbieGuide" }
    }
}