package com.example.headlines.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headlines.databinding.FragmentNewsBinding
import com.example.headlines.ui.activities.NewsDetailActivity
import com.example.headlines.ui.adapters.NewsAdapter
import com.example.headlines.ui.viewmodel.NewsViewModel
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NewsAdapter
    private var category: String = "推荐"

    private val viewModel: NewsViewModel by viewModels()

    // 添加刷新回调接口
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
        setupObservers()
        loadData()
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // 设置新闻点击监听 - 这里要处理跳转
        adapter.setOnNewsClickListener { news ->
            // 跳转到新闻详情页
            openNewsDetail(news)
        }
    }

    private fun openNewsDetail(news: com.example.headlines.data.model.News) {
        val intent = Intent(requireContext(), NewsDetailActivity::class.java).apply {
            putExtra("news_title", news.title)
            putExtra("news_source", news.source)
            putExtra("news_time", news.publishTime)
            putExtra("news_comment_count", news.commentCount)
            putExtra("news_image_url", news.imageUrl)
            putExtra("news_type", news.type.name)
            putExtra("news_id", news.id)
            // 可以添加更多信息
        }
        startActivity(intent)
    }

    private fun setupObservers() {
        viewModel.newsList.observe(viewLifecycleOwner) { newsList ->
            adapter.submitList(newsList)

            // 更新UI状态
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

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                binding.emptyView.visibility = View.VISIBLE
                binding.emptyView.text = "加载失败: $it"
                binding.progressBar.visibility = View.GONE

                // 即使出错也通知刷新完成
                refreshListener?.onRefreshComplete()
            }
        }
    }

    private fun loadData() {
        viewModel.fetchNews(category)
    }

    fun refreshData() {
        viewModel.refreshNews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}