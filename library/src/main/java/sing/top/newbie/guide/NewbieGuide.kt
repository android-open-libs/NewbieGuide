package sing.top.newbie.guide

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import sing.top.newbie.guide.core.Builder

object NewbieGuide {

    const val TAG = "NewbieGuide"

    /**
     * 成功显示标示
     */
    const val SUCCESS = 1

    /**
     * 显示失败标示，即已经显示过一次
     */
    const val FAILED = -1

    /**
     * 新手指引入口
     *
     * @param activity activity
     * @return builder对象，设置参数
     */
    fun with(activity: Activity): Builder {
        return Builder(activity)
    }

    @JvmStatic
    fun with(fragment: Fragment): Builder {
        return Builder(fragment)
    }

    /**
     * 重置标签的显示次数
     *
     * @param context
     * @param label   标签名
     */
    fun resetLabel(context: Context, label: String?) {
        val sp = context.getSharedPreferences(TAG, Activity.MODE_PRIVATE)
        sp.edit().putInt(label, 0).apply()
    }
}