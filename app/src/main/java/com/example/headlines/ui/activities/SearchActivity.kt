package com.example.headlines.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.headlines.databinding.ActivitySearchBinding
import com.example.headlines.ui.adapters.SearchPagerAdapter
import com.example.headlines.R
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchPagerAdapter: SearchPagerAdapter

    // 模拟搜索历史
    private val searchHistory = mutableListOf(
        "习近平主席重要论述",
        "乌拉圭队世界杯",
        "中非合作",
        "高铁350公里",
        "社交观察类综艺"
    )

    // 模拟热门搜索
    private val hotSearches = listOf(
        "习近平主席重要论述",
        "乌拉圭队或重返世界杯",
        "中非合作新篇章",
        "高铁350公里高标运营",
        "社交观察类综艺桃花坞"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewPager()
        setupSearchInput()
        setupHistoryAndHotSearch()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupViewPager() {
        searchPagerAdapter = SearchPagerAdapter(this)
        binding.viewPagerSearch.adapter = searchPagerAdapter

        // 连接TabLayout和ViewPager2
        TabLayoutMediator(binding.tabLayoutSearch, binding.viewPagerSearch) { tab, position ->
            tab.text = when (position) {
                0 -> "综合"
                1 -> "文章"
                2 -> "视频"
                3 -> "用户"
                4 -> "话题"
                else -> "综合"
            }
        }.attach()

        // 默认隐藏ViewPager，显示历史记录
        binding.viewPagerSearch.visibility = View.GONE
        binding.tabLayoutSearch.visibility = View.GONE
    }

    private fun setupSearchInput() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    // 显示搜索结果
                    binding.viewPagerSearch.visibility = View.VISIBLE
                    binding.tabLayoutSearch.visibility = View.VISIBLE
                    binding.layoutSearchHistory.visibility = View.GONE
                    binding.layoutHotSearch.visibility = View.GONE

                    // 在这里可以触发搜索
                    performSearch(query)
                } else {
                    // 显示历史记录和热门搜索
                    binding.viewPagerSearch.visibility = View.GONE
                    binding.tabLayoutSearch.visibility = View.GONE
                    binding.layoutSearchHistory.visibility = View.VISIBLE
                    binding.layoutHotSearch.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 设置搜索按钮
        binding.btnSearchAction.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                performSearch(query)
                addToSearchHistory(query)
            }
        }

        // 设置键盘搜索按钮
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text.toString().trim()
                if (query.isNotEmpty()) {
                    performSearch(query)
                    addToSearchHistory(query)
                }
                true
            } else {
                false
            }
        }
    }

    private fun setupHistoryAndHotSearch() {
        // 动态添加历史搜索标签
        searchHistory.forEach { keyword ->
            val chip = Chip(this).apply {
                text = keyword
                isClickable = true
                isCheckable = false
                setOnClickListener {
                    binding.etSearch.setText(keyword)
                    binding.etSearch.setSelection(keyword.length)
                    performSearch(keyword)
                }
            }
            binding.chipGroupHistory.addView(chip)
        }

        // 动态添加热门搜索项
        hotSearches.forEachIndexed { index, keyword ->
            val textView = TextView(this).apply {
                text = "${index + 1}. $keyword"
                textSize = resources.getDimension(R.dimen.text_size_medium) / resources.displayMetrics.scaledDensity
                setTextColor(ContextCompat.getColor(this@SearchActivity, R.color.text_primary))
                setPadding(0,
                    resources.getDimensionPixelSize(R.dimen.spacing_small),
                    0,
                    resources.getDimensionPixelSize(R.dimen.spacing_small)
                )
                background = ContextCompat.getDrawable(this@SearchActivity, android.R.drawable.list_selector_background)
                setOnClickListener {
                    binding.etSearch.setText(keyword)
                    binding.etSearch.setSelection(keyword.length)
                    performSearch(keyword)
                }
            }
            binding.layoutHotSearchItems.addView(textView)
        }
    }

    private fun setupClickListeners() {
        // 返回按钮
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // 清空历史按钮
        binding.btnClearHistory.setOnClickListener {
            clearSearchHistory()
        }
    }

    private fun performSearch(query: String) {
        // 这里应该执行实际的搜索逻辑
        // 暂时用Toast提示
        android.widget.Toast.makeText(this, "搜索: $query", android.widget.Toast.LENGTH_SHORT).show()

        // 隐藏键盘
        val imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    private fun addToSearchHistory(query: String) {
        if (!searchHistory.contains(query)) {
            searchHistory.add(0, query)
            if (searchHistory.size > 10) {
                searchHistory.removeAt(searchHistory.size - 1)
            }
            // 这里应该保存到SharedPreferences

            // 刷新历史记录显示
            refreshSearchHistory()
        }
    }

    private fun refreshSearchHistory() {
        binding.chipGroupHistory.removeAllViews()
        searchHistory.forEach { keyword ->
            val chip = Chip(this).apply {
                text = keyword
                isClickable = true
                isCheckable = false
                setOnClickListener {
                    binding.etSearch.setText(keyword)
                    binding.etSearch.setSelection(keyword.length)
                    performSearch(keyword)
                }
            }
            binding.chipGroupHistory.addView(chip)
        }
    }

    private fun clearSearchHistory() {
        searchHistory.clear()
        binding.chipGroupHistory.removeAllViews()
        // 这里应该清除SharedPreferences中的历史记录
        android.widget.Toast.makeText(this, "历史记录已清空", android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (binding.viewPagerSearch.visibility == View.VISIBLE) {
            // 如果正在显示搜索结果，返回到搜索历史页
            binding.viewPagerSearch.visibility = View.GONE
            binding.tabLayoutSearch.visibility = View.GONE
            binding.layoutSearchHistory.visibility = View.VISIBLE
            binding.layoutHotSearch.visibility = View.VISIBLE
            binding.etSearch.text?.clear()
        } else {
            super.onBackPressed()
        }
    }
}