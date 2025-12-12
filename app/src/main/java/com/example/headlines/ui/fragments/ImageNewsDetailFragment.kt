package com.example.headlines.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.headlines.data.model.News
import com.example.headlines.databinding.FragmentImageNewsDetailBinding

class ImageNewsDetailFragment : BaseNewsDetailFragment() {

    private var _binding: FragmentImageNewsDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNewsTitle.text = news.title
        binding.tvNewsSource.text = news.source
        binding.tvNewsTime.text = news.publishTime
        binding.tvCommentCount.text = "${news.commentCount}评论"
        binding.tvNewsContent.text = news.content

        // 加载图片
        news.imageUrl?.let { url ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.ivNewsImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(news: News): ImageNewsDetailFragment {
            return BaseNewsDetailFragment.newInstance(news, ImageNewsDetailFragment::class.java) as ImageNewsDetailFragment
        }
    }
}