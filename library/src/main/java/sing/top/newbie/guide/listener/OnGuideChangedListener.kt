package sing.top.newbie.guide.listener

import sing.top.newbie.guide.core.Controller

/**
 * 引导层显示和消失的监听
 */
interface OnGuideChangedListener {
    /**
     * 当引导层显示时回调
     *
     * @param controller
     */
    fun onShowed(controller: Controller)

    /**
     * 当引导层消失时回调
     *
     * @param controller
     */
    fun onRemoved(controller: Controller)
}