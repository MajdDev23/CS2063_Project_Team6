package ca.unb.mobiledev.plantpal

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<String>,
    private val selectedTasks: MutableSet<Int> = mutableSetOf(),
    private val onTaskClick: (String) -> Unit // Lambda to handle task clicks
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskText: TextView = itemView.findViewById(R.id.task_text)
        val checkBox: CheckBox = itemView.findViewById(R.id.task_checkbox)

        init {
            // Handle the click event
            itemView.setOnClickListener {
                onTaskClick(tasks[adapterPosition])  // Pass the task name to the onTaskClick lambda
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.taskText.text = tasks[position]
        holder.taskText.setTextColor(Color.parseColor("#4A8C61"))
        holder.checkBox.isChecked = selectedTasks.contains(position)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedTasks.add(position)
            } else {
                selectedTasks.remove(position)
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun deleteSelectedTasks() {
        val iterator = selectedTasks.sortedDescending().iterator()
        while (iterator.hasNext()) {
            tasks.removeAt(iterator.next())
        }
        selectedTasks.clear()
        notifyDataSetChanged()
    }

    fun getSelectedTaskCount(): Int {
        return selectedTasks.size
    }
}
