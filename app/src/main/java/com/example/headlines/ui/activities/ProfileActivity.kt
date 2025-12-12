package com.example.headlines.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.headlines.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupUserData()
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

    private fun setupUserData() {
        // 模拟用户数据
        binding.tvUserName.text = "xingmu-strawberry"
        binding.tvUserId.text = "ID: 123456789"
        binding.tvFollowCount.text = "128"
        binding.tvFansCount.text = "2.5K"
        binding.tvArticleCount.text = "64"
        binding.tvLikeCount.text = "1.2K"
    }

    private fun setupClickListeners() {
        // 编辑资料按钮
        binding.btnEditProfile.setOnClickListener {
            android.widget.Toast.makeText(this, "编辑资料功能开发中", android.widget.Toast.LENGTH_SHORT).show()
        }

        // 我的收藏
        binding.layoutMyCollection.setOnClickListener {
            android.widget.Toast.makeText(this, "我的收藏功能开发中", android.widget.Toast.LENGTH_SHORT).show()
        }

        // 浏览历史
        binding.layoutBrowseHistory.setOnClickListener {
            android.widget.Toast.makeText(this, "浏览历史功能开发中", android.widget.Toast.LENGTH_SHORT).show()
        }

        // 我的任务
        binding.layoutMyTasks.setOnClickListener {
            android.widget.Toast.makeText(this, "我的任务功能开发中", android.widget.Toast.LENGTH_SHORT).show()
        }

        // 设置
        binding.layoutSettings.setOnClickListener {
            // 跳转到设置页面
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}