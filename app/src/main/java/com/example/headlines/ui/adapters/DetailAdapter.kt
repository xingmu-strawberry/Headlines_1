package com.example.headlines.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.headlines.R
import com.example.headlines.data.model.NewsDetail
import com.example.headlines.data.model.NewsDetailType
import com.example.headlines.databinding.*
import com.example.headlines.ui.adapters.viewholders.*

class DetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_TEXT = 1
        private const val TYPE_IMAGE = 2
        private const val TYPE_VIDEO = 3
        private const val TYPE_LONG_IMAGE = 4
    }

    private var newsDetail: NewsDetail? = null

    fun submitDetail(detail: NewsDetail) {
        this.newsDetail = detail
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            1 -> {
                newsDetail?.let { detail ->
                    return when (detail.type) {
                        NewsDetailType.TEXT -> TYPE_TEXT
                        NewsDetailType.IMAGE -> TYPE_IMAGE
                        NewsDetailType.VIDEO -> TYPE_VIDEO
                        NewsDetailType.LONG_IMAGE -> TYPE_LONG_IMAGE
                        else -> {TYPE_TEXT}
                    }
                }
                TYPE_TEXT // 默认类型
            }
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    // 使用View Binding替代Data Binding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_HEADER -> {
                val binding = ItemDetailHeaderBinding.inflate(inflater, parent, false)
                DetailHeaderViewHolder(binding)
            }
            TYPE_TEXT -> {
                val binding = ItemDetailTextBinding.inflate(inflater, parent, false)
                TextDetailViewHolder(binding)
            }
            TYPE_IMAGE -> {
                val binding = ItemDetailImageBinding.inflate(inflater, parent, false)
                ImageDetailViewHolder(binding)
            }
            TYPE_VIDEO -> {
                val binding = ItemDetailVideoBinding.inflate(inflater, parent, false)
                VideoDetailViewHolder(binding)
            }
            TYPE_LONG_IMAGE -> {
                val binding = ItemDetailLongImageBinding.inflate(inflater, parent, false)
                LongImageDetailViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val detail = newsDetail ?: return

        when (holder) {
            is DetailHeaderViewHolder -> holder.bind(detail)
            is TextDetailViewHolder -> holder.bind(detail.content)
            is ImageDetailViewHolder -> holder.bind(detail.images)
            is VideoDetailViewHolder -> holder.bind(detail.videoUrl ?: "", detail.title)
            is LongImageDetailViewHolder -> holder.bind(detail.images)
        }
    }

    override fun getItemCount(): Int {
        return if (newsDetail != null) 2 else 0 // 头部 + 内容
    }
}