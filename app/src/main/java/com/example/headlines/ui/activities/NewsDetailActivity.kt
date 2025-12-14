package com.example.headlines.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.headlines.R
import com.example.headlines.databinding.ActivityNewsDetailBinding
import com.example.headlines.ui.fragments.NewsDetailFragment

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding
    private var newsId: Int = -1

    companion object {
        // 定义常量，确保Fragment和Activity使用相同的key
        const val EXTRA_NEWS_ID = "news_id"  // 改为小写，与Fragment匹配
        const val EXTRA_NEWS_TITLE = "news_title"

        fun startActivity(context: Context, newsId: Int, newsTitle: String = "") {
            Log.d("NewsDetailActivity", "Companion: 启动详情页, newsId=$newsId")
            val intent = Intent(context, NewsDetailActivity::class.java).apply {
                putExtra(EXTRA_NEWS_ID, newsId)
                putExtra(EXTRA_NEWS_TITLE, newsTitle)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 尝试多种可能的key来获取newsId
        newsId = intent.getIntExtra(EXTRA_NEWS_ID, -1)  // 首先尝试companion定义的key

        val newsTitle = intent.getStringExtra(EXTRA_NEWS_TITLE)
            ?: intent.getStringExtra("news_title")
            ?: intent.getStringExtra("extra_news_title")
            ?: "新闻详情"

        Log.d("NewsDetailActivity", "最终收到: newsId=$newsId, newsTitle=$newsTitle")

        if (newsId == -1) {
            Log.e("NewsDetailActivity", "无效的newsId，显示测试内容")
            // 即使newsId无效，也不要直接finish，显示测试内容
            binding = ActivityNewsDetailBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // 显示测试Fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NewsDetailFragment.newInstance(999, "测试新闻"))
                .commit()
            return
        }

        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置Fragment
        Log.d("NewsDetailActivity", "准备加载Fragment...")
        try {
            val fragment = NewsDetailFragment.newInstance(newsId, newsTitle)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            Log.d("NewsDetailActivity", "Fragment加载完成")
        } catch (e: Exception) {
            Log.e("NewsDetailActivity", "加载Fragment失败", e)
        }
    }
}