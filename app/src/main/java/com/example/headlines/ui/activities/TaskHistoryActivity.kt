package com.example.headlines.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headlines.data.model.TaskHistory
import com.example.headlines.databinding.ActivityTaskHistoryBinding
import com.example.headlines.ui.adapters.TaskHistoryAdapter
import com.example.headlines.R
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class TaskHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskHistoryBinding
    private lateinit var historyAdapter: TaskHistoryAdapter
    private val historyList = mutableListOf<TaskHistory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadHistoryData()
        setupEmptyView()
    }

    private fun setupToolbar() {
        binding.toolbar.title = "任务记录"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = TaskHistoryAdapter(historyList)
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = historyAdapter
    }

    private fun loadHistoryData() {
        historyList.clear()

        // 从SharedPreferences加载历史记录
        val sharedPref = getSharedPreferences("task_history", MODE_PRIVATE)
        val historyJson = sharedPref.getString("history_data", "[]")

        try {
            val jsonArray = JSONArray(historyJson)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val history = TaskHistory(
                    id = jsonObject.getString("id"),
                    type = TaskHistory.Type.valueOf(jsonObject.getString("type")),
                    title = jsonObject.getString("title"),
                    description = jsonObject.getString("description"),
                    points = jsonObject.getInt("points"),
                    timestamp = jsonObject.getLong("timestamp")
                )
                historyList.add(history)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 如果解析失败，加载模拟数据
            loadSampleHistoryData()
        }

        // 按时间倒序排序（最新的在前面）
        historyList.sortByDescending { it.timestamp }
        historyAdapter.notifyDataSetChanged()
    }

    private fun loadSampleHistoryData() {
        val calendar = Calendar.getInstance()

        // 添加一些示例数据
        historyList.addAll(
            listOf(
                TaskHistory(
                    id = UUID.randomUUID().toString(),
                    type = TaskHistory.Type.SIGN_IN,
                    title = "每日签到",
                    description = "连续签到第3天",
                    points = 50,
                    timestamp = calendar.timeInMillis
                ),
                TaskHistory(
                    id = UUID.randomUUID().toString(),
                    type = TaskHistory.Type.TASK_COMPLETE,
                    title = "阅读文章",
                    description = "完成阅读5篇文章任务",
                    points = 30,
                    timestamp = calendar.timeInMillis - 86400000 // 昨天
                ),
                TaskHistory(
                    id = UUID.randomUUID().toString(),
                    type = TaskHistory.Type.TASK_COMPLETE,
                    title = "分享文章",
                    description = "分享文章到微信",
                    points = 20,
                    timestamp = calendar.timeInMillis - 172800000 // 前天
                )
            )
        )
    }

    private fun setupEmptyView() {
        if (historyList.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
            binding.rvHistory.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.rvHistory.visibility = View.VISIBLE
        }
    }

    // 保存历史记录的方法（可在TaskActivity中调用）
    companion object {
        fun addHistoryRecord(context: android.content.Context, history: TaskHistory) {
            val sharedPref = context.getSharedPreferences("task_history", android.content.Context.MODE_PRIVATE)
            val historyJson = sharedPref.getString("history_data", "[]")

            try {
                val jsonArray = JSONArray(historyJson)
                val jsonObject = JSONObject().apply {
                    put("id", history.id)
                    put("type", history.type.name)
                    put("title", history.title)
                    put("description", history.description)
                    put("points", history.points)
                    put("timestamp", history.timestamp)
                }
                jsonArray.put(jsonObject)

                // 最多保存100条记录
                val maxRecords = 100
                val finalArray = if (jsonArray.length() > maxRecords) {
                    val newArray = JSONArray()
                    for (i in jsonArray.length() - maxRecords until jsonArray.length()) {
                        newArray.put(jsonArray.get(i))
                    }
                    newArray
                } else {
                    jsonArray
                }

                sharedPref.edit()
                    .putString("history_data", finalArray.toString())
                    .apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun clearAllHistory(context: android.content.Context) {
            val sharedPref = context.getSharedPreferences("task_history", android.content.Context.MODE_PRIVATE)
            sharedPref.edit().remove("history_data").apply()
        }
    }
}