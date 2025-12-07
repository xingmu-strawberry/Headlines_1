package com.example.headlines.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop  // 添加这行
import com.bumptech.glide.request.RequestOptions
import com.example.headlines.R  // 添加这行
import com.example.headlines.databinding.ItemNewsLongImageBinding
import com.example.headlines.data.model.News

class LongImageNewsViewHolder(private val binding: ItemNewsLongImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News) {
        binding.tvTitle.text = news.title

        // 创建请求选项
        val requestOptions = RequestOptions()
            .transform(CenterCrop())  // 使用 CenterCrop 变换
            .placeholder(R.drawable.ic_image_placeholder)

        // 加载长图
        news.imageUrl?.let { url ->
            Glide.with(binding.root.context)
                .load(url)
                .apply(requestOptions)  // 应用选项
                .into(binding.ivLongImage)
        } ?: run {
            // 如果没有URL，使用默认图片
            binding.ivLongImage.setImageResource(android.R.drawable.ic_menu_camera)
        }
    }
}