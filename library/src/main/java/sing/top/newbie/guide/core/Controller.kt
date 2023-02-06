package sing.top.newbie.guide.core

import android.R
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import sing.top.newbie.guide.NewbieGuide
import sing.top.newbie.guide.lifecycle.FragmentLifecycleAdapter
import sing.top.newbie.guide.lifecycle.ListenerFragment
import sing.top.newbie.guide.listener.OnGuideChangedListener
import sing.top.newbie.guide.listener.OnPageChangedListener
import sing.top.newbie.guide.model.GuidePage
import java.lang.reflect.Field
import java.security.InvalidParameterException


/**
 * guide的控制器，可以通过该类控制引导层的显示与回退，或者重置label
 */
class Controller(builder: Builder) {

    private val LISTENER_FRAGMENT = "listener_fragment"
    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var onGuideChangedListener: OnGuideChangedListener? = null
    private var onPageChangedListener: OnPageChangedListener? = null
    private var label: String? = null
    private var alwaysShow = false
    private var showCounts  = 0 //显示次数
    private var guidePages: MutableList<GuidePage> = arrayListOf()
    private var current = 0 //当前页
    private var currentLayout: GuideLayout? = null
    private var mParentView: FrameLayout? = null
    private var sp: SharedPreferences? = null
    private var indexOfChild = -1 //使用anchor时记录的在父布局的位置
    private var isShowing = false
    private var showShadow = false // 是否显示阴影

    init {
        activity = builder.activity
        fragment = builder.fragment
        onGuideChangedListener = builder.getGuideChangedListener()
        onPageChangedListener = builder.getPageChangedListener()
        label = builder.getLabel()
        alwaysShow = builder.getAlwaysShow()
        showShadow = builder.isShadow()
        guidePages = builder.getGuidePage()
        showCounts = builder.getShowCounts()
        var anchor: View? = builder.getAnchor()
        if (anchor == null) {
            anchor = activity?.findViewById(R.id.content)
        }
        if (anchor is FrameLayout) {
            mParentView = anchor
        } else {
            val frameLayout = FrameLayout(activity!!)
            val parent = anchor?.parent as ViewGroup
            indexOfChild = parent.indexOfChild(anchor)
            parent.removeView(anchor)
            if (indexOfChild >= 0) {
                parent.addView(frameLayout, indexOfChild, anchor.layoutParams)
            } else {
                parent.addView(frameLayout, anchor.layoutParams)
            }
            val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            frameLayout.addView(anchor, lp)
            mParentView = frameLayout
        }
        sp = activity?.getSharedPreferences(NewbieGuide.TAG, Activity.MODE_PRIVATE)
    }

    /**
     * 显示指引layout
     */
    fun show() {
        val showed = sp!!.getInt(label, 0)
        if (!alwaysShow) {
            if (showed >= showCounts) {
                return
            }
        }
        if (isShowing) {
            return
        }
        isShowing = true
        mParentView!!.post {
            if (guidePages.size == 0) {
                throw  IllegalStateException("there is no guide to show!! Please add at least one Page.");
            }
            current = 0
            showGuidePage()
            onGuideChangedListener?.onShowed(this@Controller)
            addListenerFragment()
            sp!!.edit().putInt(label, showed + 1).apply()
        }
    }

    /**
     * 显示相应position的引导页
     *
     * @param position from 0 to (pageSize - 1)
     */
    fun showPage(position: Int) {
        if (position < 0 || position > guidePages.size - 1) {
            throw InvalidParameterException("The Guide page position is out of range. current:$position, range: [ 0, ${guidePages.size} )")
        }
        if (current == position) {
            return
        }
        current = position
        //fix #59 GuideLayout.setOnGuideLayoutDismissListener() on a null object reference
        if (currentLayout != null) {
            currentLayout!!.setOnGuideLayoutDismissListener(object :GuideLayout.OnGuideLayoutDismissListener{
                override fun onGuideLayoutDismiss(guideLayout: GuideLayout) {
                    showGuidePage()
                }
            })
            currentLayout!!.remove()
        } else {
            showGuidePage()
        }
    }


    /**
     * 显示当前引导页的前一页
     */
    fun showPreviewPage() {
        showPage(current - 1)
    }

    /**
     * 显示current引导页
     */
    private fun showGuidePage() {
        val page = guidePages[current]
        val guideLayout = GuideLayout(activity!!, page, this,showShadow)
        guideLayout.setOnGuideLayoutDismissListener(object :GuideLayout.OnGuideLayoutDismissListener{
            override fun onGuideLayoutDismiss(guideLayout: GuideLayout) {
                showNextOrRemove()
            }
        })
        mParentView!!.addView(
            guideLayout, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        currentLayout = guideLayout
        onPageChangedListener?.onPageChanged(current)
        isShowing = true
    }

    private fun showNextOrRemove() {
        if (current < guidePages.size - 1) {
            current++
            showGuidePage()
        } else {
            onGuideChangedListener?.onRemoved(this@Controller)
            removeListenerFragment()
            isShowing = false
        }
    }


    /**
     * 清除当前Controller的label记录
     */
    fun resetLabel() {
        resetLabel(label)
    }

    /**
     * 清除"显示过"的标记
     *
     * @param label 引导标示
     */
    fun resetLabel(label: String?) {
        sp!!.edit().putInt(label, 0).apply()
    }


    /**
     * 中断引导层的显示，后续未显示的page将不再显示
     */
    fun remove() {
        if (currentLayout != null && currentLayout!!.parent != null) {
            val parent = currentLayout!!.parent as ViewGroup
            parent.removeView(currentLayout)
            //移除anchor添加的frameLayout
            if (parent !is FrameLayout) {
                val original = parent.parent as ViewGroup
                val anchor = parent.getChildAt(0)
                parent.removeAllViews()
                if (anchor != null) {
                    if (indexOfChild > 0) {
                        original.addView(anchor, indexOfChild, parent.layoutParams)
                    } else {
                        original.addView(anchor, parent.layoutParams)
                    }
                }
            }
            onGuideChangedListener?.onRemoved(this)
            currentLayout = null
        }
        isShowing = false
    }


    private fun addListenerFragment() {
        //fragment监听销毁界面关闭引导层
        if (fragment != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            compatibleFragment(fragment!!)
            val fm: FragmentManager = fragment!!.childFragmentManager
            if (fm != null) {
                var listenerFragment = fm.findFragmentByTag(LISTENER_FRAGMENT) as ListenerFragment
                if (listenerFragment == null) {
                    listenerFragment = ListenerFragment()
                    fm.beginTransaction().add(listenerFragment, LISTENER_FRAGMENT).commitAllowingStateLoss()
                }
                listenerFragment.setFragmentLifecycle(object : FragmentLifecycleAdapter() {
                    override fun onDestroyView() {
                        remove()
                    }
                })
            }
        }
    }


    private fun removeListenerFragment() {
        //隐藏引导层时移除监听fragment
        if (fragment != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            val fm = fragment!!.childFragmentManager
            if (fm != null) {
                val listenerFragment = fm.findFragmentByTag(LISTENER_FRAGMENT) as ListenerFragment?
                if (listenerFragment != null) {
                    fm.beginTransaction().remove(listenerFragment).commitAllowingStateLoss()
                }
            }
        }
    }

    /**
     * For bug of Fragment in Android
     * https://issuetracker.google.com/issues/36963722
     *
     * @param fragment
     */
    private fun compatibleFragment(fragment: Fragment) {
        try {
            val childFragmentManager: Field = Fragment::class.java.getDeclaredField("mChildFragmentManager")
            childFragmentManager.isAccessible = true
            childFragmentManager.set(fragment, null)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }
    }
}