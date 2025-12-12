package com.example.headlines.ui.adapters.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.headlines.data.model.News
import com.example.headlines.databinding.ItemNewsTextBinding

class TextNewsViewHolder(private val binding: ItemNewsTextBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News) {
        binding.tvTitle.text = news.title
        binding.tvSource.text = "${news.source} ${news.commentCount}评论"
        binding.tvTime.text = news.publishTime

        // 显示置顶标签
        if (news.isTop) {
            binding.tvTopTag.visibility = View.VISIBLE
        } else {
            binding.tvTopTag.visibility = View.GONE
        }

        // 设置更多按钮点击事件
        binding.ivMore.setOnClickListener {
            // 显示更多选项菜单
        }
    }
}