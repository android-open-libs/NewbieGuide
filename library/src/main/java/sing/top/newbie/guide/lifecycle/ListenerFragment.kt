package sing.top.newbie.guide.lifecycle

import androidx.fragment.app.Fragment
import sing.top.newbie.guide.lifecycle.FragmentLifecycle

class ListenerFragment : Fragment() {

    lateinit var mFragmentLifecycle: FragmentLifecycle

    fun setFragmentLifecycle(lifecycle: FragmentLifecycle) {
        mFragmentLifecycle = lifecycle
    }

    override fun onStart() {
        super.onStart()
        mFragmentLifecycle.onStart()
    }

    override fun onStop() {
        super.onStop()
        mFragmentLifecycle.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mFragmentLifecycle.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mFragmentLifecycle.onDestroy()
    }
}