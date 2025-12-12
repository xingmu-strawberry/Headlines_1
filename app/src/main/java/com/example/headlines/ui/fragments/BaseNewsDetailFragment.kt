package com.example.headlines.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.headlines.data.model.News

abstract class BaseNewsDetailFragment : Fragment() {

    protected lateinit var news: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // 注意：这里应该从Bundle中获取News数据
            // 但由于News没有实现Parcelable，我们需要传递各个字段
            news = createNewsFromArguments(it)
        }
    }

    private fun createNewsFromArguments(args: Bundle): News {
        return News(
            id = args.getInt("news_id", 0),
            title = args.getString("news_title") ?: "",
            content = args.getString("news_content") ?: "",
            type = com.example.headlines.data.model.NewsType.valueOf(
                args.getString("news_type") ?: "TEXT"
            ),
            source = args.getString("news_source") ?: "头条新闻",
            commentCount = args.getInt("news_comment_count", 0),
            publishTime = args.getString("news_publish_time") ?: "刚刚",
            imageUrl = args.getString("news_image_url"),
            imageUrl2 = args.getString("news_image_url_2"), // <-- 修正
            imageUrl3 = args.getString("news_image_url_3"), // <-- 修正
            videoUrl = args.getString("news_video_url"),
            videoDuration = args.getString("news_video_duration"),
            isTop = args.getBoolean("news_is_top", false)
        )
    }

    abstract override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    companion object {
        fun newInstance(news: News, fragmentClass: Class<out BaseNewsDetailFragment>): BaseNewsDetailFragment {
            val fragment = fragmentClass.newInstance()
            val args = Bundle().apply {
                // 传递News的各个字段
                putInt("news_id", news.id)
                putString("news_title", news.title)
                putString("news_content", news.content)
                putString("news_type", news.type.name)
                putString("news_source", news.source)
                putInt("news_comment_count", news.commentCount)
                putString("news_publish_time", news.publishTime)
                putString("news_image_url", news.imageUrl)
                putString("news_image_url_2", news.imageUrl2) // <-- 修正
                putString("news_image_url_3", news.imageUrl3) // <-- 修正
                putString("news_video_url", news.videoUrl)
                putString("news_video_duration", news.videoDuration)
                putBoolean("news_is_top", news.isTop)
            }
            fragment.arguments = args
            return fragment
        }
    }
}