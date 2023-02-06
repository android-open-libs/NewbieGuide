package sing.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sing.top.newbie.guide.NewbieGuide.with
import sing.top.newbie.guide.core.Controller
import sing.top.newbie.guide.demo.R
import sing.top.newbie.guide.listener.OnLayoutInflatedListener
import sing.top.newbie.guide.model.GuidePage
import java.util.*

/**
 * Created by hubert
 *
 *
 * Created on 2017/9/23.
 */
class RecyclerViewActivity : AppCompatActivity() {
    private var layoutManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recylerview)
        val recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val data = ArrayList<String>()
        for (i in 0..29) {
            data.add("item $i")
        }
        recyclerView.adapter = Adapter(data)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val firstVisibleItemPosition = layoutManager!!.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager!!.findLastVisibleItemPosition()
                    val targetPosition = 20
                    if (firstVisibleItemPosition <= targetPosition && targetPosition < lastVisibleItemPosition) { //指定位置滚动到屏幕中
                        with(this@RecyclerViewActivity)
                            .setLabel("grid_view_guide")
                            .alwaysShow(true)
                            .addGuidePage(
                                GuidePage.newInstance() //注意获取position位置view的方法，不要使用getChildAt
                                    .addHighLight(layoutManager!!.findViewByPosition(targetPosition)!!)
                                    .setLayoutRes(R.layout.view_guide_rv1)
                                    .setOnLayoutInflatedListener(object : OnLayoutInflatedListener {
                                        override fun onLayoutInflated(
                                            view: View,
                                            controller: Controller
                                        ) {
                                            val tv = view.findViewById<TextView>(R.id.tv)
                                            tv.text = "滚动后才能可见的item这样使用"
                                        }
                                    })
                            )
                            .show()
                    }
                }
            }
        })
        recyclerView.post {
            with(this@RecyclerViewActivity)
                .setLabel("grid_view_guide")
                .alwaysShow(true)
                .addGuidePage(
                    GuidePage.newInstance() //getChildAt获取的是屏幕中可见的第一个，并不是数据中的position
                        .addHighLight(recyclerView.getChildAt(0))
                        .setLayoutRes(R.layout.view_guide_rv1)
                        .setOnLayoutInflatedListener(object : OnLayoutInflatedListener {
                            override fun onLayoutInflated(view: View, controller: Controller) {
                                val tv = view.findViewById<TextView>(R.id.tv)
                                tv.text = "第一页可见的item这样使用"
                            }
                        })
                )
                .show()
        }
    }

    class Adapter(private val list: List<String>?) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = list!![position]
        }

        override fun getItemCount(): Int {
            return list!!.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView

            init {
                textView = itemView.findViewById<View>(R.id.tv) as TextView
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, RecyclerViewActivity::class.java))
        }
    }
}