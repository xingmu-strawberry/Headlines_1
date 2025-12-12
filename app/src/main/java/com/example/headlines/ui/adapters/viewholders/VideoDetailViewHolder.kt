package com.example.headlines.ui.adapters.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.headlines.R
import com.example.headlines.databinding.ItemDetailVideoBinding

class VideoDetailViewHolder(private val binding: ItemDetailVideoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(videoUrl: String, title: String) {
        binding.textVideoTitle.text = title

        // 设置播放按钮点击事件
        binding.imagePlayButton.setOnClickListener {
            // 这里可以处理视频播放逻辑
            binding.imagePlayButton.visibility = View.GONE
            // TODO: 实现视频播放
        }

        // 可以加载视频缩略图
        // Glide.with(binding.root.context)
        //     .load(videoThumbnailUrl)
        //     .placeholder(R.drawable.video_placeholder)
        //     .into(binding.imageVideoThumbnail)
    }
}