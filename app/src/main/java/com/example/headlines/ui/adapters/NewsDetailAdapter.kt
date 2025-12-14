package com.example.headlines.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.headlines.R
import com.example.headlines.data.model.NewsDetail
import com.example.headlines.data.model.NewsDetailType
import com.example.headlines.data.model.formatCount
import com.example.headlines.data.model.formatPublishTime

class NewsDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<Any>() // 可以包含不同数据类型
    private var onImageClickListener: ((String) -> Unit)? = null
    private var onVideoClickListener: (() -> Unit)? = null

    // 设置点击监听器
    fun setOnImageClickListener(listener: (String) -> Unit) {
        this.onImageClickListener = listener
    }

    fun setOnVideoClickListener(listener: () -> Unit) {
        this.onVideoClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = items[position]) {
            is NewsDetailHeader -> HEADER_VIEW_TYPE
            is NewsDetail -> when (item.type) {
                NewsDetailType.TEXT -> TEXT_VIEW_TYPE
                NewsDetailType.IMAGE -> IMAGE_VIEW_TYPE
                NewsDetailType.VIDEO -> VIDEO_VIEW_TYPE
                NewsDetailType.LONG_IMAGE -> LONG_IMAGE_VIEW_TYPE
            }
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_detail_header, parent, false)
                HeaderViewHolder(view)
            }
            TEXT_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_detail_text, parent, false)
                TextViewHolder(view)
            }
            IMAGE_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_detail_image, parent, false)
                ImageViewHolder(view)
            }
            VIDEO_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_detail_video, parent, false)
                VideoViewHolder(view)
            }
            LONG_IMAGE_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_detail_long_image, parent, false)
                LongImageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val header = items[position] as NewsDetailHeader
                holder.bind(header.newsDetail)
            }
            is TextViewHolder -> {
                val newsDetail = items[position] as NewsDetail
                holder.bind(newsDetail)
            }
            is ImageViewHolder -> {
                val newsDetail = items[position] as NewsDetail
                holder.bind(newsDetail)
            }
            is VideoViewHolder -> {
                val newsDetail = items[position] as NewsDetail
                holder.bind(newsDetail)
            }
            is LongImageViewHolder -> {
                val newsDetail = items[position] as NewsDetail
                holder.bind(newsDetail)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitData(newsDetail: NewsDetail) {
        items.clear()

        // 添加头部
        items.add(NewsDetailHeader(newsDetail))

        // 根据类型添加内容
        when (newsDetail.type) {
            NewsDetailType.TEXT -> {
                // 文本新闻：直接添加详情对象
                items.add(newsDetail)
            }
            NewsDetailType.IMAGE -> {
                // 图片新闻：一张或多张图片
                items.add(newsDetail)
            }
            NewsDetailType.VIDEO -> {
                // 视频新闻
                items.add(newsDetail)
            }
            NewsDetailType.LONG_IMAGE -> {
                // 长图新闻
                items.add(newsDetail)
            }
        }

        notifyDataSetChanged()
    }

    // ViewHolder 类
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textTitle: TextView = view.findViewById(R.id.textTitle)
        private val textAuthor: TextView = view.findViewById(R.id.textAuthor)
        private val textTime: TextView = view.findViewById(R.id.textTime)
        private val textViewCount: TextView = view.findViewById(R.id.textViewCount)
        private val textLikeCount: TextView = view.findViewById(R.id.textLikeCount)
        private val textCommentCount: TextView = view.findViewById(R.id.textCommentCount)
        private val imageAuthorAvatar: ImageView = view.findViewById(R.id.imageAuthorAvatar)

        fun bind(newsDetail: NewsDetail) {
            textTitle.text = newsDetail.title
            textAuthor.text = newsDetail.author
            textTime.text = newsDetail.formatPublishTime()
            textViewCount.text = newsDetail.formatCount(newsDetail.viewCount)
            textLikeCount.text = newsDetail.formatCount(newsDetail.likeCount)
            textCommentCount.text = newsDetail.formatCount(newsDetail.commentCount)

            // 加载作者头像
            if (newsDetail.authorAvatar.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(newsDetail.authorAvatar)
                    .placeholder(R.drawable.circle_bg)
                    .into(imageAuthorAvatar)
            }
        }
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textContent: TextView = view.findViewById(R.id.textContent)

        fun bind(newsDetail: NewsDetail) {
            textContent.text = newsDetail.content
        }
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textDescription: TextView = view.findViewById(R.id.textImageDescription)
        private val imageView: ImageView = view.findViewById(R.id.imageView)

        fun bind(newsDetail: NewsDetail) {
            textDescription.text = newsDetail.content

            // 加载第一张图片
            if (newsDetail.images.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(newsDetail.images[0])
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(imageView)

                // 设置图片点击监听
                imageView.setOnClickListener {
                    // 这里可以打开大图查看
                }
            }
        }
    }

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textVideoTitle: TextView = view.findViewById(R.id.textVideoTitle)
        private val textDuration: TextView = view.findViewById(R.id.textDuration)
        private val imagePlayButton: ImageView = view.findViewById(R.id.imagePlayButton)
        // private val frameLayout: View = view.findViewById(R.id.frameLayout)

        fun bind(newsDetail: NewsDetail) {
            textVideoTitle.text = newsDetail.content
            textDuration.text = "03:45" // 这里可以从新闻详情中获取

            // 设置点击事件
            imagePlayButton.setOnClickListener {
                // 触发视频播放
            }
        }
    }

    class LongImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textDescription: TextView = view.findViewById(R.id.textDescription)
        private val image1: ImageView = view.findViewById(R.id.image1)
        private val image2: ImageView = view.findViewById(R.id.image2)
        private val image3: ImageView = view.findViewById(R.id.image3)

        fun bind(newsDetail: NewsDetail) {
            textDescription.text = newsDetail.content

            // 加载三张图片
            newsDetail.images.take(3).forEachIndexed { index, imageUrl ->
                val imageView = when (index) {
                    0 -> image1
                    1 -> image2
                    2 -> image3
                    else -> null
                }

                imageView?.let {
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image_placeholder)
                        .into(it)
                }
            }
        }
    }

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val TEXT_VIEW_TYPE = 1
        private const val IMAGE_VIEW_TYPE = 2
        private const val VIDEO_VIEW_TYPE = 3
        private const val LONG_IMAGE_VIEW_TYPE = 4
    }

    // 内部数据类
    data class NewsDetailHeader(val newsDetail: NewsDetail)
}