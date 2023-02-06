package sing.top.newbie.guide.core

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.RelativeLayout
import sing.top.newbie.guide.NewbieGuide
import sing.top.newbie.guide.listener.AnimationListenerAdapter
import sing.top.newbie.guide.model.GuidePage
import sing.top.newbie.guide.model.HighLight


class GuideLayout : FrameLayout {

    val DEFAULT_BACKGROUND_COLOR = -0x4e000000

    private var controller: Controller? = null
    private var mPaint: Paint? = null
    private var guidePage : GuidePage? = null
    private var listener: OnGuideLayoutDismissListener? = null
    private var downX = 0f
    private var downY = 0f
    private var touchSlop = 0

    constructor(context: Context, page: GuidePage, controller: Controller,showShadow:Boolean) : super(context) {
        init(showShadow)
        setGuidePage(page)
        this.controller = controller
    }

    private fun init(showShadow:Boolean) {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        val xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        mPaint!!.xfermode = xfermode

        //设置画笔遮罩滤镜,可以传入BlurMaskFilter或EmbossMaskFilter，前者为模糊遮罩滤镜而后者为浮雕遮罩滤镜
        //这个方法已经被标注为过时的方法了，如果你的应用启用了硬件加速，你是看不到任何阴影效果的
        if (showShadow){
            mPaint!!.maskFilter = BlurMaskFilter(10F, BlurMaskFilter.Blur.INNER)
        }
        //关闭当前view的硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        //ViewGroup默认设定为true，会使onDraw方法不执行，如果复写了onDraw(Canvas)方法，需要清除此标记
        setWillNotDraw(false)
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

     fun setGuidePage(page: GuidePage) {
        guidePage = page
        setOnClickListener {
            if (guidePage!!.isEverywhereCancelable()) {
                remove()
            }
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val upX = event.x
                val upY = event.y
                if (Math.abs(upX - downX) < touchSlop && Math.abs(upY - downY) < touchSlop) {
                    val highLights = guidePage!!.getHighLights()
                    for (highLight in highLights) {
                        val rectF = highLight!!.getRectF(parent as ViewGroup)
                        if (rectF!!.contains(upX, upY)) {
                            notifyClickListener(highLight)
                            return true
                        }
                    }
                    performClick()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun notifyClickListener(highLight: HighLight) {
        val options = highLight.options
        if (options != null) {
            if (options.onClickListener != null) {
                options.onClickListener!!.onClick(this)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val backgroundColor = guidePage!!.getBackgroundColor()
        canvas.drawColor(if (backgroundColor == 0) DEFAULT_BACKGROUND_COLOR else backgroundColor)
        drawHighlights(canvas)
    }

    private fun drawHighlights(canvas: Canvas) {
        if (guidePage != null) {
            val highLights = guidePage!!.getHighLights()
            for (highLight in highLights) {
                val rectF = highLight.getRectF(parent as ViewGroup)
                if (rectF != null){
                    when (highLight.shape) {
                        HighLight.Shape.CIRCLE -> canvas.drawCircle(rectF.centerX(), rectF.centerY(), highLight.radius, mPaint!!)
                        HighLight.Shape.OVAL -> canvas.drawOval(rectF, mPaint!!)
                        HighLight.Shape.ROUND_RECTANGLE -> canvas.drawRoundRect(rectF, highLight.round.toFloat(), highLight.round.toFloat(), mPaint!!)
                        HighLight.Shape.RECTANGLE -> canvas.drawRect(rectF, mPaint!!)
                        else -> canvas.drawRect(rectF, mPaint!!)
                    }
                    notifyDrewListener(canvas, highLight, rectF)
                }
            }
        }
    }

    private fun notifyDrewListener(canvas: Canvas, highLight: HighLight, rectF: RectF) {
        val options = highLight.options
        if (options?.onHighlightDrewListener != null) {
            options.onHighlightDrewListener!!.onHighlightDrew(canvas, rectF)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addCustomToLayout(guidePage)
        val enterAnimation = guidePage!!.getEnterAnimation()
        enterAnimation?.let { startAnimation(it) }
    }

    /**
     * 将自定义布局填充到guideLayout中
     */
    private fun addCustomToLayout(guidePage: GuidePage?) {
        removeAllViews()
        val layoutResId = guidePage!!.getLayoutResId()
        if (layoutResId != 0) {
            val view = LayoutInflater.from(context).inflate(layoutResId, this, false)
            val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            val viewIds = guidePage.getClickToDismissIds()
            if (viewIds != null && viewIds.size > 0) {
                for (viewId in viewIds) {
                    val click = view.findViewById<View>(viewId)
                    if (click != null){
                        click.setOnClickListener { remove() }
                    }else{
                        Log.w(NewbieGuide.TAG, "can't find the view by id : $viewId which used to remove guide page")
                    }
                }
            }
            val inflatedListener = guidePage.getOnLayoutInflatedListener()
            inflatedListener?.onLayoutInflated(view, controller!!)
            addView(view, params)
        }
        val relativeGuides = guidePage.getRelativeGuides()
        if (relativeGuides.size > 0) {
            for (relativeGuide in relativeGuides) {
                addView(relativeGuide.getGuideLayout((parent as ViewGroup), controller))
            }
        }
    }

    fun setOnGuideLayoutDismissListener(listener: OnGuideLayoutDismissListener) {
        this.listener = listener
    }

    fun remove() {
        val exitAnimation = guidePage!!.getExitAnimation()
        if (exitAnimation != null) {
            exitAnimation.setAnimationListener(object : AnimationListenerAdapter() {
                override fun onAnimationEnd(animation: Animation) {
                    dismiss()
                }
            })
            startAnimation(exitAnimation)
        } else {
            dismiss()
        }
    }

    private fun dismiss() {
        if (parent != null) {
            (parent as ViewGroup).removeView(this)
            if (listener != null) {
                listener!!.onGuideLayoutDismiss(this)
            }
        }
    }

    interface OnGuideLayoutDismissListener {
        fun onGuideLayoutDismiss(guideLayout: GuideLayout)
    }
}