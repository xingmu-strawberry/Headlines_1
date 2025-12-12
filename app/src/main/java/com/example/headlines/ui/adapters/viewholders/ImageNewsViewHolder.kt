package com.example.headlines.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.headlines.data.model.News
import com.example.headlines.databinding.ItemNewsImageBinding

class ImageNewsViewHolder(private val binding: ItemNewsImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News) {
        binding.tvTitle.text = news.title
        binding.tvSource.text = news.source
        binding.tvComment.text = "${news.commentCount}评论"
        binding.tvTime.text = news.publishTime

        // 加载图片
        news.imageUrl?.let { url ->
            Glide.with(binding.root.context)
                .load(url)
                .apply(RequestOptions().centerCrop())
                .into(binding.ivImage)
        } ?: run {
            // 如果没有图片URL，使用默认图标
            binding.ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }
}