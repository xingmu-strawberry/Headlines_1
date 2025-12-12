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

        // 使用虚拟图片URL（示例图片）
        val imageUrls = listOf(
            "https://picsum.photos/300/200?random=1",
            "https://picsum.photos/300/200?random=2",
            "https://picsum.photos/300/200?random=3"
        )

        val imageViews = listOf(binding.ivImage1, binding.ivImage2, binding.ivImage3)

        imageViews.forEachIndexed { index, imageView ->
            // 总是加载虚拟图片（即使News没有图片数据）
            Glide.with(binding.root.context)
                .load(imageUrls[index])
                .apply(RequestOptions().centerCrop())
                .into(imageView)
        }
    }
}