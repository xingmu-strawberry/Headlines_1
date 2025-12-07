package com.example.headlines.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.headlines.databinding.FragmentNewsBinding
import com.example.headlines.data.model.News
import com.example.headlines.data.model.NewsType
import com.example.headlines.ui.adapters.NewsAdapter

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NewsAdapter
    private var category: String = "推荐"
    private var isLoading = false
    private var currentPage = 1

    // 添加刷新回调接口
    interface OnRefreshListener {
        fun onRefreshComplete()
    }

    private var refreshListener: OnRefreshListener? = null

    fun setOnRefreshListener(listener: OnRefreshListener) {
        this.refreshListener = listener
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): NewsFragment {
            val fragment = NewsFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_CATEGORY, "推荐")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadInitialData()
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // 设置点击监听
        adapter.setOnNewsClickListener { news ->
            // 这里处理新闻点击
            println("点击新闻: ${news.title}")
        }

        // 添加滚动监听实现加载更多
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // 当滚动到底部且没有正在加载时，加载更多
                if (!isLoading && lastVisibleItemPosition >= totalItemCount - 3) {
                    loadMoreData()
                }
            }
        })
    }

    private fun loadInitialData() {
        // 初始加载数据
        loadData(false)
    }

    // 修改loadData函数，添加刷新完成回调
    private fun loadData(forceRefresh: Boolean = false) {
        // 模拟网络延迟
        if (forceRefresh) {
            binding.progressBar.visibility = View.VISIBLE
        }

        // 模拟数据加载
        binding.root.postDelayed({
            val newsList = mutableListOf<News>()

            // 根据分类加载不同数据
            when (category) {
                "推荐" -> newsList.addAll(getRecommendNews())
                "热榜" -> newsList.addAll(getHotNews())
                "关注" -> newsList.addAll(getFollowNews())
                "新时代" -> newsList.addAll(getNewEraNews())
                "小说" -> newsList.addAll(getNovelNews())
                "视频" -> newsList.addAll(getVideoNews())
                else -> newsList.addAll(getRecommendNews())
            }

            adapter.submitList(newsList)

            // 隐藏加载动画
            binding.progressBar.visibility = View.GONE
            if (newsList.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.visibility = View.GONE
            }

            // 通知刷新完成
            if (forceRefresh) {
                refreshListener?.onRefreshComplete()
            }
        }, if (forceRefresh) 1000 else 300)
    }

    // 添加刷新函数
    fun refreshData() {
        loadData(true)
    }

    private fun loadMoreData() {
        if (isLoading) return

        isLoading = true
        currentPage++

        // 模拟网络请求延迟
        binding.root.postDelayed({
            val moreNews = getMoreNewsData(currentPage)
            val currentList = adapter.currentList.toMutableList()
            currentList.addAll(moreNews)

            adapter.submitList(currentList)
            isLoading = false

            // 如果没有更多数据，可以显示提示
            if (moreNews.isEmpty()) {
                // 可以显示"没有更多数据"的提示
            }
        }, 1000)
    }

    // ========== 数据生成函数 ==========

    private fun getRecommendNews(): List<News> {
        return listOf(
            News(
                id = 1,
                title = "习近平主席关于中非合作重要论述",
                type = NewsType.TEXT,
                source = "人民日报",
                commentCount = 128,
                publishTime = "2小时前",
                isTop = true
            ),
            News(
                id = 2,
                title = "这档社交观察类综艺火了，桃花坞是如何做到的",
                type = NewsType.IMAGE,
                source = "娱乐周刊",
                commentCount = 356,
                publishTime = "4小时前",
                imageUrl = "https://picsum.photos/200/150?random=1"
            ),
            News(
                id = 3,
                title = "我国已有近320公里高铁常态化按350公里高标运营",
                type = NewsType.VIDEO,
                source = "新华社",
                commentCount = 1024,
                publishTime = "6小时前",
                imageUrl = "https://picsum.photos/200/150?random=2",
                videoDuration = "02:54"
            ),
            News(
                id = 4,
                title = "全网笑出鹅叫声！腾讯被骗后悬赏1000瓶老干妈",
                type = NewsType.LONG_IMAGE,
                source = "科技新闻",
                commentCount = 892,
                publishTime = "8小时前",
                imageUrl = "https://picsum.photos/300/180?random=3"
            ),
            News(
                id = 5,
                title = "乌拉圭队或重返世界杯舞台，球迷期待已久",
                type = NewsType.IMAGE,
                source = "体育新闻",
                commentCount = 234,
                publishTime = "1小时前",
                imageUrl = "https://picsum.photos/200/150?random=4"
            )
        )
    }

    private fun getHotNews(): List<News> {
        return listOf(
            News(
                id = 6,
                title = "热榜新闻：今日热点话题",
                type = NewsType.TEXT,
                source = "热点新闻",
                commentCount = 999,
                publishTime = "刚刚",
                isTop = true
            ),
            News(
                id = 7,
                title = "热门视频：爆款内容推荐",
                type = NewsType.VIDEO,
                source = "视频热点",
                commentCount = 5678,
                publishTime = "1小时前",
                imageUrl = "https://picsum.photos/200/150?random=5",
                videoDuration = "01:30"
            ),
            News(
                id = 8,
                title = "热搜第一：大家都在讨论什么",
                type = NewsType.IMAGE,
                source = "热搜榜",
                commentCount = 4321,
                publishTime = "2小时前",
                imageUrl = "https://picsum.photos/200/150?random=6"
            )
        )
    }

    private fun getFollowNews(): List<News> {
        return listOf(
            News(
                id = 9,
                title = "您关注的作者发布了新文章",
                type = NewsType.TEXT,
                source = "关注列表",
                commentCount = 56,
                publishTime = "3小时前"
            ),
            News(
                id = 10,
                title = "关注话题：科技前沿动态",
                type = NewsType.IMAGE,
                source = "科技频道",
                commentCount = 123,
                publishTime = "5小时前",
                imageUrl = "https://picsum.photos/200/150?random=7"
            )
        )
    }

    private fun getNewEraNews(): List<News> {
        return listOf(
            News(
                id = 11,
                title = "新时代中国发展成就",
                type = NewsType.TEXT,
                source = "人民日报",
                commentCount = 789,
                publishTime = "1天前",
                isTop = true
            )
        )
    }

    private fun getNovelNews(): List<News> {
        return listOf(
            News(
                id = 12,
                title = "最新连载小说推荐",
                type = NewsType.IMAGE,
                source = "小说频道",
                commentCount = 456,
                publishTime = "2天前",
                imageUrl = "https://picsum.photos/200/150?random=8"
            )
        )
    }

    private fun getVideoNews(): List<News> {
        return listOf(
            News(
                id = 13,
                title = "精选视频：热门短视频推荐",
                type = NewsType.VIDEO,
                source = "视频频道",
                commentCount = 2345,
                publishTime = "3小时前",
                imageUrl = "https://picsum.photos/200/150?random=9",
                videoDuration = "03:45"
            ),
            News(
                id = 14,
                title = "影视剧最新更新",
                type = NewsType.IMAGE,
                source = "影视专区",
                commentCount = 987,
                publishTime = "4小时前",
                imageUrl = "https://picsum.photos/200/150?random=10"
            )
        )
    }

    private fun getMoreNewsData(page: Int): List<News> {
        // 模拟更多数据
        return listOf(
            News(
                id = 100 + page,
                title = "加载更多新闻标题 $page - $category",
                type = NewsType.TEXT,
                source = "更多新闻源",
                commentCount = page * 10,
                publishTime = "${page}分钟前"
            ),
            News(
                id = 200 + page,
                title = "加载更多图片新闻 $page - $category",
                type = NewsType.IMAGE,
                source = "更多图片新闻",
                commentCount = page * 20,
                publishTime = "${page * 2}分钟前",
                imageUrl = "https://picsum.photos/200/150?random=${page + 20}"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}