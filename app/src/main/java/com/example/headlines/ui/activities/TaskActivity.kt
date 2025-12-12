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
import com.example.headlines.data.model.TaskHistory
import java.util.Date
import java.util.UUID

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
            if (task.completed) {
                android.widget.Toast.makeText(this, "该任务已完成", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnTaskClickListener
            }

            when (task.id) {
                "daily_signin" -> performDailySignIn()
                "read_news" -> {
                    navigateToNews()
                    // 阅读文章只更新进度，不立即完成任务
                    updateTaskProgress(task.id)
                }
                "share_news" -> {
                    shareNews()
                    completeTask(task.id, task.title, task.points)
                }
                "comment_news" -> {
                    commentOnNews()
                    completeTask(task.id, task.title, task.points)
                }
                "watch_video" -> {
                    navigateToVideo()
                    // 观看视频只更新进度
                    updateTaskProgress(task.id)
                }
                "invite_friend" -> {
                    inviteFriend()
                    completeTask(task.id, task.title, task.points)
                }
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

        // 添加任务历史按钮点击监听
        binding.btnTaskHistory.setOnClickListener {
            val intent = Intent(this, TaskHistoryActivity::class.java)
            startActivity(intent)
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

        // 在签到成功后添加历史记录
        TaskHistoryActivity.addHistoryRecord(this,
            TaskHistory(
                id = UUID.randomUUID().toString(),
                type = TaskHistory.Type.SIGN_IN,
                title = "每日签到",
                description = "连续签到第${consecutiveDays}天",
                points = 50,
                timestamp = System.currentTimeMillis()
            )
        )
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

    private fun completeTask(taskId: String, taskTitle: String, points: Int) {
        // 1. 增加总积分
        val sharedPref = getSharedPreferences("task_prefs", MODE_PRIVATE)
        val currentPoints = sharedPref.getInt("total_points", 0)
        val newPoints = currentPoints + points

        sharedPref.edit()
            .putInt("total_points", newPoints)
            .apply()

        // 2. 更新任务完成状态（在任务列表中标记为完成）
        val taskIndex = taskList.indexOfFirst { it.id == taskId }
        if (taskIndex != -1) {
            val task = taskList[taskIndex]
            taskList[taskIndex] = task.copy(
                completed = true,
                progress = task.total  // 设置进度为完成
            )
            taskAdapter.notifyItemChanged(taskIndex)

            // 更新今日任务完成数显示
            updateUserInfo()
        }

        // 3. 记录任务完成历史
        TaskHistoryActivity.addHistoryRecord(this,
            TaskHistory(
                id = UUID.randomUUID().toString(),
                type = TaskHistory.Type.TASK_COMPLETE,
                title = taskTitle,
                description = "完成任务获得积分",
                points = points,
                timestamp = System.currentTimeMillis()
            )
        )

        // 4. 显示积分获取提示
        android.widget.Toast.makeText(this, "完成任务！获得${points}积分", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun updateTaskProgress(taskId: String) {
        val taskIndex = taskList.indexOfFirst { it.id == taskId }
        if (taskIndex != -1) {
            val task = taskList[taskIndex]
            val newProgress = task.progress + 1

            if (newProgress <= task.total) {
                // 更新进度
                taskList[taskIndex] = task.copy(progress = newProgress)

                // 检查是否完成任务
                if (newProgress == task.total) {
                    // 完成任务，增加积分
                    completeTask(taskId, task.title, task.points)
                } else {
                    // 只更新进度
                    taskAdapter.notifyItemChanged(taskIndex)
                    android.widget.Toast.makeText(this, "进度更新：${newProgress}/${task.total}", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun resetDailyTasks() {
        val sharedPref = getSharedPreferences("task_prefs", MODE_PRIVATE)
        val lastResetDay = sharedPref.getInt("last_reset_day", -1)
        val today = Date().date

        if (lastResetDay != today) {
            // 重置每日任务
            for (i in taskList.indices) {
                val task = taskList[i]
                if (task.type == Task.Type.DAILY) {
                    taskList[i] = task.copy(
                        completed = false,
                        progress = 0
                    )
                }
            }
            taskAdapter.notifyDataSetChanged()

            // 保存重置日期
            sharedPref.edit()
                .putInt("last_reset_day", today)
                .apply()
        }
    }

    // 在 onCreate 或 onResume 中调用
    override fun onResume() {
        super.onResume()
        resetDailyTasks()
        updateUserInfo()
    }

}