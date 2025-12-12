package com.example.headlines.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.headlines.R
import com.example.headlines.data.model.Task

class TaskAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var onTaskClickListener: ((Task) -> Unit)? = null

    fun setOnTaskClickListener(listener: (Task) -> Unit) {
        this.onTaskClickListener = listener
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivTaskIcon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        private val tvPoints: TextView = itemView.findViewById(R.id.tvTaskPoints)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBarTask)
        private val tvProgress: TextView = itemView.findViewById(R.id.tvTaskProgress)
        private val btnAction: Button = itemView.findViewById(R.id.btnTaskAction)

        fun bind(task: Task) {
            ivIcon.setImageResource(task.iconRes)
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvPoints.text = "+${task.points}积分"

            if (task.total > 1) {
                progressBar.visibility = View.VISIBLE
                tvProgress.visibility = View.VISIBLE
                progressBar.max = task.total
                progressBar.progress = task.progress
                tvProgress.text = task.getProgressText()
            } else {
                progressBar.visibility = View.GONE
                tvProgress.visibility = View.GONE
            }

            if (task.completed) {
                btnAction.text = "已完成"
                btnAction.isEnabled = false
                btnAction.setBackgroundResource(R.drawable.bg_button_disabled)
            } else {
                btnAction.text = "去完成"
                btnAction.isEnabled = true
                btnAction.setBackgroundResource(R.drawable.bg_button_primary)
                btnAction.setOnClickListener {
                    onTaskClickListener?.invoke(task)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size
}