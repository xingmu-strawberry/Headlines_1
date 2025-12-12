package com.example.headlines.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.headlines.data.model.News
import com.example.headlines.databinding.FragmentLongImageNewsDetailBinding

class LongImageNewsDetailFragment : BaseNewsDetailFragment() {

    private var _binding: FragmentLongImageNewsDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLongImageNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNewsTitle.text = news.title
        binding.tvNewsSource.text = news.source
        binding.tvNewsTime.text = news.publishTime
        binding.tvCommentCount.text = "${news.commentCount}评论"
        binding.tvNewsContent.text = news.content

        // 加载三张横向图片（使用虚拟数据）
        val imageUrls = listOf(
            "https://picsum.photos/400/300?random=1",
            "https://picsum.photos/400/300?random=2",
            "https://picsum.photos/400/300?random=3"
        )

        val imageViews = listOf(binding.ivImage1, binding.ivImage2, binding.ivImage3)

        imageViews.forEachIndexed { index, imageView ->
            Glide.with(requireContext())
                .load(imageUrls[index])
                .centerCrop()
                .into(imageView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(news: News): LongImageNewsDetailFragment {
            return BaseNewsDetailFragment.newInstance(news, LongImageNewsDetailFragment::class.java) as LongImageNewsDetailFragment
        }
    }
}