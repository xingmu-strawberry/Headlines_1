package com.example.headlines.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.headlines.R
import com.example.headlines.databinding.ItemNewsVideoBinding
import com.example.headlines.data.model.News

class VideoNewsViewHolder(private val binding: ItemNewsVideoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News) {
        binding.tvTitle.text = news.title
        binding.tvSource.text = news.source
        binding.tvComment.text = "${news.commentCount}评论"
        binding.tvTime.text = news.publishTime
        binding.tvDuration.text = news.videoDuration ?: "00:00"

        // 创建请求选项
        val requestOptions = RequestOptions()
            .transform(CenterCrop())
            .placeholder(R.drawable.ic_video_placeholder)

        // 加载视频封面
        news.imageUrl?.let { url ->
            Glide.with(binding.root.context)
                .load(url)
                .apply(requestOptions)
                .into(binding.ivVideoCover)
        } ?: run {
            // 如果没有URL，使用默认图标
            binding.ivVideoCover.setImageResource(android.R.drawable.ic_media_play)
        }

        // 设置播放按钮点击
        binding.ivVideoCover.setOnClickListener {
            // 播放视频
            println("播放视频: ${news.title}")
        }
    }
}