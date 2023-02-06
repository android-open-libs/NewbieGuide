package sing.demo

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sing.top.newbie.guide.NewbieGuide.with
import sing.top.newbie.guide.core.Controller
import sing.top.newbie.guide.demo.R
import sing.top.newbie.guide.model.GuidePage
import sing.top.newbie.guide.model.HighlightOptions

/**
 * Created by hubert on 2018/12/11.
 */
class ScrollViewActivity : AppCompatActivity(){


    private var controller: Controller? = null

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, ScrollViewActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view)
        val light = findViewById<View>(R.id.v_light)
        val lightNext = findViewById<View>(R.id.v_light_next)
        val options = HighlightOptions.Builder()
            .isFetchLocationEveryTime(true)
            .build()
        controller = with(this@ScrollViewActivity)
            .setLabel("scroll1")
            .alwaysShow(true)
            .addGuidePage(
                GuidePage.newInstance()
                    .addHighLightWithOptions(light, options)
            )
            .build()
        val scrollView = findViewById<ObservableScrollView>(R.id.scrollView)
        scrollView.setScrollViewListener(object : ObservableScrollView.OnScrollChangeListener {
            override fun onScrollChanged(scrollView: ObservableScrollView, x: Int, y: Int, oldx: Int, oldy: Int) {
                val scrollBounds = Rect()
                scrollView.getHitRect(scrollBounds)
                if (lightNext.getLocalVisibleRect(scrollBounds)) {
                    controller!!.show()
                }
            }
        })
    }
}