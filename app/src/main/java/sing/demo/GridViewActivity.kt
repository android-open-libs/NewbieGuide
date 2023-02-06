package sing.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import sing.top.newbie.guide.NewbieGuide.with
import sing.top.newbie.guide.demo.R
import sing.top.newbie.guide.model.GuidePage
import sing.top.newbie.guide.model.HighLight
import java.util.*

class GridViewActivity : AppCompatActivity() {
    private val from = arrayOf("image", "title")
    private val to = intArrayOf(R.id.image, R.id.title)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_view)
        val gridView = findViewById<View>(R.id.grid_view) as GridView
        val pictureAdapter = SimpleAdapter(
            this, list,
            R.layout.picture_item, from, to
        )
        gridView.adapter = pictureAdapter
        gridView.post { //高亮gridView的第2个子view
            val childAt = gridView.getChildAt(1)
            with(this@GridViewActivity)
                .setLabel("grid_view_guide")
                .alwaysShow(true)
                .addGuidePage(
                    GuidePage.newInstance()
                        .addHighLight(childAt, HighLight.Shape.RECTANGLE)
                        .setEverywhereCancelable(false)
                        .setLayoutRes(R.layout.view_guide, R.id.iv)
                )
                .show()
        }
    }

    val list: List<Map<String, Any?>>
        get() {
            val list: MutableList<Map<String, Any?>> = ArrayList()
            val titles =
                arrayOf("本地音乐", "我的最爱", "我的下载", "我的歌单", "最近播放", "我的最爱", "我的下载", "我的歌单", "最近播放")
            val images = arrayOf(
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
            )
            for (i in 0..49) {
                var position = i
                val map: MutableMap<String, Any?> = HashMap()
                val length = images.size - 1
                if (position > length) {
                    position = position % length
                }
                map["image"] = images[position]
                map["title"] = titles[position]
                list.add(map)
            }
            return list
        }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, GridViewActivity::class.java)
            context.startActivity(starter)
        }
    }
}