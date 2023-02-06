package sing.top.newbie.guide.model

import android.view.View
import sing.top.newbie.guide.listener.OnHighlightDrewListener

/**
 * Created by hubert on 2018/7/9.
 */
class HighlightOptions {

    var onClickListener: View.OnClickListener? = null
    var relativeGuide: RelativeGuide? = null
    var onHighlightDrewListener: OnHighlightDrewListener? = null
    var fetchLocationEveryTime = false

    class Builder {
        private val options: HighlightOptions = HighlightOptions()

        /**
         * 高亮点击事件
         */
        fun setOnClickListener(listener: View.OnClickListener): Builder {
            options.onClickListener = listener
            return this
        }

        /**
         * @param relativeGuide 高亮相对位置引导布局
         */
        fun setRelativeGuide(relativeGuide: RelativeGuide): Builder {
            options.relativeGuide = relativeGuide
            return this
        }

        /**
         * @param listener 高亮绘制后回调该监听，用于绘制额外内容
         */
        fun setOnHighlightDrewListener(listener: OnHighlightDrewListener): Builder {
            options.onHighlightDrewListener = listener
            return this
        }

        /**
         * 是否每次显示引导层都重新获取高亮位置
         */
        fun isFetchLocationEveryTime(b: Boolean): Builder {
            options.fetchLocationEveryTime = b
            return this
        }

        fun build(): HighlightOptions {
            return options
        }
    }
}