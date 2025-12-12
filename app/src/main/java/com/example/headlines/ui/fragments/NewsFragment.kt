package com.example.headlines.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headlines.data.model.News
import com.example.headlines.data.model.NewsType
import com.example.headlines.databinding.FragmentNewsBinding
import com.example.headlines.ui.activities.NewsDetailActivity
import com.example.headlines.ui.adapters.NewsAdapter
import com.example.headlines.ui.viewmodel.NewsViewModel

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

        adapter.setOnNewsClickListener { news ->
            // 跳转到新闻详情页，传递新闻的各个字段
            val intent = Intent(requireContext(), NewsDetailActivity::class.java).apply {
                putExtra("news_id", news.id)
                putExtra("news_title", news.title)
                putExtra("news_content", news.content)
                putExtra("news_type", news.type.name)  // 注意：枚举转字符串
                putExtra("news_source", news.source)
                putExtra("news_comment_count", news.commentCount)
                putExtra("news_publish_time", news.publishTime)
                putExtra("news_image_url", news.imageUrl)
                putExtra("news_image_url_2", news.imageUrl2) // <-- 修正
                putExtra("news_image_url_3", news.imageUrl3) // <-- 修正
                putExtra("news_video_url", news.videoUrl)
                putExtra("news_video_duration", news.videoDuration)
                putExtra("news_is_top", news.isTop)
            }
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        viewModel.newsList.observe(viewLifecycleOwner) { newsList ->
            adapter.submitList(newsList?.toMutableList())

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