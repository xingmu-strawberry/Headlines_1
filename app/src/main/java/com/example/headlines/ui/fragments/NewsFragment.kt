package com.example.headlines.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headlines.databinding.FragmentNewsBinding
import com.example.headlines.ui.activities.NewsDetailActivity
import com.example.headlines.ui.adapters.NewsAdapter
import com.example.headlines.ui.viewmodel.NewsViewModel
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.headlines.data.model.News
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NewsAdapter
    private var category: String = "推荐"

    // 使用activityViewModels确保同一个Activity中的Fragment共享ViewModel
    private val viewModel: NewsViewModel by activityViewModels()

    interface OnRefreshListener {
        fun onRefreshComplete()
    }

    private var refreshListener: OnRefreshListener? = null

    fun setOnRefreshListener(listener: OnRefreshListener?) {
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
        setupSwipeRefresh()
        setupObservers()
        loadData()
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter()

        // 设置新闻点击监听 - 这里要处理跳转
        adapter.setOnNewsClickListener { news ->
            // 跳转到新闻详情页
            openNewsDetail(news)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    // 在NewsFragment.kt中修改openNewsDetail方法
    private fun openNewsDetail(news: News) {

        // 关键修正：使用 Activity 提供的封装方法进行跳转
        // 这样可以确保 Key (EXTRA_NEWS_ID) 总是匹配的
        NewsDetailActivity.startActivity(requireContext(), news.id, news.title)

    }

    /*
    private fun openNewsDetail(news: News) {
        val intent = Intent(requireContext(), NewsDetailActivity::class.java).apply {
            putExtra("news_id", news.id)
            putExtra("news_title", news.title)
            putExtra("news_content", news.content)
            putExtra("news_source", news.source)
            putExtra("news_time", news.publishTime)
            putExtra("news_comment_count", news.commentCount)
            putExtra("news_image_url", news.imageUrl ?: "")
            putExtra("news_type", news.type.name)
            putExtra("news_video_url", news.videoUrl ?: "")
            putExtra("news_video_duration", news.videoDuration ?: "")
        }
        startActivity(intent)
    }

     */


    private fun setupObservers() {
        // 观察新闻列表数据
        viewModel.newsList.observe(viewLifecycleOwner, Observer { newsList ->
            if (newsList != null) {
                adapter.submitList(newsList)

                // 更新UI状态
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressBar.visibility = View.GONE

                if (newsList.isEmpty()) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.emptyView.text = "暂无新闻数据"
                } else {
                    binding.emptyView.visibility = View.GONE
                }

                // 通知刷新完成
                refreshListener?.onRefreshComplete()
            }
        })

        // 观察加载状态
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading && viewModel.newsList.value == null) {
                // 只在初次加载时显示进度条
                binding.progressBar.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            } else if (!isLoading) {
                binding.progressBar.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
            }
        })

        // 观察错误信息
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressBar.visibility = View.GONE

                if (viewModel.newsList.value.isNullOrEmpty()) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.emptyView.text = "加载失败: $it"
                }

                // 即使出错也通知刷新完成
                refreshListener?.onRefreshComplete()
            }
        })
    }

    private fun loadData() {
        viewModel.fetchNews(category)
    }

    fun refreshData() {
        viewModel.refreshNews()
    }

    override fun onResume() {
        super.onResume()
        // Fragment重新显示时刷新数据
        if (viewModel.newsList.value.isNullOrEmpty()) {
            loadData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}