package sing.top.newbie.guide.model

import android.graphics.RectF
import android.view.View

interface HighLight {
    val shape: Shape?

    /**
     * @param view anchor view
     * @return highlight's rectF
     */
    fun getRectF(view: View): RectF?

    /**
     * 当shape为CIRCLE时调用此方法获取半径
     */
    val radius: Float

    /**
     * 获取圆角，仅当shape = Shape.ROUND_RECTANGLE才调用次方法
     */
    val round: Int

    /**
     * 额外参数
     */
    val options: HighlightOptions?

    enum class Shape {
        CIRCLE,  //圆形
        RECTANGLE,  //矩形
        OVAL,  //椭圆
        ROUND_RECTANGLE //圆角矩形
    }
}