package com.example.headlines.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.headlines.R
import com.example.headlines.databinding.ItemNewsImageBinding
import com.example.headlines.data.model.News

class ImageNewsViewHolder(private val binding: ItemNewsImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News) {
        binding.tvTitle.text = news.title
        binding.tvSource.text = news.source
        binding.tvComment.text = "${news.commentCount}评论"
        binding.tvTime.text = news.publishTime

        // 创建请求选项
        val requestOptions = RequestOptions()
            .transform(CenterCrop())
            .placeholder(R.drawable.ic_image_placeholder)

        // 加载图片
        news.imageUrl?.let { url ->
            Glide.with(binding.root.context)
                .load(url)
                .apply(requestOptions)
                .into(binding.ivImage)
        } ?: run {
            // 如果没有图片URL，使用默认图标
            binding.ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }
}