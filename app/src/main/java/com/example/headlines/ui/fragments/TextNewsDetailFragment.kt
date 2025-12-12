package com.example.headlines.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.headlines.data.model.News
import com.example.headlines.databinding.FragmentTextNewsDetailBinding

class TextNewsDetailFragment : BaseNewsDetailFragment() {

    private var _binding: FragmentTextNewsDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNewsTitle.text = news.title
        binding.tvNewsSource.text = news.source
        binding.tvNewsTime.text = news.publishTime
        binding.tvCommentCount.text = "${news.commentCount}评论"
        binding.tvNewsContent.text = news.content

        // 文字新闻不显示图片区域
        binding.ivNewsImage.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(news: News): TextNewsDetailFragment {
            return BaseNewsDetailFragment.newInstance(news, TextNewsDetailFragment::class.java) as TextNewsDetailFragment
        }
    }
}