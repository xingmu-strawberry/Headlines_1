package com.example.headlines.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.headlines.R
import com.example.headlines.data.model.News
import com.example.headlines.data.model.NewsType
import com.example.headlines.databinding.ActivityNewsDetailBinding
import com.example.headlines.ui.fragments.*

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadNewsFragment()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun loadNewsFragment() {
        // 从Intent获取新闻数据
        val news = News(
            id = intent.getIntExtra("news_id", 0),
            title = intent.getStringExtra("news_title") ?: "",
            content = intent.getStringExtra("news_content") ?: "",
            type = NewsType.valueOf(intent.getStringExtra("news_type") ?: "TEXT"),
            source = intent.getStringExtra("news_source") ?: "头条新闻",
            commentCount = intent.getIntExtra("news_comment_count", 0),
            publishTime = intent.getStringExtra("news_publish_time") ?: "刚刚",
            imageUrl = intent.getStringExtra("news_image_url"),
            imageUrl2 = intent.getStringExtra("news_image_url_2"), // <-- 修正
            imageUrl3 = intent.getStringExtra("news_image_url_3"), // <-- 修正
            videoUrl = intent.getStringExtra("news_video_url"),
            videoDuration = intent.getStringExtra("news_video_duration"),
            isTop = intent.getBooleanExtra("news_is_top", false)
        )

        // 根据新闻类型加载对应的Fragment
        val fragment = when (news.type) {
            NewsType.TEXT -> TextNewsDetailFragment.newInstance(news)
            NewsType.IMAGE -> ImageNewsDetailFragment.newInstance(news)
            NewsType.VIDEO -> VideoNewsDetailFragment.newInstance(news)
            NewsType.LONG_IMAGE -> LongImageNewsDetailFragment.newInstance(news)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setupClickListeners() {
        // 底部操作栏的点击事件
        binding.btnBack.setOnClickListener { finish() }
        binding.btnLike.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                android.widget.Toast.makeText(this, "已点赞", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnCollect.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                android.widget.Toast.makeText(this, "已收藏", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                android.widget.Toast.makeText(this, "已取消收藏", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnShare.setOnClickListener { shareNews() }
        binding.btnComment.setOnClickListener {
            android.widget.Toast.makeText(this, "评论功能开发中", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareNews() {
        val title = intent.getStringExtra("news_title") ?: "新闻标题"
        val source = intent.getStringExtra("news_source") ?: "未知来源"

        val shareIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT,
                "分享新闻：$title\n来源：$source\n\n来自头条新闻App")
            type = "text/plain"
        }
        startActivity(android.content.Intent.createChooser(shareIntent, "分享新闻到"))
    }
}