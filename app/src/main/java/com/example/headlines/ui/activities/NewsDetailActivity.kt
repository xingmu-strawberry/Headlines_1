package com.example.headlines.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.headlines.R
import com.example.headlines.databinding.ActivityNewsDetailBinding
import com.example.headlines.ui.fragments.NewsDetailFragment

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 从Intent获取参数
        val newsId = intent.getStringExtra("news_id") ?: ""
        val newsType = intent.getStringExtra("news_type") ?: "TEXT"

        // 显示NewsDetailFragment
        val fragment = NewsDetailFragment.newInstance(newsId, newsType)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}