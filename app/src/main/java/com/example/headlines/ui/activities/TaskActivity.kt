package com.example.headlines.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headlines.MainActivity
import com.example.headlines.data.model.Task
import com.example.headlines.databinding.ActivityTaskBinding
import com.example.headlines.ui.adapters.TaskAdapter
import com.example.headlines.R
import java.util.Date

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupTaskList()
        loadTasks()
        setupClickListeners()
        setupDailySignIn()
    }

    private fun setupToolbar() {
        binding.toolbar.title = "任务中心"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupTaskList() {
        taskAdapter = TaskAdapter(taskList)
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = taskAdapter

        // 设置任务点击事件
        taskAdapter.setOnTaskClickListener { task ->
            when (task.id) {
                "daily_signin" -> performDailySignIn()
                "read_news" -> navigateToNews()
                "share_news" -> shareNews()
                "comment_news" -> commentOnNews()
                "watch_video" -> navigateToVideo()
                "invite_friend" -> inviteFriend()
            }
        }
    }

    private fun loadTasks() {
        // 模拟任务数据
        taskList.clear()
        taskList.addAll(
            listOf(
                Task(
                    id = "daily_signin",
                    title = "每日签到",
                    description = "每日签到领取金币",
                    iconRes = R.drawable.ic_signin,
                    points = 50,
                    progress = 0,
                    total = 1,
                    completed = false,
                    type = Task.Type.DAILY
                ),
                Task(
                    id = "read_news",
                    title = "阅读文章",
                    description = "阅读5篇文章",
                    iconRes = R.drawable.ic_read,
                    points = 30,
                    progress = 0,
                    total = 5,
                    completed = false,
                    type = Task.Type.DAILY
                ),
                Task(
                    id = "share_news",
                    title = "分享文章",
                    description = "分享文章给好友",
                    iconRes = R.drawable.ic_share,
                    points = 20,
                    progress = 0,
                    total = 1,
                    completed = false,
                    type = Task.Type.DAILY
                ),
                Task(
                    id = "comment_news",
                    title = "发表评论",
                    description = "对文章发表评论",
                    iconRes = R.drawable.ic_menu_comment,
                    points = 20,
                    progress = 0,
                    total = 1,
                    completed = false,
                    type = Task.Type.DAILY
                ),
                Task(
                    id = "watch_video",
                    title = "观看视频",
                    description = "观看10个视频",
                    iconRes = R.drawable.ic_video,
                    points = 40,
                    progress = 0,
                    total = 10,
                    completed = false,
                    type = Task.Type.DAILY
                ),
                Task(
                    id = "invite_friend",
                    title = "邀请好友",
                    description = "邀请好友注册",
                    iconRes = R.drawable.ic_invite,
                    points = 200,
                    progress = 0,
                    total = 1,
                    completed = false,
                    type = Task.Type.ACHIEVEMENT
                )
            )
        )
        taskAdapter.notifyDataSetChanged()

        updateUserInfo()
    }

    private fun setupClickListeners() {
        binding.btnCheckIn.setOnClickListener {
            performDailySignIn()
        }

        binding.btnExchange.setOnClickListener {
            // 跳转到积分兑换页面
            android.widget.Toast.makeText(this, "积分兑换功能开发中", android.widget.Toast.LENGTH_SHORT).show()
        }

        binding.btnTaskHistory.setOnClickListener {
            // 查看任务历史
            android.widget.Toast.makeText(this, "任务记录功能开发中", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupDailySignIn() {
        // 检查是否已签到
        val today = Date().date
        val sharedPref = getSharedPreferences("task_prefs", MODE_PRIVATE)
        val lastSignInDay = sharedPref.getInt("last_signin_day", -1)

        if (today == lastSignInDay) {
            binding.btnCheckIn.text = "今日已签到"
            binding.btnCheckIn.isEnabled = false
        } else {
            binding.btnCheckIn.text = "立即签到"
            binding.btnCheckIn.isEnabled = true
        }
    }

    private fun performDailySignIn() {
        val today = Date().date
        val sharedPref = getSharedPreferences("task_prefs", MODE_PRIVATE)

        // 检查是否已签到
        if (sharedPref.getInt("last_signin_day", -1) == today) {
            android.widget.Toast.makeText(this, "今日已签到", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        // 更新签到状态
        val editor = sharedPref.edit()
        editor.putInt("last_signin_day", today)

        // 获取连续签到天数
        val consecutiveDays = sharedPref.getInt("consecutive_days", 0) + 1
        editor.putInt("consecutive_days", consecutiveDays)
        editor.apply()

        // 增加积分
        val totalPoints = sharedPref.getInt("total_points", 0) + 50
        editor.putInt("total_points", totalPoints)
        editor.apply()

        // 更新UI
        binding.btnCheckIn.text = "今日已签到"
        binding.btnCheckIn.isEnabled = false
        updateUserInfo()

        android.widget.Toast.makeText(this, "签到成功！获得50积分，连续签到${consecutiveDays}天", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun updateUserInfo() {
        val sharedPref = getSharedPreferences("task_prefs", MODE_PRIVATE)
        val totalPoints = sharedPref.getInt("total_points", 0)
        val consecutiveDays = sharedPref.getInt("consecutive_days", 0)

        binding.tvTotalPoints.text = totalPoints.toString()
        binding.tvConsecutiveDays.text = "${consecutiveDays}天"

        // 计算今日完成任务数
        val completedTasks = taskList.count { it.completed }
        binding.tvTodayTasks.text = "$completedTasks/${taskList.size}"
    }

    private fun navigateToNews() {
        // 跳转到新闻页
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("tab_index", 1) // 推荐页
        startActivity(intent)
    }

    private fun shareNews() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "我在今日头条发现了一篇好文章，快来看看吧！")
        startActivity(Intent.createChooser(shareIntent, "分享文章"))
    }

    private fun commentOnNews() {
        navigateToNews()
        android.widget.Toast.makeText(this, "请在文章详情页发表评论", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun navigateToVideo() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("tab_index", 5) // 视频页
        startActivity(intent)
    }

    private fun inviteFriend() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "快来使用今日头条APP，这里有最新最热的新闻资讯！下载链接：https://headlines.example.com")
        startActivity(Intent.createChooser(shareIntent, "邀请好友"))
    }
}