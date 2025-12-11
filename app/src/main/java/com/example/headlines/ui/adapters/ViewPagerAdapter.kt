package com.example.headlines.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.headlines.ui.fragments.NewsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    // 标签页标题
    private val tabTitles = listOf("关注", "推荐", "热榜", "新时代", "小说", "视频")

    override fun getItemCount(): Int = tabTitles.size

    override fun createFragment(position: Int): Fragment {
        // 根据位置创建对应的Fragment
        return when (position) {
            0 -> NewsFragment.newInstance("关注")
            1 -> NewsFragment.newInstance("推荐")
            2 -> NewsFragment.newInstance("热榜")
            3 -> NewsFragment.newInstance("新时代")
            4 -> NewsFragment.newInstance("小说")
            5 -> NewsFragment.newInstance("视频")
            else -> NewsFragment.newInstance("推荐")
        }
    }

    override fun getItemId(position: Int): Long {
        // 确保每个Fragment有唯一ID
        return position.toLong()
    }

    fun getTabTitle(position: Int): String {
        return tabTitles[position]
    }
}