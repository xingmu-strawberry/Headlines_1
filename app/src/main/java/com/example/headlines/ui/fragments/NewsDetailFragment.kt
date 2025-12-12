package com.example.headlines.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headlines.R
import com.example.headlines.databinding.FragmentNewsDetailBinding
import com.example.headlines.ui.adapters.DetailAdapter
import com.example.headlines.ui.viewmodel.NewsDetailViewModel

class NewsDetailFragment : Fragment() {

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsDetailViewModel
    private lateinit var detailAdapter: DetailAdapter

    companion object {
        private const val ARG_NEWS_ID = "news_id"
        private const val ARG_NEWS_TYPE = "news_type"

        fun newInstance(newsId: String, type: String): NewsDetailFragment {
            val fragment = NewsDetailFragment()
            val args = Bundle().apply {
                putString(ARG_NEWS_ID, newsId)
                putString(ARG_NEWS_TYPE, type)
            }
            fragment.arguments = args
            return fragment
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

        val newsId = arguments?.getString(ARG_NEWS_ID) ?: ""
        val newsType = arguments?.getString(ARG_NEWS_TYPE) ?: "TEXT"

        // 初始化ViewModel
        viewModel = ViewModelProvider(this).get(NewsDetailViewModel::class.java)

        // 初始化UI
        setupToolbar()
        setupRecyclerView()
        setupSwipeRefresh()

        // 加载数据
        viewModel.loadNewsDetail(newsId, newsType)

        // 观察数据变化
        observeViewModel()

        // 设置错误重试按钮
        binding.buttonRetry.setOnClickListener {
            viewModel.loadNewsDetail(newsId, newsType)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            // 设置菜单
            inflateMenu(R.menu.detail_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_share -> {
                        shareNews()
                        true
                    }
                    R.id.action_comment -> {
                        showCommentDialog()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupRecyclerView() {
        detailAdapter = DetailAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = detailAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.apply {
            setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.color_primary)
            )
            setOnRefreshListener {
                viewModel.refresh()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.newsDetail.observe(viewLifecycleOwner) { detail ->
            detail?.let {
                // 更新适配器数据
                detailAdapter.submitDetail(detail)

                // 更新UI状态
                showContentView()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading

            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.errorLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showErrorView(it)
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showContentView() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun showErrorView(errorMessage: String) {
        binding.recyclerView.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.textErrorMessage.text = errorMessage
    }

    private fun shareNews() {
        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, "分享一条新闻给你：${viewModel.newsDetail.value?.title}")
        }
        startActivity(android.content.Intent.createChooser(shareIntent, "分享到"))
    }

    private fun showCommentDialog() {
        Toast.makeText(requireContext(), "评论功能开发中...", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}