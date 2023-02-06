package sing.top.newbie.guide.util

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.viewpager.widget.ViewPager

object ViewUtils {
    private const val FRAGMENT_CON = "NoSaveStateFrameLayout"

    @JvmStatic
    fun getLocationInView(parent: View?, child: View?): Rect {
        require(!(child == null || parent == null)) { "parent and child can not be null ." }

        var decorView: View? = null
        val context = child.context
        if (context is Activity) {
            decorView = context.window.decorView
        }
        val result = Rect()
        val tmpRect = Rect()
        var tmp = child
        if (child == parent) {
            child.getHitRect(result)
            return result
        }
        while (tmp != decorView && tmp != parent) {
            tmp!!.getHitRect(tmpRect)
            if (!tmp.javaClass.equals(FRAGMENT_CON)) {
                result.left += tmpRect.left
                result.top += tmpRect.top
            }
            tmp = tmp.parent as View
            if (tmp.parent is ScrollView) {
                val scrollView = tmp.parent as ScrollView
                val scrollY = scrollView.scrollY
                result.top -= scrollY
            }
            if (tmp.parent is HorizontalScrollView) {
                val horizontalScrollView = tmp.parent as HorizontalScrollView
                val scrollX = horizontalScrollView.scrollX
                result.left -= scrollX
            }
            if (tmp.parent != null && tmp.parent is ViewPager) {
                tmp = tmp.parent as View
            }
        }
        result.right = result.left + child.measuredWidth
        result.bottom = result.top + child.measuredHeight
        return result
    }
}