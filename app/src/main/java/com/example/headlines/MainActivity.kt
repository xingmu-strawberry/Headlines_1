
package com.example.headlines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.headlines.databinding.ActivityMainBinding
import com.example.headlines.ui.adapters.ViewPagerAdapter
import com.example.headlines.ui.fragments.NewsFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPagerAndTabs()
        setupBottomNavigation()
        setupRefreshLayout()  // 调用外部的函数
        setupClickListeners()
    }

    private fun setupViewPagerAndTabs() {
        // 设置ViewPager2
        viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        // 连接TabLayout和ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getTabTitle(position)
        }.attach()

        // 设置默认选中第二个标签（推荐）
        binding.tabLayout.getTabAt(1)?.select()
    }

    private fun setupBottomNavigation() {
        // 设置底部导航点击事件
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // 首页：滚动到顶部并显示推荐标签
                    binding.viewPager.currentItem = 1 // 推荐标签
                    true
                }
                R.id.nav_video -> {
                    // 视频：切换到视频标签页
                    binding.viewPager.currentItem = 5 // 视频标签
                    true
                }
                R.id.nav_search -> {
                    // 搜索：滚动到搜索栏位置
                    binding.appBarLayout.setExpanded(true, true)
                    true
                }
                R.id.nav_task -> {
                    // 任务：暂时显示推荐页
                    binding.viewPager.currentItem = 1
                    true
                }
                R.id.nav_profile -> {
                    // 我的：暂时显示推荐页
                    binding.viewPager.currentItem = 1
                    true
                }
                else -> false
            }
        }

        // 设置默认选中首页
        binding.bottomNavigationView.selectedItemId = R.id.nav_home
    }

    // 独立的 setupRefreshLayout 函数（不能放在 setupClickListeners 内部）
    private fun setupRefreshLayout() {
        // 设置下拉刷新
        binding.swipeRefreshLayout.setOnRefreshListener {
            // 获取当前Fragment并刷新数据
            val currentFragment = supportFragmentManager.findFragmentByTag("f${binding.viewPager.currentItem}")
            if (currentFragment is NewsFragment) {
                currentFragment.refreshData()
                currentFragment.setOnRefreshListener(object : NewsFragment.OnRefreshListener {
                    override fun onRefreshComplete() {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                })
            } else {
                // 如果没有找到Fragment，直接停止刷新
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        // 设置刷新指示器颜色
        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.color_primary),
            ContextCompat.getColor(this, android.R.color.holo_blue_bright),
            ContextCompat.getColor(this, android.R.color.holo_green_light),
            ContextCompat.getColor(this, android.R.color.holo_orange_light)
        )
    }

    private fun setupClickListeners() {
        // AI回答按钮点击
        binding.btnAiAnswer.setOnClickListener {
            // 暂时显示Toast
            android.widget.Toast.makeText(this, "AI回答功能开发中", android.widget.Toast.LENGTH_SHORT).show()
        }

        // 加号按钮点击
        binding.ivAdd.setOnClickListener {
            // 暂时显示Toast
            android.widget.Toast.makeText(this, "发布功能开发中", android.widget.Toast.LENGTH_SHORT).show()
        }

        // 搜索按钮点击
        val searchButton = binding.root.findViewById<android.widget.Button>(R.id.btnSearch)
        searchButton?.setOnClickListener {
            val searchText = binding.root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editTextSearch)?.text.toString()
            if (searchText.isNotEmpty()) {
                android.widget.Toast.makeText(this, "搜索: $searchText", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                android.widget.Toast.makeText(this, "请输入搜索内容", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        // 注意：这里不再有 setupRefreshLayout 函数定义
    }
}



