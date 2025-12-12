package com.example.headlines.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.headlines.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupSettings()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "设置"
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupSettings() {
        // 初始化设置状态
        // 这里可以从SharedPreferences读取设置
    }

    private fun setupClickListeners() {
        // 夜间模式开关
        binding.switchNightMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Wi-Fi下自动播放视频
        binding.switchAutoPlay.setOnCheckedChangeListener { _, isChecked ->
            // 保存设置到SharedPreferences
            android.widget.Toast.makeText(this,
                if (isChecked) "Wi-Fi下自动播放已开启" else "Wi-Fi下自动播放已关闭",
                android.widget.Toast.LENGTH_SHORT).show()
        }

        // 清除缓存
        binding.layoutClearCache.setOnClickListener {
            clearCache()
        }

        // 检查更新
        binding.layoutCheckUpdate.setOnClickListener {
            checkForUpdate()
        }

        // 反馈建议
        binding.layoutFeedback.setOnClickListener {
            sendFeedback()
        }

        // 关于我们
        binding.layoutAbout.setOnClickListener {
            showAboutDialog()
        }

        // 隐私政策
        binding.layoutPrivacy.setOnClickListener {
            openPrivacyPolicy()
        }

        // 用户协议
        binding.layoutAgreement.setOnClickListener {
            openUserAgreement()
        }
    }

    private fun clearCache() {
        // 清除缓存逻辑
        android.widget.Toast.makeText(this, "缓存已清除", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun checkForUpdate() {
        android.widget.Toast.makeText(this, "当前已是最新版本", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun sendFeedback() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:contact@toutiao.com")
            putExtra(Intent.EXTRA_SUBJECT, "头条新闻App反馈建议")
            putExtra(Intent.EXTRA_TEXT, "请在这里描述您的问题或建议：\n\n")
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "发送邮件"))
        } catch (e: Exception) {
            android.widget.Toast.makeText(this, "未找到邮件应用", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAboutDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("关于头条新闻")
            .setMessage("版本：1.0.0\n\n" +
                    "头条新闻是一款聚合新闻资讯应用，提供实时、全面的新闻信息服务。\n\n" +
                    "开发者：头条新闻团队\n" +
                    "联系方式：contact@toutiao.com")
            .setPositiveButton("确定") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openPrivacyPolicy() {
        val uri = Uri.parse("https://www.toutiao.com/privacy")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun openUserAgreement() {
        val uri = Uri.parse("https://www.toutiao.com/agreement")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}