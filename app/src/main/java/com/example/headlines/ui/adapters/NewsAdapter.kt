package com.example.headlines.ui.adapters

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

    // 改为私有变量，避免自动生成setter
    private var itemClickListener: ((News) -> Unit)? = null

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

    // 修改方法名，避免冲突
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