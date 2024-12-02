package ca.unb.mobiledev.plantpal

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SubtaskAdapter(private val subtasks: MutableList<String>) :
    RecyclerView.Adapter<SubtaskAdapter.SubtaskViewHolder>() {

    // ViewHolder class for managing each item view
    inner class SubtaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subtaskText: TextView = itemView.findViewById(android.R.id.text1)
    }

    // Inflates the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return SubtaskViewHolder(view)
    }

    // Binds data to the views for a given position
    override fun onBindViewHolder(holder: SubtaskViewHolder, position: Int) {
        val subtask = subtasks[position]
        holder.subtaskText.text = subtask
        holder.subtaskText.setTextColor(Color.parseColor("#4A8C61")) // Custom color
    }

    // Returns the total count of items
    override fun getItemCount(): Int = subtasks.size

    // Method to remove an item from the list and notify the adapter
    fun removeItem(position: Int) {
        subtasks.removeAt(position)
        notifyItemRemoved(position)
    }
}
