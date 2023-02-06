package sing.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sing.top.newbie.guide.NewbieGuide.with
import sing.top.newbie.guide.core.Controller
import sing.top.newbie.guide.demo.R
import sing.top.newbie.guide.listener.OnLayoutInflatedListener
import sing.top.newbie.guide.listener.OnPageChangedListener
import sing.top.newbie.guide.model.GuidePage
import sing.top.newbie.guide.model.HighLight

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.tv).setOnClickListener {
            GridViewActivity.start(this@MainActivity)
        }

        // 底下高亮的view
        val tvBottom = findViewById<TextView>(R.id.tv_bottom)


        val enterAnimation: Animation = AlphaAnimation(0f, 1f)
        enterAnimation.duration = 600
        enterAnimation.fillAfter = true

        val exitAnimation: Animation = AlphaAnimation(1f, 0f)
        exitAnimation.duration = 600
        exitAnimation.fillAfter = true

        //新增多页模式，即一个引导层显示多页引导内容
        with(this)
            .setLabel("page") //设置引导层标示区分不同引导层，必传！否则报错
            .setOnPageChangedListener(object : OnPageChangedListener {
                override fun onPageChanged(page: Int) {
                    //引导页切换，page为当前页位置，从0开始
                    Toast.makeText(this@MainActivity, "引导页切换：$page", Toast.LENGTH_SHORT).show()
                }
            })
            .alwaysShow(true) //是否每次都显示引导层，默认false，只显示一次
//            .showShadow(false) //是否显示阴影
            .addGuidePage( //添加一页引导页
                GuidePage.newInstance() //创建一个实例
                    .addHighLight(findViewById<View>(R.id.btn)) //添加高亮的view
                    .addHighLight(tvBottom)
                    .setLayoutRes(R.layout.view_guide) //设置引导页布局
                    .setEnterAnimation(enterAnimation) //进入动画
                    .setExitAnimation(exitAnimation) //退出动画
            )
            .addGuidePage(
                GuidePage.newInstance()
                    .addHighLight(tvBottom, HighLight.Shape.RECTANGLE, 20)
                    .setLayoutRes(R.layout.view_guide_custom, R.id.tv) //引导页布局，点击跳转下一页或者消失引导层的控件id
                    .setOnLayoutInflatedListener(object : OnLayoutInflatedListener {
                        override fun onLayoutInflated(view: View, controller: Controller) {
                            view.findViewById<View>(R.id.textView).setOnClickListener { controller.showPreviewPage() }
                        }
                    })
                    .setEverywhereCancelable(false) //是否点击任意地方跳转下一页或者消失引导层，默认true
                    .setBackgroundColor(resources.getColor(R.color.testColor)) //设置背景色，建议使用有透明度的颜色
                    .setEnterAnimation(enterAnimation) //进入动画
                    .setExitAnimation(exitAnimation) //退出动画
            )
            .addGuidePage(
                GuidePage.newInstance()
                    .addHighLight(tvBottom)
                    .setLayoutRes(R.layout.view_guide_dialog)
                    .setOnLayoutInflatedListener(object : OnLayoutInflatedListener {
                        override fun onLayoutInflated(view: View, controller: Controller) {
                            view.findViewById<View>(R.id.btn_ok).setOnClickListener { controller.showPage(0) }
                        }
                    })
            )
            .show() //显示引导层(至少需要一页引导页才能显示)
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }
}