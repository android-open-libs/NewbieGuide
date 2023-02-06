package sing.demo

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sing.top.newbie.guide.NewbieGuide
import sing.top.newbie.guide.core.Controller
import sing.top.newbie.guide.demo.R
import sing.top.newbie.guide.listener.OnGuideChangedListener
import sing.top.newbie.guide.listener.OnHighlightDrewListener
import sing.top.newbie.guide.listener.OnLayoutInflatedListener
import sing.top.newbie.guide.model.GuidePage
import sing.top.newbie.guide.model.HighLight
import sing.top.newbie.guide.model.HighlightOptions
import sing.top.newbie.guide.model.RelativeGuide

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        simple()
        anchor()
        listener()
        rectHighLight()
        relative()

        findViewById<View>(R.id.btn_multi).setOnClickListener { MainActivity.start(this@FirstActivity) }
        findViewById<View>(R.id.btn_list).setOnClickListener { RecyclerViewActivity.start(this@FirstActivity) }
        findViewById<View>(R.id.btn_scroll).setOnClickListener { ScrollViewActivity.start(this@FirstActivity) }
    }

    //简单使用
    private fun simple() {
        val btnSimple = findViewById<Button>(R.id.btn_simple)
        btnSimple.setOnClickListener {
            NewbieGuide.with(this@FirstActivity)
                .setLabel("guide1") //
                //  .setShowCounts(3)//控制次数
                .alwaysShow(true) //总是显示，调试时可以打开
                .addGuidePage(
                    GuidePage.newInstance()
                        .addHighLight(btnSimple)
//                        .setLayoutRes(R.layout.view_guide_simple)
                        .setLayoutRes(R.layout.view_guide_simple,R.id.aaa)
                        .setEverywhereCancelable(false) //是否点击任意位置消失引导页
                        .setOnLayoutInflatedListener(object : OnLayoutInflatedListener {
                            override fun onLayoutInflated(view: View, controller: Controller) {
                                Log.e("TAG","初始化完成")
                            }
                        })
                )
                .show()
        }
    }

    //设置anchor 及 自定义绘制图形
    private fun anchor(){
        val anchorView = findViewById<View>(R.id.ll_anchor)
        val btnAnchor = findViewById<Button>(R.id.btn_anchor)
        btnAnchor.setOnClickListener {
            val options = HighlightOptions.Builder()
                .setOnHighlightDrewListener(object : OnHighlightDrewListener {
                    override fun onHighlightDrew(canvas: Canvas, rectF: RectF) {
                        val paint = Paint()
                        paint.color = Color.WHITE
                        paint.style = Paint.Style.STROKE
                        paint.strokeWidth = 10f
                        paint.pathEffect = DashPathEffect(floatArrayOf(20f, 20f), 0F)
                        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint)
                    }
                })
                .build()
            NewbieGuide.with(this@FirstActivity)
                .setLabel("anchor")
                .anchor(anchorView) // 设置浮层的大小
                .alwaysShow(true) //总是显示，调试时可以打开
                .addGuidePage(
                    GuidePage.newInstance()
                        .addHighLightWithOptions(btnAnchor, HighLight.Shape.CIRCLE, options)
                        .setLayoutRes(R.layout.view_guide_anchor)
                )
                .show()
        }
    }

    // 监听
    private fun listener(){
        val btnListener = findViewById<Button>(R.id.btn_listener)
        btnListener.setOnClickListener {
            NewbieGuide.with(this@FirstActivity)
                .setLabel("listener")
                .alwaysShow(true) //总是显示，调试时可以打开
                .setOnGuideChangedListener(object : OnGuideChangedListener {
                    override fun onShowed(controller: Controller) {
                        Toast.makeText(this@FirstActivity, "引导层显示", Toast.LENGTH_SHORT).show()
                    }

                    override fun onRemoved(controller: Controller) {
                        Toast.makeText(this@FirstActivity, "引导层消失", Toast.LENGTH_SHORT).show()
                    }
                })
                .addGuidePage(
                    GuidePage.newInstance()
                        .addHighLight(btnListener)
                )
                .show()
        }
    }

    // 指定地方高亮
    private fun rectHighLight(){
        findViewById<View>(R.id.btn_rect).setOnClickListener {
            NewbieGuide.with(this@FirstActivity)
                .setLabel("rect")
                .alwaysShow(true) //总是显示，调试时可以打开
                .addGuidePage(
                    GuidePage.newInstance()
                        .addHighLight(RectF(100F, 300F, 500F, 700F))
                )
                .show()
        }
    }

    // 相对位置展示引导布局
    private fun relative(){
        val btnRelative = findViewById<View>(R.id.btn_relative)
        btnRelative.setOnClickListener {
            val options = HighlightOptions.Builder()
                .setRelativeGuide(object : RelativeGuide(R.layout.view_relative_guide, Gravity.BOTTOM, 100) {
                    override fun onLayoutInflated(view: View?, controller: Controller?) {
                        val textView = view!!.findViewById<TextView>(R.id.tv)
                        textView.text = "inflated"
                    }

                    // 偏移多少
//                    override fun offsetMargin(marginInfo: MarginInfo?, viewGroup: ViewGroup?, view: View?) {
//                        marginInfo!!.leftMargin += 100
//                    }
                })
                .setOnClickListener {
                    Toast.makeText(this@FirstActivity, "highlight click", Toast.LENGTH_SHORT).show()
                }
                .build()
            val page = GuidePage.newInstance()
                .addHighLightWithOptions(btnRelative, options)
            NewbieGuide.with(this@FirstActivity)
                .setLabel("relative")
                .alwaysShow(true) //总是显示，调试时可以打开
                .addGuidePage(page)
                .show()
        }
    }
}