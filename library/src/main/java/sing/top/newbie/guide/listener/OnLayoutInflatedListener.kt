package sing.top.newbie.guide.listener

import android.view.View
import sing.top.newbie.guide.core.Controller

/**
 * 用于引导层布局初始化
 */
interface OnLayoutInflatedListener {
    /**
     * @param view       [sing.top.newbie.guide.model.GuidePage.setLayoutRes]方法传入的layoutRes填充后的view
     * @param controller [Controller]
     */
    fun onLayoutInflated(view: View, controller: Controller)
}