package com.example.headlines.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headlines.R
import com.example.headlines.databinding.FragmentNewsDetailBinding
import com.example.headlines.ui.adapter.NewsDetailAdapter
import com.example.headlines.ui.viewmodel.NewsDetailViewModel
import kotlinx.coroutines.launch

class NewsDetailFragment : Fragment() {

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsDetailViewModel by viewModels()
    private lateinit var adapter: NewsDetailAdapter

    private var newsId: Int = -1
    private var newsTitle: String = "新闻详情"

    companion object {
        private const val ARG_NEWS_ID = "arg_news_id"
        private const val ARG_NEWS_TITLE = "arg_news_title"

        fun newInstance(newsId: Int, newsTitle: String = "新闻详情"): NewsDetailFragment {
            return NewsDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_NEWS_ID, newsId)
                    putString(ARG_NEWS_TITLE, newsTitle)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            newsId = it.getInt(ARG_NEWS_ID, -1)
            newsTitle = it.getString(ARG_NEWS_TITLE, "新闻详情")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        loadData()
    }

    private fun initView() {
        // 初始化RecyclerView
        adapter = NewsDetailAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NewsDetailFragment.adapter
        }

        // 设置Toolbar
        binding.toolbar.title = newsTitle
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // 设置下拉刷新
        binding.swipeRefresh.setOnRefreshListener {
            loadData()
        }

        // 设置重试按钮
        binding.buttonRetry.setOnClickListener {
            loadData()
        }
    }

    private fun initViewModel() {
        // 观察新闻详情数据
        viewModel.newsDetail.observe(viewLifecycleOwner) { newsDetail ->
            newsDetail?.let {
                // 更新UI
                binding.swipeRefresh.isRefreshing = false
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.errorLayout.visibility = View.GONE

                // 更新适配器数据
                adapter.submitData(it)

                // 更新Toolbar标题为实际新闻标题
                binding.toolbar.title = it.title
            }
        }

        // 观察加载状态
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading && viewModel.newsDetail.value == null) {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
            }
        }

        // 观察错误信息
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                binding.swipeRefresh.isRefreshing = false
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.errorLayout.visibility = View.VISIBLE
                binding.textErrorMessage.text = it
            }
        }
    }

    private fun loadData() {
        if (newsId != -1) {
            viewModel.loadNewsDetail(newsId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clearState()
    }
}