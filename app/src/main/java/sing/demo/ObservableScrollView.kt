package sing.demo

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

class ObservableScrollView : ScrollView {
    private var scrollChangeListener: OnScrollChangeListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setScrollViewListener(onScrollChangeListener: OnScrollChangeListener) {
        scrollChangeListener = onScrollChangeListener
    }

    override fun onScrollChanged(x: Int, y: Int, oldx: Int, oldy: Int) {
        super.onScrollChanged(x, y, oldx, oldy)
        if (scrollChangeListener != null) {
            scrollChangeListener!!.onScrollChanged(this, x, y, oldx, oldy)
        }
    }

    interface OnScrollChangeListener {
        fun onScrollChanged(scrollView: ObservableScrollView, x: Int, y: Int, oldx: Int, oldy: Int)
    }
}