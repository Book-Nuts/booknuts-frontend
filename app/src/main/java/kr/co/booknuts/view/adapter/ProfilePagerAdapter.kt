package kr.co.booknuts.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.booknuts.view.fragment.ProfileArchiveFragment
import kr.co.booknuts.view.fragment.ProfilePostFragment
import kr.co.booknuts.view.fragment.ProfileSeriesFragment

private const val NUM_TABS = 3

class ProfilePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ProfilePostFragment()
            1 -> return ProfileSeriesFragment()
            2 -> return ProfileArchiveFragment()
        }
        return ProfilePostFragment()
    }
}