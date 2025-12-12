package com.example.headlines.ui.adapters.viewholders

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.headlines.databinding.ItemDetailHeaderBinding
import com.example.headlines.R
import com.example.headlines.data.model.NewsDetail
import com.example.headlines.data.model.formatCount
import com.example.headlines.data.model.formatPublishTime

class DetailHeaderViewHolder(
    private val binding: ItemDetailHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(detail: NewsDetail) {
        binding.textTitle.text = detail.title
        binding.textAuthor.text = detail.author
        binding.textTime.text = detail.formatPublishTime()
        binding.textViewCount.text = detail.formatCount(detail.viewCount)
        binding.textCommentCount.text = detail.formatCount(detail.commentCount)
        binding.textLikeCount.text = detail.formatCount(detail.likeCount)

        // 加载作者头像（如果有）
        if (detail.authorAvatar.isNotEmpty()) {
            Glide.with(binding.imageAuthorAvatar)
                .load(detail.authorAvatar)
                .circleCrop()
                .placeholder(R.drawable.circle_bg)
                .into(binding.imageAuthorAvatar)
        }

        // 设置点赞点击事件
        binding.buttonLike.setOnClickListener {
            // 模拟点赞功能
            binding.buttonLike.isSelected = !binding.buttonLike.isSelected
        }

        // 设置收藏点击事件
        binding.buttonFavorite.setOnClickListener {
            binding.buttonFavorite.isSelected = !binding.buttonFavorite.isSelected
        }
    }
}