package com.example.headlines.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.headlines.databinding.ActivityNewsDetailBinding
import com.example.headlines.data.model.NewsType

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupNewsData()
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

    private fun setupNewsData() {
        // 从Intent获取新闻数据
        val title = intent.getStringExtra("news_title") ?: "新闻标题"
        val source = intent.getStringExtra("news_source") ?: "未知来源"
        val time = intent.getStringExtra("news_time") ?: "刚刚"
        val commentCount = intent.getIntExtra("news_comment_count", 0)
        val imageUrl = intent.getStringExtra("news_image_url")
        val newsType = intent.getStringExtra("news_type")
        val newsId = intent.getIntExtra("news_id", 0)

        binding.tvNewsTitle.text = title
        binding.tvNewsSource.text = source
        binding.tvNewsTime.text = time
        binding.tvNewsCommentCount.text = commentCount.toString()
        binding.tvNewsContent.text = getNewsContent(newsId, title, source)

        // 设置工具栏标题
        binding.collapsingToolbar.title = title

        // 加载图片
        imageUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.ivNewsDetailImage)
        }

        // 根据新闻类型显示不同的内容
        when (newsType) {
            NewsType.VIDEO.name -> {
                // 如果是视频新闻，可以显示播放按钮
                binding.tvNewsContent.text = "这是一条视频新闻，点击播放按钮观看视频。\n\n${getDefaultContent()}"
            }
            NewsType.IMAGE.name, NewsType.LONG_IMAGE.name -> {
                // 如果是图片新闻
                binding.tvNewsContent.text = "这条新闻包含多张精彩图片。\n\n${getDefaultContent()}"
            }
            else -> {
                binding.tvNewsContent.text = getDefaultContent()
            }
        }
    }

    private fun getNewsContent(id: Int, title: String, source: String): String {
        return "新闻ID: $id\n\n" +
                "这是一篇关于'$title'的详细报道。\n\n" +
                "来源：$source\n\n" +
                getDefaultContent()
    }

    private fun getDefaultContent(): String {
        return "这里是新闻的详细内容。根据聚合数据API返回的信息，这里将显示完整的新闻正文。" +
                "\n\n习近平主席的重要论述为我们指明了方向，中非合作将持续深化。" +
                "\n\n乌拉圭队的世界杯表现备受期待，球迷们都在关注他们的精彩比赛。" +
                "\n\n新时代的中国特色社会主义事业正在蓬勃发展，我们要继续努力奋斗。" +
                "\n\n高铁350公里高标准运营展示了中国科技的进步。" +
                "\n\n社交观察类综艺桃花坞的成功，反映了观众对真实人际关系的关注。"
    }

    private fun setupClickListeners() {
        // 返回按钮
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 点赞按钮
        binding.btnLike.setOnClickListener {
            // 点赞功能
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                android.widget.Toast.makeText(this, "已点赞", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // 收藏按钮
        binding.btnCollect.setOnClickListener {
            // 收藏功能
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                android.widget.Toast.makeText(this, "已收藏", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                android.widget.Toast.makeText(this, "已取消收藏", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // 分享按钮
        binding.btnShare.setOnClickListener {
            // 分享功能
            shareNews()
        }

        // 评论按钮
        binding.btnComment.setOnClickListener {
            // 跳转到评论页面
            android.widget.Toast.makeText(this, "评论功能开发中", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareNews() {
        val title = binding.tvNewsTitle.text.toString()
        val source = binding.tvNewsSource.text.toString()
        val shareIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT,
                "分享新闻：$title\n来源：$source\n\n来自头条新闻App")
            type = "text/plain"
        }
        startActivity(android.content.Intent.createChooser(shareIntent, "分享新闻到"))
    }
}