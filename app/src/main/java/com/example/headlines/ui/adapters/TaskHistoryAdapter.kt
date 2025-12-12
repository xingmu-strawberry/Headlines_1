package com.example.headlines.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.headlines.R
import com.example.headlines.data.model.TaskHistory
import java.text.SimpleDateFormat
import java.util.*

class TaskHistoryAdapter(private val historyList: List<TaskHistory>) :
    RecyclerView.Adapter<TaskHistoryAdapter.TaskHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_history, parent, false)
        return TaskHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount() = historyList.size

    inner class TaskHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivHistoryIcon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvHistoryTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvHistoryDescription)
        private val tvPoints: TextView = itemView.findViewById(R.id.tvHistoryPoints)
        private val tvTime: TextView = itemView.findViewById(R.id.tvHistoryTime)

        fun bind(history: TaskHistory) {
            // 设置图标
            when (history.type) {
                TaskHistory.Type.SIGN_IN -> ivIcon.setImageResource(R.drawable.ic_signin)
                TaskHistory.Type.TASK_COMPLETE -> ivIcon.setImageResource(R.drawable.ic_task_complete)
                TaskHistory.Type.POINTS_EXCHANGE -> ivIcon.setImageResource(R.drawable.ic_exchange)
            }

            tvTitle.text = history.title
            tvDescription.text = history.description

            // 设置积分（正数为获得，负数为消耗）
            if (history.points > 0) {
                tvPoints.text = "+${history.points}分"
                tvPoints.setTextColor(itemView.context.getColor(R.color.color_accent))
            } else {
                tvPoints.text = "${history.points}分"
                tvPoints.setTextColor(itemView.context.getColor(R.color.color_primary))
            }

            tvTime.text = history.getFormattedDateShort()
        }
    }
}