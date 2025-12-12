package com.example.headlines.ui.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.headlines.ui.fragments.SearchResultFragment

class SearchPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 5 // 综合、文章、视频、用户、话题

    override fun createFragment(position: Int): Fragment {
        return SearchResultFragment.newInstance(
            when (position) {
                0 -> "all"      // 综合
                1 -> "article"  // 文章
                2 -> "video"    // 视频
                3 -> "user"     // 用户
                4 -> "topic"    // 话题
                else -> "all"
            }
        )
    }
}