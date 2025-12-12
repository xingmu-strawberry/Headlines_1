package com.example.headlines.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.headlines.databinding.*
import com.example.headlines.data.model.News
import com.example.headlines.data.model.NewsType
import com.example.headlines.ui.adapters.viewholders.*

class NewsAdapter : ListAdapter<News, RecyclerView.ViewHolder>(NewsDiffCallback()) {

    private var itemClickListener: ((News) -> Unit)? = null

    // 新增：保存原始数据用于排序
    private var originalList: List<News> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (NewsType.values()[viewType]) {
            NewsType.TEXT -> {
                val binding = ItemNewsTextBinding.inflate(inflater, parent, false)
                TextNewsViewHolder(binding)
            }
            NewsType.IMAGE -> {
                val binding = ItemNewsImageBinding.inflate(inflater, parent, false)
                ImageNewsViewHolder(binding)
            }
            NewsType.VIDEO -> {
                val binding = ItemNewsVideoBinding.inflate(inflater, parent, false)
                VideoNewsViewHolder(binding)
            }
            NewsType.LONG_IMAGE -> {
                val binding = ItemNewsLongImageBinding.inflate(inflater, parent, false)
                LongImageNewsViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val news = getItem(position)

        // 添加调试日志
        Log.d("NewsAdapter", "绑定位置: $position, 标题: ${news.title}, isTop: ${news.isTop}")

        when (holder) {
            is TextNewsViewHolder -> holder.bind(news)
            is ImageNewsViewHolder -> holder.bind(news)
            is VideoNewsViewHolder -> holder.bind(news)
            is LongImageNewsViewHolder -> holder.bind(news)
        }

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(news)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }

    // ========== 新增：置顶排序逻辑 ==========

    // 重写 submitList 方法，在提交前排序
    override fun submitList(list: MutableList<News>?) {
        originalList = list ?: emptyList()
        val sortedList = sortNewsByTop(originalList)
        Log.d("NewsAdapter", "提交列表，原始数量: ${originalList.size}, 排序后数量: ${sortedList.size}")
        super.submitList(sortedList)
    }

    override fun submitList(list: MutableList<News>?, commitCallback: Runnable?) {
        originalList = list ?: emptyList()
        val sortedList = sortNewsByTop(originalList)
        Log.d("NewsAdapter", "提交列表(带回调)，原始数量: ${originalList.size}, 排序后数量: ${sortedList.size}")
        super.submitList(sortedList, commitCallback)
    }

    // 按置顶排序函数
    private fun sortNewsByTop(newsList: List<News>): MutableList<News> {
        val result = mutableListOf<News>()
        val topNews = mutableListOf<News>()
        val normalNews = mutableListOf<News>()

        // 分离置顶和非置顶新闻
        newsList.forEach { news ->
            Log.d("NewsAdapter", "排序检查: ${news.title}, isTop: ${news.isTop}")
            if (news.isTop) {
                topNews.add(news)
                Log.d("NewsAdapter", "→ 添加到置顶列表")
            } else {
                normalNews.add(news)
                Log.d("NewsAdapter", "→ 添加到普通列表")
            }
        }

        Log.d("NewsAdapter", "排序结果: 置顶新闻 ${topNews.size} 条, 普通新闻 ${normalNews.size} 条")

        // 合并：置顶在前，非置顶在后
        result.addAll(topNews)
        result.addAll(normalNews)

        // 打印最终排序结果
        result.forEachIndexed { index, news ->
            Log.d("NewsAdapter", "最终排序 $index: ${news.title}, isTop: ${news.isTop}")
        }

        return result
    }

    fun setOnNewsClickListener(listener: (News) -> Unit) {
        this.itemClickListener = listener
    }
}

class NewsDiffCallback : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }
}