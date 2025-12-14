package com.example.headlines.ui.adapters.viewholders

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.headlines.data.model.News
import com.example.headlines.databinding.ItemNewsLongImageBinding // 假设这个 binding 包含了 ivImage1, ivImage2, ivImage3

class LongImageNewsViewHolder(private val binding: ItemNewsLongImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News) {
        binding.tvTitle.text = news.title

        // 1. 获取并分割 URL 字符串
        val imageUrls = news.imageUrl?.split(',') ?: emptyList()

        // 2. 将三个 ImageView 放入一个列表中，方便统一处理
        val imageViews = listOf(binding.ivImage1, binding.ivImage2, binding.ivImage3)

        // 3. 循环遍历 URL 列表和 ImageView 列表进行绑定
        for (i in imageViews.indices) {
            val imageView = imageViews[i]

            if (i < imageUrls.size) {
                // 如果 URL 列表中有对应的 URL，则加载图片并显示 ImageView
                val url = imageUrls[i].trim() // 清除可能存在的空格

                // 使用辅助函数加载图片
                loadImage(imageView, url)

                // 确保 ImageView 是可见的（如果布局中默认是 GONE/INVISIBLE）
                // 这一步取决于您的 XML 布局文件，但通常需要显式设置
                // imageView.visibility = View.VISIBLE

            } else {
                // 如果 URL 不足 3 个，隐藏多余的 ImageView
                // imageView.visibility = View.GONE

                // 或者直接给一个默认占位图
                imageView.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }

        // 确保其他信息（如来源、时间等）也绑定到对应的 TextView
        binding.tvSource.text = news.source
        binding.tvPublishTime.text = news.publishTime
        binding.tvCommentCount.text = "${news.commentCount} 评论"
    }

    /**
     * 辅助函数：使用 Glide 加载图片
     */
    private fun loadImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .load(url)
            // 列表页横向多图，一般使用 centerCrop 填充
            .apply(RequestOptions().centerCrop())
            .placeholder(android.R.drawable.ic_menu_gallery) // 加载占位符
            .error(android.R.drawable.ic_menu_close_clear_cancel) // 错误占位符
            .into(imageView)
    }
}