package com.example.headlines

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.headlines.data.model.News
import com.example.headlines.data.remote.RetrofitClient
import com.example.headlines.databinding.ActivityMainBinding
import com.example.headlines.ui.activities.NewsDetailActivity
import com.example.headlines.ui.activities.ProfileActivity
import com.example.headlines.ui.activities.SearchActivity
import com.example.headlines.ui.activities.TaskActivity
import com.example.headlines.ui.adapters.ViewPagerAdapter
import com.example.headlines.ui.fragments.NewsFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  初始化Retrofit（必须先于setContentView）
        RetrofitClient.init()


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPagerAndTabs()
        setupBottomNavigation()
        setupRefreshLayout()
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

                    // 文件2的首页功能：滚动到顶部
                    binding.appBarLayout.setExpanded(true, true)

                    true
                }
                R.id.nav_video -> {
                    // 视频：切换到视频标签页
                    binding.viewPager.currentItem = 5 // 视频标签
                    true
                }
                R.id.nav_search -> {
                    // 搜索：滚动到搜索栏位置
                    // binding.appBarLayout.setExpanded(true, true)

                    // 文件5的新增功能：跳转到搜索页面
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)

                    // 保持当前选中状态不变
                    binding.bottomNavigationView.selectedItemId = R.id.nav_home
                    true
                }
                R.id.nav_task -> {
                    // 跳转到任务页面
                    val intent = Intent(this, TaskActivity::class.java)
                    startActivity(intent)

                    // 保持当前选中状态不变
                    binding.bottomNavigationView.selectedItemId = R.id.nav_home
                    true
                }
                R.id.nav_profile -> {
                    // 我的：暂时显示推荐页
                    // binding.viewPager.currentItem = 1

                    // 文件5的新增功能：跳转到个人资料页面
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)

                    // 保持当前选中状态不变
                    binding.bottomNavigationView.selectedItemId = R.id.nav_home
                    true
                }
                else -> false
            }
        }

        // 设置默认选中首页
        binding.bottomNavigationView.selectedItemId = R.id.nav_home
    }

    // 独立的 setupRefreshLayout 函数
    private fun setupRefreshLayout() {
        // 设置下拉刷新
        binding.swipeRefreshLayout.setOnRefreshListener {
            // 获取当前Fragment并刷新数据
            val currentFragment = supportFragmentManager.findFragmentByTag("f${binding.viewPager.currentItem}")
            if (currentFragment is NewsFragment) {
                // 设置刷新回调
                currentFragment.setOnRefreshListener(object : NewsFragment.OnRefreshListener {
                    override fun onRefreshComplete() {
                        // Fragment通知刷新完成后，停止刷新指示器
                        binding.swipeRefreshLayout.isRefreshing = false
                        // 清除回调，避免内存泄漏
                        currentFragment.setOnRefreshListener(null)
                    }
                })

                // 触发刷新
                currentFragment.refreshData()
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

        // 搜索框点击
        val searchEditText =
            binding.root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editTextSearch)
        // 搜索按钮点击
        val searchButton = binding.root.findViewById<android.widget.Button>(R.id.btnSearch)

        // 点击搜索框时跳转
        searchEditText?.setOnClickListener {
            // 直接跳转到搜索页面
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        // 点击搜索按钮时也跳转
        searchButton?.setOnClickListener {
            // 同样跳转到搜索页面
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    // 在 MainActivity.kt 中添加
    private fun openNewsDetail(news: News) {
        val intent = Intent(this, NewsDetailActivity::class.java).apply {
            putExtra("news_title", news.title)
            putExtra("news_source", news.source)
            putExtra("news_time", news.publishTime)
            putExtra("news_comment_count", news.commentCount)
            putExtra("news_image_url", news.imageUrl)
        }
        startActivity(intent)
    }

    private fun openSearchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun openProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}