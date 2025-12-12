package com.example.headlines.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.headlines.data.model.News
import com.example.headlines.databinding.ItemNewsLongImageBinding

class LongImageNewsViewHolder(private val binding: ItemNewsLongImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News) {
        binding.tvTitle.text = news.title

        // 加载长图
        news.imageUrl?.let { url ->
            Glide.with(binding.root.context)
                .load(url)
                .apply(RequestOptions().centerCrop())
                .into(binding.ivLongImage)
        } ?: run {
            // 如果没有URL，使用默认图片
            binding.ivLongImage.setImageResource(android.R.drawable.ic_menu_camera)
        }
    }
}