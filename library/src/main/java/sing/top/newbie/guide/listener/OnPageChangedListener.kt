package sing.top.newbie.guide.listener

/**
 * 引导页改变的监听
 */
interface OnPageChangedListener {
    /**
     * @param page 当前引导页的position，第一页为0
     */
    fun onPageChanged(page: Int)
}